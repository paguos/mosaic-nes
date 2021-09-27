package com.github.paguos.mosaic.app;

import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.app.message.SpeedReportMsg;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.CamBuilder;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedAcknowledgement;
import org.eclipse.mosaic.fed.application.ambassador.simulation.communication.ReceivedV2xMessage;
import org.eclipse.mosaic.fed.application.app.api.CommunicationApplication;
import org.eclipse.mosaic.fed.application.app.api.os.RoadSideUnitOperatingSystem;
import org.eclipse.mosaic.interactions.communication.V2xMessageTransmission;
import org.eclipse.mosaic.lib.objects.v2x.V2xMessage;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.rti.api.InternalFederateException;

public class NesRSUSourceApp extends NesSourceApp<RoadSideUnitOperatingSystem> implements CommunicationApplication {

    @Override
    public void onStartup() {
        start();
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
        processSpeedReportMsg(report);
    }

    @Override
    public void onShutdown() {
        shutdown();
    }

    @Override
    public void processEvent(Event event) {}

    @Override
    public void onAcknowledgementReceived(ReceivedAcknowledgement receivedAcknowledgement) {}

    @Override
    public void onCamBuilding(CamBuilder camBuilder) {}

    @Override
    public void onMessageTransmitted(V2xMessageTransmission v2xMessageTransmission) {}

}
