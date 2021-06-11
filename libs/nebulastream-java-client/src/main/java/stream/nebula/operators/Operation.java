package stream.nebula.operators;

public class Operation {
    private String leftFieldName;
    private String rightFieldName;
    private final String operator;

    private Number constantNumber;

    public Operation(String left, String right, String operator) {
        this.leftFieldName = left;
        this.rightFieldName = right;
        this.operator = operator;
    }

    public Operation(Number constantNumber, String operator) {
        this.operator = operator;
        this.constantNumber = constantNumber;
    }

    public static Operation constant(Number constantNumber) {
        return new Operation(constantNumber, "constant");
    }

    public static Operation add(String left, String right) {
        return new Operation(left, right, "add");
    }

    public static Operation substract(String left, String right) {
        return new Operation(left, right, "substract");
    }

    public static Operation multiply(String left, String right) {
        return new Operation(left, right, "multiply");
    }

    public static Operation divide(String left, String right) {
        return new Operation(left, right, "divide");
    }

    public String getLeftFieldName() {
        return leftFieldName;
    }

    public String getRightFieldName() {
        return rightFieldName;
    }

    public void setLeftFieldName(String leftFieldName) {
        this.leftFieldName = leftFieldName;
    }

    public void setRightFieldName(String rightFieldName) {
        this.rightFieldName = rightFieldName;
    }

    public String getOperator() {
        return operator;
    }

    public Number getConstantNumber() {
        return constantNumber;
    }

    public void setConstantNumber(Number constantNumber) {
        this.constantNumber = constantNumber;
    }
}
