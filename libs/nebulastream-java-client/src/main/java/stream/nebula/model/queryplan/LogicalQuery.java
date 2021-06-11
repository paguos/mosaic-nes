package stream.nebula.model.queryplan;

import stream.nebula.utils.GraphBuilder;

public class LogicalQuery {
    private String id;
    private String name;
    private String type;

    public LogicalQuery(String id, String title, String type) {
        this.id = id;
        this.name = title;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // dash and parentheses are not valid for representing ID
    public String toString(){
        return GraphBuilder.cleanNodeId(this.name);
    }
}
