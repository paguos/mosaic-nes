package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.directory.LocationDirectory;
import com.github.paguos.mosaic.app.directory.RSULocationData;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import com.github.paguos.mosaic.app.output.SpeedReportWriter;
import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import com.github.paguos.mosaic.fed.nebulastream.node.NesBuilder;
import com.github.paguos.mosaic.fed.nebulastream.node.NesNode;
import com.github.paguos.mosaic.fed.nebulastream.stream.BufferBuilder;
import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQSource;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.objects.v2x.V2xMessage;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class NesMobilitySourceApp extends AbstractApplication<RoadSideUnitOperatingSystem> implements CommunicationApplication {

    private final NesClient nesClient = new NesClient("localhost", "8081");
    private final ArrayBlockingQueue<byte[]> messages = new ArrayBlockingQueue<>(2000);
    private SpeedReportWriter reportWriter;
    private Thread sourceThread;
    private ZeroMQSource zeroMQSource;

    @Override
    public void onStartup() {
        getLog().infoSimTime(this, "Activating Cell Module ...");
        getOs().getCellModule().enable();
        getLog().infoSimTime(this, "Activated AdHoc Module");

        getLog().infoSimTime(this, "Registering RSU ..");
        LocationDirectory.register(new RSULocationData(getOs().getId(), getOs().getPosition(), 100));
        getLog().infoSimTime(this, "RSU registered!");

        int zeroMQPort = com.github.paguos.mosaic.fed.nebulastream.node.ZeroMQSource.getNextZeroMQPort();
        String zmqAddress = String.format("tcp://127.0.0.1:%d", zeroMQPort);

        try {
            NesController controller = NesController.getController();
            com.github.paguos.mosaic.fed.nebulastream.node.ZeroMQSource source = NesBuilder.createZeroMQSource(getOs().getId())
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
                    .zmqPort(zeroMQPort)
                    .logicalStreamName("mosaic_nes")
                    .physicalStreamName(getOs().getId())
                    .parentId(2)
                    .registerLocation(true)
                    .workerRange(98696)
                    .build();
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
            List<String> logicalStreams = nesClient.getAvailableLogicalStreams();
            getLog().info(String.format("Found Logical Stream 'QnV': %b", logicalStreams.contains("QnV")));
            getLog().info(String.format("Found Logical Stream 'mosaic_nes': %b", logicalStreams.contains("mosaic_nes")));

            int nodeCount = nesClient.getTopologyNodeCount();
            getLog().info(String.format("The Nes Topology has '%d' nodes.", nodeCount));

            nesClient.updateLocation(getOs().getId(), getOs().getPosition());
            getLog().info("Location updated in nes location service!");
        } catch (InternalFederateException e) {
            getLog().error("Error interacting with NES!");
        }

        zeroMQSource = new ZeroMQSource(zmqAddress, messages);
        sourceThread = new Thread(zeroMQSource);
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
        zeroMQSource.terminate();

        try {
            sourceThread.join(5000);
        } catch (InterruptedException e) {
            getLog().error("Error while waiting for thread");
            e.printStackTrace();
        }
        getLog().info("NES ZMQ Source stopped!");
    }

    @Override
    public void processEvent(Event event) {}

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {}

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {}

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {}
}
