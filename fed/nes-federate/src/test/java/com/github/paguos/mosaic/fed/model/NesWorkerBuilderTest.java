package com.github.paguos.mosaic.fed.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NesWorkerBuilderTest {

    private NesWorker defaultWorker;

    @Before
    public void setup() {
        defaultWorker = NesWorkerBuilder.createWorker("test", 2).build();
    }

    @Test
    public void createDefaultWorker() {
        assertEquals("test", defaultWorker.getName());
        assertEquals(2, defaultWorker.getId());
        assertEquals(-1, defaultWorker.getParentId());
        assertEquals(3001, defaultWorker.getDataPort());
        assertEquals(3000, defaultWorker.getRpcPort());
        assertEquals(0, defaultWorker.getChildren().size());
    }

    @Test
    public void createCustomWorker() {
        List<NesNode> children = new ArrayList<>();
        children.add(defaultWorker);

        NesWorker customWorker = NesWorkerBuilder.createWorker("custom-test", 3)
                .parentId(10)
                .rpcPort(9999)
                .dataPort(9998)
                .children(children)
                .build();

        assertEquals("custom-test", customWorker.getName());
        assertEquals(3, customWorker.getId());
        assertEquals(10, customWorker.getParentId());
        assertEquals(9998, customWorker.getDataPort());
        assertEquals(9999, customWorker.getRpcPort());
        assertEquals(1, customWorker.getChildren().size());
    }
}
