package com.github.paguos.mosaic.fed.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

public class DockerController {

    private static DockerClient client;

    /**
     * Singleton method to obtain a docker client
     * @return an instance of the docker client
     */
    public static DockerClient getClient(){
        if (client == null) {
            client = initDockerClient();
        }
        return client;
    }

    /**
     * Initializes the docker client
     * @return the docker client
     */
    private static DockerClient initDockerClient(){
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }
}
