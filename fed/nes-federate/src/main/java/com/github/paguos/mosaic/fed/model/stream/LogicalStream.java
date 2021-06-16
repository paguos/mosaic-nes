package com.github.paguos.mosaic.fed.model.stream;

public class LogicalStream {

    private final String name;
    private final Schema schema;

    public LogicalStream(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public Schema getSchema() {
        return schema;
    }
}
