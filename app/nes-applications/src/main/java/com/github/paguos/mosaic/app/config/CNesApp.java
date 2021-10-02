package com.github.paguos.mosaic.app.config;

public class CNesApp {

    public String nesRestApiHost = "localhost";
    public String nesRestApiPort = "8081";

    /* Sink related configs */

    /**
     * Submit a single moving range query
     * Otherwise submit a range query on a given interval
     */
    public boolean movingRangeEnabled = true;

    /**
     * Area of the moving range query in square meters.
     */
    public long movingRangeArea = 1000000;

    /**
     * Query interval if range query is enabled
     */
    public long queryInterval = 6;

    /**
     * Min time to submit a query to NES
     */
    public int startProcessingTime = 0;

    /* Source related configs */

    /**
     * Radius of the circular range of the source (in meters)
     */
    public int sourceRangeRadius = 100;

    /**
     * Interval for refreshing the status of the source (in milliseconds)
     */
    public int updateLocationInterval = 500;
}
