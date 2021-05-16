//
// Created by parallels on 4/30/21.
//

#include <list>
#include <map>
#include <string>

#include "src/Mosaic/Scenario.h"
#include "src/Generator/GeneratorConfig.h"
#include "src/Mosaic/ScenarioConvert.h"

#ifndef SCENARIO_GENERATOR_UTILS_H
#define SCENARIO_GENERATOR_UTILS_H

using std::list;
using std::map;
using std::string;

class ScenarioGenerator {
private:
    GeneratorConfig config;
    Scenario scenario;

    string rename(int routeId) const;

    void generateRoutes(ScenarioConvert scenarioConvert);
    void generateScenario(ScenarioConvert scenarioConvert);
public:
    ScenarioGenerator(GeneratorConfig config, Scenario routes);
    void run();

};

#endif //SCENARIO_GENERATOR_UTILS_H
