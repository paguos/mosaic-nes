package stream.nebula.operators.sink;

/**
 * Represents a NES print sink
 */
public class PrintSink extends Sink {
    @Override
    public String getCppCode() {
        return ".sink(PrintSinkDescriptor::create())";
    }
}
