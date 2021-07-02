//
// Created by parallels on 5/4/21.
//

#include "Scenario.h"

#include <fstream>
#include <loguru.hpp>
#include <utility>
#include "SQLiteCpp/SQLiteCpp.h"

Scenario::Scenario() = default;

Scenario::Scenario(NesFederate nesFederate) : nesFederate(std::move(nesFederate)) {}

void Scenario::LoadCars(const json& carsJson) {
    for (auto carJson : carsJson) {
        Car car(carJson["name"], carJson["metadata"]);

        for (const auto& applicationName: carJson["applications"]) {
            car.applications.push_back(applicationName);
        }

        this->cars.push_back(car);
    }
}


void Scenario::LoadRoutes(json routes) {
    int routeId = 1;

    for (auto routeConfig : routes)
    {
        Position sourcePosition(routeConfig["source"]["latitude"], routeConfig["source"]["longitude"]);
        Position targetPosition(routeConfig["target"]["latitude"], routeConfig["target"]["longitude"]);
        Route route(routeId, sourcePosition, targetPosition);
        this->routes.push_back(route);

        routeId++;
    }
}

void Scenario::LoadRoadSideUnits(json rsus) {
    for (auto rsuConfig : rsus) {
        RoadSideUnit rsu(rsuConfig["name"]);

        for (const auto& applicationName: rsuConfig["applications"]) {
            rsu.applications.push_back(applicationName);
        }

        for (auto positionConfig: rsuConfig["positions"]) {
            Position position(positionConfig["latitude"], positionConfig["longitude"]);
            rsu.positions.push_back(position);
        }

        this->rsus.push_back(rsu);
    }
}

void Scenario::LoadVehicles(json vehicles) {
    int vehicleId = 1;

    for (auto vehicleConfig : vehicles) {
        Vehicle vehicle(vehicleId, findCar(vehicleConfig["car"]));

        if (vehicleConfig["routes"] == "*") {
            vehicle.routes = this->routes;
        } else {
            string routeStr = vehicleConfig["routes"];
            int routeId = std::stoi(routeStr);
            vehicle.routes.push_back(routes.at(routeId - 1));
        }

        vehicle.metadata = vehicleConfig["metadata"];
        this->vehicles.push_back(vehicle);

        vehicleId++;
    }
}

Scenario Scenario::LoadFile(const string& filePath) {
    std::ifstream inputFileStream(filePath);
    json config;
    inputFileStream >> config;

    NesFederate nes = NesFederate::LoadFromJson(config["federates"]["nes"]);
    Scenario scenario(nes);

    const json cars = config["cars"];
    scenario.LoadCars(cars);

    const json routes = config["routes"];
    scenario.LoadRoutes(routes);

    const json rsus = config["rsus"];
    scenario.LoadRoadSideUnits(rsus);

    const json vehicles = config["vehicles"];
    scenario.LoadVehicles(vehicles);

    return scenario;
}

void Scenario::exportRoutes(const string& sqliteDBPath) {
    SQLite::Database    db(sqliteDBPath, SQLite::OPEN_READWRITE);
    for(Route route: routes){
        LOG_F(INFO,"Exporting route '%d'", route.id);
        SQLite::Transaction transaction(db);
        for (auto it=route.metadata.begin(); it!=route.metadata.end(); ++it){
            SQLite::Statement   query(db, "INSERT INTO Route(id, sequence_number, connection_id) VALUES (?,?,?);");
            query.bind(1,route.id);
            query.bind(2,it->first);
            query.bind(3,it->second);
            query.exec();
        }
        transaction.commit();
        LOG_F(INFO,"Route '%d' exported!", route.id);
    }
}

json Scenario::ExportPrototypes() {
    list<json> prototypes;

    for (Car car: cars) {
        prototypes.push_back(car.toJson());
    }

    for (RoadSideUnit rsu: rsus) {
        prototypes.push_back(rsu.toJson());
    }

    return prototypes;
}

json Scenario::ExportRoadSideUnits() {
    list<json> rsuList;

    for (const RoadSideUnit& rsu: rsus) {
        int id = 0;
        for (const Position& position : rsu.positions) {
            json rsuJson = {};
            rsuJson["name"] = rsu.name + "_" + std::to_string(id++);
            rsuJson["applications"] = rsu.applications;
            rsuJson["position"]["latitude"] = ::atof(position.latitude.c_str());;
            rsuJson["position"]["longitude"] = ::atof(position.longitude.c_str());
            rsuList.push_back(rsuJson);
        }
    }

    return rsuList;
}

json Scenario::ExportVehicles() {
    list<json> vehiclesJson;
    for (const Vehicle& v: vehicles){
        for (const Route& r: v.routes){
            json v_metadata = v.metadata;

            v_metadata["types"] = {{{"name", v.car.name}}};
            v_metadata["route"] = std::to_string(r.id);
            v_metadata["pos"] = 0;
            vehiclesJson.push_back(v_metadata);
        }
    }
    return vehiclesJson;
}

json Scenario::ExportToJson() {
    json mappingJson = {};

    mappingJson["prototypes"] = ExportPrototypes();
    mappingJson["rsus"] = ExportRoadSideUnits();
    mappingJson["vehicles"] = ExportVehicles();

    return mappingJson;
}

Car Scenario::findCar(const string& carName) {
    for (Car& car: cars) {
        if (car.name == carName) { return car; }
    }
    return Car("", nlohmann::basic_json());
}
