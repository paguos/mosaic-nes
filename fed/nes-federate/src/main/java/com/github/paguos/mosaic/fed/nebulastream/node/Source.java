package com.github.paguos.mosaic.fed.nebulastream.node;

public class Source extends NesNode {

    private final String logicalStreamName;
    private final int numberOfTuplesToProducePerBuffer;
    private final String physicalStreamName;
    protected String sourceConfig;
    protected SourceType sourceType;


    public Source(NesBuilder.NesSourceBuilder builder) {
        super(builder.name, builder.parentId, builder.dataPort, builder.rpcPort);
        this.logicalStreamName = builder.logicalStreamName;
        this.numberOfTuplesToProducePerBuffer = builder.numberOfTuplesToProducePerBuffer;
        this.physicalStreamName = builder.physicalStreamName;
        this.sourceConfig = builder.sourceConfig;
        this.sourceType = builder.sourceType;
    }

    public String getLogicalStreamName() {
        return logicalStreamName;
    }

    public int getNumberOfTuplesToProducePerBuffer() {
        return numberOfTuplesToProducePerBuffer;
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
