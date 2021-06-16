package com.github.paguos.mosaic.fed.config.util;

import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.nebulastream.node.NesBuilder;
import com.github.paguos.mosaic.fed.nebulastream.node.Coordinator;
import com.github.paguos.mosaic.fed.nebulastream.node.NesNode;
import com.github.paguos.mosaic.fed.nebulastream.node.Worker;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationParser {

    public static Coordinator parseConfig(CNes config) {
        List<NesNode> nodes = parseNodes(config.nodes);

        return NesBuilder.createCoordinator("nes-coordinator")
                .children(nodes)
                .build();
    }

    private static List<NesNode> parseNodes(List<CNesNode> nodeConfigs) {
        List<NesNode> nodes = new ArrayList<>();

        for (CNesNode nodeConfig :
                nodeConfigs) {
            Worker worker = NesBuilder.createWorker(nodeConfig.name)
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
