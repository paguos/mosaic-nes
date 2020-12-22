package stream.nebula.operators;

import stream.nebula.queryinterface.KafkaConfiguration;

public class WriteToKafkaOperator extends Operator {
    String brokers;
    String topic;
    int kafkaProducerTimeout;
    KafkaConfiguration kafkaConfiguration;
    String configurationMethod;

    public WriteToKafkaOperator(String brokers, String topic, int kafkaProducerTimeout) {
        this.brokers = brokers;
        this.topic = topic;
        this.kafkaProducerTimeout = kafkaProducerTimeout;
        this.configurationMethod = "broker-topic-timeout";
    }

    public WriteToKafkaOperator(String topic, KafkaConfiguration configuration) {
        this.topic = topic;
        this.kafkaConfiguration = configuration;
        this.configurationMethod = "topic-kafkaConfiguration";
    }

    @Override
    public String getCppCode() {
        throw new UnsupportedOperationException();
//        if (configurationMethod.equals("broker-topic-timeout")) {
//            return ".writeToKafka(\"" + this.brokers + "\",\"" + this.topic + "\"," + kafkaProducerTimeout + ")";
//        } else   {
//            return ".writeToKafka(\"" + this.topic + "\"," + this.kafkaConfiguration.build() + ")";
//        }
    }
}
