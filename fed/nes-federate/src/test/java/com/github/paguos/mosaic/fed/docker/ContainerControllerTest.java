package com.github.paguos.mosaic.fed.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ContainerControllerTest {

    private final String TEST_IMAGE = "yobasystems/alpine-nginx:stable";

    private ContainerController containerController;
    private DockerClient dockerClient;

    @Before
    public void setup() throws InterruptedException {
        containerController = new ContainerController();
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .build();

        dockerClient = DockerClientImpl.getInstance(config, httpClient);
        dockerClient.pullImageCmd(TEST_IMAGE)
                .exec(new PullImageResultCallback())
                .awaitCompletion(60, TimeUnit.SECONDS);
    }

    @Test
    public void runAndStopContainerTest() {
        CreateContainerCmd testCmd = dockerClient.createContainerCmd(TEST_IMAGE).withName("test");
        containerController.run(testCmd);

        List<Container> containers = dockerClient.listContainersCmd().exec();
        assertEquals(1, containers.size());

        Container testContainer = containers.get(0);
        assertEquals("/test",
                testContainer.getNames()[0]);
        assertEquals(TEST_IMAGE, testContainer.getImage());

        containerController.stopAll();
        containers = dockerClient.listContainersCmd().exec();
        assertEquals(0, containers.size());
    }
}
