package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.model.*;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

public class NesSourceApp extends AbstractApplication<RoadSideUnitOperatingSystem>  {

    @Override
    public void onStartup() {

        try {
            NesController controller = NesController.getController();
            NesSource source = NesBuilder.createSource("rsu")
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
                    .sourceType(NesSourceType.CSVSource)
                    .sourceConfig("/opt/local/nebula-stream/exdra.csv")
                    .physicalStreamName("mosaic")
                    .parentId(2)
                    .build();
            controller.addNode(source);
        } catch (InternalFederateException e) {
            getLog().error("RSU couldn't start the nes container!");
        }

    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void processEvent(Event event) throws Exception {

    }
}
