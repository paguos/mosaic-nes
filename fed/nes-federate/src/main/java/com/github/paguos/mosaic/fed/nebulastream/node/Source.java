package com.github.paguos.mosaic.fed.nebulastream.node;

public class Source extends NesNode {

    private final String logicalStreamName;
    private final String physicalStreamName;
    private final String sourceConfig;
    private final SourceType sourceType;

    public Source(NesBuilder.NesSourceBuilder builder) {
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

    public SourceType getSourceType() {
        return sourceType;
    }
}
