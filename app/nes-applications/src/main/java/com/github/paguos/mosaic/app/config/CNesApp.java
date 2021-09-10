package com.github.paguos.mosaic.app.config;

public class CNesApp {

    public String nesRestApiHost = "localhost";
    public String nesRestApiPort = "8081";

    /**
     * Submit a single moving range query
     * Otherwise submit a range query on a given interval
     */
    public boolean movingRangeEnabled = true;

    /**
     * Query interval if range query is enabled
     */
    public long queryInterval = 6;

    /* Source related configs */

    /**
     * Radius of the circular range of the source (in meters)
     */
    public int rangeRadius = 100;

    /**
     * Interval for refreshing the status of the source (in milliseconds)
     */
    public int updateLocationInterval = 500;
}
