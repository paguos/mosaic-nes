//
// Created by parallels on 5/4/21.
//

#include <any>
#include <filesystem>
#include <list>
#include <map>
#include <string>

#include "nlohmann/json.hpp"

#ifndef SCENARIO_GENERATOR_SCENARIO_H
#define SCENARIO_GENERATOR_SCENARIO_H

namespace fs = std::filesystem;

using json = nlohmann::json;
using std::list;
using std::map;
using std::string;


struct Position {
    string longitude;
    string latitude;
    Position(string longitude, string latitude);
    ~Position()=default;

    bool operator==(const Position &rhs) const;

    bool operator!=(const Position &rhs) const;
};

struct Route {
    int id;
    Position source;
    Position target;
    map<int, string> metadata;

    bool operator==(const Route &rhs) const;

    bool operator!=(const Route &rhs) const;

    Route(const Position& source, const Position& target);

    Route(int id, const Position &source, const Position &target);

    ~Route()=default;

    void LoadSQLite(const string& sqliteDBPath);
};

struct Vehicle {
    int id;
    json metadata;

    bool operator==(const Vehicle &rhs) const;

    bool operator!=(const Vehicle &rhs) const;

    explicit Vehicle(int id);
};

struct Scenario {
    list<Route> routes;
    list<Vehicle> vehicles;

    static Scenario LoadFile(const string& filePath);
    void exportRoutes(const string& sqlDBPath);
    void exportVehicles(const string& mappingConfigPath);
};

#endif //SCENARIO_GENERATOR_SCENARIO_H
