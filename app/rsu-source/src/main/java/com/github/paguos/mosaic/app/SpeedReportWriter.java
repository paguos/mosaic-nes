package com.github.paguos.mosaic.app;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import com.github.paguos.mosaic.app.message.SpeedReport;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SpeedReportWriter {

    public static String REPORT_NAME = "SpeedReport";
    public static String REPORT_EXTENSION = "csv";
    private final FileWriter fileWriter;

    public SpeedReportWriter(String rsuId) throws IOException {
        String fileName = String.format("%s-%s.%s", REPORT_NAME, rsuId, REPORT_EXTENSION);
        this.fileWriter = new FileWriter(getLogDirectory() + "/" + fileName);
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
    }

    /**
     * Get the log directory of the application
     * @return the directory of the logs
     */
    private static File getLogDirectory() {
        LoggerContext  context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (Logger logger: context.getLoggerList()) {
            Appender<ILoggingEvent> appender = logger.getAppender("ApplicationLog");

            if (appender instanceof FileAppender) {
                FileAppender<?> fileAppender = ((FileAppender<?>) appender);
                return new File(fileAppender.getFile()).getParentFile();
            }
        }

        return null;
    }

}
