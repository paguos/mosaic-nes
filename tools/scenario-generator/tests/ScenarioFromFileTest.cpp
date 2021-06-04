//
// Created by parallels on 5/15/21.
//

#include <gtest/gtest.h>

#include <filesystem>

#include "src/Mosaic/Route.h"
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

    static Route expectedRoute(int id) {
        string sourceLat = "test_source_lat_" + std::to_string(id);
        string sourceLong = "test_source_long_" + std::to_string(id);
        string targetLat = "test_target_lat_" + std::to_string(id);
        string targetLong = "test_target_long_" + std::to_string(id);

        return Route(id,
                Position(sourceLat, sourceLong),
                Position(targetLat, targetLong)
                );
    }

    static json expectedMetadata(int id) {
        json metadata = {};
        metadata["randomInt"] = id;
        float f = id * 10;
        metadata["randomFloat"] = f;
        metadata["randomString"] = "TEST_" + std::to_string(id);
        metadata["randomBoolean"] = true;
        return metadata;
    }

    Scenario s;
};

TEST_F(ScenarioFromFile, Cars) {
    const int expectedNumberOfCars = 2;
    EXPECT_EQ(expectedNumberOfCars, s.cars.size());

    Car c1 = s.cars.at(0);
    EXPECT_EQ("TEST_CAR_1", c1.name);
    EXPECT_EQ(2, c1.applications.size());

    int applicationCount = 1;
    for (auto it = c1.applications.begin(); it != c1.applications.end(); ++it) {
        string expectedName = "TEST_CAR_APP_" + std::to_string(applicationCount++);
        EXPECT_EQ(expectedName, it->c_str());
    }

    EXPECT_EQ(expectedMetadata(1), c1.metadata);

    Car c2 = s.cars.at(1);
    EXPECT_EQ("TEST_CAR_2", c2.name);
    EXPECT_EQ(2, c2.applications.size());

    applicationCount = 1;
    for (auto it = c2.applications.begin(); it != c2.applications.end(); ++it) {
        string expectedName = "TEST_CAR_APP_" + std::to_string(applicationCount++);
        EXPECT_EQ(expectedName, it->c_str());
    }

    EXPECT_EQ(expectedMetadata(2), c2.metadata);
}

TEST_F(ScenarioFromFile, NesFederate) {
    NesNodeConfig coordinatorConfig("nes-coordinator:test");
    NesNodeConfig workerConfig("nes-worker:test");

    NesFederate expectedFederate(coordinatorConfig, workerConfig, true);

    NesNode w1("test_worker_1");
    NesNode w2("test_worker_2");
    w2.nodes.emplace_back("test_source");

    expectedFederate.nodes.push_back(w1);
    expectedFederate.nodes.push_back(w2);

    EXPECT_EQ(expectedFederate, s.nesFederate);
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

    vector<Route> expectedRoutes{expectedRoute(1), expectedRoute(2)};
    EXPECT_EQ(expectedRoutes, s.routes);
}

TEST_F(ScenarioFromFile, Vehicles){
    EXPECT_EQ(2, s.vehicles.size());

    // Vehicle #1
    Vehicle v1 = s.vehicles.at(0);
    EXPECT_EQ(1, v1.id);
    EXPECT_EQ(2, v1.routes.size());

    vector<Route> expectedRoutes{expectedRoute(1), expectedRoute(2)};
    EXPECT_EQ(expectedRoutes, v1.routes);
    EXPECT_EQ(expectedMetadata(5), v1.metadata);

    // Vehicle #2
    Vehicle v2 = s.vehicles.at(1);
    EXPECT_EQ(2, v2.id);
    EXPECT_EQ(1, v2.routes.size());

    EXPECT_EQ(vector<Route>{expectedRoute(2)}, v2.routes);
    EXPECT_EQ(expectedMetadata(6), v2.metadata);
}
