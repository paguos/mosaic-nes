package com.github.paguos.mosaic.fed.docker.nebulastream;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.docker.DockerController;
import com.github.paguos.mosaic.fed.docker.NetworkController;
import com.github.paguos.mosaic.fed.model.NesCoordinator;
import com.github.paguos.mosaic.fed.model.NesWorker;
import org.eclipse.mosaic.rti.api.InternalFederateException;

import java.util.ArrayList;
import java.util.List;

public class NesCmdFactory {

    /**
     * Create a docker java api command to create a container for the NES coordinator
     * @param coordinator a coordinator model for the creation of the container
     * @return the api command
     * @throws InternalFederateException for errors related to the docker client
     */
    public static CreateContainerCmd createNesCoordinatorCmd(NesCoordinator coordinator) throws InternalFederateException {
        DockerClient client = DockerController.getClient();
        CNes config = ConfigurationReader.getConfig();

        ExposedPort coordinatorPort = ExposedPort.tcp(NesCoordinator.DEFAULT_COORDINATOR_PORT);
        ExposedPort restPort = ExposedPort.tcp(NesCoordinator.DEFAULT_REST_PORT);

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
     * @param worker a worker model for the creation of the container
     * @param coordinator a coordinator model for the creation of the container
     * @return the api command
     * @throws InternalFederateException for errors related to the docker client
     */
    public static CreateContainerCmd createNesWorkerCmd(NesWorker worker, NesCoordinator coordinator) throws InternalFederateException {
        DockerClient client = DockerController.getClient();
        CNes config = ConfigurationReader.getConfig();

        ExposedPort dataPort = ExposedPort.tcp(NesWorker.DEFAULT_DATA_PORT);
        ExposedPort rpcPort = ExposedPort.tcp(worker.getRpcPort());

        Ports portBindings = new Ports();
        portBindings.bind(dataPort, Ports.Binding.bindPort(worker.getDataPort()));
        portBindings.bind(rpcPort, Ports.Binding.bindPort(worker.getRpcPort()));

        List<String> cmd = new ArrayList<>();
        cmd.add("/opt/local/nebula-stream/nesWorker");
        cmd.add(String.format("--coordinatorIp=%s", coordinator.getName()));
        cmd.add(String.format("--coordinatorPort=%d", coordinator.getCoordinatorPort()));
        cmd.add(String.format("--dataPort=%d", NesWorker.DEFAULT_DATA_PORT));
        cmd.add("--localWorkerIp=0.0.0.0");
        cmd.add(String.format("--rpcPort=%d", worker.getRpcPort()));

        if (worker.getParentId() != -1) {
            cmd.add(String.format("--parentId=%d", worker.getParentId()));
        }

        return client.createContainerCmd(config.worker.image)
                .withName(worker.getName())
                .withNetworkMode(NetworkController.DEFAULT_NETWORK_NAME)
                .withExposedPorts(dataPort, rpcPort)
                .withPortBindings(portBindings)
                .withCmd(cmd);
    }

    public static CreateNetworkCmd createNetworkCmd (String networkName) {
        DockerClient client = DockerController.getClient();
        return client.createNetworkCmd()
                .withName(networkName)
                .withAttachable(true);
    }

}
