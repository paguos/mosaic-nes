package com.github.paguos.mosaic.fed.nebulastream.query.operator;

import org.apache.commons.lang3.tuple.Pair;
import stream.nebula.operators.Operator;

public class FilterOperator extends Operator {

    private Predicate predicate;

    public FilterOperator(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public String getCppCode() {
        StringBuilder cppCode = new StringBuilder(".filter(");

        while (predicate != null) {
            cppCode.append(predicate.cppCode());
            if (predicate.hasNext()) {
                Pair<String, Predicate> next = predicate.getNext();
                cppCode.append(String.format(" %s ", next.getKey()));
                predicate = next.getValue();
            } else {
                break;
            }
        }

        cppCode.append(")");
        return cppCode.toString();
    }

}
