package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.model.node.NesBuilder;
import com.github.paguos.mosaic.fed.model.node.NesNode;
import com.github.paguos.mosaic.fed.model.node.NesSource;
import com.github.paguos.mosaic.fed.model.node.NesSourceType;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.util.List;

public class NesSourceApp extends AbstractApplication<RoadSideUnitOperatingSystem>  {

    private final NesClient nesClient = new NesClient("localhost", "8081");

    @Override
    public void onStartup() {

        try {
            NesController controller = NesController.getController();
            NesSource source = NesBuilder.createSource("rsu")
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
                    .sourceType(NesSourceType.CSVSource)
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

            int nodeCount = nesClient.getTopologyNodeCount();
            getLog().info(String.format("The Nes Topology has '%d' nodes.", nodeCount));

        } catch (InternalFederateException e) {
            getLog().error("Error interacting with NES!");
        }

    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void processEvent(Event event) {
        
    }
}
