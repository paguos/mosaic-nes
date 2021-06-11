package stream.nebula.examples;

import org.jgrapht.Graph;
import org.jgrapht.nio.dot.DOTExporter;
import stream.nebula.model.topology.TopologyEntry;
import stream.nebula.model.topology.TopologyLink;
import stream.nebula.runtime.NebulaStreamRuntime;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * This example demonstrates how to use graph object to manipulate Topology graph fetched from the REST API.
 **/
public class GetTopologyExample {
    public static void main(String[] args) throws Exception {

        // Configure network connection to NES REST server
        NebulaStreamRuntime ner = NebulaStreamRuntime.getRuntime();
        ner.getConfig().setHost("localhost")
                .setPort("8081");
        Graph<TopologyEntry, TopologyLink> topology = ner.getNesTopology();

        // Exporting topology graph to DOT format
        // ID provider can not have dash character ("-"), hence we remove it in the toString() method
        DOTExporter<TopologyEntry, TopologyLink> exporter = new DOTExporter<>(TopologyEntry::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(topology, writer);
        System.out.println(writer.toString());
    }
}
