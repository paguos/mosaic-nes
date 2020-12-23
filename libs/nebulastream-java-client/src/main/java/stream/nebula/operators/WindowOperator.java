package stream.nebula.operators;

import stream.nebula.operators.windowtype.WindowType;

public class WindowOperator extends Operator {

    WindowType windowType;
    Aggregation aggregation;
    public WindowOperator(WindowType windowType, Aggregation aggregation) {
        this.windowType = windowType;
        this.aggregation = aggregation;
    }

    @Override
    public String getCppCode() {
        return ".window("+windowType.toString()+", "+aggregation.toString()+")";
    }
}
