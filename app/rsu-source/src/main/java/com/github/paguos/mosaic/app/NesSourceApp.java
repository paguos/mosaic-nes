package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.nebulastream.node.NesBuilder;
import com.github.paguos.mosaic.fed.nebulastream.node.NesNode;
import com.github.paguos.mosaic.fed.nebulastream.node.Source;
import com.github.paguos.mosaic.fed.nebulastream.node.SourceType;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.AdHocModuleConfiguration;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.enums.AdHocChannel;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.util.List;

public class NesSourceApp extends AbstractApplication<RoadSideUnitOperatingSystem> implements CommunicationApplication {

    private final NesClient nesClient = new NesClient("localhost", "8081");

    @Override
    public void onStartup() {

        getLog().infoSimTime(this, "Activating AdHoc Module ...");

        getOs().getAdHocModule().enable(new AdHocModuleConfiguration()
                .addRadio()
                .channel(AdHocChannel.CCH)
                .power(50)
                .create());
        getLog().infoSimTime(this, "Activated AdHoc Module");

        try {
            NesController controller = NesController.getController();
            Source source = NesBuilder.createSource("rsu")
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
                    .sourceType(SourceType.CSVSource)
                    .sourceConfig("/nes/data/QnV_short.csv")
                    .logicalStreamName("QnV")
                    .physicalStreamName("mosaic")
                    .parentId(2)
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

        } catch (InternalFederateException e) {
            getLog().error("Error interacting with NES!");
        }

    }

    @Override
    public void onMessageReceived(ReceivedV2xMessage receivedV2xMessage) {
        getLog().infoSimTime(this, "Received V2X Message from {}", receivedV2xMessage.getMessage().getRouting().getSource().getSourceName());
    }

    @Override
    public void onShutdown() {

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
