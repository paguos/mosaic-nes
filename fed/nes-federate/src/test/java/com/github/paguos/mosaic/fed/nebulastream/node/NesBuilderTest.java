package com.github.paguos.mosaic.fed.nebulastream.node;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NesBuilderTest {

    private Worker defaultWorker;
    private List<NesNode> nodes;

    @Before
    public void setup() {
        defaultWorker = NesBuilder.createWorker("test").build();
        nodes = new ArrayList<>();
        nodes.add(defaultWorker);
    }

    @Test
    public void createDefaultCoordinator() {
        Coordinator coordinator = NesBuilder.createCoordinator("test-coordinator").build();

        assertEquals("test-coordinator", coordinator.getName());
        assertEquals(1, coordinator.getId());
        assertEquals(4000, coordinator.getCoordinatorPort());
        assertEquals(8081, coordinator.getRestPort());
        assertEquals(0, coordinator.getChildren().size());
    }

    @Test
    public void createCustomCoordinator() {
        Coordinator coordinator = NesBuilder.createCoordinator("custom-coordinator")
                .coordinatorPort(1000)
                .restPort(2000)
                .children(nodes)
                .build();

        assertEquals("custom-coordinator", coordinator.getName());
        assertEquals(1, coordinator.getId());
        assertEquals(1000, coordinator.getCoordinatorPort());
        assertEquals(2000, coordinator.getRestPort());
        assertEquals(1, coordinator.getChildren().size());
    }

    @Test
    public void createDefaultSource() {
        Source source = NesBuilder.createSource("test-source").build();

        assertEquals("test-source", source.getName());
        assertEquals(-1, source.getParentId());
        assertEquals(3001, source.getDataPort());
        assertEquals(3002, source.getRpcPort());
        assertEquals("default_logical", source.getLogicalStreamName());
        assertEquals("default_physical", source.getPhysicalStreamName());
        assertEquals(0, source.getNumberOfTuplesToProducePerBuffer());
        assertNull(source.getSourceConfig());
        assertEquals(SourceType.DefaultSource, source.getSourceType());
    }

    @Test
    public void createCustomSource() {
        Source source = NesBuilder.createSource("custom-source")
                .parentId(2)
                .dataPort(1001)
                .rpcPort(2001)
                .sourceType(SourceType.CSVSource)
                .logicalStreamName("test_logical")
                .physicalStreamName("test_physical")
                .sourceConfig("test_config")
                .numberOfTuplesToProducePerBuffer(2)
                .build();

        assertEquals("custom-source", source.getName());
        assertEquals(2, source.getParentId());
        assertEquals(1001, source.getDataPort());
        assertEquals(2001, source.getRpcPort());
        assertEquals("test_logical", source.getLogicalStreamName());
        assertEquals("test_physical", source.getPhysicalStreamName());
        assertEquals("test_config", source.getSourceConfig());
        assertEquals(2, source.getNumberOfTuplesToProducePerBuffer());
        assertEquals(SourceType.CSVSource, source.getSourceType());
    }

    @Test
    public void createDefaultWorker() {
        assertEquals("test", defaultWorker.getName());
        assertEquals(-1, defaultWorker.getParentId());
        assertEquals(3001, defaultWorker.getDataPort());
        assertEquals(3002, defaultWorker.getRpcPort());
        assertEquals(0, defaultWorker.getChildren().size());
    }

    @Test
    public void createCustomWorker() {
        Worker customWorker = NesBuilder.createWorker( "custom-test")
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

    @Test
    public void createZeroMQSource() {
        ZeroMQSource zeroMQSource = NesBuilder.createZeroMQSource("custom-source")
                .dataPort(1001)
                .rpcPort(2001)
                .parentId(2)
                .logicalStreamName("test_logical")
                .physicalStreamName("test_physical")
                .numberOfTuplesToProducePerBuffer(2)
                .zmqHost("test-host")
                .zmqPort(12345)
                .build();

        assertEquals("custom-source", zeroMQSource.getName());
        assertEquals(2, zeroMQSource.getParentId());
        assertEquals(1001, zeroMQSource.getDataPort());
        assertEquals(2001, zeroMQSource.getRpcPort());
        assertEquals("test_logical", zeroMQSource.getLogicalStreamName());
        assertEquals("test_physical", zeroMQSource.getPhysicalStreamName());
        assertEquals("test-host:12345", zeroMQSource.getSourceConfig());
        assertEquals(2, zeroMQSource.getNumberOfTuplesToProducePerBuffer());
        assertEquals(SourceType.ZMQSource, zeroMQSource.getSourceType());
    }
}
