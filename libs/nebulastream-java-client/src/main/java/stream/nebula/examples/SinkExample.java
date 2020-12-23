package stream.nebula.examples;

import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.runtime.NebulaStreamRuntime;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.queryinterface.Query;
import stream.nebula.queryinterface.KafkaConfiguration;

import java.io.IOException;
import java.util.List;

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

        // Get a list of available logical stream and choose one
        List<LogicalStream> availableLogicalStream = ner.getAvailableLogicalStreams();
        LogicalStream defaultLogical = availableLogicalStream.get(0);

        Query query;

        // Example of using writeToZmq
        query = new Query();
        query.from(defaultLogical)
                .writeToZmq(defaultLogical.getName(), "localhost", 5555);

        System.out.println(query.generateCppCode());
        System.out.println("============================================================");

        // Example of writeToKafka with kafkaProducerTimeout
        query = new Query();
        query.from(defaultLogical)
                .writeToKafka("broker1", "topic1", 1000);
        System.out.println(query.generateCppCode());
        System.out.println("============================================================");

        // Example of using topic-kafkaConfiguration configuration method
        KafkaConfiguration configuration = new KafkaConfiguration()
                .set("request.timeout.ms",30000)
                .set("group.id","nes")
                .set("enable.auto.commit",false);

        query = new Query();
        query.from(defaultLogical)
                .writeToKafka("topic1", configuration);
        System.out.println(query.generateCppCode());
        System.out.println("============================================================");

        // Example of using writeToFile
        query = new Query();
        query.from(defaultLogical)
                .writeToFile("blob.txt");
        System.out.println(query.generateCppCode());
    }
}
