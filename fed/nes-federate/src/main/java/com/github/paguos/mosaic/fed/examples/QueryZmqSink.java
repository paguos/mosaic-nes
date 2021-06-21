package com.github.paguos.mosaic.fed.examples;

import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.sink.ZMQSink;
import stream.nebula.queryinterface.Query;


public class QueryZmqSink {

    public static void main(String[] args) throws EmptyFieldException, InternalFederateException {
        NesClient client = new NesClient("localhost", "8081");
        Query query = new Query().from("mosaic_nes")
                .sink(new ZMQSink("localhost", 5555));
        client.executeQuery(query);
    }
}
