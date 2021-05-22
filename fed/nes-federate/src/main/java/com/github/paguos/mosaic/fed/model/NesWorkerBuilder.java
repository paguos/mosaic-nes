package com.github.paguos.mosaic.fed.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to create nes workers
 */
public class NesWorkerBuilder {

    // NesComponent attributes
    protected final String name;

    // NesNode attributes
    protected final int id;
    protected int parentId;
    protected List<NesNode> children;

    // NesWorker attributes
    protected int dataPort;
    protected int rpcPort;

    /**
     * Create a builder for a nes worker
     * @param name of the nes worker
     * @param id of the nes worker
     * @return an instance of the builder
     */
    public static NesWorkerBuilder createWorker(String name, int id) {
        return new NesWorkerBuilder(name, id);
    }

    private NesWorkerBuilder(String name, int id) {
        this.name = name;
        this.id = id;

        children = new ArrayList<>();
        dataPort = NesWorker.DEFAULT_DATA_PORT;
        rpcPort = NesWorker.DEFAULT_RPC_PORT;
        parentId = -1;
    }

    public NesWorkerBuilder children(List<NesNode> children) {
        this.children = children;
        return this;
    }

    public NesWorkerBuilder dataPort(int dataPort) {
        this.dataPort = dataPort;
        return this;
    }

    public NesWorkerBuilder parentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public NesWorkerBuilder rpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
        return this;
    }

    public NesWorker build() {
        return new NesWorker(this);
    }
}
