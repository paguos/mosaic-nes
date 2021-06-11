package com.github.paguos.mosaic.fed.config;

import com.github.paguos.mosaic.fed.config.util.ConfigurationParser;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import com.github.paguos.mosaic.fed.model.node.NesCoordinator;
import com.github.paguos.mosaic.fed.model.node.NesNode;
import com.github.paguos.mosaic.fed.model.node.NesWorker;
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
        NesCoordinator coordinator = ConfigurationParser.parseConfig(nesConfig);

        assertEquals("nes-coordinator", coordinator.getName());
        assertEquals(4000, coordinator.getCoordinatorPort());
        assertEquals(8081, coordinator.getRestPort());

        // Nes Workers
        List<NesNode> childNodes = coordinator.getChildren();
        assertEquals(1, childNodes.size());

        NesWorker root = (NesWorker) childNodes.get(0);
        assertEquals("worker_00", root.getName());
        assertEquals(-1, root.getParentId());
        assertEquals(6000, root.getDataPort());
        assertEquals(6001, root.getRpcPort());
        assertEquals(1, root.getChildren().size());

        NesWorker child = (NesWorker) root.getChildren().get(0);
        assertEquals("worker_01", child.getName());
        assertEquals(6010, child.getDataPort());
        assertEquals(6011, child.getRpcPort());
        assertEquals(0, child.getChildren().size());

    }

}
