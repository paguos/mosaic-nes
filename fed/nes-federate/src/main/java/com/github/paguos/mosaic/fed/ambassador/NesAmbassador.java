package com.github.paguos.mosaic.fed.ambassador;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.util.ConfigurationParser;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.docker.ContainerController;
import com.github.paguos.mosaic.fed.docker.NetworkController;
import com.github.paguos.mosaic.fed.docker.nebulastream.NesCmdFactory;
import com.github.paguos.mosaic.fed.model.NesCoordinator;
import com.github.paguos.mosaic.fed.model.NesNode;
import com.github.paguos.mosaic.fed.model.NesTopology;
import com.github.paguos.mosaic.fed.model.NesWorker;
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

    private NesTopology topology;

    public NesAmbassador(AmbassadorParameter ambassadorParameter) {
        super(ambassadorParameter);
    }

    @Override
    public void initialize(final long startTime, final long endTime) throws InternalFederateException {
        super.initialize(startTime, endTime);

        log.info("Initializing NES Federate ...");

        readConfiguration();
        NetworkController.createNetwork(NetworkController.DEFAULT_NETWORK_NAME);
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
        ConfigurationReader.importNesConfiguration(nesConfigurationPath);
        CNes config = ConfigurationReader.getConfig();
        topology = ConfigurationParser.parseTopology(config);
    }

    private void startContainers() throws InternalFederateException {
        CreateContainerCmd coordinatorCmd = NesCmdFactory.createNesCoordinatorCmd(topology.getCoordinator());
        ContainerController.run(coordinatorCmd);
        startNodes(topology.getRootNodes(), topology.getCoordinator());
    }

    private void startNodes(List<NesNode> nodes, NesCoordinator coordinator) throws InternalFederateException {
        for (NesNode node : nodes) {
            NesWorker worker = (NesWorker) node;
            CreateContainerCmd createWorkerCmd = NesCmdFactory.createNesWorkerCmd(worker, coordinator);
            ContainerController.run(createWorkerCmd);
            startNodes(worker.getChildren(), coordinator);
        }
    }

    @Override
    public void finishSimulation() throws InternalFederateException {
        super.finishSimulation();
        log.info("Cleaning up NES containers ...");
        ContainerController.stopAll();
        NetworkController.removeNetworks();
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
