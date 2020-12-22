package stream.nebula.model.executioplan;

public class ExecutionLink {
    private float linkCapacity;
    private float linkLatency;

    public ExecutionLink(float linkCapacity, float linkLatency) {
        this.linkCapacity = linkCapacity;
        this.linkLatency = linkLatency;
    }

    public float getLinkCapacity() {
        return linkCapacity;
    }

    public void setLinkCapacity(float linkCapacity) {
        this.linkCapacity = linkCapacity;
    }

    public float getLinkLatency() {
        return linkLatency;
    }

    public void setLinkLatency(float linkLatency) {
        this.linkLatency = linkLatency;
    }
}
