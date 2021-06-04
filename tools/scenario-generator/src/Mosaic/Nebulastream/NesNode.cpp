//
// Created by parallels on 6/3/21.
//

#include "NesNode.h"

#include <utility>

NesNode::NesNode(string name) : name(std::move(name)) {}

NesNode NesNode::LoadNode(json nodeJson) {
    NesNode nesNode(nodeJson["name"]);

    for (const auto& childNode: nodeJson["nodes"]) {
        nesNode.nodes.push_back(NesNode::LoadNode(childNode));
    }

    return nesNode;
}

json NesNode::generateJson() {
    json nodeJson{};
    nodeJson["name"] = name;

    auto nodesJson = json::array();
    for (NesNode n: nodes){
        nodesJson.push_back(n.generateJson());
    }
    nodeJson["nodes"] = nodesJson;

    return nodeJson;
}

bool NesNode::operator==(const NesNode &rhs) const {
    return name == rhs.name &&
           nodes == rhs.nodes;
}

bool NesNode::operator!=(const NesNode &rhs) const {
    return !(rhs == *this);
}

NesNode::NesNode() = default;
