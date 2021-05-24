package com.github.paguos.mosaic.fed.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Network;
import com.github.paguos.mosaic.fed.docker.nebulastream.NesCmdFactory;

import java.util.ArrayList;
import java.util.List;

public class NetworkController {

    public static final String DEFAULT_NETWORK_NAME = "mosaic-nes";

    /**
     * List of running networks
     */
    private static final List<String> networks = new ArrayList<>();

    /**
     * Create a new docker network
     * @param networkName name of the network to be created
     */
    public static void createNetwork(String networkName) {
        DockerClient dockerClient = DockerController.getClient();
        List<Network> existingNetworks = dockerClient.listNetworksCmd().withNameFilter(networkName).exec();

        if (existingNetworks.isEmpty()) {
            NesCmdFactory.createNetworkCmd(networkName).exec();
        }

        networks.add(networkName);
    }

    /**
     * Remove all the created networks
     */
    public static void removeNetworks() {
        DockerClient dockerClient = DockerController.getClient();
        for (String networkName: networks) {
            dockerClient.removeNetworkCmd(networkName);
        }
    }

}
