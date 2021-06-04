//
// Created by parallels on 6/3/21.
//

#include <list>
#include <string>

#include "nlohmann/json.hpp"

#ifndef SCENARIO_GENERATOR_NESNODE_H
#define SCENARIO_GENERATOR_NESNODE_H

using json = nlohmann::json;
using std::list;
using std::string;

struct NesNode {
    string  name;
    list<NesNode> nodes;

    NesNode();

    explicit NesNode(string name);
    static NesNode LoadNode(json nodeJson);
    json generateJson();

    bool operator==(const NesNode &rhs) const;

    bool operator!=(const NesNode &rhs) const;
};


#endif //SCENARIO_GENERATOR_NESNODE_H
