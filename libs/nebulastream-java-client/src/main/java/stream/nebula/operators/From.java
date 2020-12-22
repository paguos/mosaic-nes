package stream.nebula.operators;

import stream.nebula.model.logicalstream.LogicalStream;

public class From extends Operator {
    private final LogicalStream logicalStream;

    public From(LogicalStream logicalStream) {
        this.logicalStream = logicalStream;
    }

    @Override
    public String getCppCode() {
        return "Query::from(\""+this.logicalStream.getName()+"\")";
    }
}
