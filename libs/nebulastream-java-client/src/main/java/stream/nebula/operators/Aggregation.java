package stream.nebula.operators;

public class Aggregation {
    String aggregationFunction;
    String aggregationFieldName;

    private Aggregation(String aggregationFunction, String fieldName){
        this.aggregationFunction = aggregationFunction;
        this.aggregationFieldName = fieldName;
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

    public String getAggregationFunction() {
        return aggregationFunction;
    }

    public void setAggregationFieldName(String aggregationFieldName) {
        this.aggregationFieldName = aggregationFieldName;
    }
}
