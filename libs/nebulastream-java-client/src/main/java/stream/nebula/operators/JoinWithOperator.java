package stream.nebula.operators;

import stream.nebula.operators.windowdefinition.WindowDefinition;
import stream.nebula.queryinterface.Query;

public class JoinWithOperator extends Operator {
    private final Query other;
    private final String leftFieldName;
    private final String rightFieldName;
    private final WindowDefinition windowDefinition;

    public JoinWithOperator(Query other, String leftFieldName, String rightFieldName, WindowDefinition windowDefinition) {
        this.other = other;
        this.leftFieldName = leftFieldName;
        this.rightFieldName = rightFieldName;
        this.windowDefinition = windowDefinition;
    }

    @Override
    public String getCppCode() {
        return ".joinWith("+other.generateCppCode().replace(";","") +
                ").where(Attribute(\""+leftFieldName+"\")).equalsTo(Attribute(\""+rightFieldName+"\")).window(" + windowDefinition.toString()+")";
    }
}
