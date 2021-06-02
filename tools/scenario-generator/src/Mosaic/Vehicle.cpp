//
// Created by parallels on 5/26/21.
//

#include "Vehicle.h"


Vehicle::Vehicle(int id, const Car &car) : id(id), car(car) {}

bool Vehicle::operator==(const Vehicle &rhs) const {
    return id == rhs.id &&
           car == rhs.car &&
           routes == rhs.routes &&
           metadata == rhs.metadata;
}

bool Vehicle::operator!=(const Vehicle &rhs) const {
    return !(rhs == *this);
}
