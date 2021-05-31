package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.fed.ambassador.NesController;
import com.github.paguos.mosaic.fed.model.NesBuilder;
import com.github.paguos.mosaic.fed.model.NesNode;
import com.github.paguos.mosaic.fed.model.NesSource;
import com.github.paguos.mosaic.fed.model.NesWorker;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

public class NesSourceApp extends AbstractApplication<RoadSideUnitOperatingSystem>  {

    @Override
    public void onStartup() {

        try {
            NesController controller = NesController.getController();
            NesWorker source = NesBuilder.createWorker("rsu")
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
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
        System.out.println("Here");
    }
}
