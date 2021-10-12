package com.github.paguos.mosaic.fed.config.model;

public class CNesCoordinator {

    /**
     * Name of the docker image
     */
    public String image;

    /**
     * Interval to recalculate which sources should be enabled
     */
    public int updateLocationInterval = 500;

    public boolean dynamicDuplicatesFilterEnabled = false;

    public boolean routePredictionEnabled = false;

    public int numberOfPointsInLocationStorage = 10;

    public int numberOfTuplesInFilterStorage = 50;

}
