package com.github.paguos.mosaic.fed.examples;

import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.Predicate;
import stream.nebula.operators.sink.FileSink;
import stream.nebula.queryinterface.Query;;

public class QueryCSVSink {

    public static void main(String[] args) throws EmptyFieldException, InternalFederateException {
        NesClient client = new NesClient("localhost", "8081");
        Query query = new Query().from("mosaic_nes").filter(Predicate.onField("speed").greaterThan(0))
                .sink(new FileSink("/home/parallels/Downloads/mosaic_nes.txt", "CSV_FORMAT", "APPEND"));
        client.executeQuery(query);
    }
}
