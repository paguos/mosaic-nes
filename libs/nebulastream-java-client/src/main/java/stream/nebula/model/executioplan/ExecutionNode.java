package stream.nebula.model.executioplan;

import stream.nebula.utils.GraphBuilder;

public class ExecutionNode {
    private String id;
    private String nodeType;
    private String operators;
    private float remainingCapacity;

    public ExecutionNode(String id, String nodeType, String operators, float remainingCapacity) {
        this.id = id;
        this.nodeType = nodeType;
        this.operators = operators;
        this.remainingCapacity = remainingCapacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public float getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(float remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public String toString(){
        return GraphBuilder.cleanNodeId(this.id)+"_"+this.operators.replace("=>","_")
                .replace("(","").replace(")","").replace("-","");
    }
}
