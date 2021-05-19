package com.github.paguos.mosaic.fed.ambassador;

import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.docker.DockerContainerController;
import org.eclipse.mosaic.lib.util.scheduling.Event;
import org.eclipse.mosaic.lib.util.scheduling.EventManager;
import org.eclipse.mosaic.rti.api.AbstractFederateAmbassador;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.eclipse.mosaic.rti.api.parameters.AmbassadorParameter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Ambassador for the NES framework which handles the interaction with Eclipse MOSAIC.
 */
public class NesAmbassador extends AbstractFederateAmbassador implements EventManager {

    private CNes config;

    public NesAmbassador(AmbassadorParameter ambassadorParameter) {
        super(ambassadorParameter);
    }

    @Override
    public void initialize(final long startTime, final long endTime) throws InternalFederateException {
        super.initialize(startTime, endTime);

        log.info("Initializing NES Federate ...");

        readConfiguration();
        startContainers();

        if (log.isTraceEnabled()) {
            log.trace("subscribedInteractions: {}", Arrays.toString(this.rti.getSubscribedInteractions().toArray()));
        }

        log.info("Initializing NES Federate ... done!");
    }

    private void readConfiguration() throws InternalFederateException {
        log.debug("Read nes Configuration");

        if (log.isTraceEnabled()) {
            log.trace("Opening nes configuration file {}", ambassadorParameter.configuration);
        }

        String nesConfigurationPath = ambassadorParameter.configuration.getAbsolutePath();
        config = ConfigurationReader.importNesConfiguration(nesConfigurationPath);
    }

    private void startContainers() {
        DockerContainerController.run("coordinator", config.coordinator.image);
        startWorkers(config.nodes);
    }

    private void startWorkers(List<CNesNode> nodes) {
        for (CNesNode node : nodes) {
            DockerContainerController.run(node.name, config.worker.image);
            startWorkers(node.nodes);
        }
    }

    @Override
    public void finishSimulation() throws InternalFederateException {
        super.finishSimulation();

        log.info("Cleaning up NES containers ...");
        DockerContainerController.stopAll();
        log.info("NES containers cleaned!");
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
