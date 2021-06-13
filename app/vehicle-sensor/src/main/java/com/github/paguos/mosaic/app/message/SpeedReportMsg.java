package com.github.paguos.mosaic.app.message;

import org.eclipse.mosaic.lib.objects.v2x.EncodedPayload;
import org.eclipse.mosaic.lib.objects.v2x.MessageRouting;
import org.eclipse.mosaic.lib.objects.v2x.V2xMessage;

import javax.annotation.Nonnull;

public class SpeedReportMsg extends V2xMessage {

    private final SpeedReport report;

    public SpeedReportMsg(MessageRouting routing, SpeedReport report) {
        super(routing);
        this.report = report;
    }

    public SpeedReport getReport() {
        return report;
    }

    @Nonnull
    @Override
    public EncodedPayload getPayLoad() {
        return null;
    }

    @Override
    public String toString(){
        return String.format("SpeedReportMsg %s", report);
    }

}
