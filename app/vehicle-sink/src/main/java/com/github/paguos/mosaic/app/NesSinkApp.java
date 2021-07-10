package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.config.CNesSinkApp;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQSink;
import org.eclipse.mosaic.fed.application.app.ConfigurableApplication;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.lib.geo.GeoPoint;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.TIME;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.sink.ZMQSink;
import stream.nebula.queryinterface.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

public class NesSinkApp extends ConfigurableApplication<CNesSinkApp, VehicleOperatingSystem> implements VehicleApplication {

    private final CNesSinkApp config = new CNesSinkApp();
    private final Queue<String> receivedMessages = new LinkedList<>();
    private final NesClient nesClient = new NesClient(config.nesRestApiHost, config.nesRestApiPort);

    private int currenQueryId;
    private Thread sinkThread;
    private final int zeroMQPort;
    private ZeroMQSink zeroMQSink;

    public NesSinkApp() {
        super(CNesSinkApp.class, "NesSinkApp");
        this.currenQueryId = -1;
        this.zeroMQPort = ZeroMQSink.getNextZeroMQPort();
    }

    @Override
    public void onStartup() {
        getLog().info("Starting NES ZMQ Sink ...");
        String zmqAddress = String.format("tcp://127.0.0.1:%d", zeroMQPort);
        zeroMQSink = new ZeroMQSink(zmqAddress, receivedMessages);
        sinkThread = new Thread(zeroMQSink);
        sinkThread.start();
        getLog().info("NES ZMQ Sink started!");

        scheduleNextEvent();
    }

    @Override
    public void onShutdown() {
        getLog().info("Stopping NES ZMQ Sink ...");
        zeroMQSink.terminate();

        getLog().info("Waiting for sink ...");
        try {
            sinkThread.join(5000);
        } catch (InterruptedException e) {
            getLog().error("Interrupted waiting for thread to end.");
            e.printStackTrace();
        }

        getLog().info("NES ZMQ Sink stopped!");
    }

    @Override
    public void processEvent(Event event) {
        if (currenQueryId != -1) {
            deleteQuery();
        }
        synchronized (receivedMessages) {
            consumeMessages();
            submitQuery(getOs().getPosition());
        }
        scheduleNextEvent();
    }

    private void scheduleNextEvent() {
        long nextEventTime = getOs().getSimulationTime() + config.queryInterval * TIME.SECOND;
        getOs().getEventManager().addEvent(new Event(nextEventTime, this));
    }

    private void deleteQuery () {
        getLog().info("Deleting query ...");
        try {
            nesClient.deleteQuery(currenQueryId);
        } catch (InternalFederateException e) {
            getLog().error("Error while deleting the query!");
        }
        getLog().info("Query deleted!");

        currenQueryId = -1;
    }

    private void submitQuery(GeoPoint location) {
        try {
            getLog().info("Submitting query ...");
            Query query = new Query().from("mosaic_nes")
                    .sink(new ZMQSink("localhost", zeroMQPort));
            getLog().debug(String.format("Query: %s", query.generateCppCode()));
            currenQueryId = nesClient.executeQuery(query);
            getLog().info("Query submitted!");
            getLog().info(String.format(
                    "Query for location: %f/%f",
                    location.getLatitude(),
                    location.getLongitude()
            ));
        } catch (EmptyFieldException e) {
            getLog().error("Error creating query!");
            getLog().error(e.getMessage());
        } catch (InternalFederateException e) {
            getLog().error("Error executing the query!");
            getLog().error(e.getMessage());
        }
    }

    private void consumeMessages() {
        while (!receivedMessages.isEmpty()) {
            getLog().info(String.format("Message received: %s",  receivedMessages.poll()));
        }
    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData vehicleData, @Nonnull VehicleData vehicleData1) {}
}
