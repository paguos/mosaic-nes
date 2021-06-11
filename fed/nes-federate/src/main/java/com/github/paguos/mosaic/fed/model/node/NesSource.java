package com.github.paguos.mosaic.fed.model.node;

public class NesSource extends NesNode {

    private final String logicalStreamName;
    private final String physicalStreamName;
    private final String sourceConfig;
    private final NesSourceType sourceType;

    public NesSource(NesBuilder.NesSourceBuilder builder) {
        super(builder.name, builder.parentId, builder.dataPort, builder.rpcPort);
        this.logicalStreamName = builder.logicalStreamName;
        this.physicalStreamName = builder.physicalStreamName;
        this.sourceConfig = builder.sourceConfig;
        this.sourceType = builder.sourceType;
    }

    public String getLogicalStreamName() {
        return logicalStreamName;
    }

    public String getPhysicalStreamName() {
        return physicalStreamName;
    }

    public String getSourceConfig() {
        return sourceConfig;
    }

    public NesSourceType getSourceType() {
        return sourceType;
    }
}
