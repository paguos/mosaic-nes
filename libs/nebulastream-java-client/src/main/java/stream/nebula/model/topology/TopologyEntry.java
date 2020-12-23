package stream.nebula.model.topology;

import stream.nebula.utils.GraphBuilder;

public class TopologyEntry {
    private String id;
    private String title;
    private String nodeType;
    private float nodeCapacity;
    private float remainingCapacity;

    public TopologyEntry(String id, String title, String nodeType, float nodeCapacity, float remainingCapacity) {
        this.id = id;
        this.title = title;
        this.nodeType = nodeType;
        this.nodeCapacity = nodeCapacity;
        this.remainingCapacity = remainingCapacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public float getNodeCapacity() {
        return nodeCapacity;
    }

    public void setNodeCapacity(float nodeCapacity) {
        this.nodeCapacity = nodeCapacity;
    }

    public float getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(float remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    // dash and parentheses are not valid for representing ID
    public String toString(){
        return GraphBuilder.cleanNodeId(this.id);
    }
}
