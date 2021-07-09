package com.github.paguos.mosaic.fed.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import org.eclipse.mosaic.fed.application.ambassador.util.UnitLogger;
import org.eclipse.mosaic.fed.application.ambassador.util.UnitLoggerImpl;


import java.util.ArrayList;
import java.util.List;

public class ContainerController {

    /**
     * List of running containers
     */
    private final List<String> runningContainers;

    public ContainerController() {
        this.runningContainers = new ArrayList<>();
    }

    /**
     * Run a new docker container
     * @param createContainerCmd unique identifier of the container
     */
    public void run(CreateContainerCmd createContainerCmd) {
        DockerClient dockerClient = DockerController.getClient();
        CreateContainerResponse container = createContainerCmd.exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        runningContainers.add(container.getId());

        dockerClient
                .logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .exec(new LoggerCallback(createContainerCmd.getName()));
    }

    /**
     * Stop all containers registered in the running list.
     */
    public void stopAll() {
        DockerClient dockerClient = DockerController.getClient();
        for (String containerId :
                runningContainers) {
            dockerClient.killContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        }
    }

    private static class LoggerCallback extends  ResultCallbackTemplate<LogContainerResultCallback, Frame> {

        private final UnitLogger logger;

        public LoggerCallback(String name) {
            this.logger = new UnitLoggerImpl(name, "container");
        }

        @Override
        public void onNext(Frame frame) {
            logger.info(new String(frame.getPayload()));
        }
    }
}
