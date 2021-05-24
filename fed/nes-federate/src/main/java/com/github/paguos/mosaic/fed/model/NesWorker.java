package com.github.paguos.mosaic.fed.model;

import java.util.List;

public class NesWorker extends NesNode {

    public static final int DEFAULT_DATA_PORT = 3001;
    public static final int DEFAULT_RPC_PORT = 3000;

    private final int dataPort;
    private final int rpcPort;

    private final List<NesNode> children;

    protected NesWorker(NesWorkerBuilder builder) {
        super(builder.name, builder.id, builder.parentId);

        this.children = builder.children;
        this.dataPort = builder.dataPort;
        this.rpcPort = builder.rpcPort;
    }

    public List<NesNode> getChildren() {
        return children;
    }

    public int getDataPort() {
        return dataPort;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public void addChild(NesNode child) {
        child.setParentId(super.id);
        children.add(child);
    }
}
