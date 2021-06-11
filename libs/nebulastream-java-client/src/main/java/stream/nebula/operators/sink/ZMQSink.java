package stream.nebula.operators.sink;

/**
 * Represents a NES ZMQ sink
 */
public class ZMQSink extends Sink {
    private String host;
    private int port;

    /**
     * Create a ZMQ sink
     * @param host host of the ZMQ sink
     * @param port port of the ZMQ sink
     */
    public ZMQSink(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getCppCode() {
        return ".sink(ZmqSinkDescriptor::create(\""+this.host+"\","+this.port+"))";
    }
}
