package com.github.paguos.mosaic.fed.nebulastream.node;

import java.util.List;

public class Coordinator extends NesComponent {

    public static final int DEFAULT_COORDINATOR_PORT = 4000;
    public static final int DEFAULT_REST_PORT = 8081;
    public static final int DEFAULT_UPDATE_INTERVAL = 500;
    public static final boolean DEFAULT_DYNAMIC_FILTER_ENABLED = false;
    public static final boolean DEFAULT_ROUTE_PREDICTION_ENABLED = false;
    public static final int DEFAULT_POINTS_IN_LOCAL_STORAGE = 10;
    public static final int DEFAULT_TUPLES_IN_FILTER_STORAGE = 50;

    private static final int NES_COORDINATOR_ID = 1;

    private final int coordinatorPort;
    private final int restPort;
    private final int locationUpdateInterval;

    private final boolean dynamicDuplicatesFilterEnabled;
    private final boolean routePredictionEnabled;
    private final int numberOfPointsInLocationStorage;
    private final int numberOfTuplesInFilterStorage;

    private final List<NesNode> children;

    public Coordinator(NesBuilder.NesCoordinatorBuilder builder) {
        super(NES_COORDINATOR_ID, builder.name);
        this.coordinatorPort = builder.coordinatorPort;
        this.restPort = builder.restPort;
        this.children = builder.children;
        this.locationUpdateInterval = builder.locationUpdateInterval;
        this.dynamicDuplicatesFilterEnabled = builder.dynamicDuplicatesFilterEnabled;
        this.routePredictionEnabled = builder.routePredictionEnabled;
        this.numberOfPointsInLocationStorage = builder.numberOfPointsInLocationStorage;
        this.numberOfTuplesInFilterStorage = builder.numberOfTuplesInFilterStorage;
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

    public boolean isDynamicDuplicatesFilterEnabled() {
        return dynamicDuplicatesFilterEnabled;
    }

    public boolean isRoutePredictionEnabled() {
        return routePredictionEnabled;
    }

    public int getNumberOfPointsInLocationStorage() {
        return numberOfPointsInLocationStorage;
    }

    public int getNumberOfTuplesInFilterStorage() {
        return numberOfTuplesInFilterStorage;
    }

    public List<NesNode> getChildren() {
        return children;
    }

}
