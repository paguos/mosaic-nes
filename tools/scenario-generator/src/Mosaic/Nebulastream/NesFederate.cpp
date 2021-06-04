//
// Created by parallels on 6/3/21.
//

#include "NesFederate.h"

#include <utility>

NesFederate::NesFederate() = default;

NesFederate::NesFederate(const NesNodeConfig &coordinatorConfig, const NesNodeConfig &workerConfig, bool enabled)
        : coordinatorConfig(coordinatorConfig), workerConfig(workerConfig), enabled(enabled) {}

NesFederate NesFederate::LoadFromJson(const json &nesFederateJson) {
    NesNodeConfig coordinatorConfig(nesFederateJson["coordinator"]["image"]);
    NesNodeConfig workerConfig(nesFederateJson["worker"]["image"]);
    NesFederate federate(coordinatorConfig, workerConfig, nesFederateJson["enabled"]);

    for (const auto& nesNode: nesFederateJson["nodes"]) {
        federate.nodes.push_back(NesNode::LoadNode(nesNode));
    }

    return federate;
}

json NesFederate::generateJson() {
    json federate = {};
    federate["coordinator"] = {{"image", coordinatorConfig.image }};
    federate["worker"] = {{"image", workerConfig.image }};

    auto nodesArray = json::array();
    for(NesNode nesNode: nodes){
        nodesArray.push_back(nesNode.generateJson());
    }
    federate["nodes"] = nodesArray;
    return federate;
}

bool NesFederate::operator==(const NesFederate &rhs) const {
    return coordinatorConfig == rhs.coordinatorConfig &&
           workerConfig == rhs.workerConfig &&
           nodes == rhs.nodes &&
           enabled == rhs.enabled;
}

bool NesFederate::operator!=(const NesFederate &rhs) const {
    return !(rhs == *this);
}


NesNodeConfig::NesNodeConfig() = default;

NesNodeConfig::NesNodeConfig(const string &image) : image(image) {}

bool NesNodeConfig::operator==(const NesNodeConfig &rhs) const {
    return image == rhs.image;
}

bool NesNodeConfig::operator!=(const NesNodeConfig &rhs) const {
    return !(rhs == *this);
}
