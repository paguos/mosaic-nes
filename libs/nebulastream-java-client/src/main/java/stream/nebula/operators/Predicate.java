package stream.nebula.operators;

public class Predicate {
    private String field;
    private String value;
    private String operator;
    private int fieldIndex;
    private Class valueType;

    private Predicate nextPredicate;
    private String nextPredicateConnector;

    private Predicate(int fieldIndex){
        this.fieldIndex = fieldIndex;
    }

    public static Predicate onField(int fieldIndex) {
        return new Predicate(fieldIndex);
    }
    public static Predicate onField(String fieldName) {
        return new Predicate(fieldName);
    }

    private Predicate(String fieldName){
        this.field = fieldName;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public Predicate greaterThan(Number intValue) {
        this.value = String.valueOf(intValue);
        this.valueType = intValue.getClass();
        this.operator = ">";
        return this;
    }

    public Predicate and(Predicate other) {
        this.nextPredicate = other;
        this.nextPredicateConnector = " && ";
        return this;
    }

    public Predicate or(Predicate other) {
        this.nextPredicate = other;
        this.nextPredicateConnector = " || ";
        return this;
    }

    public Predicate getNextPredicate() {
        return nextPredicate;
    }

    public String getNextPredicateConnector() {
        return nextPredicateConnector;
    }

    public Predicate lessthanThan(Number intValue) {
        this.value = String.valueOf(intValue);
        this.valueType = intValue.getClass();
        this.operator = "<";
        return this;
    }

    public Predicate greaterThanOrEqual(Number intValue) {
        this.value = String.valueOf(intValue);
        this.valueType = intValue.getClass();
        this.operator = ">=";
        return this;
    }

    public Predicate lessthanThanOrEqual(Number intValue) {
        this.value = String.valueOf(intValue);
        this.valueType = intValue.getClass();
        this.operator = "<=";
        return this;
    }

    public Predicate equal(Number intValue) {
        this.value = String.valueOf(intValue);
        this.valueType = intValue.getClass();
        this.operator = "==";
        return this;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

    public Class getValueType() {
        return valueType;
    }
}
