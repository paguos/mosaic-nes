package com.github.paguos.mosaic.fed.nebulastream.stream;

public class LogicalStream {

    private final Schema schema;

    public LogicalStream(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
