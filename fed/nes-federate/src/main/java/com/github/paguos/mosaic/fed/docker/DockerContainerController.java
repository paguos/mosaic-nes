package com.github.paguos.mosaic.fed.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DockerContainerController {

    private static final DockerClient dockerClient = initDockerClient();

    /**
     * List of running containers
     */
    private static final List<String> runningContainers = new ArrayList<String>();

    /**
     * Init the docker client
     *
     * @return the initialized client
     */
    private static DockerClient initDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }

    /**
     * Run a new docker container
     *
     * @param containerName unique identifier of the container
     * @param image         complete image name (image + tag) of the container
     */
    public static void run(String containerName, String image) {

        try {
            dockerClient.pullImageCmd(image).exec(
                    new PullImageResultCallback()
            ).awaitCompletion(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CreateContainerResponse container
                = dockerClient.createContainerCmd(image)
                .withName(containerName)
                .exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        runningContainers.add(container.getId());
    }

    /**
     * Stop all containers registered in the running list.
     */
    public static void stopAll() {
        for (String containerId :
                runningContainers) {
            dockerClient.killContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        }
    }
}
