import org.eclipse.mosaic.starter.MosaicSimulation;
import org.eclipse.mosaic.test.junit.LogAssert;
import org.eclipse.mosaic.test.junit.MosaicSimulationRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BarcelonaRangeQueryIT {

    @ClassRule
    public static MosaicSimulationRule simulationRule = new MosaicSimulationRule().logLevelOverride("INFO");

    private static MosaicSimulation.SimulationResult simulationResult;

    @BeforeClass
    public static void runSimulation() {
        String scenariosDirectory = System.getProperty("scenariosDirectory");
        simulationResult = simulationRule.executeSimulation(scenariosDirectory, "barcelona-range-query");
    }

    @Test
    public void executionSuccessful() {
        assertNull(simulationResult.exception);
        assertTrue(simulationResult.success);
    }

    static int deployedSources = 6;

    @Test
    public void logFilesExisting() {
        LogAssert.exists(simulationRule, "MOSAIC.log");
        LogAssert.exists(simulationRule, "Traffic.log");
        LogAssert.exists(simulationRule, "Application.log");
        LogAssert.exists(simulationRule, "Mapping.log");
        LogAssert.exists(simulationRule, "Communication.log");
        LogAssert.exists(simulationRule, "apps/nes-coordinator/container.log");

        // Source
        for (int i = 0; i < deployedSources; i++) {
            LogAssert.exists(simulationRule, String.format("apps/veh_%d/NesVehicleSourceApp.log", i));
            LogAssert.exists(simulationRule, String.format("SpeedReport-veh_%d.csv", i));
        }

        // Sink
        LogAssert.exists(simulationRule, "apps/veh_6/NesSinkApp.log");
        LogAssert.exists(simulationRule, "SpeedReport-veh_6.csv");
    }

    @Test
    public void allVehiclesLoaded() throws Exception {
        LogAssert.contains(simulationRule, "Traffic.log", ".*SumoAmbassador - Process sumo :  Inserted: 7.*");
    }

    @Test
    public void logicalStreamCreated() throws Exception {
        LogAssert.contains(simulationRule, "apps/veh_0/NesVehicleSourceApp.log", ".*Found Logical Stream 'QnV': true.*");
        LogAssert.contains(simulationRule, "apps/veh_0/NesVehicleSourceApp.log", ".*Found Logical Stream 'mosaic_nes': true.*");
    }

    @Test
    public void nesNodesCreated() throws Exception {
        LogAssert.contains(
                simulationRule,
                String.format("apps/veh_%d/NesVehicleSourceApp.log", 1),
                String.format(".*The Nes Topology has '%d' nodes.*", deployedSources + 1)
        );
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
