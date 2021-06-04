package stream.nebula.operators.sink;

/**
 * Represents a NES NullOutput sink
 */
public class NullOutputSink extends Sink {
    @Override
    public String getCppCode() {
        return ".sink(NullOutputSinkDescriptor::create())";
    }
}
