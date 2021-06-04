package stream.nebula.model.executioplan;

import stream.nebula.utils.GraphBuilder;

public class ExecutionNode {
    private Integer id;
    private Integer topologyNodeId;
    private String topologyNodeIpAddress;
    private String operators;

    public ExecutionNode(Integer id, Integer topologyNodeId, String topologyNodeIpAddress, String operators) {
        this.id = id;
        this.topologyNodeId = topologyNodeId;
        this.topologyNodeIpAddress = topologyNodeIpAddress;
        this.operators = operators;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTopologyNodeId(Integer topologyNodeId) {
        this.topologyNodeId = topologyNodeId;
    }

    public Integer getTopologyNodeId() {
        return topologyNodeId;
    }

    public String getTopologyNodeIpAddress() {
        return topologyNodeIpAddress;
    }

    public void setTopologyNodeIpAddress(String topologyNodeIpAddress) { this.topologyNodeIpAddress = topologyNodeIpAddress; }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }


    public String toString(){
        return GraphBuilder.cleanNodeId("NodeId"+Integer.toString(this.id))+"_"+this.operators.replace("=>","_")
                .replace("(","").replace(")","").replace("-","").replace(",", "");
    }
}
