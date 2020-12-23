package stream.nebula.model.physicalstream;

public class PhysicalStream {
    String physicalName;
    String logicalStreamName;
    String sourceType;
    String sourceConfig;
    int sourceFrequency;
    int numberOfBuffersToProduce;
    String node;

    public PhysicalStream(String physicalName, String logicalStreamName, String sourceType, String sourceConfig, int sourceFrequency, int numberOfBuffersToProduce, String node) {
        this.physicalName = physicalName;
        this.logicalStreamName = logicalStreamName;
        this.sourceType = sourceType;
        this.sourceConfig = sourceConfig;
        this.sourceFrequency = sourceFrequency;
        this.numberOfBuffersToProduce = numberOfBuffersToProduce;
        this.node = node;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public String getLogicalStreamName() {
        return logicalStreamName;
    }

    public void setLogicalStreamName(String logicalStreamName) {
        this.logicalStreamName = logicalStreamName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceConfig() {
        return sourceConfig;
    }

    public void setSourceConfig(String sourceConfig) {
        this.sourceConfig = sourceConfig;
    }

    public int getSourceFrequency() {
        return sourceFrequency;
    }

    public void setSourceFrequency(int sourceFrequency) {
        this.sourceFrequency = sourceFrequency;
    }

    public int getNumberOfBuffersToProduce() {
        return numberOfBuffersToProduce;
    }

    public void setNumberOfBuffersToProduce(int numberOfBuffersToProduce) {
        this.numberOfBuffersToProduce = numberOfBuffersToProduce;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
