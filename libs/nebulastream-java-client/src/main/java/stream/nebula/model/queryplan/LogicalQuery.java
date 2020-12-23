package stream.nebula.model.queryplan;

import stream.nebula.utils.GraphBuilder;

public class LogicalQuery {
    private String id;
    private String title;
    private String type;

    public LogicalQuery(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // dash and parentheses are not valid for representing ID
    public String toString(){
        return GraphBuilder.cleanNodeId(this.id);
    }
}
