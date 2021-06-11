package stream.nebula.model.topology;

import stream.nebula.utils.GraphBuilder;

public class TopologyEntry {
    private Integer id;
    private String ip_address;
    private float available_resources;

    public TopologyEntry(Integer id, String ip_address, float available_resources) {
        this.id = id;
        this.ip_address= ip_address;
        this.available_resources = available_resources;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public float getAvailable_resources() {
        return available_resources;
    }

    public void setAvailable_resources(float available_resources) {
        this.available_resources = available_resources;
    }
    // dash and parentheses are not valid for representing ID
    public String toString(){
        return GraphBuilder.cleanNodeId(Integer.toString(this.id));
    }
}
