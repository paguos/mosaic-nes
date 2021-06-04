package com.nebula.stream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTExporter;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import stream.nebula.model.executioplan.ExecutionLink;
import stream.nebula.model.executioplan.ExecutionNode;
import stream.nebula.model.queryplan.LogicalQuery;
import stream.nebula.model.topology.TopologyEntry;
import stream.nebula.model.topology.TopologyLink;
import stream.nebula.utils.GraphBuilder;
import java.io.StringWriter;
import java.io.Writer;

public class GraphBuilderTest {
    @Test
    public void buildTopologyGraphReturnCorrectTopologyGraph() {

        JSONObject topologyJSonObject = new JSONObject("{\"nodes\":[{\"available_resources\":7,\"id\":1,\"ip_address\":\"127.0.0.1\"},{\"available_resources\":5,\"id\":2,\"ip_address\":\"127.0.0.1\"}],\"edges\":[{\"source\":2,\"target\":1}]}");
        Graph<TopologyEntry, TopologyLink> topology = GraphBuilder.buildTopologyGraphFromJson(topologyJSonObject);

        // Exporting topology graph to DOT format
        // ID provider can not have dash character ("-"), hence we remove it in the toString() method
        DOTExporter<TopologyEntry, TopologyLink> exporter = new DOTExporter<>(TopologyEntry::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(topology, writer);

        String expectedGraphInDotFormat =
                "strict digraph G {\n" +
                        "  1;\n" +
                        "  2;\n" +
                        "  2 -> 1;\n" +
                        "}\n";
        Assert.assertEquals(expectedGraphInDotFormat, writer.toString());

    }

    @Test
    public void buildQueryPlanGraphReturnCorrectQueryPlanGraph() {
        JSONObject queryPlanJSonObject = new JSONObject("{\"nodes\":[{\"name\":\"SINK(OP-2)\",\"id\":\"2\",\"nodeType\":\"SINK\"},{\"name\":\"SOURCE(OP-1)\",\"id\":\"1\",\"nodeType\":\"SOURCE\"}],\"edges\":[{\"source\":\"SOURCE(OP-1)\",\"target\":\"SINK(OP-2)\"}]}");

        Graph<LogicalQuery, DefaultEdge> queryPlan = GraphBuilder.buildQueryPlanGraphFromJson(queryPlanJSonObject);
        DOTExporter<LogicalQuery, DefaultEdge> exporter = new DOTExporter<>(LogicalQuery::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(queryPlan, writer);

        String expectedQueryPlanInDotFormat =
                "strict digraph G {\n" +
                        "  SINKOP_2;\n" +
                        "  SOURCEOP_1;\n" +
                        "  SOURCEOP_1 -> SINKOP_2;\n" +
                        "}\n";
        Assert.assertEquals(expectedQueryPlanInDotFormat, writer.toString());
    }

    @Test
    public void buildExecutionPlanGraphReturnCorrectExecutionPlanGraph() {
        JSONObject topologyJSonObject = new JSONObject("{\"nodes\":[{\"available_resources\":7,\"id\":1,\"ip_address\":\"127.0.0.1\"},{\"available_resources\":5,\"id\":2,\"ip_address\":\"127.0.0.1\"}],\"edges\":[{\"source\":2,\"target\":1}]}");
        Graph<TopologyEntry, TopologyLink> topology = GraphBuilder.buildTopologyGraphFromJson(topologyJSonObject);

        JSONObject executionPlanJSonObject = new JSONObject("{\"executionNodes\":[{\"topologyNodeId\":2,\"ScheduledQueries\":[{\"queryId\":2,\"querySubPlans\":[{\"querySubPlanId\":3,\"operator\":\"SINK(13)\\n  CENTRALWINDOW(11)\\n    WATERMARKASSIGNER(8)\\n      SOURCE(7,default_logical)\\n\"}]}],\"topologyNodeIpAddress\":\"127.0.0.1\",\"executionNodeId\":2},{\"topologyNodeId\":1,\"ScheduledQueries\":[{\"queryId\":2,\"querySubPlans\":[{\"querySubPlanId\":4,\"operator\":\"SINK(10)\\n  SOURCE(12,)\\n\"}]}],\"topologyNodeIpAddress\":\"127.0.0.1\",\"executionNodeId\":1}]}\n");


        Graph<ExecutionNode, ExecutionLink> executionPlanGraph = GraphBuilder.buildExecutionPlanGraphFromJson(executionPlanJSonObject, topology);
        DOTExporter<ExecutionNode, ExecutionLink> exporter = new DOTExporter<>(ExecutionNode::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(executionPlanGraph, writer);

        String expectedExecutionPlanInDotFormat =
                "strict digraph G {\n" +
                        "  NodeId2_SINK13_CENTRALWINDOW11_WATERMARKASSIGNER8_SOURCE7default_logical;\n" +
                        "  NodeId1_SINK10_SOURCE12;\n" +
                        "  NodeId2_SINK13_CENTRALWINDOW11_WATERMARKASSIGNER8_SOURCE7default_logical -> NodeId1_SINK10_SOURCE12;\n" +
                        "}\n";

        Assert.assertEquals(expectedExecutionPlanInDotFormat, writer.toString());
    }
}
