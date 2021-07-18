package com.github.paguos.mosaic.fed.ambassador;

import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.lib.util.scheduling.EventManager;
import org.eclipse.mosaic.rti.api.AbstractFederateAmbassador;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.eclipse.mosaic.rti.api.parameters.AmbassadorParameter;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Ambassador for the NES framework which handles the interaction with Eclipse MOSAIC.
 */
public class NesAmbassador extends AbstractFederateAmbassador implements EventManager {

    private NesController controller;

    public NesAmbassador(AmbassadorParameter ambassadorParameter) {
        super(ambassadorParameter);
    }

    @Override
    public void initialize(final long startTime, final long endTime) throws InternalFederateException {
        super.initialize(startTime, endTime);

        log.info("Initializing NES Federate ...");
        readConfiguration();
        controller = NesController.getController();

        if (log.isTraceEnabled()) {
            log.trace("subscribedInteractions: {}", Arrays.toString(this.rti.getSubscribedInteractions().toArray()));
        }

        log.info("Starting NES ...");
        controller.start();
        log.info("NES coordinator and nodes started!");

        log.info("NES Federate initialized!");
    }

    private void readConfiguration() throws InternalFederateException {
        log.debug("Read nes Configuration");

        if (log.isTraceEnabled()) {
            log.trace("Opening nes configuration file {}", ambassadorParameter.configuration);
        }

        String nesConfigurationPath = ambassadorParameter.configuration.getAbsolutePath();
        ConfigurationReader.importNesConfiguration(nesConfigurationPath);
    }

    @Override
    public void finishSimulation() throws InternalFederateException {
        super.finishSimulation();
        log.info("Stopping NES ...");
        controller.stop();
        log.info("NES stopped!");
    }

    @Override
    public void addEvent(@Nonnull Event event) {}

    @Override
    public boolean isTimeConstrained() {
        return false;
    }

    @Override
    public boolean isTimeRegulating() {
        return false;
    }

}
