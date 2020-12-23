package stream.nebula.operators;

public class PrintOperator extends Operator {
    @Override
    public String getCppCode() {
        return ".sink(PrintSinkDescriptor::create())";
    }
}
