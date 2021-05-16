#include <gmock/gmock.h>
#include <gtest/gtest.h>

#include "src/Mosaic/ScenarioConvert.h"

const GeneratorConfig config(false, "test-output", "test-osm", "test-config", "test-scenario");

class MockCommand : public CommandInterface {
public:
    MOCK_METHOD(std::string, execute, (string cmd), (override));
};

TEST(ScenarioConvert, Db2Sumo){
    MockCommand mockCommand;
    string expectedCommand = "java -jar test-scenario --db2sumo -d test-osm/application/test-osm.db";
    EXPECT_CALL(mockCommand, execute(expectedCommand));
    ScenarioConvert scenarioConvert(config);

    scenarioConvert.db2sumo(mockCommand);
}

TEST(ScenarioConvert, Osm2Mosaic){
    MockCommand mockCommand;
    string expectedCommand = "java -jar test-scenario --osm2mosaic -i test-osm";
    EXPECT_CALL(mockCommand, execute(expectedCommand));
    ScenarioConvert scenarioConvert(config);

    scenarioConvert.osm2mosaic(mockCommand);
}

TEST(ScenarioConvert, Osm2MosaicWithRoute){

    Position origin("1","1");
    Position destination("2","2");
    Route testRoute(origin, destination);

    MockCommand mockCommand;
    string expectedCommand = "java -jar test-scenario --osm2mosaic -i test-osm --generate-routes --route-begin-latlon 1,1 --route-end-latlon 2,2 --number-of-routes 1";
    EXPECT_CALL(mockCommand, execute(expectedCommand));
    ScenarioConvert scenarioConvert(config);

    scenarioConvert.osm2mosaic(testRoute, mockCommand);
}