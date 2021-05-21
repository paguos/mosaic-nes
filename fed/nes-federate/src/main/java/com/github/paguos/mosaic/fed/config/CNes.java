package com.github.paguos.mosaic.fed.config;

import com.github.paguos.mosaic.fed.config.model.CNesCoordinator;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.config.model.CNesWorker;

import java.util.List;

public final class CNes {

    /**
     * General properties of the NES Coordinator
     */
    public CNesCoordinator coordinator;

    /**
     * General properties of the NES Workers
     */
    public CNesWorker worker;

    /**
     * List of the topology of nodes to be deployed.
     */
    public List<CNesNode> nodes;

}
