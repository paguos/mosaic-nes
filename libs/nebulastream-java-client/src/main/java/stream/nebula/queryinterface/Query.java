package stream.nebula.queryinterface;

import stream.nebula.exceptions.*;
import stream.nebula.operators.*;
import stream.nebula.operators.sink.Sink;
import stream.nebula.operators.windowdefinition.WindowDefinition;

import java.util.ArrayList;


public class Query {
    private final ArrayList<Operator> operators;
    private String logicalStreamName;

    public Query() {
        this.operators = new ArrayList<>();
    }

    public Query filter(Predicate predicate) throws EmptyFieldException {

        Predicate rootPredicate = predicate;

        // Check that all predicates has a field
        boolean isLastPredicate = false;
        while(!isLastPredicate) {
            // Throw an exception if the field of the current predicate is empty
            if (predicate.getField().isEmpty()) {
               throw new EmptyFieldException();
            }

            // go to the next predicate
            isLastPredicate = predicate.getNextPredicate() == null;
            predicate = predicate.getNextPredicate();
        }

        this.operators.add(new FilterOperator(rootPredicate));
        return this;
    }

    public Query map(Map map) throws EmptyFieldException {
        // Throw an exception if the field of the current map operator is empty
        if (map.getFieldName().isEmpty()) {
            throw new EmptyFieldException();
        }

        this.operators.add(new MapOperator(map));
        return this;
    }

    public Query from(String logicalStreamName) throws EmptyFieldException {
        // Throw an exception if the field of the current map operator is empty
        if (logicalStreamName.isEmpty()) {
            throw new EmptyFieldException();
        }
        this.operators.add(new From(logicalStreamName));
        this.logicalStreamName = logicalStreamName;
        return this;
    }

    public Query unionWith(Query other) {
        this.operators.add(new UnionWith(other));
        return this;
    }

    public Query joinWith(Query other, String leftKey, String rightKey, WindowDefinition windowDefinition) {
        this.operators.add(new JoinWithOperator(other, leftKey, rightKey, windowDefinition));
        return this;
    }

    public Query sink(Sink sink) {
        this.operators.add(sink);
        return this;
    }

    public Query window(WindowDefinition windowDefinition, Aggregation aggregation) throws EmptyFieldException {
        // Throw an EmptyFieldException if aggregation field is empty
        if (aggregation.getAggregationFieldName().isEmpty()) {
            throw new EmptyFieldException("Aggregation");
        }
        this.operators.add(new WindowOperator( windowDefinition, aggregation));
        return this;
    }

    public Query windowByKey(String fieldName, WindowDefinition windowDefinition, Aggregation aggregation) throws EmptyFieldException {
        // Check if the key field is empty
        if (fieldName.isEmpty()) {
            throw new EmptyFieldException("Window key");
        }

        // Throw an EmptyFieldException if aggregation field is empty
        if (aggregation.getAggregationFieldName().isEmpty()) {
            throw new EmptyFieldException("Aggregation");
        }

        this.operators.add(new WindowByKeyOperator(fieldName, windowDefinition, aggregation));
        return this;
    }

    public String generateCppCode() {
        StringBuilder cppCode = new StringBuilder();

        // build the query
        for (Operator operator : this.operators) {
            cppCode.append(operator.getCppCode());
        }
        cppCode.append(";");
        return cppCode.toString();
    }
}