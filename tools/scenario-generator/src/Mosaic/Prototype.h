//
// Created by parallels on 5/26/21.
//

#include <list>
#include <string>
#include "Position.h"

#include "nlohmann/json.hpp"

#ifndef SCENARIO_GENERATOR_PROTOTYPE_H
#define SCENARIO_GENERATOR_PROTOTYPE_H

using std::list;
using std::string;
using json = nlohmann::json;

struct Prototype {
    string name;
    list<string> applications;

    explicit Prototype(const string &name);

    bool operator==(const Prototype &rhs) const;

    bool operator!=(const Prototype &rhs) const;

    json toJson();
};

struct RoadSideUnit : Prototype {
    list<Position> positions;

    explicit RoadSideUnit(const string &name);

    bool operator==(const RoadSideUnit &rhs) const;
    bool operator!=(const RoadSideUnit &rhs) const;
};

struct Car : Prototype {
    json metadata;

    Car(const string &name, const json &metadata);

    bool operator==(const Car &rhs) const;
    bool operator!=(const Car &rhs) const;

    json toJson();
};


#endif //SCENARIO_GENERATOR_PROTOTYPE_H
