package stream.nebula.operators.sink;

/**
 * Represents a NES file sink
 */
public class FileSink extends Sink {
    private String path;
    private String textFormat;
    private String mode;

    /**
     * Create a file sink
     * @param path The path in the NES to store the file
     * @param textFormat Whether to use "CSV" or "BINARY"
     * @param mode Whether to "OVERRIDE" or "APPEND"
     */
    public FileSink(String path, String textFormat, String mode) {
        this.path = path;
        this.textFormat = textFormat;
        this.mode = mode;
    }

    @Override
    public String getCppCode() {
        return ".sink(FileSinkDescriptor::create(\""+this.path+"\",\""+this.textFormat+"\",\""+this.mode+"\"))";
    }
}
