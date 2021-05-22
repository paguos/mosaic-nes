package com.github.paguos.mosaic.fed.config.util;

import com.github.paguos.mosaic.fed.config.CNes;
import com.github.paguos.mosaic.fed.config.model.CNesNode;
import com.github.paguos.mosaic.fed.model.*;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationParser {

    private static int nodeCount = 1;
    private static int currentDataPort = NesWorker.DEFAULT_DATA_PORT;
    private static int currentRPCPort = NesWorker.DEFAULT_RPC_PORT;

    public static NesTopology parseTopology(CNes config) {
        NesCoordinator coordinator = new NesCoordinator("nes-coordinator");
        List<NesNode> rootNodes = parseNodes(config.nodes);

        return new NesTopology(coordinator, rootNodes);
    }

    private static List<NesNode> parseNodes(List<CNesNode> nodeConfigs) {
        List<NesNode> nodes = new ArrayList<>();

        for (CNesNode nodeConfig :
                nodeConfigs) {
            NesWorker worker = NesWorkerBuilder.createWorker(nodeConfig.name, ++nodeCount)
                    .dataPort(currentDataPort)
                    .rpcPort(currentRPCPort)
                    .build();

            currentDataPort += 10;
            currentRPCPort += 10;

            List<NesNode> children = parseNodes(nodeConfig.nodes);
            for (NesNode child:
                 children) {
                worker.addChild(child);
            }
            nodes.add(worker);
        }

        return nodes;
    }
}
