package stream.nebula.operators;

public class From extends Operator {
    private final String logicalStreamName;

    public From(String logicalStreamName) {
        this.logicalStreamName = logicalStreamName;
    }

    @Override
    public String getCppCode() {
        return "Query::from(\""+this.logicalStreamName+"\")";
    }
}
