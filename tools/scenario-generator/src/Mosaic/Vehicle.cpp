//
// Created by parallels on 5/26/21.
//

#include "Vehicle.h"

Vehicle::Vehicle(int id) : id(id) {}

bool Vehicle::operator==(const Vehicle &rhs) const {
    return id == rhs.id &&
           metadata == rhs.metadata;
}

bool Vehicle::operator!=(const Vehicle &rhs) const {
    return !(rhs == *this);
}
