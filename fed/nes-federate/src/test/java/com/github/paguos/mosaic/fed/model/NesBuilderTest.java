package com.github.paguos.mosaic.fed.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NesBuilderTest {

    private NesWorker defaultWorker;
    private List<NesNode> nodes;

    @Before
    public void setup() {
        defaultWorker = NesBuilder.createWorker(2, "test").build();
        nodes = new ArrayList<>();
        nodes.add(defaultWorker);
    }

    @Test
    public void createDefaultCoordinator() {
        NesCoordinator nesCoordinator = NesBuilder.createCoordinator(1, "test-coordinator").build();

        assertEquals("test-coordinator", nesCoordinator.getName());
        assertEquals(1, nesCoordinator.getId());
        assertEquals(4000, nesCoordinator.getCoordinatorPort());
        assertEquals(8081, nesCoordinator.getRestPort());
        assertEquals(0, nesCoordinator.getChildren().size());
    }

    @Test
    public void createCustomCoordinator() {
        NesCoordinator nesCoordinator = NesBuilder.createCoordinator(1, "custom-coordinator")
                .coordinatorPort(1000)
                .restPort(2000)
                .children(nodes)
                .build();

        assertEquals("custom-coordinator", nesCoordinator.getName());
        assertEquals(1, nesCoordinator.getId());
        assertEquals(1000, nesCoordinator.getCoordinatorPort());
        assertEquals(2000, nesCoordinator.getRestPort());
        assertEquals(1, nesCoordinator.getChildren().size());
    }

    @Test
    public void createDefaultSource() {
        NesSource source = NesBuilder.createSource(3, "test-source").build();

        assertEquals(3, source.getId());
        assertEquals("test-source", source.getName());
        assertEquals(-1, source.getParentId());
        assertEquals(3001, source.getDataPort());
        assertEquals(3000, source.getRpcPort());
        assertEquals(NesSourceType.DefaultSource, source.getSourceType());
    }

    @Test
    public void createCustomSource() {
        NesSource source = NesBuilder.createSource(3, "custom-source")
                .parentId(2)
                .dataPort(1001)
                .rpcPort(2001)
                .sourceType(NesSourceType.CSVSource)
                .build();

        assertEquals(3, source.getId());
        assertEquals("custom-source", source.getName());
        assertEquals(2, source.getParentId());
        assertEquals(1001, source.getDataPort());
        assertEquals(2001, source.getRpcPort());
        assertEquals(NesSourceType.CSVSource, source.getSourceType());
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
        NesWorker customWorker = NesBuilder.createWorker(3, "custom-test")
                .parentId(10)
                .rpcPort(9999)
                .dataPort(9998)
                .children(nodes)
                .build();

        assertEquals("custom-test", customWorker.getName());
        assertEquals(3, customWorker.getId());
        assertEquals(10, customWorker.getParentId());
        assertEquals(9998, customWorker.getDataPort());
        assertEquals(9999, customWorker.getRpcPort());
        assertEquals(1, customWorker.getChildren().size());
    }
}
