package com.github.paguos.mosaic.fed.nebulastream.docker;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.nebulastream.node.*;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class NesCmdFactoryTest {

    private final static String CONFIGS_DIRECTORY = "src" + File.separator + "test" + File.separator + "resources"
            + File.separator + "configs";
    private final static String NES_CONF_PATH = CONFIGS_DIRECTORY + File.separator + "sample_nes.json";

    private NesCmdFactory nesCmdFactory;

    @Before
    public void setup() throws InternalFederateException {
        ConfigurationReader.importNesConfiguration(NES_CONF_PATH);
        Coordinator coordinator = NesBuilder.createCoordinator("test-coordinator")
                .coordinatorPort(1000)
                .restPort(2000)
                .build();
        nesCmdFactory = new NesCmdFactory(coordinator);
    }

    private String listToString(String[] list) {
        StringBuilder str = new StringBuilder();
        for (String s:
             list) {
            str.append(s).append(" ");
        }
        return str.toString().trim();
    }

    @Test
    public void createCoordinatorCmd() throws InternalFederateException {
        CreateContainerCmd testCmd = nesCmdFactory.createNesCoordinatorCmd();

        assertEquals("test-coordinator:latest", testCmd.getImage());
        assertEquals("test-coordinator", testCmd.getName());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesCoordinator --coordinatorIp=%s --restIp=%s --locationUpdateInterval=%d --dynamicDuplicatesFilterEnabled=%b --routePredictionEnabled=%b --numberOfPointsInLocationStorage=%d --numberOfTuplesInFilterStorage=%d",
                "0.0.0.0", "0.0.0.0", 500, false, false, 10, 50
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getCmd())));

        Map<ExposedPort, Ports.Binding[]> portMap = testCmd.getPortBindings().getBindings();

        ExposedPort coordinatorPort = ExposedPort.tcp(4000);
        Ports.Binding[] coordinatorBindings = portMap.get(coordinatorPort);
        assertEquals(1, coordinatorBindings.length);
        assertEquals("1000", coordinatorBindings[0].getHostPortSpec());

        ExposedPort restPort = ExposedPort.tcp(8081);
        Ports.Binding[] restBindings = portMap.get(restPort);
        assertEquals(1, restBindings.length);
        assertEquals("2000", restBindings[0].getHostPortSpec());
    }

    @Test
    public void createDefaultSourceCmd() throws InternalFederateException {
        Source source = NesBuilder.createSource("test-source" )
                .dataPort(3000)
                .rpcPort(4000)
                .build();
        CreateContainerCmd testCmd = nesCmdFactory.createNesNodeCmd(source);

        assertEquals("test-source", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --coordinatorRestPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d --sourceType=%s --logicalStreamName=%s --physicalStreamName=%s --registerLocation=%b --locationUpdateInterval=%d --workerName=%s --workerRange=%d",
                "127.0.0.1", 1000, 2000, 3000, "0.0.0.0", 4000, "DefaultSource", "default_logical", "default_physical", false, 500, "test-source", 0
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getEntrypoint())));
        testNodePorts(testCmd, false);
    }

    @Test
    public void createCSVSourceCmd() throws InternalFederateException {
        Source source = NesBuilder.createSource("test-source" )
                .dataPort(3000)
                .rpcPort(4000)
                .sourceType(SourceType.CSVSource)
                .sourceConfig("test_config")
                .logicalStreamName("test_logical")
                .physicalStreamName("test_physical")
                .numberOfTuplesToProducePerBuffer(2)
                .build();
        CreateContainerCmd testCmd = nesCmdFactory.createNesNodeCmd(source);

        assertEquals("test-source", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --coordinatorRestPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d --sourceType=%s --sourceConfig=%s --logicalStreamName=%s --physicalStreamName=%s --registerLocation=%b --locationUpdateInterval=%d --workerName=%s --workerRange=%d --numberOfTuplesToProducePerBuffer=%d",
                "127.0.0.1", 1000, 2000, 3000, "0.0.0.0", 4000, "CSVSource", "test_config", "test_logical", "test_physical", false, 500, "test-source", 0, 2
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getEntrypoint())));
        testNodePorts(testCmd, false);
    }

    @Test
    public void createZeroMQSourceCmd() throws InternalFederateException {
        ZeroMQSource source = NesBuilder.createZeroMQSource("test-source" )
                .dataPort(3000)
                .rpcPort(4000)
                .logicalStreamName("test_logical")
                .physicalStreamName("test_physical")
                .numberOfTuplesToProducePerBuffer(2)
                .zmqHost("127.0.0.1")
                .zmqPort(12345)
                .build();
        CreateContainerCmd testCmd = nesCmdFactory.createNesNodeCmd(source);

        assertEquals("test-source", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --coordinatorRestPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d --sourceType=%s --sourceConfig=%s --logicalStreamName=%s --physicalStreamName=%s --registerLocation=%b --locationUpdateInterval=%d --workerName=%s --workerRange=%d --numberOfTuplesToProducePerBuffer=%d",
                "127.0.0.1", 1000, 2000, 3000, "0.0.0.0", 4000, "ZMQSource", "127.0.0.1:12345", "test_logical", "test_physical", false, 500, "test-source", 0, 2
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getEntrypoint())));
        testNodePorts(testCmd, true);
    }

    @Test
    public void createWorkerCmd() throws InternalFederateException {
        Worker worker = NesBuilder.createWorker("test-worker" )
                .dataPort(3000)
                .rpcPort(4000)
                .build();
        CreateContainerCmd testCmd = nesCmdFactory.createNesNodeCmd(worker);

        assertEquals("test-worker", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --coordinatorRestPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d",
                "127.0.0.1", 1000, 2000,3000, "0.0.0.0", 4000
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getEntrypoint())));
        testNodePorts(testCmd, false);
    }

    @Test
    public void createWorkerWithParent () throws InternalFederateException {
        Worker worker = NesBuilder.createWorker("test-worker-with-parent")
                .dataPort(3000)
                .rpcPort(4000)
                .parentId(2)
                .build();

        CreateContainerCmd testCmd = nesCmdFactory.createNesNodeCmd(worker);
        assertEquals("test-worker-with-parent", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());

        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --coordinatorRestPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d --parentId=%d",
                "127.0.0.1", 1000, 2000, 3000, "0.0.0.0", 4000, 2
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getEntrypoint())));
        testNodePorts(testCmd, false);
    }
    private void testNodePorts(CreateContainerCmd testCmd, boolean zeroMQEnabled) {
        Map<ExposedPort, Ports.Binding[]> portMap = testCmd.getPortBindings().getBindings();

        ExposedPort dataPort = ExposedPort.tcp(3000);
        Ports.Binding[] coordinatorBindings = portMap.get(dataPort);
        assertEquals(1, coordinatorBindings.length);
        assertEquals("3000", coordinatorBindings[0].getHostPortSpec());

        ExposedPort rpcPort = ExposedPort.tcp(4000);
        Ports.Binding[] restBindings = portMap.get(rpcPort);
        assertEquals(1, restBindings.length);
        assertEquals("4000", restBindings[0].getHostPortSpec());

        if (zeroMQEnabled) {
            ExposedPort zeroMQPort = ExposedPort.tcp(12345);
            Ports.Binding[] zeroMQBindings = portMap.get(zeroMQPort);
            assertEquals(1, zeroMQBindings.length);
            assertEquals("12345", zeroMQBindings[0].getHostPortSpec());

        }
    }
}
