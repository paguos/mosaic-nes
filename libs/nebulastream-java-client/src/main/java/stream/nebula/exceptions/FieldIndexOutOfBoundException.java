package stream.nebula.exceptions;

public class FieldIndexOutOfBoundException extends RuntimeException {
    public FieldIndexOutOfBoundException(int accessedFieldIndex, String logicalStreamName, int numberOfField) {
        super("Field index "+accessedFieldIndex+" is out of bound. " +
                "Logical stream \""+logicalStreamName+"\" has "+numberOfField+" field(s).");
    }

    public FieldIndexOutOfBoundException(String logicalStreamName, int numberOfField) {
        super("Field index is out of bound. " +
                "Logical stream \""+logicalStreamName+"\" has "+numberOfField+" field(s).");
    }
}
