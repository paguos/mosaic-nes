//
// Created by parallels on 6/4/21.
//

#include <gtest/gtest.h>
#include "nlohmann/json.hpp"

#include "src/Mosaic/Nebulastream/NesFederate.h"
#include "src/Mosaic/Nebulastream/NesNode.h"

using json = nlohmann::json;

class NesFederateToJson : public testing::Test {
protected:
    void SetUp() override {
        NesNodeConfig coordinatorConfig("test-coordinator-image");
        NesUIConfig uiConfig("nes-ui:test", 12345, true);
        NesNodeConfig workerConfig("test-worker-image");
        nesFederate = NesFederate(coordinatorConfig, workerConfig, uiConfig, true);

        NesNode n1("test-node-1");
        nesFederate.nodes.push_back(n1);
    }

    NesFederate nesFederate;
};

TEST_F(NesFederateToJson, SingleNode) {
    json expectedNesFederateJson{};
    expectedNesFederateJson["coordinator"]["image"] = "test-coordinator-image";
    expectedNesFederateJson["worker"]["image"] = "test-worker-image";
    expectedNesFederateJson["ui"]["enabled"] = true;
    expectedNesFederateJson["ui"]["image"] = "nes-ui:test";
    expectedNesFederateJson["ui"]["reactPort"] = 12345;

    expectedNesFederateJson["nodes"] = {{{"name", "test-node-1"}, {"nodes", json::array()}}};

    EXPECT_EQ(expectedNesFederateJson, nesFederate.generateJson());
}

TEST_F(NesFederateToJson, MutipleNodes) {
    NesNode n2("test-node-2");
    n2.nodes.emplace_back("test-node-3");
    nesFederate.nodes.push_back(n2);

    json expectedNesFederateJson{};
    expectedNesFederateJson["coordinator"]["image"] = "test-coordinator-image";
    expectedNesFederateJson["worker"]["image"] = "test-worker-image";

    expectedNesFederateJson["ui"]["enabled"] = true;
    expectedNesFederateJson["ui"]["image"] = "nes-ui:test";
    expectedNesFederateJson["ui"]["reactPort"] = 12345;

    expectedNesFederateJson["nodes"] = {
            {{"name", "test-node-1"}, {"nodes", json::array()}},
            {{"name", "test-node-2"}, {
                                       "nodes", {{{"name", "test-node-3"}, {"nodes", json::array()}}}
                                      }}
    };

    EXPECT_EQ(expectedNesFederateJson, nesFederate.generateJson());
}