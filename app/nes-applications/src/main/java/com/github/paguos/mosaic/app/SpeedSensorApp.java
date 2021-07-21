package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.directory.RSUDirectory;
import com.github.paguos.mosaic.app.directory.RoadSideUnit;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
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

public class SpeedSensorApp extends AbstractApplication<VehicleOperatingSystem> implements VehicleApplication, CommunicationApplication {

    @Override
    public void onStartup() {
        getLog().infoSimTime(this, "Initializing speed sensor application ...");
        getOs().getCellModule().enable();
        getLog().infoSimTime(this, "Cell module activated!");
    }

    private void sendAdHocBroadcast(VehicleData vehicleData) {
        List<RoadSideUnit> roadSideUnits = RSUDirectory.getRoadSideUnitsInRange(vehicleData.getPosition());

        for (RoadSideUnit rsu: roadSideUnits) {
            Inet4Address rsuIp = IpResolver.getSingleton().nameToIp(rsu.getId());
            MessageRouting routing = getOs().getCellModule().createMessageRouting().topoCast(rsuIp.getAddress());

            final SpeedReport report = new SpeedReport(
                    getOs().getSimulationTime(),
                    getOs().getId(),
                    vehicleData.getPosition(),
                    vehicleData.getSpeed()
            );

            final SpeedReportMsg message = new SpeedReportMsg(routing, report);
            getOs().getCellModule().sendV2xMessage(message);
        }

    }

    @Override
    public void onVehicleUpdated(@Nullable VehicleData vehicleData, @Nonnull VehicleData vehicleData1) {
        getLog().infoSimTime(this, "Sending speed report ...");
        sendAdHocBroadcast(vehicleData1);
        getLog().infoSimTime(this, "Speed report sent!");
    }

    @Override
    public void onMessageReceived(ReceivedV2xMessage receivedV2xMessage) {

    }

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {

    }

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {

    }

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {

    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void processEvent(Event event) {

    }
}
