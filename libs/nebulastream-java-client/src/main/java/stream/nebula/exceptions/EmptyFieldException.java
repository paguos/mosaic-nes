package stream.nebula.exceptions;

public class EmptyFieldException extends Exception {

    public EmptyFieldException() {
        super("Field cannot be empty");
    }

    public EmptyFieldException(String field) {
        super(field + " cannot be empty");
    }

}
