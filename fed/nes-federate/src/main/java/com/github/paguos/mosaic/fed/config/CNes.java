package com.github.paguos.mosaic.fed.config;

import com.github.paguos.mosaic.fed.config.model.CNesCoordinator;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.config.model.CNesUI;
import com.github.paguos.mosaic.fed.config.model.CNesWorker;

import java.util.List;

public final class CNes {

    /**
     * Host address of the NES api
     */
    public String clientHost = "localhost";

    /**
     * Port of the NES api
     */
    public String clientPort = "8081";

    /**
     * General properties of the NES Coordinator
     */
    public CNesCoordinator coordinator;

    /**
     * General properties of the NES UI
     */
    public CNesUI ui;

    /**
     * General properties of the NES Workers
     */
    public CNesWorker worker;

    /**
     * List of the topology of nodes to be deployed.
     */
    public List<CNesNode> nodes;

}
