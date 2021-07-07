package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.fed.application.ambassador.util.UnitLogger;
import org.eclipse.mosaic.lib.geo.GeoPoint;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.sink.ZMQSink;
import stream.nebula.queryinterface.Query;

import java.util.Queue;

public class NesQueryExecutor implements Runnable {

    private int currenQueryId;
    private GeoPoint location;

    private final UnitLogger logger;
    private final NesClient nesClient;
    private final Queue<String> receivedMessages;

    public NesQueryExecutor(UnitLogger logger, NesClient nesClient, Queue<String> receivedMessages) {
        this.logger = logger;
        this.nesClient = nesClient;
        this.receivedMessages = receivedMessages;

        this.currenQueryId = -1;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    @Override
    public void run() {
        if (currenQueryId != -1) {
            deleteQuery();
        }
        synchronized (receivedMessages) {
            consumeMessages();
            submitQuery();
        }
    }

    private void deleteQuery () {
        logger.info("Deleting query ...");
        try {
            nesClient.deleteQuery(currenQueryId);
        } catch (InternalFederateException e) {
            logger.error("Error while deleting the query!");
        }
        logger.info("Query deleted!");

        currenQueryId = -1;
    }

    private void submitQuery() {
        try {
            logger.info("Submitting query ...");
            Query query = new Query().from("mosaic_nes")
                    .sink(new ZMQSink("localhost", 5555));
            logger.debug(String.format("Query: %s", query.generateCppCode()));
            currenQueryId = nesClient.executeQuery(query);
            logger.info("Query submitted!");
            logger.info(String.format(
                    "Query for location: %f/%f",
                    location.getLatitude(),
                    location.getLongitude()
            ));
        } catch (EmptyFieldException e) {
            logger.error("Error creating query!");
            logger.error(e.getMessage());
        } catch (InternalFederateException e) {
            logger.error("Error executing the query!");
            logger.error(e.getMessage());
        }
    }

    private void consumeMessages() {
        while (!receivedMessages.isEmpty()) {
            logger.info(String.format("Message received: %s",  receivedMessages.poll()));
        }
    }
}
