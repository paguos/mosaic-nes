package com.github.paguos.mosaic.fed.docker.nebulastream;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.model.NesCoordinator;
import com.github.paguos.mosaic.fed.model.NesWorker;
import com.github.paguos.mosaic.fed.model.NesWorkerBuilder;
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

    @Before
    public void setup() throws InternalFederateException {
        ConfigurationReader.importNesConfiguration(NES_CONF_PATH);
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
        NesCoordinator coordinator = new NesCoordinator("test", 1000, 2000);
        CreateContainerCmd testCmd = NesCmdFactory.createNesCoordinatorCmd(coordinator);

        assertEquals("test-coordinator:latest", testCmd.getImage());
        assertEquals("test", testCmd.getName());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesCoordinator --coordinatorIp=%s --restIp=%s",
                "0.0.0.0", "0.0.0.0"
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
    public void createWorkerCmd() throws InternalFederateException {
        NesWorker worker = NesWorkerBuilder.createWorker("test-worker", 2)
                .dataPort(1000)
                .rpcPort(2000)
                .build();

        NesCoordinator coordinator = new NesCoordinator("test-coordinator", 3000, 4000);
        CreateContainerCmd testCmd = NesCmdFactory.createNesWorkerCmd(worker, coordinator);


        assertEquals("test-worker", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());
        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d",
                "test-coordinator", 3000, 3001, "0.0.0.0", 2000
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getCmd())));
        testWorkerPorts(testCmd);
    }

    @Test
    public void createWorkerWithParent () throws InternalFederateException {
        NesWorker worker = NesWorkerBuilder.createWorker("test-worker-with-parent", 3)
                .dataPort(1000)
                .rpcPort(2000)
                .parentId(2)
                .build();
        NesCoordinator coordinator = new NesCoordinator("test-coordinator", 3000, 4000);

        CreateContainerCmd testCmd = NesCmdFactory.createNesWorkerCmd(worker, coordinator);
        assertEquals("test-worker-with-parent", testCmd.getName());
        assertEquals("test-worker:latest", testCmd.getImage());

        String expectedCmd = String.format(
                "/opt/local/nebula-stream/nesWorker --coordinatorIp=%s --coordinatorPort=%d --dataPort=%d --localWorkerIp=%s --rpcPort=%d --parentId=%d",
                "test-coordinator", 3000, 3001, "0.0.0.0", 2000, 2
        );
        assertEquals(expectedCmd, listToString(Objects.requireNonNull(testCmd.getCmd())));
        testWorkerPorts(testCmd);
    }
    private void testWorkerPorts(CreateContainerCmd testCmd) {
        Map<ExposedPort, Ports.Binding[]> portMap = testCmd.getPortBindings().getBindings();

        ExposedPort dataPort = ExposedPort.tcp(3001);
        Ports.Binding[] coordinatorBindings = portMap.get(dataPort);
        assertEquals(1, coordinatorBindings.length);
        assertEquals("1000", coordinatorBindings[0].getHostPortSpec());

        ExposedPort restPort = ExposedPort.tcp(2000);
        Ports.Binding[] restBindings = portMap.get(restPort);
        assertEquals(1, restBindings.length);
        assertEquals("2000", restBindings[0].getHostPortSpec());
    }
}
