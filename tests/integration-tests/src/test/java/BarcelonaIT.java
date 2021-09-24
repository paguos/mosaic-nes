import org.eclipse.mosaic.starter.MosaicSimulation;
import org.eclipse.mosaic.test.junit.LogAssert;
import org.eclipse.mosaic.test.junit.MosaicSimulationRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BarcelonaIT {

    @ClassRule
    public static MosaicSimulationRule simulationRule = new MosaicSimulationRule().logLevelOverride("INFO");

    private static MosaicSimulation.SimulationResult simulationResult;

    @BeforeClass
    public static void runSimulation() {
        String scenariosDirectory = System.getProperty("scenariosDirectory");
        simulationResult = simulationRule.executeSimulation(scenariosDirectory, "barcelona");
    }

    private final int deployedRoadSideUnits = 6;

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

        // Sink
        LogAssert.exists(simulationRule, "apps/veh_6/SinkApp.log");
        LogAssert.exists(simulationRule, "SpeedReport-veh_6.csv");
    }

    @Test
    public void allVehiclesLoaded() throws Exception {
        LogAssert.contains(simulationRule, "Traffic.log", ".*SumoAmbassador - Process sumo :  Inserted: 7.*");
    }

    @Test
    public void sinkReceivedMessages() throws Exception {
        LogAssert.contains(
                simulationRule,
                "SpeedReport-veh_6.csv",
                ".*veh_[0-9]*,[0-9]*,[0-9]*[.][0-9]*,[0-9]*[.][0-9]*,[0-9]*[.][0-9]*"
        );
    }
}
