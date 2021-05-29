//
// Created by parallels on 5/26/21.
//

#include "nlohmann/json.hpp"

#ifndef SCENARIO_GENERATOR_VEHICLE_H
#define SCENARIO_GENERATOR_VEHICLE_H

using json = nlohmann::json;

struct Vehicle {
    int id;
    json metadata;

    bool operator==(const Vehicle &rhs) const;

    bool operator!=(const Vehicle &rhs) const;

    explicit Vehicle(int id);
};

#endif //SCENARIO_GENERATOR_VEHICLE_H
