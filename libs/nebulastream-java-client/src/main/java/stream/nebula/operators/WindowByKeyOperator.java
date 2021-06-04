package stream.nebula.operators;

import stream.nebula.operators.windowdefinition.WindowDefinition;

/**
 * A class to represent NES windowByKey operator
 */

public class WindowByKeyOperator extends Operator {

    WindowDefinition windowDefinition;
    Aggregation aggregation;
    String fieldName;

    /**
     * Constructor for WindowByKeyOperator
     * @param fieldName the name of the key field
     * @param windowDefinition definition of the window used (e.g., TumblingWindow, SlidingWindow)
     * @param aggregation aggregation function to be used in the operator
     */
    public WindowByKeyOperator(String fieldName, WindowDefinition windowDefinition, Aggregation aggregation) {
        this.windowDefinition = windowDefinition;
        this.aggregation = aggregation;
        this.fieldName = fieldName;
    }

    @Override
    public String getCppCode() {
        return ".window("+ windowDefinition.toString()+").byKey(Attribute(\""+fieldName+"\")).apply("+aggregation.toString()+")";
    }
}
