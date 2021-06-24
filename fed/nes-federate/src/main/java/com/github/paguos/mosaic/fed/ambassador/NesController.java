package com.github.paguos.mosaic.fed.ambassador;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.paguos.mosaic.fed.nebulastream.catalog.SchemaCatalog;
import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.util.ConfigurationParser;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.docker.ContainerController;
import com.github.paguos.mosaic.fed.docker.NetworkController;
import com.github.paguos.mosaic.fed.nebulastream.docker.NesCmdFactory;
import com.github.paguos.mosaic.fed.nebulastream.node.Coordinator;
import com.github.paguos.mosaic.fed.nebulastream.node.NesNode;
import com.github.paguos.mosaic.fed.nebulastream.node.Worker;
import com.github.paguos.mosaic.fed.nebulastream.stream.LogicalStream;
import com.github.paguos.mosaic.fed.nebulastream.stream.Schema;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.util.List;

public class NesController {

    private static NesController controller;

    private final NesCmdFactory nesCmdFactory;
    private final NesClient nesClient;
    private final Coordinator coordinator;


    private NesController(CNes config) {
        this.coordinator = ConfigurationParser.parseConfig(config);
        this.nesCmdFactory = new NesCmdFactory(this.coordinator);

        this.nesClient = new NesClient(config.clientHost, config.clientPort);
    }

    public static NesController getController() throws InternalFederateException {
        if (controller == null) {
            CNes config = ConfigurationReader.getConfig();
            controller = new NesController(config);
        }
        return controller;
    }

    public void start() throws InternalFederateException {
        NetworkController.createNetwork(NetworkController.DEFAULT_NETWORK_NAME);
        CreateContainerCmd coordinatorCmd = nesCmdFactory.createNesCoordinatorCmd();
        ContainerController.run(coordinatorCmd);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new InternalFederateException(e);
        }

        Schema qnvSchema = SchemaCatalog.getQnVSchema();
        nesClient.addLogicalStream(new LogicalStream(qnvSchema));

        Schema mosaicSchema = SchemaCatalog.getMosaicNesSchema();
        nesClient.addLogicalStream(new LogicalStream(mosaicSchema));

        startNodes(coordinator.getChildren());
    }

    public void stop() {
        ContainerController.stopAll();
        NetworkController.removeNetworks();
    }

    public void addNode(NesNode node) throws InternalFederateException {
        CreateContainerCmd sourceCmd = nesCmdFactory.createNesNodeCmd(node);
        ContainerController.run(sourceCmd);
    }

    private void startNodes(List<NesNode> nodes) throws InternalFederateException {
        for (NesNode node : nodes) {
            CreateContainerCmd createWorkerCmd = nesCmdFactory.createNesNodeCmd(node);
            ContainerController.run(createWorkerCmd);

            if (node instanceof Worker) {
                Worker worker = (Worker) node;
                startNodes(worker.getChildren());
            }
        }
    }

}
