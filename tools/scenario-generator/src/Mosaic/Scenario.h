//
// Created by parallels on 5/4/21.
//

#include <any>
#include <filesystem>
#include <list>
#include <map>
#include <string>
#include <vector>

#include "nlohmann/json.hpp"

#include "Position.h"
#include "Prototype.h"
#include "Route.h"
#include "Vehicle.h"

#ifndef SCENARIO_GENERATOR_SCENARIO_H
#define SCENARIO_GENERATOR_SCENARIO_H

namespace fs = std::filesystem;

using json = nlohmann::json;
using std::list;
using std::map;
using std::string;
using std::vector;

class Scenario {

private:
    void LoadCars(const json& carsJson);
    void LoadRoutes(json routes);
    void LoadRoadSideUnits(json rsus);
    void LoadVehicles(json vehicles);

    json ExportPrototypes();
    json ExportRoadSideUnits();
    json ExportVehicles();
    Car findCar(const string& carName);

public:
    vector<Car> cars;
    vector<Route> routes;
    vector<RoadSideUnit> rsus;
    vector<Vehicle> vehicles;

    static Scenario LoadFile(const string& filePath);
    void exportRoutes(const string& sqlDBPath);
    json ExportToJson();
};

#endif //SCENARIO_GENERATOR_SCENARIO_H
