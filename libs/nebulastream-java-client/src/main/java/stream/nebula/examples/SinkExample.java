package stream.nebula.examples;

import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.runtime.NebulaStreamRuntime;

import java.io.IOException;

/**
 * This example demonstrate how to use sink in NES Java Client. Currently it support zmq, kafka, and file sink with
 * writeToZmq(), writeToKafka(), and writeToFile method respectively.
 *
 * About writeToZmq()
 * The writeToZmq() method receive three parameters: the logical stream name (which can be extracted from a logical
 * stream object), the zmq host, and the zmq port.
 *
 * About writeToKafka()
 * The writeToKafka method can be called by either simply providing three parameters: broker name, topic name, and
 * kafkaProducer Timeout or by setting further configuration using KafkaConfiguration object. A kafka configuration
 * object can be first created and then its set() method can be called to set key-value pair of the configuration. After
 * that we can supply this configuration along with topic name to the writeToKafka() method.
 *
 * About writeToFile()
 * The writeToFile method expect a single string parameter of the desired filename to which the output is written.
 */

public class SinkExample {
    public static void main(String[] args) throws UnknownDataTypeException, IOException, RESTExecption {
        // Configure network connection to NES REST server
        NebulaStreamRuntime ner = NebulaStreamRuntime.getRuntime();
        ner.getConfig().setHost("localhost")
                .setPort("8081");

        // TODO: Example of using print sink (#88)

        // TODO: Example of using CSV sink (#88)

        // TODO: Example of using Binary sink (#88)
    }
}
