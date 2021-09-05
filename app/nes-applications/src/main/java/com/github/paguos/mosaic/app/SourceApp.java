package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.directory.LocationDirectory;
import com.github.paguos.mosaic.app.directory.RSULocationData;
import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import com.github.paguos.mosaic.app.output.SpeedReportWriter;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.AbstractApplication;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.objects.addressing.IpResolver;
import org.eclipse.mosaic.lib.objects.v2x.MessageRouting;
import org.eclipse.mosaic.lib.objects.v2x.V2xMessage;
import org.eclipse.mosaic.lib.util.scheduling.Event;

import java.io.IOException;
import java.net.Inet4Address;

public class SourceApp  extends AbstractApplication<RoadSideUnitOperatingSystem> implements CommunicationApplication {

    private SpeedReportWriter reportWriter;

    @Override
    public void onStartup() {
        getLog().infoSimTime(this, "Activating Cell Module ...");
        getOs().getCellModule().enable();
        getLog().infoSimTime(this, "Activated AdHoc Module");

        getLog().infoSimTime(this, "Registering RSU ..");
        LocationDirectory.register(new RSULocationData(getOs().getId(), getOs().getPosition(), 100));
        getLog().infoSimTime(this, "RSU registered!");

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
        try {
            reportWriter.write(report);
        } catch (IOException e) {
            getLog().error("Error while writing report!");
            getLog().error(e.getMessage());
        }

        Inet4Address vehicleIp = IpResolver.getSingleton().nameToIp(LocationDirectory.getFocalVehicle().getId());
        MessageRouting routing = getOs().getCellModule().createMessageRouting().topoCast(vehicleIp.getAddress());

        final SpeedReportMsg message = new SpeedReportMsg(routing, report);
        getOs().getCellModule().sendV2xMessage(message);
    }

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {}

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {}

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {}

    @Override
    public void onShutdown() {}

    @Override
    public void processEvent(Event event) throws Exception {}
}
