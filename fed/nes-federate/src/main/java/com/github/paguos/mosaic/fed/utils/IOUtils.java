package com.github.paguos.mosaic.fed.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.io.File;

public class IOUtils {

    /**
     * Get the log directory of the application
     * @return the directory of the logs
     */
    public static File getLogDirectory() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (Logger logger: context.getLoggerList()) {
            Appender<ILoggingEvent> appender = logger.getAppender("ApplicationLog");

            if (appender instanceof FileAppender) {
                FileAppender<?> fileAppender = ((FileAppender<?>) appender);
                return new File(fileAppender.getFile()).getParentFile();
            }
        }

        return null;
    }

    public static void reverseArray(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
}
