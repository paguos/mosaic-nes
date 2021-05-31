package com.github.paguos.mosaic.fed.model;


public abstract class NesNode extends NesComponent {

    public static final int DEFAULT_DATA_PORT = 3001;
    public static final int DEFAULT_RPC_PORT = 3000;

    private static int nextId = 2;

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
}
