package com.github.paguos.mosaic.fed.model;

public class NesCoordinator extends NesComponent {

    public static final int DEFAULT_COORDINATOR_PORT = 4000;
    public static final int DEFAULT_REST_PORT = 8081;

    private final int coordinatorPort;
    private final int restPort;

    public NesCoordinator(String name) {
        this(name, DEFAULT_COORDINATOR_PORT, DEFAULT_REST_PORT);
    }

    public NesCoordinator(String name, int coordinatorPort, int restPort) {
        super(name);
        this.coordinatorPort = coordinatorPort;
        this.restPort = restPort;
    }

    public int getCoordinatorPort() {
        return coordinatorPort;
    }

    public int getRestPort() {
        return restPort;
    }
}
