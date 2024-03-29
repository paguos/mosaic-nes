package com.github.paguos.mosaic.fed.config.util;

import com.github.paguos.mosaic.fed.config.CNes;
import org.eclipse.mosaic.lib.util.objects.ObjectInstantiation;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConfigurationReader {

    private static CNes config;
    private static final Logger log = LoggerFactory.getLogger(ConfigurationReader.class);

    public static CNes getConfig() throws InternalFederateException {
        if (config == null) {
            throw new InternalFederateException("Configuration was not imported!");
        }
        return config;
    }

    /**
     * Small helper class, which returns the instantiated object of a json-configuration.
     *
     * @param path the path to the configuration
     * @throws InstantiationException if there was an error during deserialization/instantiation
     */
    public static void importNesConfiguration(String path) throws InternalFederateException {

        if (!new File(path).exists()) {
            throw new InternalFederateException(
                    String.format("The nes config '%s' does not exist.", new File(path).getName())
            );
        }

        try {
            config = new ObjectInstantiation<>(CNes.class).readFile(new File(path));
        } catch (InstantiationException e) {
            log.error(String.format("Could not read configuration: %s", path), e);
            throw new InternalFederateException(e);
        }

        if (config.coordinator == null || config.worker == null || config.nodes == null) {
            throw new InternalFederateException("Invalid nes config file!");
        }
    }
}
