package stream.nebula.operators;

import stream.nebula.queryinterface.Query;

public class UnionWith extends Operator {
    private Query other;

    public UnionWith(Query other) {
        this.other = other;
    }

    @Override
    public String getCppCode() {
        return ".unionWith("+other.generateCppCode().replace(";","")+")";
    }
}
