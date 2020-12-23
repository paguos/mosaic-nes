package stream.nebula.model.query;

public class Query {
    String queryId;
    String queryString;

    public Query(String queryId, String queryString) {
        this.queryId = queryId;
        this.queryString = queryString;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
