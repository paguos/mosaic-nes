package com.github.paguos.mosaic.app.config;

public class CNesSinkApp {

    public int messageQueueSize = 2000;
    public String nesRestApiHost = "localhost";
    public String nesRestApiPort = "8081";
    public long queryInterval = 6;
    public String zmqAddress = "tcp://localhost:5555";

}