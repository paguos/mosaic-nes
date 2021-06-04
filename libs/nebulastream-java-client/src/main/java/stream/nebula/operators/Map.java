package stream.nebula.operators;

public class Map {
    private String fieldName;
    private Operation operation;

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Operation getOperation() {
        return operation;
    }

    public Map(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Map onField(String fieldName) {
        return new Map(fieldName);
    }

    // Assign an operation
    public Map assign(Operation operation) {
        this.operation = operation;
        return this;
    }

}
