//
// Created by parallels on 5/13/21.
//

#ifndef SCENARIO_GENERATOR_GENERATORCONFIG_H
#define SCENARIO_GENERATOR_GENERATORCONFIG_H

#include <filesystem>
#include <string>

namespace fs = std::filesystem;

struct GeneratorConfig {
    bool debug{};

    fs::path outputPath;
    fs::path osmFilePath;
    fs::path routesFilePath;
    fs::path scenarioConvertPath;
    std::string scenarioName;
    fs::path scenarioPath;

    GeneratorConfig(bool debug, std::string outputPath, std::string osmFilePath, std::string routesFilePath,
                    std::string scenarioConvertPath);

    std::string info() const;
};


#endif //SCENARIO_GENERATOR_GENERATORCONFIG_H
