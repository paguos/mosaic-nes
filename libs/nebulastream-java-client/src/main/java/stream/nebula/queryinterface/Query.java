package stream.nebula.queryinterface;

import stream.nebula.exceptions.*;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.operators.*;
import stream.nebula.operators.windowdefinition.WindowDefinition;
import stream.nebula.model.logicalstream.LogicalStream;

import java.util.ArrayList;


public class Query {
    private final ArrayList<Operator> operators;
    private LogicalStream logicalStream;

    public Query() {
        this.operators = new ArrayList<>();
    }

    public Query filter(Predicate predicate) throws FieldCompareTypeMismatchException, UnknownDataTypeException, FieldNotFoundException {

        Predicate rootPredicate = predicate;

        // Check validity if all predicate expressions
        boolean isLastPredicate = false;
        while(!isLastPredicate) {
            // If fieldName not provided, set the fieldName using fieldIndex, otherwise (i.e. fieldName provided) set
            // fieldIndex using fieldName
            if (predicate.getField() == null) {
                // Check if index in the bound
                boolean isPredicateIndexInLogicalStreamFieldListBound = (predicate.getFieldIndex() >= 0)
                        && (predicate.getFieldIndex() < this.logicalStream.getFieldList().size());
                if (isPredicateIndexInLogicalStreamFieldListBound) {
                    // Set the predicate's fieldName
                    predicate.setField(this.logicalStream.getFieldList().get(predicate.getFieldIndex()).getName());
                } else {
                    throw new FieldIndexOutOfBoundException(predicate.getFieldIndex(),
                            logicalStream.getName(), this.logicalStream.getFieldList().size());
                }
            } else {
                // Check if fieldName is on the list
                boolean isFieldInTheLogicalStreamFieldList = false;
                for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
                    if (this.logicalStream.getFieldList().get(i).getName().equals(predicate.getField())) {
                        isFieldInTheLogicalStreamFieldList = true;
                        predicate.setFieldIndex(i);
                        break;
                    }
                }
                // Throw fieldNotFoundException if the fieldName is not in the list
                if (!(isFieldInTheLogicalStreamFieldList)) {
                    throw new FieldNotFoundException(predicate.getField(), this.logicalStream.getName());
                }
            }

            // Perform type matching check
            String comparedFieldName = this.logicalStream.getFieldList().get(predicate.getFieldIndex()).getName();
            Class comparedFieldType = this.logicalStream.getFieldList().get(predicate.getFieldIndex()).getJavaDataType();

            // check for matched type
            if (predicate.getValueType() == comparedFieldType) {
                // set the predicate field
                predicate.setField(comparedFieldName);

                isLastPredicate = predicate.getNextPredicate() == null;
                predicate = predicate.getNextPredicate();
            } else {
                throw new FieldCompareTypeMismatchException(comparedFieldName, comparedFieldType.getSimpleName(), predicate.getValue()
                        , predicate.getValueType().getSimpleName());
            }
        }
        this.operators.add(new FilterOperator(rootPredicate));
        return this;
    }

    public Query map(Map map) throws FieldNotFoundException {
        // Resolve fieldIndex and fieldName on "onField" property
        if (map.getFieldName() == null) {
            boolean isMapFieldIndexInLogicalStreamFieldListBound = (map.getFieldIndex() >=0) && (map.getFieldIndex() < this.logicalStream.getFieldList().size());

            if (isMapFieldIndexInLogicalStreamFieldListBound) {
                // set map's fieldName
                map.setFieldName(this.logicalStream.getFieldList().get(map.getFieldIndex()).getName());
            } else {
                throw new FieldIndexOutOfBoundException(map.getFieldIndex(),
                        logicalStream.getName(), this.logicalStream.getFieldList().size());
            }
        } else {
            // Check if fieldName is on the list
            boolean isFieldInTheLogicalStreamFieldList = false;
            for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
                if (this.logicalStream.getFieldList().get(i).getName().equals(map.getFieldName())) {
                    isFieldInTheLogicalStreamFieldList = true;
                    map.setFieldIndex(i);
                    break;
                }
            }
            // Add as new field if provided field is not in the list
            if (!(isFieldInTheLogicalStreamFieldList)) {
                map.setFieldIndex(logicalStream.getFieldList().size()+1);
            }
        }

        // Resolve fieldIndex and fieldName on "assign" property
        if (!map.getOperation().getOperator().equals("constant")){
            if (map.getOperation().getLeftFieldName() == null && map.getOperation().getRightFieldName() ==null){
                boolean isMapOperationFieldIndexInLogicalStreamFieldListBound = (map.getOperation().getLeftFieldIndex() > 0)
                        && (map.getOperation().getLeftFieldIndex() < this.logicalStream.getFieldList().size())
                        && (map.getOperation().getRightFieldIndex() > 0)
                        && (map.getOperation().getRightFieldIndex() < this.logicalStream.getFieldList().size());
                if (isMapOperationFieldIndexInLogicalStreamFieldListBound) {
                    map.getOperation().setLeftFieldName(this.logicalStream.getFieldList().get(map.getOperation().getLeftFieldIndex()).getName());
                    map.getOperation().setRightFieldName(this.logicalStream.getFieldList().get(map.getOperation().getRightFieldIndex()).getName());
                } else {
                    throw new FieldIndexOutOfBoundException(logicalStream.getName(), this.logicalStream.getFieldList().size());
                }
            } else {
                // Check if left fieldName is on the list
                boolean isLeftFieldInTheLogicalStreamFieldList = false;
                for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
                    if (this.logicalStream.getFieldList().get(i).getName().equals(map.getOperation().getLeftFieldName())) {
                        isLeftFieldInTheLogicalStreamFieldList = true;
                        map.getOperation().setLeftFieldIndex(i);
                        break;
                    }
                }
                // Throw fieldNotFoundException if the fieldName is not in the list
                if (!(isLeftFieldInTheLogicalStreamFieldList)) {
                    throw new FieldNotFoundException(map.getOperation().getLeftFieldName(), this.logicalStream.getName());
                }

                // Check if right fieldName is on the list
                boolean isRightFieldInTheLogicalStreamFieldList = false;
                for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
                    if (this.logicalStream.getFieldList().get(i).getName().equals(map.getOperation().getRightFieldName())) {
                        isRightFieldInTheLogicalStreamFieldList = true;
                        map.getOperation().setRightFieldIndex(i);
                        break;
                    }
                }
                // Throw fieldNotFoundException if the fieldName is not in the list
                if (!(isRightFieldInTheLogicalStreamFieldList)) {
                    throw new FieldNotFoundException(map.getOperation().getRightFieldName(), this.logicalStream.getName());
                }
            }
        }

        this.operators.add(new MapOperator(map));
        return this;
    }

    public Query from(LogicalStream logicalStream) {
        this.operators.add(new From(logicalStream));
        this.logicalStream = logicalStream;
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

    public Query print() {
        this.operators.add(new PrintOperator());
        return this;
    }

    public Query writeToFile(String filePath) {
        this.operators.add(new WriteToFileOperator(filePath));
        return this;
    }

    public void writeToCsv(String filePath) {
        this.operators.add(new WriteToCSVOperator(filePath));
    }

    public Query writeToZmq(String streamName, String host, int port) {
        this.operators.add(new WriteToZmqOperator(streamName, host, port));
        return this;
    }

    public Query writeToKafka(String brokers, String topic, int kafkaProducerTimeout) {
        this.operators.add(new WriteToKafkaOperator(brokers, topic, kafkaProducerTimeout));
        return this;
    }

    public Query writeToKafka(String topic, KafkaConfiguration configuration) {
        this.operators.add(new WriteToKafkaOperator(topic, configuration));
        return this;
    }

    public Query window(WindowDefinition windowDefinition, Aggregation aggregation) throws FieldNotFoundException, InvalidAggregationFieldException {
        // If aggregationFieldName not provided, set the fieldName using aggregationFieldIndex, otherwise (i.e. fieldName provided) set
        // aggregationFieldIndex using fieldName
        if (aggregation.getAggregationFieldName() == null) {
            // Check if index in the bound
            boolean isAggregationFieldInLogicalStreamListBound = (aggregation.getAggregationFieldIndex() >= 0)
                    && (aggregation.getAggregationFieldIndex() < this.logicalStream.getFieldList().size());
            if (isAggregationFieldInLogicalStreamListBound) {
                aggregation.setAggregationFieldName(this.logicalStream.getFieldList().get(aggregation.getAggregationFieldIndex()).getName());
            } else {
                throw new FieldIndexOutOfBoundException(aggregation.getAggregationFieldIndex(),
                        logicalStream.getName(), this.logicalStream.getFieldList().size());
            }
        } else {
            // Check if aggregationFieldName is in the LogicalStream's fieldList
            boolean isFieldInTheLogicalStreamFieldList = false;
            for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
                if (this.logicalStream.getFieldList().get(i).getName().equals(aggregation.getAggregationFieldName())) {
                    isFieldInTheLogicalStreamFieldList = true;
                    aggregation.setAggregationFieldIndex(i);
                    break;
                }
            }
            // Throw fieldNotFoundException if the aggregationFieldName is not in the list
            if (!(isFieldInTheLogicalStreamFieldList)) {
                throw new FieldNotFoundException(aggregation.getAggregationFieldName(), this.logicalStream.getName());
            }
        }

        // Perform type Checking
        Field aggregationField = this.logicalStream.getFieldList().get(aggregation.getAggregationFieldIndex());
        // check if aggregated field has type of subclass of Number
        if (Number.class.isAssignableFrom(aggregationField.getJavaDataType())) {
            // set the aggregation field
            this.operators.add(new WindowOperator( windowDefinition, aggregation));
            return this;
        } else {
            throw new InvalidAggregationFieldException(aggregationField.getType(), aggregationField.getJavaDataType().getSimpleName());
        }
    }

    public Query windowByKey(String fieldName, WindowDefinition windowDefinition, Aggregation aggregation) throws FieldNotFoundException, InvalidAggregationFieldException {
        // Check if key is in the LogicalStream's fieldList
        boolean iskeyInTheLogicalStreamFieldList = false;
        for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
            if (this.logicalStream.getFieldList().get(i).getName().equals(fieldName)) {
                iskeyInTheLogicalStreamFieldList = true;
                break;
            }
        }
        // Throw fieldNotFoundException if the aggregationFieldName is not in the list
        if (!(iskeyInTheLogicalStreamFieldList)) {
            throw new FieldNotFoundException(fieldName, this.logicalStream.getName());
        }

        // If aggregationFieldName not provided, set the fieldName using aggregationFieldIndex, otherwise (i.e. fieldName provided) set
        // aggregationFieldIndex using fieldName
        if (aggregation.getAggregationFieldName() == null) {
            // Check if index in the bound
            boolean isAggregationFieldInLogicalStreamListBound = (aggregation.getAggregationFieldIndex() >= 0)
                    && (aggregation.getAggregationFieldIndex() < this.logicalStream.getFieldList().size());
            if (isAggregationFieldInLogicalStreamListBound) {
                aggregation.setAggregationFieldName(this.logicalStream.getFieldList().get(aggregation.getAggregationFieldIndex()).getName());
            } else {
                throw new FieldIndexOutOfBoundException(aggregation.getAggregationFieldIndex(),
                        logicalStream.getName(), this.logicalStream.getFieldList().size());
            }
        } else {
            // Check if aggregationFieldName is in the LogicalStream's fieldList
            boolean isFieldInTheLogicalStreamFieldList = false;
            for (int i = 0; i < this.logicalStream.getFieldList().size(); i++) {
                if (this.logicalStream.getFieldList().get(i).getName().equals(aggregation.getAggregationFieldName())) {
                    isFieldInTheLogicalStreamFieldList = true;
                    aggregation.setAggregationFieldIndex(i);
                    break;
                }
            }
            // Throw fieldNotFoundException if the aggregationFieldName is not in the list
            if (!(isFieldInTheLogicalStreamFieldList)) {
                throw new FieldNotFoundException(aggregation.getAggregationFieldName(), this.logicalStream.getName());
            }
        }

        // Perform type Checking
        Field aggregationField = this.logicalStream.getFieldList().get(aggregation.getAggregationFieldIndex());
        // check if aggregated field has type of subclass of Number
        if (Number.class.isAssignableFrom(aggregationField.getJavaDataType())) {
            // set the aggregation field
            this.operators.add(new WindowByKeyOperator(fieldName, windowDefinition, aggregation));
            return this;
        } else {
            throw new InvalidAggregationFieldException(aggregationField.getType(), aggregationField.getJavaDataType().getSimpleName());
        }
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