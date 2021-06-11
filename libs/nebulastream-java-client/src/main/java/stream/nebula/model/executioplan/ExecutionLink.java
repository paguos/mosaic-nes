package stream.nebula.model.executioplan;

public class ExecutionLink {
    private Integer source;
    private Integer target;

    public ExecutionLink(Integer source, Integer target) {
        this.source = source;
        this.target = target;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer gettarget() {
        return target;
    }

    public void settarget(Integer target) {
        this.target = target;
    }
}
