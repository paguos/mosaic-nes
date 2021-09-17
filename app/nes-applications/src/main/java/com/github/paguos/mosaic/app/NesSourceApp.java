package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.config.CNesApp;
import com.github.paguos.mosaic.app.directory.LocationDirectory;
import com.github.paguos.mosaic.app.directory.RSULocationData;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import com.github.paguos.mosaic.app.output.SpeedReportWriter;
import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.nebulastream.node.*;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import com.github.paguos.mosaic.fed.nebulastream.stream.BufferBuilder;
import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQProducer;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.ConfigurableApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.geo.GeoCircle;
import org.eclipse.mosaic.lib.objects.v2x.V2xMessage;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class NesSourceApp extends ConfigurableApplication<CNesApp,RoadSideUnitOperatingSystem> implements CommunicationApplication {

    private final ArrayBlockingQueue<byte[]> messages = new ArrayBlockingQueue<>(2000);
    private NesClient nesClient;
    private SpeedReportWriter reportWriter;
    private Thread sourceThread;
    private ZeroMQProducer zeroMQProducer;

    private boolean enabled = false;

    public NesSourceApp (){
        super(CNesApp.class, "NesApp");
    }


    @Override
    public void onStartup() {
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

    @Override
    public void onMessageReceived(ReceivedV2xMessage receivedV2xMessage) {
        if (getConfiguration().movingRangeEnabled) {
            try {
                boolean status = nesClient.isSourceEnabled(getOs().getId());
                if (status != enabled ) {
                    getLog().infoSimTime(this,"Status changed to: " + status);
                    enabled = status;
                }

            } catch (InternalFederateException e) {
                getLog().error("Error while fetching status!");
                getLog().error(e.getMessage());
            }
        }

        getLog().infoSimTime(this, "Received V2X Message from {}", receivedV2xMessage.getMessage().getRouting().getSource().getSourceName());
        V2xMessage msg = receivedV2xMessage.getMessage();
        if (msg instanceof SpeedReportMsg) {
            processSpeedReportMsg((SpeedReportMsg) msg);
        }
    }

    private void processSpeedReportMsg (SpeedReportMsg msg) {

        SpeedReport report = msg.getReport();
        try {
            reportWriter.write(msg.getReport());
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
        messages.add(msgBuffer);
    }

    @Override
    public void onShutdown() {
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

    @Override
    public void processEvent(Event event) {
        
    }

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {

    }

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {

    }

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {

    }
}
