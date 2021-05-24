package com.github.paguos.mosaic.fed.model;

public abstract class NesComponent {

    private String containerId;
    private final String name;

    public NesComponent(String name) {
        this.name = name;
        this.containerId = "";
    }

    public String getContainerId() {
        return containerId;
    }

    public String getName() {
        return name;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
