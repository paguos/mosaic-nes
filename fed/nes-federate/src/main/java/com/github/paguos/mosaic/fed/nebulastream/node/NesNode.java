package com.github.paguos.mosaic.fed.nebulastream.node;


public abstract class NesNode extends NesComponent {

    public static final int DEFAULT_DATA_PORT = 3001;
    public static final int DEFAULT_RPC_PORT = 3000;

    private static int nextId = 2;
    private static int nextDataPort = 6000;
    private static int nextRPCPort = 6001;

    private int parentId;
    private final int dataPort;
    private final int rpcPort;

    public NesNode(String name, int parentId, int dataPort, int rpcPort) {
        super(nextId++, name);
        this.parentId = parentId;
        this.dataPort = dataPort;
        this.rpcPort = rpcPort;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getDataPort() {
        return dataPort;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public static int getNextDataPort() {
        int port = nextDataPort;
        nextDataPort += 10;
        return port;
    }

    public static int getNextRPCPort() {
        int port = nextRPCPort;
        nextRPCPort += 10;
        return port;
    }
}
