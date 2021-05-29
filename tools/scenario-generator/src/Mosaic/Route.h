//
// Created by parallels on 5/26/21.
//

#include "Position.h"

#include <map>

#ifndef SCENARIO_GENERATOR_ROUTE_H
#define SCENARIO_GENERATOR_ROUTE_H

using std::map;

struct Route {
    int id;
    Position source;
    Position target;
    map<int, string> metadata;

    bool operator==(const Route &rhs) const;

    bool operator!=(const Route &rhs) const;

    Route(const Position &source, const Position &target);

    Route(int id, const Position &source, const Position &target);

    ~Route() = default;

    void LoadSQLite(const string &sqliteDBPath);
};


#endif //SCENARIO_GENERATOR_ROUTE_H
