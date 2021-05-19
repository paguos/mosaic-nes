package com.github.paguos.mosaic.fed.config.model;

import java.util.List;

public final class CNesNode {

    /**
     * Name of the node (must be unique)
     */
    public String name;

    /**
     * List of children nodes
     */
    public List<CNesNode> nodes;

}
