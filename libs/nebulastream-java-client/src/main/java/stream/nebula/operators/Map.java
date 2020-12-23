package stream.nebula.operators;

public class Map {
    private String fieldName;
    private int fieldIndex;
    private Operation operation;

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public Operation getOperation() {
        return operation;
    }

    public Map(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public Map(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Map onField(int fieldIndex) {
        return new Map(fieldIndex);
    }

    public static Map onField(String fieldName) {
        return new Map(fieldName);
    }
    // Assign constant
    public Map assign(Operation operation) {
        this.operation = operation;
        return this;
    }

}
