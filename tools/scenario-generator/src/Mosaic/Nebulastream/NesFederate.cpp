//
// Created by parallels on 6/3/21.
//

#include "NesFederate.h"

#include <utility>


NesFederate NesFederate::LoadFromJson(const json &nesFederateJson) {
    NesNodeConfig coordinatorConfig(nesFederateJson["coordinator"]["image"]);
    NesNodeConfig workerConfig(nesFederateJson["worker"]["image"]);
    NesUIConfig nesUiConfig(nesFederateJson["ui"]["image"], nesFederateJson["ui"]["reactPort"], nesFederateJson["ui"]["enabled"]);
    NesFederate federate(coordinatorConfig, workerConfig, nesUiConfig,nesFederateJson["enabled"]);

    for (const auto& nesNode: nesFederateJson["nodes"]) {
        federate.nodes.push_back(NesNode::LoadNode(nesNode));
    }

    return federate;
}

json NesFederate::generateJson() {
    json federate = {};
    federate["coordinator"] = {{"image", coordinatorConfig.image }};
    federate["ui"] = {{"enabled", nesUIConfig.enabled }, {"image", nesUIConfig.image }, {"reactPort", nesUIConfig.reactPort }};
    federate["worker"] = {{"image", workerConfig.image }};

    auto nodesArray = json::array();
    for(NesNode nesNode: nodes){
        nodesArray.push_back(nesNode.generateJson());
    }
    federate["nodes"] = nodesArray;
    return federate;
}

NesFederate::NesFederate(const NesNodeConfig &coordinatorConfig, const NesNodeConfig &workerConfig,
                         const NesUIConfig &nesUiConfig, bool enabled) : coordinatorConfig(
        coordinatorConfig), workerConfig(workerConfig), nesUIConfig(nesUiConfig), nodes(nodes), enabled(enabled) {}

bool NesFederate::operator==(const NesFederate &rhs) const {
    return coordinatorConfig == rhs.coordinatorConfig &&
           workerConfig == rhs.workerConfig &&
           nesUIConfig == rhs.nesUIConfig &&
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

NesUIConfig::NesUIConfig() {}

NesUIConfig::NesUIConfig(const string &image, int reactPort, bool enabled) : enabled(enabled), image(image),
                                                                             reactPort(reactPort) {}

bool NesUIConfig::operator==(const NesUIConfig &rhs) const {
    return enabled == rhs.enabled &&
           image == rhs.image &&
           reactPort == rhs.reactPort;
}

bool NesUIConfig::operator!=(const NesUIConfig &rhs) const {
    return !(rhs == *this);
}
