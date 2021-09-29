package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.message.SpeedReport;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NesVehicleSourceApp extends NesSourceApp<VehicleOperatingSystem> implements VehicleApplication {

    @Override
    public void onStartup() {
        start();
    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData oldVehicleData, @Nonnull VehicleData newVehicleData) {
        if (getConfiguration().movingRangeEnabled) {
            try {
                nesClient.updateLocation(getOs().getId(), newVehicleData.getPosition());
            } catch (InternalFederateException e) {
                getLog().error("Error updating the location!");
                getLog().error(e.getMessage());
            }
        }

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
