package com.github.paguos.mosaic.fed.model.node;

import java.util.List;

public class NesCoordinator extends NesComponent {

    public static final int DEFAULT_COORDINATOR_PORT = 4000;
    public static final int DEFAULT_REST_PORT = 8081;
    private static final int NES_COORDINATOR_ID = 1;

    private final int coordinatorPort;
    private final int restPort;

    private final List<NesNode> children;

    public NesCoordinator(NesBuilder.NesCoordinatorBuilder builder) {
        super(NES_COORDINATOR_ID, builder.name);
        this.coordinatorPort = builder.coordinatorPort;
        this.restPort = builder.restPort;
        this.children = builder.children;
    }

    public int getCoordinatorPort() {
        return coordinatorPort;
    }

    public int getRestPort() {
        return restPort;
    }

    public List<NesNode> getChildren() {
        return children;
    }

}
