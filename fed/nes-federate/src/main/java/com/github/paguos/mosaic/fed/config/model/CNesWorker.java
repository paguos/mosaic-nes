package com.github.paguos.mosaic.fed.config.model;

public class CNesWorker {

    /**
     * Name of the docker image
     */
    public String image;

    /**
     * Interval to recalculate which sources should be enabled
     */
    public int updateLocationInterval = 500;

}
