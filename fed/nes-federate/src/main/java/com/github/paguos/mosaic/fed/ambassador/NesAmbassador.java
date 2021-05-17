package com.github.paguos.mosaic.fed.ambassador;

import org.eclipse.mosaic.fed.application.ambassador.SimulationKernel;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.lib.util.scheduling.EventManager;
import org.eclipse.mosaic.rti.api.AbstractFederateAmbassador;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.eclipse.mosaic.rti.api.parameters.AmbassadorParameter;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class NesAmbassador extends AbstractFederateAmbassador implements EventManager {

    public NesAmbassador(AmbassadorParameter ambassadorParameter) {
        super(ambassadorParameter);
    }

    @Override
    public void initialize(final long startTime, final long endTime) throws InternalFederateException {
        super.initialize(startTime, endTime);

        log.info("Initializing NES Federate ...");
        if (log.isTraceEnabled()) {
            log.trace("subscribedInteractions: {}", Arrays.toString(this.rti.getSubscribedInteractions().toArray()));
        }
        SimulationKernel.SimulationKernel.getCentralNavigationComponent().initialize(this.rti);
        log.info("Initializing NES Federate ... done!");
    }

    @Override
    public void addEvent(@Nonnull Event event) {

    }

    @Override
    public boolean isTimeConstrained() {
        return false;
    }

    @Override
    public boolean isTimeRegulating() {
        return false;
    }
}
