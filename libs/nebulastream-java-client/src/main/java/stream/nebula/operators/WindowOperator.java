package stream.nebula.operators;

import stream.nebula.operators.windowdefinition.WindowDefinition;

public class WindowOperator extends Operator {

    WindowDefinition windowDefinition;
    Aggregation aggregation;
    public WindowOperator(WindowDefinition windowDefinition, Aggregation aggregation) {
        this.windowDefinition = windowDefinition;
        this.aggregation = aggregation;
    }

    @Override
    public String getCppCode() {
        return ".window("+ windowDefinition.toString()+").apply("+aggregation.toString()+")";
    }
}
