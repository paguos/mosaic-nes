package stream.nebula.exceptions;

public class UnknownDataTypeException extends Exception {
    public UnknownDataTypeException(String message) {
        super("Unknown data type: "+message);
    }
}
