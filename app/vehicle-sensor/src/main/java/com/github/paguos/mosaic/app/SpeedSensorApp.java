package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.AdHocModuleConfiguration;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.enums.AdHocChannel;
import org.eclipse.mosaic.lib.geo.GeoCircle;
import org.eclipse.mosaic.lib.geo.GeoPoint;
import org.eclipse.mosaic.lib.objects.v2x.MessageRouting;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.TIME;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpeedSensorApp extends AbstractApplication<VehicleOperatingSystem> implements VehicleApplication, CommunicationApplication {

    /**
     * Interval at which messages are sent (every 2 seconds).
     */
    private final static long TIME_INTERVAL = 2 * TIME.SECOND;


    @Override
    public void onStartup() {
        getLog().infoSimTime(this, "Initializing speed sensor application ...");
        getOs().getAdHocModule().enable(new AdHocModuleConfiguration()
                .addRadio()
                .channel(AdHocChannel.CCH)
                .power(50)
                .create());
        getLog().infoSimTime(this, "Activated AdHoc Module");
    }

    private void sendAdHocBroadcast(VehicleData vehicleData) {
        GeoPoint center = GeoPoint.latLon(52.5128417, 13.3213595);
        GeoCircle adHocModule = new GeoCircle(center, 100);

        if (adHocModule.contains(vehicleData.getPosition())){
            MessageRouting routing = getOs().getAdHocModule().createMessageRouting().viaChannel(AdHocChannel.CCH).topoBroadCast();
            final SpeedReport report = new SpeedReport(
                    getOs().getSimulationTime(),
                    getOs().getId(),
                    vehicleData.getPosition(),
                    vehicleData.getSpeed()
            );
            final SpeedReportMsg message = new SpeedReportMsg(routing, report);
            getOs().getAdHocModule().sendV2xMessage(message);
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
    public void processEvent(Event event) throws Exception {

    }
}
