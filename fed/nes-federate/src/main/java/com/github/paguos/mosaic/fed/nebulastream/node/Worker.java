package com.github.paguos.mosaic.fed.nebulastream.node;

import java.util.List;

public class Worker extends NesNode {

    private final List<NesNode> children;

    protected Worker(NesBuilder.NesWorkerBuilder builder) {
        super(builder.name, builder.parentId, builder.dataPort, builder.rpcPort);
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
