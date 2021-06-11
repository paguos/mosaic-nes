package com.github.paguos.mosaic.fed.model.node;

public abstract class NesComponent {

    private final int id;
    private final String name;

    public NesComponent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
