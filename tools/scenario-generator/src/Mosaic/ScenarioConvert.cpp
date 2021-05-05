//
// Created by parallels on 5/13/21.
//

#include "ScenarioConvert.h"

string Command::execute(string cmd) {
    std::array<char, 128> buffer{};
    std::string result;
    std::unique_ptr<FILE, decltype(&pclose)> pipe(popen(cmd.c_str(), "r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }
    return result;
}

ScenarioConvert::ScenarioConvert(const GeneratorConfig &config) : config(config) {}

void ScenarioConvert::db2sumo() {
    Command command;
    db2sumo(command);
}

void ScenarioConvert::db2sumo(CommandInterface &command) {
    std::string cmd = "java -jar";
    cmd += " " + config.scenarioConvertPath.string();
    cmd += " --db2sumo -d " + config.scenarioPath.string();
    cmd += "/application/" + config.scenarioName + ".db";

    command.execute(cmd);
}

void ScenarioConvert::osm2mosaic() {
    Command command;
    osm2mosaic(command);
}

void ScenarioConvert::osm2mosaic(CommandInterface &command) {
    std::string cmd = "java -jar";
    cmd += " " + config.scenarioConvertPath.string();
    cmd += " --osm2mosaic -i " + config.osmFilePath.string();

    command.execute(cmd);
}

void ScenarioConvert::osm2mosaic(Route &route) {
    Command command;
    osm2mosaic(route, command);
}

void ScenarioConvert::osm2mosaic(Route &route, CommandInterface &command) {
    std::string cmd = "java -jar";
    cmd += " " + config.scenarioConvertPath.string();
    cmd += " --osm2mosaic -i " + config.osmFilePath.string() + " --generate-routes";
    cmd += " --route-begin-latlon " + route.source.latitude + "," + route.source.longitude;
    cmd += " --route-end-latlon " + route.target.latitude + "," + route.target.longitude;
    cmd += " --number-of-routes 1";

    command.execute(cmd);
}
