import org.eclipse.mosaic.starter.MosaicSimulation;
import org.eclipse.mosaic.test.junit.LogAssert;
import org.eclipse.mosaic.test.junit.MosaicSimulationRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BerlinMovingRangeIT {

    @ClassRule
    public static MosaicSimulationRule simulationRule = new MosaicSimulationRule().logLevelOverride("INFO");

    private static MosaicSimulation.SimulationResult simulationResult;

    @BeforeClass
    public static void runSimulation() {
        String scenariosDirectory = System.getProperty("scenariosDirectory");
        simulationResult = simulationRule.executeSimulation(scenariosDirectory, "berlin-moving-range");
    }

    private final int deployedRoadSideUnits = 3;

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

        LogAssert.exists(simulationRule, "apps/nes-coordinator/container.log");

        // Source
        for (int i = 0; i < deployedRoadSideUnits; i++) {
            LogAssert.exists(simulationRule, String.format("apps/rsu_%d/NesRSUSourceApp.log", i));
            LogAssert.exists(simulationRule, String.format("SpeedReport-rsu_%d.csv", i));
        }

        // Sink
        LogAssert.exists(simulationRule, "apps/veh_10/NesSinkApp.log");
    }

    @Test
    public void allVehiclesLoaded() throws Exception {
        LogAssert.contains(simulationRule, "Traffic.log", ".*SumoAmbassador - Process sumo :  Inserted: 101.*");
    }

    @Test
    public void logicalStreamCreated() throws Exception {
        LogAssert.contains(simulationRule, "apps/rsu_0/NesRSUSourceApp.log", ".*Found Logical Stream 'QnV': true.*");
        LogAssert.contains(simulationRule, "apps/rsu_0/NesRSUSourceApp.log", ".*Found Logical Stream 'mosaic_nes': true.*");
    }

    @Test
    public void nesNodesCreated() throws Exception {
        int deployedNesNodes = 1;

        for (int i = 0; i < deployedRoadSideUnits; i++) {
            LogAssert.contains(
                    simulationRule,
                    String.format("apps/rsu_%d/NesRSUSourceApp.log", i),
                    String.format(".*The Nes Topology has '%d' nodes.*", ++deployedNesNodes)
            );
        }
    }

    @Test
    public void sourceReceivedV2xMessages() throws Exception {
        for (int i = 0; i < deployedRoadSideUnits; i++) {
            LogAssert.contains(
                    simulationRule,
                    String.format("apps/rsu_%d/NesRSUSourceApp.log", i),
                    ".*Received V2X Message from veh_.*"
            );
        }

    }

    @Test
    public void sinkReceivedMessages() throws Exception {
        LogAssert.contains(
                simulationRule,
                "apps/veh_10/NesSinkApp.log",
                ".*Message received: veh_[0-9]*,[0-9]*,[0-9]*[.][0-9]*,[0-9]*[.][0-9]*,[0-9]*[.][0-9]*"
        );
    }
}
