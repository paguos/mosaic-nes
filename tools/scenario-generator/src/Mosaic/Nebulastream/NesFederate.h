//
// Created by parallels on 6/3/21.
//

#include <list>
#include <string>

#include "nlohmann/json.hpp"

#include "NesNode.h"

#ifndef SCENARIO_GENERATOR_NESFEDERATE_H
#define SCENARIO_GENERATOR_NESFEDERATE_H

using json = nlohmann::json;
using std::list;
using std::string;

struct NesNodeConfig {
    string image;

    NesNodeConfig();

    NesNodeConfig(const string &image);

    bool operator==(const NesNodeConfig &rhs) const;

    bool operator!=(const NesNodeConfig &rhs) const;
};

struct NesFederate {
    NesNodeConfig coordinatorConfig;
    NesNodeConfig workerConfig;
    list<NesNode> nodes;
    bool enabled;

    NesFederate();
    NesFederate(const NesNodeConfig &coordinatorConfig, const NesNodeConfig &workerConfig, bool enabled);

    static NesFederate LoadFromJson(const json& nesFederateJson);
    json generateJson();

    bool operator==(const NesFederate &rhs) const;
    bool operator!=(const NesFederate &rhs) const;
};


#endif //SCENARIO_GENERATOR_NESFEDERATE_H
