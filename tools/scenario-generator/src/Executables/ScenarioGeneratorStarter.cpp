#include <iostream>
#include <filesystem>

#include <cxxopts.hpp>

#include "src/Generator/ScenarioGenerator.h"

cxxopts::ParseResult parse(int argc, char* argv[]){
    string scenarioConvertDefaultPath = std::filesystem::current_path().string() + "/scenario-convert-21.0.jar";

    cxxopts::Options options("MOSAIC Scenario Generator", "A small tool to generateRoutes complex scenarios");
    options.add_options()
            ("h,help", "Print help")
            ("c,config", "Path to config file (in json format)", cxxopts::value<std::string>())
            ("d,debug", "Enable debugging")
            ("osm", "OSM file to generateRoutes the scenario from", cxxopts::value<std::string>())
            ("o,output", "Output path", cxxopts::value<std::string>()->default_value(std::filesystem::current_path()))
            ("s,scenario-convert", "Path of the scenario converter jar",
             cxxopts::value<std::string>()->default_value(scenarioConvertDefaultPath));

    auto result = options.parse(argc, argv);


    if (result.count("help"))
    {
        std::cout << options.help({""}) << std::endl;
        exit(0);
    }

    auto required = {"config", "osm"};
    for(const std::string &arg : required) {
        if (result.count(arg) == 0) {
            std::cout << "Missing required argument: --" << arg << std::endl;
            exit(0);
        }
    }

    return result;
}

int main(int argc, char* argv[]) {
    auto result = parse(argc, argv);
    const auto& arguments = result.arguments();

    GeneratorConfig config(
            result["debug"].as<bool>(),
            result["output"].as<std::string>(),
            result["osm"].as<std::string>(),
            result["config"].as<std::string>(),
            result["scenario-convert"].as<std::string>());

    Scenario scenario = Scenario::LoadFile(config.routesFilePath);
    ScenarioGenerator generator(config, scenario);
    generator.run();
    return 0;
}
