import org.eclipse.mosaic.starter.MosaicSimulation;
import org.eclipse.mosaic.test.junit.LogAssert;
import org.eclipse.mosaic.test.junit.MosaicSimulationRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LeipzigIT {

    @ClassRule
    public static MosaicSimulationRule simulationRule = new MosaicSimulationRule().logLevelOverride("INFO");

    private static MosaicSimulation.SimulationResult simulationResult;

    @BeforeClass
    public static void runSimulation() {
        String scenariosDirectory = System.getProperty("scenariosDirectory");
        simulationResult = simulationRule.executeSimulation(scenariosDirectory, "leipzig");
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

        // Sink
        LogAssert.exists(simulationRule, "apps/veh_2/SinkApp.log");
        LogAssert.exists(simulationRule, "SpeedReport-veh_2.csv");
    }

    @Test
    public void allVehiclesLoaded() throws Exception {
        LogAssert.contains(simulationRule, "Traffic.log", ".*SumoAmbassador - Process sumo :  Inserted: 8.*");
    }

    @Test
    public void sinkReceivedMessages() throws Exception {
        LogAssert.contains(
                simulationRule,
                "SpeedReport-veh_2.csv",
                ".*veh_[0-9]*,[0-9]*,[0-9]*[.][0-9]*,[0-9]*[.][0-9]*,[0-9]*[.][0-9]*"
        );
    }
}
