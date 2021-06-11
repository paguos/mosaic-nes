package stream.nebula.operators.sink;

/**
 * Represents a NES Kafka sink
 */
public class KafkaSink extends Sink {
    private String topic;
    private String broker;
    private int timeout;

    /**
     * Create a Kafka sink
     * @param topic The topic name of the Kafka sink
     * @param broker The broker of the Kafka sink
     * @param timeout The timeout of the Kafka sink
     */
    public KafkaSink(String topic, String broker, int timeout) {
        this.topic = topic;
        this.broker = broker;
        this.timeout = timeout;
    }

    @Override
    public String getCppCode() {
        return ".sink(KafkaSinkDescriptor::create(\""+this.topic+"\",\""+this.broker+"\","+timeout+"))";
    }
}
