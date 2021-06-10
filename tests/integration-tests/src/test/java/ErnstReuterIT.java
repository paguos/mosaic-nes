import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.mosaic.starter.MosaicSimulation;
import org.eclipse.mosaic.test.junit.LogAssert;
import org.eclipse.mosaic.test.junit.MosaicSimulationRule;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class ErnstReuterIT {

    @ClassRule
    public static MosaicSimulationRule simulationRule = new MosaicSimulationRule().logLevelOverride("DEBUG");

    private static MosaicSimulation.SimulationResult simulationResult;

    @BeforeClass
    public static void runSimulation() {
        String scenariosDirectory = System.getProperty("scenariosDirectory");
        simulationResult = simulationRule.executeSimulation(scenariosDirectory, "ernst-reuter");
    }

    @Test
    public void executionSuccessful() {
        assertNull(simulationResult.exception);
        assertTrue(simulationResult.success);
    }

    @Test
    public void logFilesExisting() {
        LogAssert.exists(simulationRule, "MOSAIC.log");
        LogAssert.exists(simulationRule, "Traffic.log");
        LogAssert.exists(simulationRule, "Application.log");
        LogAssert.exists(simulationRule, "Mapping.log");
        LogAssert.exists(simulationRule, "Communication.log");
        LogAssert.exists(simulationRule, "apps/rsu_0/NesSourceApp.log");
    }

    @Test
    public void allVehiclesLoaded() throws Exception {
        LogAssert.contains(simulationRule, "Traffic.log", ".*SumoAmbassador - Process sumo :  Inserted: 966.*");
    }

    @Test
    public void logicalStreamCreated() throws Exception {
        LogAssert.contains(simulationRule, "apps/rsu_0/NesSourceApp.log", ".*Found Logical Stream 'QnV': true.*");
    }
}
