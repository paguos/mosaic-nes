package com.github.paguos.mosaic.fed.model;

import java.util.ArrayList;
import java.util.List;

public class NesTopology {

    private final NesCoordinator coordinator;
    private final List<NesNode> rootNodes;

    public NesTopology(NesCoordinator coordinator) {
        this.coordinator = coordinator;
        this.rootNodes = new ArrayList<>();
    }

    public NesTopology(NesCoordinator coordinator, List<NesNode> rootNodes) {
        this.coordinator = coordinator;
        this.rootNodes = rootNodes;
    }

    public NesCoordinator getCoordinator() {
        return coordinator;
    }

    public List<NesNode> getRootNodes() {
        return rootNodes;
    }

    public void addRootNode(NesNode node) {
        rootNodes.add(node);
    }
}
