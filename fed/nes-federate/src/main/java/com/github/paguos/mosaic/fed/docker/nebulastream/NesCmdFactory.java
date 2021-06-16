package com.github.paguos.mosaic.fed.docker.nebulastream;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.docker.DockerController;
import com.github.paguos.mosaic.fed.docker.NetworkController;
import com.github.paguos.mosaic.fed.nebulastream.node.*;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.util.ArrayList;
import java.util.List;

public class NesCmdFactory {

    private Coordinator coordinator;

    public NesCmdFactory(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Create a docker java api command to create a container for the NES coordinator
     * @return the api command
     * @throws InternalFederateException for errors related to the docker client
     */
    public CreateContainerCmd createNesCoordinatorCmd() throws InternalFederateException {
        DockerClient client = DockerController.getClient();
        CNes config = ConfigurationReader.getConfig();

        ExposedPort coordinatorPort = ExposedPort.tcp(Coordinator.DEFAULT_COORDINATOR_PORT);
        ExposedPort restPort = ExposedPort.tcp(Coordinator.DEFAULT_REST_PORT);

        Ports portBindings = new Ports();
        portBindings.bind(coordinatorPort, Ports.Binding.bindPort(coordinator.getCoordinatorPort()));
        portBindings.bind(restPort, Ports.Binding.bindPort(coordinator.getRestPort()));

        List<String> cmd = new ArrayList<>();
        cmd.add("/opt/local/nebula-stream/nesCoordinator");
        cmd.add("--coordinatorIp=0.0.0.0");
        cmd.add("--restIp=0.0.0.0");

        return  client.createContainerCmd(config.coordinator.image)
                .withName(coordinator.getName())
                .withNetworkMode(NetworkController.DEFAULT_NETWORK_NAME)
                .withExposedPorts(coordinatorPort, restPort)
                .withPortBindings(portBindings)
                .withCmd(cmd);
    }

    /**
     * Create a docker java api command to create a container for the NES worker
     * @param node a worker model for the creation of the container
     * @return the api command
     * @throws InternalFederateException for errors related to the docker client
     */
    public CreateContainerCmd createNesNodeCmd(NesNode node) throws InternalFederateException {
        DockerClient client = DockerController.getClient();
        CNes config = ConfigurationReader.getConfig();

        ExposedPort dataPort = ExposedPort.tcp(node.getDataPort());
        ExposedPort rpcPort = ExposedPort.tcp(node.getRpcPort());

        Ports portBindings = new Ports();
        portBindings.bind(dataPort, Ports.Binding.bindPort(node.getDataPort()));
        portBindings.bind(rpcPort, Ports.Binding.bindPort(node.getRpcPort()));

        List<String> cmd = new ArrayList<>();
        cmd.add("/opt/local/nebula-stream/nesWorker");
        cmd.add(String.format("--coordinatorIp=%s", "127.0.0.1"));
        cmd.add(String.format("--coordinatorPort=%d", coordinator.getCoordinatorPort()));
        cmd.add(String.format("--dataPort=%d", node.getDataPort()));
        cmd.add("--localWorkerIp=0.0.0.0");
        cmd.add(String.format("--rpcPort=%d", node.getRpcPort()));

        if (node instanceof Source) {
            Source source = (Source) node;

            cmd.add(String.format("--sourceType=%s", source.getSourceType()));
            if (! source.getSourceType().equals(SourceType.DefaultSource)) {
                cmd.add(String.format("--sourceConfig=%s", source.getSourceConfig()));
            }

            cmd.add(String.format("--logicalStreamName=%s", source.getLogicalStreamName()));
            cmd.add(String.format("--physicalStreamName=%s", source.getPhysicalStreamName()));

        }

        if (node.getParentId() != -1) {
            cmd.add(String.format("--parentId=%d", node.getParentId()));
        }

        return client.createContainerCmd(config.worker.image)
                .withName(node.getName())
                .withNetworkMode(NetworkController.DEFAULT_NETWORK_NAME)
                .withExposedPorts(dataPort, rpcPort)
                .withPortBindings(portBindings)
                .withEntrypoint(cmd);
    }

    public static CreateNetworkCmd createNetworkCmd (String networkName) {
        DockerClient client = DockerController.getClient();
        return client.createNetworkCmd()
                .withName(networkName)
                .withAttachable(true);
    }

}
