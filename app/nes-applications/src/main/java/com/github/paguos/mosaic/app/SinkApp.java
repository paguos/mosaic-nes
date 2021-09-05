package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.directory.LocationDirectory;
import com.github.paguos.mosaic.app.directory.VehicleLocationData;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import com.github.paguos.mosaic.app.output.SpeedReportWriter;
import com.github.paguos.mosaic.fed.geo.GeoSquare;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.VehicleApplication;
import org.eclipse.mosaic.fed.application.app.api.os.VehicleOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.objects.v2x.V2xMessage;
import org.eclipse.mosaic.lib.objects.vehicle.VehicleData;
import org.eclipse.mosaic.lib.util.scheduling.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;


public class SinkApp extends AbstractApplication<VehicleOperatingSystem> implements VehicleApplication, CommunicationApplication {

    private GeoSquare range;
    private SpeedReportWriter reportWriter;

    @Override
    public void onVehicleUpdated(@Nullable VehicleData vehicleData, @Nonnull VehicleData vehicleData1) {
        range = new GeoSquare(vehicleData1.getPosition(), 1000000);
    }

    @Override
    public void onStartup() {
        getLog().infoSimTime(this, "Activating Cell Module ...");
        getOs().getCellModule().enable();
        getLog().infoSimTime(this, "Activated AdHoc Module");

        getLog().infoSimTime(this, "Registering focal point ..");
        LocationDirectory.register(new VehicleLocationData(getOs().getId(), null));
        getLog().infoSimTime(this, "Focal point registered!");

        try {
            reportWriter = new SpeedReportWriter(getOs().getId());
        } catch (IOException e) {
            getLog().error("Error creating file writer!");
            getLog().error(e.getMessage());
        }
    }

    @Override
    public void onMessageReceived(ReceivedV2xMessage receivedV2xMessage) {
        getLog().infoSimTime(this, "Received V2X Message from {}", receivedV2xMessage.getMessage().getRouting().getSource().getSourceName());
        V2xMessage msg = receivedV2xMessage.getMessage();
        if (msg instanceof SpeedReportMsg) {
            processSpeedReportMsg((SpeedReportMsg) msg);
        }
    }

    private void processSpeedReportMsg (SpeedReportMsg msg) {
        SpeedReport report = msg.getReport();
        if (range.contains(msg.getReport().getVehiclePosition())) {
            try {
                reportWriter.write(report);
            } catch (IOException e) {
                getLog().error("Error while writing report!");
                getLog().error(e.getMessage());
            }
        }
    }

    @Override
    public void onShutdown() {}

    @Override
    public void processEvent(Event event) throws Exception {}

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {}

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {}

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {}
}
