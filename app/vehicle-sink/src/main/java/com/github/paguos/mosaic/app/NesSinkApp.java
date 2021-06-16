package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.config.CNesSinkApp;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.fed.application.app.ConfigurableApplication;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.sink.ZMQSink;
import stream.nebula.queryinterface.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ArrayBlockingQueue;

public class NesSinkApp extends ConfigurableApplication<CNesSinkApp, VehicleOperatingSystem> implements VehicleApplication {

    private final CNesSinkApp config = new CNesSinkApp();
    private final ArrayBlockingQueue<String> receivedMessages = new ArrayBlockingQueue<>(config.messageQueueSize);
    private final NesClient nesClient = new NesClient(config.nesRestApiHost, config.nesRestApiPort);

    private int currenQueryId = -1;
    private Thread zmqSink;

    public NesSinkApp() {
        super(CNesSinkApp.class, "NesSinkApp");
    }

    @Override
    public void onStartup() {
        getLog().info("Starting NES ZMQ Sink ...");
        zmqSink = new Thread(new ZmqSink(config.zmqAddress, receivedMessages));
        zmqSink.start();
        getLog().info("NES ZMQ Sink started!");
    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData vehicleData, @Nonnull VehicleData vehicleData1) {
        while (!receivedMessages.isEmpty()) {
            getLog().info(String.format("Message received: %s",  receivedMessages.poll()));
        }

        if (currenQueryId == -1) {
            try {
                getLog().info("Submitting query ...");
                Query query = new Query().from("mosaic_nes")
                        .sink(new ZMQSink("localhost", 5555));
                getLog().debug(String.format("Query: %s", query.generateCppCode()));
                currenQueryId = nesClient.executeQuery(query);
                getLog().info("Query submitted!");
            } catch (EmptyFieldException e) {
                getLog().error("Error creating query!");
                getLog().error(e.getMessage());
            } catch (InternalFederateException e) {
                getLog().error("Error executing the query!");
            }
        }
    }


    @Override
    public void onShutdown() {
        if (currenQueryId != -1) {
            getLog().info("Deleting query ...");
            try {
                nesClient.deleteQuery(currenQueryId);
            } catch (InternalFederateException e) {
                getLog().error("Error while deleting the query!");
            }
            getLog().info("Query deleted!");
        }

        getLog().info("Stopping NES ZMQ Sink ...");
        zmqSink.interrupt();
        getLog().info("NES ZMQ Sink stopped!");
    }

    @Override
    public void processEvent(Event event) {

    }
}
