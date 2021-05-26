//
// Created by parallels on 5/26/21.
//

#include "Position.h"

Position::Position(string latitude, string longitude) : latitude(std::move(latitude)), longitude(std::move(longitude)) {}

bool Position::operator==(const Position &rhs) const {
    return longitude == rhs.longitude &&
           latitude == rhs.latitude;
}

bool Position::operator!=(const Position &rhs) const {
    return !(rhs == *this);
}
