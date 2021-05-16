//
// Created by parallels on 5/13/21.
//

#include <string>
#include "src/Generator/GeneratorConfig.h"
#include "Scenario.h"

#ifndef SCENARIO_GENERATOR_SCENARIOCONVERT_H
#define SCENARIO_GENERATOR_SCENARIOCONVERT_H

struct CommandInterface {
    virtual std::string execute(string cmd) = 0;
};

struct Command : CommandInterface {
    std::string execute(string cmd) override;
};


class ScenarioConvert {

private:
    GeneratorConfig config;
public:
    ScenarioConvert(const GeneratorConfig &config);

    void db2sumo();
    void db2sumo(CommandInterface& command);
    void osm2mosaic();
    void osm2mosaic(CommandInterface& command);
    void osm2mosaic(Route& route);
    void osm2mosaic(Route& route, CommandInterface& command);
};


#endif //SCENARIO_GENERATOR_SCENARIOCONVERT_H
