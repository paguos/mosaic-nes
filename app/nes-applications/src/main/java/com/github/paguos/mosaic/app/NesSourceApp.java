package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.config.CNesApp;
import com.github.paguos.mosaic.app.directory.LocationDirectory;
import com.github.paguos.mosaic.app.directory.RSULocationData;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.output.SpeedReportWriter;
import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import com.github.paguos.mosaic.fed.nebulastream.node.NesBuilder;
import com.github.paguos.mosaic.fed.nebulastream.node.NesNode;
import com.github.paguos.mosaic.fed.nebulastream.node.ZeroMQSource;
import com.github.paguos.mosaic.fed.nebulastream.stream.BufferBuilder;
import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQProducer;
import org.eclipse.mosaic.fed.application.app.ConfigurableApplication;
import org.eclipse.mosaic.fed.application.app.api.os.OperatingSystem;
import org.eclipse.mosaic.lib.geo.GeoCircle;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class NesSourceApp<T extends OperatingSystem> extends ConfigurableApplication<CNesApp, T> {

    private final Queue<byte[]> messages;
    protected NesClient nesClient;
    protected SpeedReportWriter reportWriter;
    private Thread sourceThread;
    private ZeroMQProducer zeroMQProducer;

    protected boolean enabled;

    public NesSourceApp() {
        super(CNesApp.class, "NesApp");
        this.messages = new LinkedList<>();
        this.enabled = false;
    }

    public void start() {
        getLog().infoSimTime(this, "Activating Cell Module ...");
        getOs().getCellModule().enable();
        getLog().infoSimTime(this, "Activated AdHoc Module");

        getLog().infoSimTime(this, "Registering RSU ..");
        GeoCircle range = new GeoCircle(getOs().getPosition(), getConfiguration().rangeRadius);
        LocationDirectory.register(new RSULocationData(getOs().getId(), getOs().getPosition(), getConfiguration().rangeRadius));
        getLog().infoSimTime(this, "RSU registered!");

        int zeroMQPort = com.github.paguos.mosaic.fed.nebulastream.node.ZeroMQSource.getNextZeroMQPort();
        String zmqAddress = String.format("tcp://127.0.0.1:%d", zeroMQPort);

        try {
            NesController controller = NesController.getController();
            NesBuilder.ZeroMQSourceBuilder builder = NesBuilder.createZeroMQSource(getOs().getId())
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
                    .zmqPort(zeroMQPort)
                    .logicalStreamName("mosaic_nes")
                    .physicalStreamName(getOs().getId())
                    .parentId(2);

            if (getConfiguration().movingRangeEnabled) {
                builder.registerLocation(true)
                        .locationUpdateInterval(getConfiguration().updateLocationInterval)
                        .workerRange((int) range.getArea());
            }

            ZeroMQSource source  = builder.build();
            controller.addNode(source);
        } catch (InternalFederateException e) {
            getLog().error("RSU couldn't start the nes container!");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            getLog().error("Error while sleeping!");
        }

        try {
            nesClient = new NesClient(getConfiguration().nesRestApiHost, getConfiguration().nesRestApiPort);
            List<String> logicalStreams = nesClient.getAvailableLogicalStreams();
            getLog().info(String.format("Found Logical Stream 'QnV': %b", logicalStreams.contains("QnV")));
            getLog().info(String.format("Found Logical Stream 'mosaic_nes': %b", logicalStreams.contains("mosaic_nes")));

            int nodeCount = nesClient.getTopologyNodeCount();
            getLog().info(String.format("The Nes Topology has '%d' nodes.", nodeCount));

            if (getConfiguration().movingRangeEnabled) {
                nesClient.updateLocation(getOs().getId(), getOs().getPosition());
                getLog().info("Location updated in nes location service!");
            }

        } catch (InternalFederateException e) {
            getLog().error("Error interacting with NES!");
        }

        zeroMQProducer = new ZeroMQProducer(zmqAddress, messages);
        sourceThread = new Thread(zeroMQProducer);
        sourceThread.start();

        try {
            reportWriter = new SpeedReportWriter(getOs().getId());
        } catch (IOException e) {
            getLog().error("Error creating file writer!");
            getLog().error(e.getMessage());
        }

    }

    protected void processSpeedReportMsg (SpeedReport report) {
        try {
            reportWriter.write(report);
        } catch (IOException e) {
            getLog().error("Error while writing report!");
            getLog().error(e.getMessage());
        }

        byte[] msgBuffer = BufferBuilder.createBuffer(39)
                .fill(report.getVehicleId(), 7)
                .fill(report.getTimestamp())
                .fill(report.getVehiclePosition().getLatitude())
                .fill(report.getVehiclePosition().getLongitude())
                .fill(report.getVehicleSpeed())
                .build();

        synchronized (messages) {
            messages.add(msgBuffer);
        }

    }

    public void shutdown() {
        getLog().info("Stopping NES ZMQ Source ...");
        zeroMQProducer.terminate();

        try {
            sourceThread.join(5000);
        } catch (InterruptedException e) {
            getLog().error("Error while waiting for thread");
            e.printStackTrace();
        }
        getLog().info("NES ZMQ Source stopped!");
    }
}
