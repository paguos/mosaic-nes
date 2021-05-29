//
// Created by parallels on 5/4/21.
//

#include "Scenario.h"

#include <fstream>
#include <loguru.hpp>
#include "SQLiteCpp/SQLiteCpp.h"

void Scenario::LoadCars(const json& carsJson) {
    for (auto carJson : carsJson) {
        json config = carJson["config"];
        Car car(config["name"], carJson["metadata"]);

        for (const auto& applicationName: config["applications"]) {
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
        json config = rsuConfig["config"];
        RoadSideUnit rsu(config["name"]);

        for (auto applicationName: config["applications"]) {
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
        Vehicle vehicle(vehicleId);
        vehicle.metadata = vehicleConfig;
        this->vehicles.push_back(vehicle);

        vehicleId++;
    }
}

Scenario Scenario::LoadFile(const string& filePath) {
    Scenario scenario{};

    std::ifstream inputFileStream(filePath);
    json config;
    inputFileStream >> config;

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
        for (const Position& position : rsu.positions) {
            json rsuJson = {};
            rsuJson["name"] = rsu.name;
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
        for (const Route& r: routes){
            json v_metadata = v.metadata;
            list<json> types;
            for (Car car: cars) {
                types.push_back({{"name", car.name}});
            }

            v_metadata["types"] = types;
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
