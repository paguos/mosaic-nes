package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.config.CNesSinkApp;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQSink;
import org.eclipse.mosaic.fed.application.app.ConfigurableApplication;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.TIME;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NesSinkApp extends ConfigurableApplication<CNesSinkApp, VehicleOperatingSystem> implements VehicleApplication {

    private final CNesSinkApp config = new CNesSinkApp();
    private final Queue<String> receivedMessages = new LinkedList<>();
    private final List<Thread> queryExecutors = new LinkedList<>();
    private final NesClient nesClient = new NesClient(config.nesRestApiHost, config.nesRestApiPort);

    private NesQueryExecutor queryExecutor;
    private ZeroMQSink zeroMQSink;

    public NesSinkApp() {
        super(CNesSinkApp.class, "NesSinkApp");
    }

    @Override
    public void onStartup() {
        getLog().info("Starting NES ZMQ Sink ...");
        zeroMQSink = new ZeroMQSink(config.zmqAddress, receivedMessages);
        Thread zmqSinkThread = new Thread(zeroMQSink);
        zmqSinkThread.start();
        getLog().info("NES ZMQ Sink started!");

        queryExecutor = new NesQueryExecutor(getLog(), nesClient, receivedMessages);
        scheduleNextEvent();
    }

    @Override
    public void onShutdown() {
        getLog().info("Wait for query executors ...");
        for(Thread thread: queryExecutors) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                getLog().error("Interrupted waiting for thread to end.");
                e.printStackTrace();
            }
        }
        getLog().info("Query executors finished!");

        getLog().info("Stopping NES ZMQ Sink ...");
        zeroMQSink.terminate();
        getLog().info("NES ZMQ Sink stopped!");
    }

    @Override
    public void processEvent(Event event) {
        queryExecutor.setLocation(getOs().getPosition());
        Thread queryExecutorThread = new Thread(queryExecutor);
        queryExecutorThread.start();
        queryExecutors.add(queryExecutorThread);
        scheduleNextEvent();
    }

    private void scheduleNextEvent() {
        long nextEventTime = getOs().getSimulationTime() + config.queryInterval * TIME.SECOND;
        getOs().getEventManager().addEvent(new Event(nextEventTime, this));
    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData vehicleData, @Nonnull VehicleData vehicleData1) {}
}
