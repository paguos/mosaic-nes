package com.github.paguos.mosaic.fed.ambassador;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.util.ConfigurationParser;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.docker.ContainerController;
import com.github.paguos.mosaic.fed.docker.NetworkController;
import com.github.paguos.mosaic.fed.docker.nebulastream.NesCmdFactory;
import com.github.paguos.mosaic.fed.model.node.NesCoordinator;
import com.github.paguos.mosaic.fed.model.node.NesNode;
import com.github.paguos.mosaic.fed.model.node.NesWorker;
import com.github.paguos.mosaic.fed.model.stream.NesLogicalStream;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.util.List;

public class NesController {

    private static NesController controller;

    private final NesCmdFactory nesCmdFactory;
    private final NesClient nesClient;
    private final NesCoordinator coordinator;


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

        nesClient.addLogicalStream(getLogicalStream("QnV"));

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

            if (node instanceof NesWorker) {
                NesWorker worker = (NesWorker) node;
                startNodes(worker.getChildren());
            }
        }
    }

    private NesLogicalStream getLogicalStream(String name){
        String schema = "Schema::create()->addField(\"sensor_id\", ";
        schema += "DataTypeFactory::createFixedChar(8))->addField(createField(\"timestamp\", ";
        schema += "UINT64))->addField(createField(\"velocity\", FLOAT32))->addField(createField(\"quantity\", UINT64))";

        return new NesLogicalStream(name, schema);
    }

}
