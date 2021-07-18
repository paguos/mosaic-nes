package com.github.paguos.mosaic.fed.nebulastream.query.operator;

import org.apache.commons.lang3.tuple.Pair;

public class Predicate {

    private final String field;
    private String operator;
    private String value;

    private Pair<String, Predicate> next;

    public static Predicate onField(String field) {
        return new Predicate(field);
    }

    private Predicate(String field) {
        this.field = field;
    }

    public Predicate greaterThan(Number value) {
        this.value = String.valueOf(value);
        this.operator = ">";
        return this;
    }

    public Predicate greaterThanOrEqual(Number value) {
        this.value = String.valueOf(value);
        this.operator = ">=";
        return this;
    }

    public Predicate lessThan(Number value) {
        this.value = String.valueOf(value);
        this.operator = "<";
        return this;
    }

    public Predicate lessThanOrEqual(Number value) {
        this.value = String.valueOf(value);
        this.operator = "<=";
        return this;
    }


    public String getField() {
        return field;
    }

    public Pair<String, Predicate> getNext() {
        return next;
    }

    public boolean hasNext() {
        return  (next != null);
    }

    public void setNext(Pair<String, Predicate> next) {
        this.next = next;
    }

    public String cppCode () {
        return String.format("Attribute(\"%s\")%s%s", this.field, operator, this.value);
    }
}
