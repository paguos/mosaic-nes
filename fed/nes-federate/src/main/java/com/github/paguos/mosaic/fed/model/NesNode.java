package com.github.paguos.mosaic.fed.model;

import java.util.ArrayList;
import java.util.List;

public abstract class NesNode extends NesComponent {

    protected final int id;
    private int parentId;

    public NesNode(String name, int id, int parentId) {
        super(name);
        this.id = id;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
