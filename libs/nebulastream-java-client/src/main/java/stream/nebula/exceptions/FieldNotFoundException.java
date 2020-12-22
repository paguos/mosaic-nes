package stream.nebula.exceptions;

public class FieldNotFoundException extends Exception {
    public FieldNotFoundException(String fieldName, String logicalStreamName) {
        super("Field \""+fieldName+"\" is not found in the logical stream \""+logicalStreamName+"\".");
    }
}

