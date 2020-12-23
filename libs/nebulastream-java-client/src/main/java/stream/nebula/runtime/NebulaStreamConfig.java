package stream.nebula.runtime;

public class NebulaStreamConfig {
    private String host;
    private String port;

    public String getHost() {
        return host;
    }

    public NebulaStreamConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return port;
    }

    public NebulaStreamConfig setPort(String port) {
        this.port = port;
        return this;
    }
}
