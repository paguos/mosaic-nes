package com.nebula.stream;

import org.junit.Assert;
import org.junit.Test;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.sink.*;
import stream.nebula.queryinterface.Query;

public class SinkOperatorTest {
    @Test
    public void testAddPrintSink() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .sink(new PrintSink());

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".sink(PrintSinkDescriptor::create());"
                , query.generateCppCode());
    }

    @Test
    public void testAddZMQSink() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .sink(new ZMQSink("localhost", 1234));

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".sink(ZmqSinkDescriptor::create(\"localhost\",1234));"
                , query.generateCppCode());
    }

    @Test
    public void testAddKafkaSink() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .sink(new KafkaSink("test","localhost:9092", 60));

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".sink(KafkaSinkDescriptor::create(\"test\",\"localhost:9092\",60));"
                , query.generateCppCode());
    }

    @Test
    public void testAddNullOutputSink() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .sink(new NullOutputSink());

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".sink(NullOutputSinkDescriptor::create());"
                , query.generateCppCode());
    }

    @Test
    public void testAddFileSink() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .sink(new FileSink("/tmp/out.txt", "CSV", "OVERRIDE"));

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".sink(FileSinkDescriptor::create(\"/tmp/out.txt\",\"CSV\",\"OVERRIDE\"));"
                , query.generateCppCode());
    }

    @Test
    public void testAddFMQTTSink() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .sink(new MQTTSink("127.0.0.1:8081", "nes-mqtt-test-client", "v1/devices/me/telemetry","rfRqLGZRChg8eS30PEeR", "5", "MQTTSinkDescriptor::milliseconds", "500", "MQTTSinkDescriptor::atLeastOnce", "true"));

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".sink(MQTTSinkDescriptor::create(\"127.0.0.1:8081\",\"nes-mqtt-test-client\",\"v1/devices/me/telemetry\",\"rfRqLGZRChg8eS30PEeR\",5,MQTTSinkDescriptor::milliseconds,500,MQTTSinkDescriptor::atLeastOnce,true));"
                , query.generateCppCode());
    }

}
