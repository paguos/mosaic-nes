package stream.nebula.operators;

public class WriteToFileOperator extends Operator {
    private final String filePath;

    public WriteToFileOperator(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getCppCode() {
        throw new UnsupportedOperationException();
//        return ".writeToFile(\""+this.filePath+"\")";
    }
}