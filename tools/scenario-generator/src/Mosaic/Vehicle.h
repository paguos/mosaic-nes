//
// Created by parallels on 5/26/21.
//

#include <vector>
#include "nlohmann/json.hpp"

#include "Prototype.h"
#include "Route.h"

#ifndef SCENARIO_GENERATOR_VEHICLE_H
#define SCENARIO_GENERATOR_VEHICLE_H

using json = nlohmann::json;
using std::vector;

struct Vehicle {
    int id;
    Car car;
    vector<Route> routes;
    json metadata;

    Vehicle(int id, const Car &car);

    bool operator==(const Vehicle &rhs) const;

    bool operator!=(const Vehicle &rhs) const;
};

#endif //SCENARIO_GENERATOR_VEHICLE_H
