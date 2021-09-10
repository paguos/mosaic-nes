package com.github.paguos.mosaic.app.config;

public class CNesSinkApp {

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

}
