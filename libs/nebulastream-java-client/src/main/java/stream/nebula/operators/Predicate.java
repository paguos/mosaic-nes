package stream.nebula.operators;

public class Predicate {
    private String field;
    private String value;
    private String operator;
    private Class valueType;

    private Predicate nextPredicate;
    private String nextPredicateConnector;

    public static Predicate onField(String fieldName) {
        return new Predicate(fieldName);
    }

    private Predicate(String fieldName){
        this.field = fieldName;
    }

    public void setField(String field) {
        this.field = field;
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

    public Predicate lessThan(Number intValue) {
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

    public Predicate lessThanOrEqual(Number intValue) {
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
