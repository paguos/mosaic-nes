//
// Created by parallels on 5/26/21.
//

#include <gtest/gtest.h>

#include <filesystem>

#include "src/Mosaic/Scenario.h"

class LoadFromDB : public ::testing::Test {
protected:
    void SetUp() override {
        std::string FIXTURES_DIR = TEST_FIXTURES_DIR;
        dbFilePath = FIXTURES_DIR + "/test.db";
    }

    std::string dbFilePath;
};

TEST_F(LoadFromDB, Route) {
    Route r( 1,
         Position("test_source_long_1", "test_source_lat_1"),
         Position("test_target_long_1", "test_target_lat_1")
    );
    Route expectedRoute = r;
    for (int i = 0; i < 4; ++i) {
        expectedRoute.metadata.insert(std::pair<int, std::string>(i, "connection_" + std::to_string(i)));
    }

    r.LoadSQLite(dbFilePath);
    EXPECT_EQ(expectedRoute, r);
}