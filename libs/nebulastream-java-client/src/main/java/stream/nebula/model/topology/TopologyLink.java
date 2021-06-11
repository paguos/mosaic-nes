package stream.nebula.model.topology;

public class TopologyLink {
    private Integer source;
    private Integer target;


    public TopologyLink(Integer source, Integer target) {
        this.source = source;
        this.target = target;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer linkCapacity) {
        this.source = source;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
