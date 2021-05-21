package com.github.paguos.mosaic.fed.config;

import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.config.util.ConfigurationReader;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class CNesImportTest {

    private final static String CONFIGS_DIRECTORY = "src" + File.separator + "test" + File.separator + "resources"
            + File.separator + "configs";

    private final static String NES_CONF_PATH = CONFIGS_DIRECTORY + File.separator + "sample_nes.json";
    private final static String NES_CONF_PATH_INVALID = CONFIGS_DIRECTORY + File.separator + "sample_invalid_nes.json";

    private CNes getNesConfig() throws InternalFederateException {
        // Read the region configuration file
        return ConfigurationReader.importNesConfiguration(NES_CONF_PATH);
    }

    @Test
    public void checkNesConfigAsExpected() throws InternalFederateException {
        CNes nesConfig = getNesConfig();
        assertEquals("test-coordinator:latest", nesConfig.coordinator.image);
        assertEquals("test-worker:latest", nesConfig.worker.image);

        CNesNode root = nesConfig.nodes.get(0);
        assertEquals("worker_00", root.name);
        assertEquals(1, root.nodes.size());

        CNesNode child = root.nodes.get(0);
        assertEquals("worker_01", child.name);
        assertEquals(0, child.nodes.size());
    }

    @Test(expected = InternalFederateException.class)
    public void checkInvalidNesConfig() throws InternalFederateException {
        ConfigurationReader.importNesConfiguration(NES_CONF_PATH_INVALID);
    }

}
