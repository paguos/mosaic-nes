package com.github.paguos.mosaic.fed.config.util;

import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.model.node.NesBuilder;
import com.github.paguos.mosaic.fed.model.node.NesCoordinator;
import com.github.paguos.mosaic.fed.model.node.NesNode;
import com.github.paguos.mosaic.fed.model.node.NesWorker;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationParser {

    public static NesCoordinator parseConfig(CNes config) {
        List<NesNode> nodes = parseNodes(config.nodes);

        return NesBuilder.createCoordinator("nes-coordinator")
                .children(nodes)
                .build();
    }

    private static List<NesNode> parseNodes(List<CNesNode> nodeConfigs) {
        List<NesNode> nodes = new ArrayList<>();

        for (CNesNode nodeConfig :
                nodeConfigs) {
            NesWorker worker = NesBuilder.createWorker(nodeConfig.name)
                    .dataPort(NesNode.getNextDataPort())
                    .rpcPort(NesNode.getNextRPCPort())
                    .build();

            List<NesNode> children = parseNodes(nodeConfig.nodes);
            for (NesNode child :
                    children) {
                worker.addChild(child);
            }
            nodes.add(worker);
        }

        return nodes;
    }
}
