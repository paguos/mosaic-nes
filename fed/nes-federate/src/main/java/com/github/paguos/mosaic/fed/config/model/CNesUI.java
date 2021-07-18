package com.github.paguos.mosaic.fed.config.model;

public class CNesUI {

    /**
     * Flag to determine if the UI has to be started
     */
    public boolean enabled;

    /**
     * Name of the docker image
     */
    public String image;

    /**
     * Exposed port for the react application
     */
    public int reactPort;

}
