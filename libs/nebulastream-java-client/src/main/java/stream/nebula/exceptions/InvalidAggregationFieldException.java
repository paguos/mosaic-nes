package stream.nebula.exceptions;

public class InvalidAggregationFieldException extends Exception {
    public InvalidAggregationFieldException(String cppType, String javaType) {
        super("Can not aggregate field of type: "+cppType+"/"+javaType);
    }
}
