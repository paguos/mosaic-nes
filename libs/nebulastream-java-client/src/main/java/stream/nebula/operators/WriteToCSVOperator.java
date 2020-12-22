package stream.nebula.operators;

public class WriteToCSVOperator extends Operator {
    private final String filePath;

    public WriteToCSVOperator(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getCppCode() {
        throw new UnsupportedOperationException();
        //return ".writeToCSV(\""+this.filePath+"\")";
    }
}