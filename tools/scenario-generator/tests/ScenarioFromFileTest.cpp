//
// Created by parallels on 5/15/21.
//

#include <gtest/gtest.h>

#include <filesystem>

#include "src/Mosaic/Scenario.h"

class ScenarioFromFile : public ::testing::Test {
protected:
    void SetUp() override {
        std::string fixturesDir = TEST_FIXTURES_DIR;
        std::string scenarioConfigPath = fixturesDir + "/config.json";
        s = Scenario::LoadFile(scenarioConfigPath);
    }

    static string floatToString(double f) {
        std::ostringstream streamObj;
        streamObj << std::fixed;
        streamObj << std::setprecision(7);
        streamObj << f;

        return streamObj.str();
    }

    Scenario s;
};

TEST_F(ScenarioFromFile, Cars) {
    const int expectedNumberOfCars = 1;
    EXPECT_EQ(expectedNumberOfCars, s.cars.size());

    Car car = s.cars.front();
    EXPECT_EQ("TEST_CAR", car.name);
    EXPECT_EQ(2, car.applications.size());

    int applicationCount = 1;
    for (auto it = car.applications.begin(); it != car.applications.end(); ++it) {
        string expectedName = "TEST_CAR_APP_" + std::to_string(applicationCount++);
        EXPECT_EQ(expectedName, it->c_str());
    }

    json expectedVehicleMetadata = {};
    expectedVehicleMetadata["randomInt"] = 6;
    expectedVehicleMetadata["randomFloat"] = 60.6;
    expectedVehicleMetadata["randomString"] = "TEST";
    expectedVehicleMetadata["randomBoolean"] = true;
    EXPECT_EQ(expectedVehicleMetadata, car.metadata);

}

TEST_F(ScenarioFromFile, RoadSideUnits) {
    const int expectedRSUCount = 1;
    EXPECT_EQ(expectedRSUCount, s.rsus.size());

    RoadSideUnit roadSideUnit = s.rsus.front();
    EXPECT_EQ("TEST_RSU", roadSideUnit.name);
    EXPECT_EQ(2, roadSideUnit.applications.size());
    EXPECT_EQ(2, roadSideUnit.positions.size());

    int applicationCount = 1;
    for (auto it = roadSideUnit.applications.begin(); it != roadSideUnit.applications.end(); ++it) {
        string expectedName = "TEST_RSU_APP_" + std::to_string(applicationCount++);
        EXPECT_EQ(expectedName, it->c_str());
    }

    float positionsCount = 1.0;
    double baseLatitude = 50.5067654;
    double baseLongitude = 10.3310764;

    for (const Position& p: roadSideUnit.positions) {
        Position expectedPosition(
                floatToString(baseLatitude + positionsCount),
                floatToString(baseLongitude + positionsCount)
                );
        EXPECT_EQ(expectedPosition, p);
        positionsCount++;
    }
}

TEST_F(ScenarioFromFile, Routes) {
    const int expectedRoutesCount = 2;
    EXPECT_EQ(expectedRoutesCount, s.routes.size());

    Route expectedFirstRoute( 1,
                              Position("test_source_lat_1", "test_source_long_1"),
                              Position("test_target_lat_1", "test_target_long_1")
    );
    Route expectedSecondRoute( 2,
                               Position("test_source_lat_2", "test_source_long_2"),
                               Position("test_target_lat_2", "test_target_long_2")
    );

    vector<Route> expectedRoutes{expectedFirstRoute, expectedSecondRoute};
    EXPECT_EQ(expectedRoutes, s.routes);
}

TEST_F(ScenarioFromFile, Vehicles){
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
