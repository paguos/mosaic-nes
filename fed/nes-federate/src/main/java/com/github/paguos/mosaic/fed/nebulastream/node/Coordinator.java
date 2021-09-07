package com.github.paguos.mosaic.fed.nebulastream.node;

import java.util.List;

public class Coordinator extends NesComponent {

    public static final int DEFAULT_COORDINATOR_PORT = 4000;
    public static final int DEFAULT_REST_PORT = 8081;
    public static final int DEFAULT_UPDATE_INTERVAL = 500;
    private static final int NES_COORDINATOR_ID = 1;

    private final int coordinatorPort;
    private final int restPort;
    private final int locationUpdateInterval;

    private final List<NesNode> children;

    public Coordinator(NesBuilder.NesCoordinatorBuilder builder) {
        super(NES_COORDINATOR_ID, builder.name);
        this.coordinatorPort = builder.coordinatorPort;
        this.restPort = builder.restPort;
        this.children = builder.children;
        this.locationUpdateInterval = builder.locationUpdateInterval;
    }

    public int getCoordinatorPort() {
        return coordinatorPort;
    }

    public int getRestPort() {
        return restPort;
    }

    public int getLocationUpdateInterval() {
        return locationUpdateInterval;
    }

    public List<NesNode> getChildren() {
        return children;
    }

}
