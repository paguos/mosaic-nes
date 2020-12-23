package stream.nebula.model.topology;

public class TopologyLink {
    private float linkCapacity;
    private float linkLatency;

    public TopologyLink(float linkCapacity, float linkLatency) {
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
