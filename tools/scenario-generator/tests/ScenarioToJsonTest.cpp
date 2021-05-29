//
// Created by parallels on 5/28/21.
//

#include <gtest/gtest.h>

#include <filesystem>

#include "src/Mosaic/Scenario.h"

class ScenarioToJson : public testing::Test {
protected:
    void SetUp() override {
        std::string fixturesDir = TEST_FIXTURES_DIR;
        std::string scenarioConfigPath = fixturesDir + "/config.json";
        Scenario s = Scenario::LoadFile(scenarioConfigPath);
        generatedJson = s.ExportToJson();
    }

    json generatedJson;
};

TEST_F(ScenarioToJson, Prototypes) {
    json prototypes = generatedJson["prototypes"];
    EXPECT_EQ(2, prototypes.size());

    json expectedCar = {};
    expectedCar["name"] = "TEST_CAR";
    expectedCar["applications"] = {"TEST_CAR_APP_1", "TEST_CAR_APP_2"};
    expectedCar["randomInt"] = 6;
    expectedCar["randomFloat"] = 60.6;
    expectedCar["randomString"] = "TEST";
    expectedCar["randomBoolean"] = true;

    json expectedRSU = {};
    expectedRSU["name"] = "TEST_RSU";
    expectedRSU["applications"] = {"TEST_RSU_APP_1", "TEST_RSU_APP_2"};

    json expectedJson = {expectedCar, expectedRSU};

    EXPECT_EQ(expectedJson, prototypes);
}

TEST_F(ScenarioToJson, RoadSideUnits) {
    json rsus = generatedJson["rsus"];
    EXPECT_EQ(2, rsus.size());

    json rsu1 = {};
    rsu1["name"] = "TEST_RSU";
    rsu1["applications"] = {"TEST_RSU_APP_1", "TEST_RSU_APP_2"};
    rsu1["position"]["latitude"] = 51.5067654;
    rsu1["position"]["longitude"] = 11.3310764;

    json rsu2 = {};
    rsu2["name"] = "TEST_RSU";
    rsu2["applications"] = {"TEST_RSU_APP_1", "TEST_RSU_APP_2"};
    rsu2["position"]["latitude"] = 52.5067654;
    rsu2["position"]["longitude"] = 12.3310764;

    json expectedJson = {rsu1, rsu2};
    EXPECT_EQ(expectedJson, rsus);
}

TEST_F(ScenarioToJson, Vehicles) {
    json vehicles = generatedJson["vehicles"];
    EXPECT_EQ(2, vehicles.size());

    json v1 = {};
    v1["randomInt"] = 5;
    v1["randomFloat"] = 50.5;
    v1["randomString"] = "TEST";
    v1["randomBoolean"] = true;

    v1["pos"] = 0;
    v1["route"] = "1";
    v1["types"] = {{{"name", "TEST_CAR"}}};

    json v2 = {};
    v2["randomInt"] = 5;
    v2["randomFloat"] = 50.5;
    v2["randomString"] = "TEST";
    v2["randomBoolean"] = true;

    v2["pos"] = 0;
    v2["route"] = "2";
    v2["types"] = {{{"name", "TEST_CAR"}}};

    json expectedVehicles = {v1, v2};
    EXPECT_EQ(expectedVehicles, vehicles);
}
