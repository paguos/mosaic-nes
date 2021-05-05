//
// Created by parallels on 5/13/21.
//

#include "GeneratorConfig.h"

GeneratorConfig::GeneratorConfig(bool debug, std::string outputPath, std::string osmFilePath, std::string routesFilePath,
                                 std::string scenarioConvertPath) : debug(debug), outputPath(outputPath),
                                                               osmFilePath(std::move(osmFilePath)),
                                                               routesFilePath(std::move(routesFilePath)),
                                                               scenarioConvertPath(std::move(scenarioConvertPath)) {
    scenarioName = this->osmFilePath.stem().string();
    scenarioPath = this->osmFilePath.parent_path().append(scenarioName);
}

std::string GeneratorConfig::info() const {
    std::string info = "Generator Config {";
    info += "\n\tOutput path: " + outputPath.string();
    info += "\n\tOSM file: " + osmFilePath.string();
    info += "\n\tConfig file: " + routesFilePath.string();
    info += "\n\tScenario convert path: " + scenarioConvertPath.string();
    info += "\n}";
    return info;
}