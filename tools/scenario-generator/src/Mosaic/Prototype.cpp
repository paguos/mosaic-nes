//
// Created by parallels on 5/26/21.
//

#include "Prototype.h"

Prototype::Prototype(const string &name) : name(name) {}

bool Prototype::operator==(const Prototype &rhs) const {
    return name == rhs.name &&
           applications == rhs.applications;
}

bool Prototype::operator!=(const Prototype &rhs) const {
    return !(rhs == *this);
}

json Prototype::toJson() {
    json prototypeJson = {};
    prototypeJson["name"] = name;
    prototypeJson["applications"] = applications;
    return prototypeJson;
}

RoadSideUnit::RoadSideUnit(const string &name) : Prototype(name) {}

bool RoadSideUnit::operator==(const RoadSideUnit &rhs) const {
    return static_cast<const Prototype &>(*this) == static_cast<const Prototype &>(rhs) &&
           positions == rhs.positions;
}

bool RoadSideUnit::operator!=(const RoadSideUnit &rhs) const {
    return !(rhs == *this);
}

Car::Car(const string &name, const json &metadata) : Prototype(name), metadata(metadata) {}

bool Car::operator==(const Car &rhs) const {
    return static_cast<const Prototype &>(*this) == static_cast<const Prototype &>(rhs) &&
           metadata == rhs.metadata;
}

bool Car::operator!=(const Car &rhs) const {
    return !(rhs == *this);
}

json Car::toJson() {
    json carJson = Prototype::toJson();
    carJson.merge_patch(metadata);
    return carJson;
}
