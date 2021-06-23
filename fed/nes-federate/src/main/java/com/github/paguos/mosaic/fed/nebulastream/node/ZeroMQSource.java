package com.github.paguos.mosaic.fed.nebulastream.node;

public class ZeroMQSource extends Source {

    private static int nextZeroMQPort = 7005;

    private final int zeroMQPort;

    public ZeroMQSource(NesBuilder.ZeroMQSourceBuilder builder) {
        super(builder);
        this.sourceConfig = String.format("%s:%d", builder.zeroMQHost, builder.zeroMQPort);
        this.sourceType = SourceType.ZMQSource;
        this.zeroMQPort = builder.zeroMQPort;
    }

    public int getZeroMQPort() {
        return zeroMQPort;
    }

    public static int getNextZeroMQPort() {
        int port = nextZeroMQPort;
        nextZeroMQPort += 10;
        return port;
    }
}
