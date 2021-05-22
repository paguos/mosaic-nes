package com.github.paguos.mosaic.fed.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;


import java.util.ArrayList;
import java.util.List;

public class ContainerController {

    /**
     * List of running containers
     */
    private static final List<String> runningContainers = new ArrayList<>();

    /**
     * Run a new docker container
     * @param createContainerCmd unique identifier of the container
     */
    public static void run(CreateContainerCmd createContainerCmd) {
        DockerClient dockerClient = DockerController.getClient();
        CreateContainerResponse container = createContainerCmd.exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        runningContainers.add(container.getId());

        dockerClient
                .logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .exec(new ResultCallbackTemplate<LogContainerResultCallback, Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        System.out.print(createContainerCmd.getName() + "> " + new String(frame.getPayload()));
                    }
                });
    }

    /**
     * Stop all containers registered in the running list.
     */
    public static void stopAll() {
        DockerClient dockerClient = DockerController.getClient();
        for (String containerId :
                runningContainers) {
            dockerClient.killContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        }
    }
}
