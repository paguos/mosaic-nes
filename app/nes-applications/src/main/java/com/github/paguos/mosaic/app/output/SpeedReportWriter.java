package com.github.paguos.mosaic.app.output;

import com.github.paguos.mosaic.app.message.SpeedReport;
import com.github.paguos.mosaic.fed.utils.IOUtils;

import java.io.FileWriter;
import java.io.IOException;

public class SpeedReportWriter {

    public static String REPORT_NAME = "SpeedReport";
    public static String REPORT_EXTENSION = "csv";
    private final FileWriter fileWriter;

    public SpeedReportWriter(String rsuId) throws IOException {
        String fileName = String.format("%s-%s.%s", REPORT_NAME, rsuId, REPORT_EXTENSION);
        this.fileWriter = new FileWriter(IOUtils.getLogDirectory() + "/" + fileName);
    }

    public void write(SpeedReport speedReport) throws IOException {
        String reportEntry = String.format(
                "%s,%d,%f,%f,%f\n",
                speedReport.getVehicleId(),
                speedReport.getTimestamp(),
                speedReport.getVehiclePosition().getLatitude(),
                speedReport.getVehiclePosition().getLongitude(),
                speedReport.getVehicleSpeed()
        );

        fileWriter.write(reportEntry);
        fileWriter.flush();
    }

    public void writeLine(String line) throws IOException {
        fileWriter.write(line + "\n");
        fileWriter.flush();
    }


}
