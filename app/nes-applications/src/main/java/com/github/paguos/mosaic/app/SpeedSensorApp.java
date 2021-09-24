package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.config.CSpeedSensorApp;
import com.github.paguos.mosaic.app.directory.LocationDirectory;
import com.github.paguos.mosaic.app.directory.RSULocationData;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.ConfigurableApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.objects.addressing.IpResolver;
import org.eclipse.mosaic.lib.objects.v2x.MessageRouting;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.Inet4Address;
import java.util.List;

public class SpeedSensorApp extends ConfigurableApplication<CSpeedSensorApp, VehicleOperatingSystem> implements VehicleApplication, CommunicationApplication {

    public SpeedSensorApp() {
        super(CSpeedSensorApp.class, "SpeedSensorApp");
    }

    @Override
    public void onStartup() {
        getLog().infoSimTime(this, "Initializing speed sensor application ...");
        getOs().getCellModule().enable();
        getLog().infoSimTime(this, "Cell module activated!");
    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData vehicleData, @Nonnull VehicleData newVehicleData) {
        getLog().infoSimTime(this, "Sending speed report ...");
        if (getConfiguration().rsuEnabled) {
            sendRSUAdHocBroadcast(newVehicleData);
        } else {
            sendVehicleAdHocBroadcast(newVehicleData);
        }
        getLog().infoSimTime(this, "Speed report sent!");
    }

    private void sendRSUAdHocBroadcast(VehicleData vehicleData) {
        List<RSULocationData> RSULocationData = LocationDirectory.getRoadSideUnitsInRange(vehicleData.getPosition());

        for (RSULocationData rsu: RSULocationData) {
            Inet4Address rsuIp = IpResolver.getSingleton().nameToIp(rsu.getId());
            MessageRouting routing = getOs().getCellModule().createMessageRouting().topoCast(rsuIp.getAddress());
            sendSeepReport(vehicleData, routing);
        }

    }

    private void sendVehicleAdHocBroadcast(VehicleData vehicleData) {
        if (LocationDirectory.isLocationInRange(vehicleData.getPosition())) {
            Inet4Address vehicleIp = IpResolver.getSingleton().nameToIp(LocationDirectory.getSinkId());
            MessageRouting routing = getOs().getCellModule().createMessageRouting().topoCast(vehicleIp.getAddress());
            sendSeepReport(vehicleData, routing);
        }
    }

    private void sendSeepReport(VehicleData vehicleData, MessageRouting routing) {
        final SpeedReport report = new SpeedReport(
                getOs().getSimulationTime(),
                getOs().getId(),
                vehicleData.getPosition(),
                vehicleData.getSpeed()
        );

        final SpeedReportMsg message = new SpeedReportMsg(routing, report);
        getOs().getCellModule().sendV2xMessage(message);

    }

    @Override
    public void onMessageReceived(ReceivedV2xMessage receivedV2xMessage) {}

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {}

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {}

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {}

    @Override
    public void onShutdown() {}

    @Override
    public void processEvent(Event event) {}

}
