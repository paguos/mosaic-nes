//
// Created by parallels on 5/26/21.
//

#include <string>

#ifndef SCENARIO_GENERATOR_POSITION_H
#define SCENARIO_GENERATOR_POSITION_H

using std::string;

struct Position {
    string latitude;
    string longitude;
    Position(string latitude, string longitude);
    ~Position()=default;

    bool operator==(const Position &rhs) const;

    bool operator!=(const Position &rhs) const;
};

#endif //SCENARIO_GENERATOR_POSITION_H
