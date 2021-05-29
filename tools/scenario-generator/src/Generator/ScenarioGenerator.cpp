//
// Created by parallels on 4/30/21.
//

#include "ScenarioGenerator.h"

#include <filesystem>
#include <memory>
#include <utility>

#include <loguru.hpp>
#include <fstream>

ScenarioGenerator::ScenarioGenerator(GeneratorConfig config, Scenario routes) : config(std::move(config)),
                                                                                scenario(std::move(routes)) {
}

string ScenarioGenerator::rename(int routeId) const {
    fs::path scenarioPathWithId = config.scenarioPath.parent_path().append(config.scenarioPath.stem().string() + "_" + std::to_string(routeId));

    if(exists(scenarioPathWithId)) {
        fs::remove_all(scenarioPathWithId);
    }

    fs::rename(config.scenarioPath, scenarioPathWithId);
    return scenarioPathWithId.string();
}

void ScenarioGenerator::generateRoutes(ScenarioConvert scenarioConvert) {
    LOG_SCOPE_F(INFO, "routes generation");

    for (Route &route: scenario.routes) {
        LOG_F(INFO, "Generating scenario '%d' ...", route.id);
        scenarioConvert.osm2mosaic(route);
        string tempScenarioPath = rename(route.id);
        route.LoadSQLite(tempScenarioPath + "/application/" + config.scenarioName + ".db");
        if (!config.debug) {
            std::filesystem::remove_all(tempScenarioPath.c_str());
        }

        LOG_F(INFO, "Route '%d' generated!", route.id);
    }
}

void ScenarioGenerator::generateScenario(ScenarioConvert scenarioConvert) {
    LOG_SCOPE_F(INFO, "scenario creation");

    scenarioConvert.osm2mosaic();
    scenario.exportRoutes(config.scenarioPath.string() + "/application/" + config.scenarioName + ".db");

    LOG_F(INFO, "Converting routes from the database to sumo ...");
    fs::path sumoPath(config.scenarioPath.string() + "/sumo");
    fs::remove_all(sumoPath); // remove existing sumo files
    scenarioConvert.db2sumo(); // generate sumo files based on the routes
    fs::create_directory(sumoPath); // restore sumo directory

    // Move files to the sumo directory
    for (const auto & file : fs::directory_iterator(config.scenarioPath.string() + "/application")) {
        if (file.path().extension() == ".xml" || file.path().extension() == ".sumocfg") {
            fs::rename(file.path(), sumoPath.string() + "/" + file.path().filename().string());
        }
    }
    LOG_F(INFO, "Sumo routes generated!");

    LOG_F(INFO, "Generating mapping ...");
    json mappingConfig = scenario.ExportToJson();

    string mappingConfigPath = config.scenarioPath.string() + "/mapping/mapping_config.json";
    std::filesystem::remove(mappingConfigPath);

    std::ofstream outputFileStream(mappingConfigPath);
    outputFileStream << std::setw(4) << mappingConfig << std::endl;
    LOG_F(INFO, "Mapping generated!");
}

void ScenarioGenerator::run() {
    LOG_F(INFO, "Starting scenario generator ...");
    LOG_IF_F(INFO, config.debug, "%s", config.info().c_str());

    if(exists(config.scenarioPath)) {
        fs::remove_all(config.scenarioPath);
    }

    ScenarioConvert scenarioConvert(config);

    generateRoutes(scenarioConvert);
    generateScenario( scenarioConvert);

    LOG_F(INFO, "Scenario generator FINISHED!");
}
