//
// Created by parallels on 5/4/21.
//

#include "Scenario.h"

#include <fstream>

#include <loguru.hpp>
#include "SQLiteCpp/SQLiteCpp.h"

Position::Position(string longitude, string latitude) : longitude(std::move(longitude)), latitude(std::move(latitude)) {}

bool Position::operator==(const Position &rhs) const {
    return longitude == rhs.longitude &&
           latitude == rhs.latitude;
}

bool Position::operator!=(const Position &rhs) const {
    return !(rhs == *this);
}

Route::Route(const Position& source, const Position& target) : source(source), target(target) {}
Route::Route(int id, const Position &source, const Position &target) : id(id), source(source), target(target) {}

void Route::LoadSQLite(const string& sqliteDBPath) {
    SQLite::Database    db(sqliteDBPath);
    const bool tableExists = db.tableExists("Route");
    if (tableExists) {
        SQLite::Statement   query(db, "SELECT sequence_number, connection_id FROM Route;");
        while (query.executeStep())
        {
            const int sequenceNr     = query.getColumn(0);
            const string connectionId  = query.getColumn(1);
            metadata.insert(std::pair<int, string>(sequenceNr, connectionId));
        }
    } else {
        LOG_F(WARNING, "SQLite could not find table 'Route'!");
    }
}

bool Route::operator==(const Route &rhs) const {
    return id == rhs.id &&
           source == rhs.source &&
           target == rhs.target &&
           metadata == rhs.metadata;
}

bool Route::operator!=(const Route &rhs) const {
    return !(rhs == *this);
}

Vehicle::Vehicle(int id) : id(id) {}

bool Vehicle::operator==(const Vehicle &rhs) const {
    return id == rhs.id &&
           metadata == rhs.metadata;
}

bool Vehicle::operator!=(const Vehicle &rhs) const {
    return !(rhs == *this);
}

Scenario Scenario::LoadFile(const string& filePath) {
    Scenario scenario{};

    std::ifstream inputFileStream(filePath);
    json config;
    inputFileStream >> config;

    const json routes = config["routes"];
    int routeId = 1;

    for (auto routeConfig : routes)
    {
        Position sourcePosition(routeConfig["source"]["longitude"], routeConfig["source"]["latitude"]);
        Position targetPosition(routeConfig["target"]["longitude"], routeConfig["target"]["latitude"]);
        Route route(routeId, sourcePosition, targetPosition);
        scenario.routes.push_back(route);

        routeId++;
    }

    int vehicleId = 1;
    const json vehicles = config["vehicles"];

    for (auto vehicleConfig : vehicles) {
        Vehicle vehicle(vehicleId);
        vehicle.metadata = vehicleConfig;
        scenario.vehicles.push_back(vehicle);

        vehicleId++;
    }

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

void Scenario::exportVehicles(const string& mappingConfigPath) {
    std::ifstream inputFileStream(mappingConfigPath);
    json mappingConfig;
    inputFileStream >> mappingConfig;

    list<json> vehiclesJson;
    for (const Vehicle& v: vehicles){
        for (const Route& r: routes){
            json v_metadata = v.metadata;
            v_metadata["route"] = std::to_string(r.id);
            v_metadata["pos"] = 0;
            vehiclesJson.push_back(v_metadata);
        }
    }

    mappingConfig["vehicles"] = vehiclesJson;

    std::ofstream outputFileStream(mappingConfigPath);
    outputFileStream << std::setw(4) << mappingConfig << std::endl;
}
