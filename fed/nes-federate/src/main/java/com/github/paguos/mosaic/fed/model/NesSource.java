package com.github.paguos.mosaic.fed.model;

public class NesSource extends NesNode {

    private final NesSourceType sourceType;

    public NesSource(NesBuilder.NesSourceBuilder builder) {
        super(builder.name, builder.parentId, builder.dataPort, builder.rpcPort);
        this.sourceType = builder.sourceType;
    }

    public NesSourceType getSourceType() {
        return sourceType;
    }
}
