package com.github.paguos.mosaic.fed.model;

import java.util.List;

public class NesWorker extends NesNode {

    private final List<NesNode> children;

    protected NesWorker(NesBuilder.NesWorkerBuilder builder) {
        super(builder.id, builder.name, builder.parentId, builder.dataPort, builder.rpcPort);
        this.children = builder.children;
    }

    public List<NesNode> getChildren() {
        return children;
    }

    public void addChild(NesNode child) {
        child.setParentId(getId());
        children.add(child);
    }
}
