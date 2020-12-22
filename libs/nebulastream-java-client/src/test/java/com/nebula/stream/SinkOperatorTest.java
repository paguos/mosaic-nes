package com.nebula.stream;

import org.junit.BeforeClass;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import org.junit.Assert;
import org.junit.Test;
import stream.nebula.queryinterface.Query;
import stream.nebula.queryinterface.KafkaConfiguration;

import java.util.ArrayList;

public class SinkOperatorTest {
    private static LogicalStream defaultLogical;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        fieldArrayList.add(new Field("id","UINT32"));
        fieldArrayList.add(new Field("value","UINT32"));
        defaultLogical = new LogicalStream("default_logical", fieldArrayList);
    }

    @Test
    public void printSinkTest() {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                .print();

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\")" +
                        ".sink(PrintSinkDescriptor::create());"
                , query.generateCppCode());
    }

    // Currently this test is disabled until there is a clear way of defining CSVSink
    @Test (expected = UnsupportedOperationException.class)
    public void writeToCSVTest(){
        Query query;

        String csvFilePath = "output.csv";

        query = new Query();
        query.from(defaultLogical)
                .writeToCsv(csvFilePath);

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\")" +
                        ".writeToCSV(\""+csvFilePath+"\");"
                , query.generateCppCode());
    }

    // Currently this test is disabled until there is a clear way of defining ZMQSink
    @Test (expected = UnsupportedOperationException.class)
    public void writeToZmqGenerateCorrectCppCode() {
        Query query;
        String streamName = "default_logical";

        query = new Query();
        query.from(defaultLogical)
                .writeToZmq(streamName, "localhost", 5555);

        Assert.assertEquals("Query::from("+defaultLogical.getName()+")" +
                        ".writeToZmq("+defaultLogical.getName()+",\"localhost\",5555);"
                , query.generateCppCode());
    }

    // Currently this test is disabled until there is a clear way of defining KafkaSink
    @Test (expected = UnsupportedOperationException.class)
    public void writeToKafkaGenerateCorrectCppCode() {
        Query query;

        // Testing using broker-topic-timeout configuration method
        query = new Query();
        query.from(defaultLogical)
                .writeToKafka("broker1", "topic1", 1000);

        Assert.assertEquals("Query::from("+defaultLogical.getName()+")" +
                        ".writeToKafka(\"broker1\",\"topic1\",1000);"
                , query.generateCppCode());

        // Testing using topic-kafkaConfiguration configuration method
        KafkaConfiguration configuration = new KafkaConfiguration()
                .set("request.timeout.ms",30000)
                .set("group.id","nes")
                .set("enable.auto.commit",false);

        query = new Query();
        query.from(defaultLogical)
                .writeToKafka("topic1", configuration);

        Assert.assertEquals("InputQuery::from("+defaultLogical.getName()+")" +
                        ".writeToKafka(\"topic1\",{{\"enable.auto.commit\",false}," +
                        "{\"request.timeout.ms\",30000}," + "{\"group.id\",\"nes\"},});"
                , query.generateCppCode());
    }




}
