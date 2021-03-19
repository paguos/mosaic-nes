package stream.nebula.operators;

public class Aggregation {
    String aggregationFunction;
    int aggregationFieldIndex;
    String aggregationFieldName;

    private Aggregation(String function, int fieldIndex){
        this.aggregationFunction = function;
        this.aggregationFieldIndex = fieldIndex;
    }

    private Aggregation(String aggregationFunction, String fieldName){
        this.aggregationFunction = aggregationFunction;
        this.aggregationFieldName = fieldName;
    }

    public static Aggregation sum(int fieldIndex){
        return new Aggregation("Sum",fieldIndex);
    }

    public static Aggregation sum(String fieldName){
        return new Aggregation("Sum",fieldName);
    }

    public String toString(){
        return this.aggregationFunction+"(Attribute(\""+this.aggregationFieldName +"\"))";
    }

    public String getAggregationFieldName() {
        return aggregationFieldName;
    }

    public void setAggregationFieldIndex(int aggregationFieldIndex) {
        this.aggregationFieldIndex = aggregationFieldIndex;
    }

    public String getAggregationFunction() {
        return aggregationFunction;
    }

    public int getAggregationFieldIndex() {
        return aggregationFieldIndex;
    }

    public void setAggregationFieldName(String aggregationFieldName) {
        this.aggregationFieldName = aggregationFieldName;
    }
}
