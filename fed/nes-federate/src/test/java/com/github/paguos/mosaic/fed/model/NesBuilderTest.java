package com.github.paguos.mosaic.fed.model;

import com.github.paguos.mosaic.fed.model.node.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NesBuilderTest {

    private NesWorker defaultWorker;
    private List<NesNode> nodes;

    @Before
    public void setup() {
        defaultWorker = NesBuilder.createWorker("test").build();
        nodes = new ArrayList<>();
        nodes.add(defaultWorker);
    }

    @Test
    public void createDefaultCoordinator() {
        NesCoordinator nesCoordinator = NesBuilder.createCoordinator("test-coordinator").build();

        assertEquals("test-coordinator", nesCoordinator.getName());
        assertEquals(1, nesCoordinator.getId());
        assertEquals(4000, nesCoordinator.getCoordinatorPort());
        assertEquals(8081, nesCoordinator.getRestPort());
        assertEquals(0, nesCoordinator.getChildren().size());
    }

    @Test
    public void createCustomCoordinator() {
        NesCoordinator nesCoordinator = NesBuilder.createCoordinator("custom-coordinator")
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
        NesSource source = NesBuilder.createSource("test-source").build();

        assertEquals("test-source", source.getName());
        assertEquals(-1, source.getParentId());
        assertEquals(3001, source.getDataPort());
        assertEquals(3000, source.getRpcPort());
        assertEquals("default_logical", source.getLogicalStreamName());
        assertEquals("default_physical", source.getPhysicalStreamName());
        assertNull(source.getSourceConfig());
        assertEquals(NesSourceType.DefaultSource, source.getSourceType());
    }

    @Test
    public void createCustomSource() {
        NesSource source = NesBuilder.createSource("custom-source")
                .parentId(2)
                .dataPort(1001)
                .rpcPort(2001)
                .sourceType(NesSourceType.CSVSource)
                .logicalStreamName("test_logical")
                .physicalStreamName("test_physical")
                .sourceConfig("test_config")
                .build();

        assertEquals("custom-source", source.getName());
        assertEquals(2, source.getParentId());
        assertEquals(1001, source.getDataPort());
        assertEquals(2001, source.getRpcPort());
        assertEquals("test_logical", source.getLogicalStreamName());
        assertEquals("test_physical", source.getPhysicalStreamName());
        assertEquals("test_config", source.getSourceConfig());
        assertEquals(NesSourceType.CSVSource, source.getSourceType());
    }

    @Test
    public void createDefaultWorker() {
        assertEquals("test", defaultWorker.getName());
        assertEquals(-1, defaultWorker.getParentId());
        assertEquals(3001, defaultWorker.getDataPort());
        assertEquals(3000, defaultWorker.getRpcPort());
        assertEquals(0, defaultWorker.getChildren().size());
    }

    @Test
    public void createCustomWorker() {
        NesWorker customWorker = NesBuilder.createWorker( "custom-test")
                .parentId(10)
                .rpcPort(9999)
                .dataPort(9998)
                .children(nodes)
                .build();

        assertEquals("custom-test", customWorker.getName());
        assertEquals(10, customWorker.getParentId());
        assertEquals(9998, customWorker.getDataPort());
        assertEquals(9999, customWorker.getRpcPort());
        assertEquals(1, customWorker.getChildren().size());
    }
}
