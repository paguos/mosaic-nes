package com.github.paguos.mosaic.app.config;

public class CSpeedSensorApp {

    /**
     * Send the messages to road side units.
     * Otherwise send them directly to the moving sink.
     */
    public boolean rsuEnabled = true;

    /**
     * Min time to enable the sending of data.
     */
    public int startProcessingTime = 0;

}
