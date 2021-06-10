package com.github.paguos.mosaic.fed.model.stream;

public class NesLogicalStream {

    private final String name;
    private final String schema;

    public NesLogicalStream(String name, String schema) {
        this.name = name;
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }
}
