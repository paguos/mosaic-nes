package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.message.SpeedReport;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NesVehicleSourceApp extends NesSourceApp<VehicleOperatingSystem> implements VehicleApplication {

    @Override
    public void onStartup() {
        start();
    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData oldVehicleData, @Nonnull VehicleData newVehicleData) {
        final SpeedReport report = new SpeedReport(
                getOs().getSimulationTime(),
                getOs().getId(),
                newVehicleData.getPosition(),
                newVehicleData.getSpeed()
        );
        processSpeedReportMsg(report);
    }

    @Override
    public void onShutdown() {
        shutdown();
    }

    @Override
    public void processEvent(Event event) {}

}
