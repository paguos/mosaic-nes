package com.github.paguos.mosaic.fed.config;

import com.github.paguos.mosaic.fed.config.util.ConfigurationParser;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.model.NesCoordinator;
import com.github.paguos.mosaic.fed.model.NesNode;
import com.github.paguos.mosaic.fed.model.NesTopology;
import com.github.paguos.mosaic.fed.model.NesWorker;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConfigurationParserTest {

    private final static String CONFIGS_DIRECTORY = "src" + File.separator + "test" + File.separator + "resources"
            + File.separator + "configs";
    private final static String NES_CONF_PATH = CONFIGS_DIRECTORY + File.separator + "sample_nes.json";

    private CNes getNesConfig() throws InternalFederateException {
        // Read the region configuration file
        ConfigurationReader.importNesConfiguration(NES_CONF_PATH);
        return ConfigurationReader.getConfig();
    }

    @Test
    public void parseTopologyFromConfig() throws InternalFederateException {
        CNes nesConfig = getNesConfig();
        NesTopology topology = ConfigurationParser.parseTopology(nesConfig);

        // Nes Coordinator
        NesCoordinator coordinator = topology.getCoordinator();
        assertEquals("nes-coordinator", coordinator.getName());
        assertEquals("", coordinator.getContainerId());
        assertEquals(4000, coordinator.getCoordinatorPort());
        assertEquals(8081, coordinator.getRestPort());

        // Nes Workers
        List<NesNode> rootNodes = topology.getRootNodes();
        assertEquals(1, rootNodes.size());

        NesWorker root = (NesWorker) rootNodes.get(0);
        assertEquals("worker_00", root.getName());
        assertEquals(2, root.getId());
        assertEquals(-1, root.getParentId());
        assertEquals("", root.getContainerId());
        assertEquals(3001, root.getDataPort());
        assertEquals(3000, root.getRpcPort());
        assertEquals(1, root.getChildren().size());

        NesWorker child = (NesWorker) root.getChildren().get(0);
        assertEquals("worker_01", child.getName());
        assertEquals(3, child.getId());
        assertEquals(2, child.getParentId());
        assertEquals("", child.getContainerId());
        assertEquals(3011, child.getDataPort());
        assertEquals(3010, child.getRpcPort());
        assertEquals(0, child.getChildren().size());

    }

}
