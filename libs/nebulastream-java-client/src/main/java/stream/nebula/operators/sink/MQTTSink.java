package stream.nebula.operators.sink;

/**
 * Represents a NES MQTT sink
 */
public class MQTTSink extends Sink {
    private String address;
    private String clientId;
    private String topic;
    private String user;
    private String maxBufferedMSGs;
    private String timeUnit;
    private String messageDelay;
    private String qualityOfService;
    private String asynchronousClient;

    /**
     * Create an MQTT sink
     * @param address: address name of MQTT broker
     * @param clientId: client ID for MQTT client
     * @param topic: MQTT topic chosen to publish client data to
     * @param user: user identification for client
     * @param maxBufferedMSGs: maximal number of messages that can be buffered by the client before disconnecting
     * @param timeUnit: time unit chosen by client user for message delay (e.g., 'MQTTSinkDescriptor::milliseconds')
     * @param messageDelay: time before next message is sent by client to broker
     * @param qualityOfService: either 'at most once' or 'at least once' (e.g., 'MQTTSinkDescriptor::atLeastOnce')
     * @param asynchronousClient: determine whether client is async- or synchronous ('true' or 'false')
     */
    public MQTTSink(String address, String clientId, String topic, String user, String maxBufferedMSGs, String timeUnit, String messageDelay, String qualityOfService, String asynchronousClient) {
        this.address = address;
        this.clientId = clientId;
        this.topic = topic;
        this.user = user;
        this.maxBufferedMSGs = maxBufferedMSGs;
        this.timeUnit = timeUnit;
        this.messageDelay = messageDelay;
        this.qualityOfService = qualityOfService;
        this.asynchronousClient = asynchronousClient;
    }

    @Override
    public String getCppCode() {
        return ".sink(MQTTSinkDescriptor::create(\""+this.address+"\",\""+this.clientId+"\",\""+this.topic+"\",\""
                +this.user+"\","+this.maxBufferedMSGs+","+this.timeUnit+","+this.messageDelay+","
                +this.qualityOfService+","+this.asynchronousClient+"))";
    }
}
