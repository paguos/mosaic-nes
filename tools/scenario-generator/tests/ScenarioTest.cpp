//
// Created by parallels on 5/15/21.
//

#include <gtest/gtest.h>

#include <filesystem>

#include "src/Mosaic/Scenario.h"

std::string FIXTURES_DIR = TEST_FIXTURES_DIR;
std::string scenarioConfigPath = FIXTURES_DIR + "/config.json";

TEST(ScenarioLoadFromFile, Vehicles){
    Scenario s = Scenario::LoadFile(scenarioConfigPath);

    const int expectedVehiclesCount = 1;
    EXPECT_EQ(expectedVehiclesCount, s.vehicles.size());

    Vehicle v = s.vehicles.front();
    const int expectedVehicleId = 1;
    EXPECT_EQ(expectedVehicleId, v.id);

    json expectedVehicleMetadata = {};
    expectedVehicleMetadata["randomInt"] = 5;
    expectedVehicleMetadata["randomFloat"] = 50.5;
    expectedVehicleMetadata["randomString"] = "TEST";
    expectedVehicleMetadata["randomBoolean"] = true;
    EXPECT_EQ(expectedVehicleMetadata, v.metadata);
}

TEST(ScenarioLoadFromFile, Routes) {
    Scenario s = Scenario::LoadFile(scenarioConfigPath);

    const int expectedRoutesCount = 2;
    EXPECT_EQ(expectedRoutesCount, s.routes.size());

    Route expectedFirstRoute( 1,
                              Position("test_source_long_1", "test_source_lat_1"),
                              Position("test_target_long_1", "test_target_lat_1")
    );
    Route expectedSecondRoute( 2,
                               Position("test_source_long_2", "test_source_lat_2"),
                               Position("test_target_long_2", "test_target_lat_2")
    );

    list<Route> expectedRoutes{expectedFirstRoute, expectedSecondRoute};
    EXPECT_EQ(expectedRoutes, s.routes);
}

TEST(Route, LoadFromDB) {
    Route r( 1,
             Position("test_source_long_1", "test_source_lat_1"),
             Position("test_target_long_1", "test_target_lat_1")
    );
    Route expectedRoute = r;
    for (int i = 0; i < 4; ++i) {
        expectedRoute.metadata.insert(std::pair<int, std::string>(i, "connection_" + std::to_string(i)));
    }

    r.LoadSQLite(FIXTURES_DIR + "/test.db");

    EXPECT_EQ(expectedRoute, r);
}
