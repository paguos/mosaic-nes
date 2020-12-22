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
        JSONObject topologyJSonObject = new JSONObject("{\"edges\":[{\"linkCapacity\":\"1\",\"linkLatency\":\"1\",\"source\":\"Node-6002131727931192513\",\"target\":\"Node-14333794020467441903\"}],\"nodes\":[{\"capacity\":\"2\",\"id\":\"Node-14333794020467441903\",\"nodeType\":\"Worker\",\"remainingCapacity\":\"2\",\"title\":\"Node-14333794020467441903\"},{\"capacity\":\"2\",\"id\":\"Node-6002131727931192513\",\"nodeType\":\"Sensor(default_physical)\",\"remainingCapacity\":\"2\",\"title\":\"Node-6002131727931192513\"}]}");

        Graph<TopologyEntry, TopologyLink> topology = GraphBuilder.buildTopologyGraphFromJson(topologyJSonObject);
        DOTExporter<TopologyEntry, TopologyLink> exporter = new DOTExporter<>(TopologyEntry::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(topology, writer);

        String expectedGraphInDotFormat =
                "strict digraph G {\n" +
                        "  Node_14333794020467441903;\n" +
                        "  Node_6002131727931192513;\n" +
                        "  Node_6002131727931192513 -> Node_14333794020467441903;\n" +
                        "}\n";
        Assert.assertEquals(expectedGraphInDotFormat, writer.toString());
    }

    @Test
    public void buildQueryPlanGraphReturnCorrectQueryPlanGraph() {
        JSONObject queryPlanJsonObject = new JSONObject("{\"edges\":[{\"source\":\"FILTER(OP-2)\",\"target\":\"SINK(OP-3)\"},{\"source\":\"SOURCE(OP-1)\",\"target\":\"FILTER(OP-2)\"}],\"nodes\":[{\"id\":\"SINK(OP-3)\",\"nodeType\":\"Source\",\"title\":\"SINK(OP-3)\"},{\"id\":\"FILTER(OP-2)\",\"nodeType\":\"Processor\",\"title\":\"FILTER(OP-2)\"},{\"id\":\"SOURCE(OP-1)\",\"nodeType\":\"Source\",\"title\":\"SOURCE(OP-1)\"}]}");

        Graph<LogicalQuery, DefaultEdge> queryPlan = GraphBuilder.buildQueryPlanGraphFromJson(queryPlanJsonObject);
        DOTExporter<LogicalQuery, DefaultEdge> exporter = new DOTExporter<>(LogicalQuery::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(queryPlan, writer);

        String expectedQueryPlanInDotFormat =
                "strict digraph G {\n" +
                        "  SINKOP_3;\n" +
                        "  FILTEROP_2;\n" +
                        "  SOURCEOP_1;\n" +
                        "  FILTEROP_2 -> SINKOP_3;\n" +
                        "  SOURCEOP_1 -> FILTEROP_2;\n" +
                        "}\n";
        Assert.assertEquals(expectedQueryPlanInDotFormat, writer.toString());
    }

    @Test
    public void buildExecutionPlanGraphReturnCorrectExecutionPlanGraph() {
        JSONObject executionPlanJsonObject = new JSONObject("{\"executionGraph\":{\"edges\":[{\"linkCapacity\":\"3\",\"linkLatency\":\"1\",\"source\":\"Node-1\",\"target\":\"Node-0\"},{\"linkCapacity\":\"3\",\"linkLatency\":\"1\",\"source\":\"Node-2\",\"target\":\"Node-0\"},{\"linkCapacity\":\"3\",\"linkLatency\":\"1\",\"source\":\"Node-3\",\"target\":\"Node-0\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-4\",\"target\":\"Node-1\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"1\",\"source\":\"Node-5\",\"target\":\"Node-2\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-6\",\"target\":\"Node-3\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-10\",\"target\":\"Node-6\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"1\",\"source\":\"Node-7\",\"target\":\"Node-4\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-8\",\"target\":\"Node-4\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-8\",\"target\":\"Node-5\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-9\",\"target\":\"Node-6\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"1\",\"source\":\"Node-10\",\"target\":\"Node-5\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"1\",\"source\":\"Node-11\",\"target\":\"Node-7\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-12\",\"target\":\"Node-8\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-13\",\"target\":\"Node-12\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"1\",\"source\":\"Node-13\",\"target\":\"Node-11\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-14\",\"target\":\"Node-12\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"1\",\"source\":\"Node-15\",\"target\":\"Node-10\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-16\",\"target\":\"Node-15\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-17\",\"target\":\"Node-15\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-18\",\"target\":\"Node-9\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-12\",\"target\":\"Node-7\"},{\"linkCapacity\":\"2\",\"linkLatency\":\"2\",\"source\":\"Node-15\",\"target\":\"Node-18\"},{\"linkCapacity\":\"1\",\"linkLatency\":\"3\",\"source\":\"Node-19\",\"target\":\"Node-13\"},{\"linkCapacity\":\"1\",\"linkLatency\":\"3\",\"source\":\"Node-20\",\"target\":\"Node-14\"},{\"linkCapacity\":\"1\",\"linkLatency\":\"3\",\"source\":\"Node-21\",\"target\":\"Node-16\"},{\"linkCapacity\":\"1\",\"linkLatency\":\"3\",\"source\":\"Node-22\",\"target\":\"Node-17\"}],\"nodes\":[{\"capacity\":\"3\",\"id\":\"Node-19\",\"nodeType\":\"Sensor(temperature1)\",\"operators\":\"SOURCE(OP-1)=>FILTER(OP-2)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"3\",\"id\":\"Node-0\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(OP-3)\",\"remainingCapacity\":\"2\"},{\"capacity\":\"1\",\"id\":\"Node-21\",\"nodeType\":\"Sensor(temperature2)\",\"operators\":\"SOURCE(OP-1)=>SINK(SYS)\",\"remainingCapacity\":\"0\"},{\"capacity\":\"2\",\"id\":\"Node-16\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>FILTER(OP-2)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-13\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-12\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-8\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-4\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-1\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"1\",\"id\":\"Node-15\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"0\"},{\"capacity\":\"2\",\"id\":\"Node-10\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-6\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-3\",\"nodeType\":\"Worker\",\"operators\":\"SOURCE(SYS)=>SINK(SYS)\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-2\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"2\"},{\"capacity\":\"2\",\"id\":\"Node-5\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"2\"},{\"capacity\":\"3\",\"id\":\"Node-7\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"3\"},{\"capacity\":\"1\",\"id\":\"Node-9\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"1\"},{\"capacity\":\"3\",\"id\":\"Node-11\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"3\"},{\"capacity\":\"2\",\"id\":\"Node-14\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"2\"},{\"capacity\":\"2\",\"id\":\"Node-17\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"2\"},{\"capacity\":\"2\",\"id\":\"Node-18\",\"nodeType\":\"Worker\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"2\"},{\"capacity\":\"1\",\"id\":\"Node-20\",\"nodeType\":\"Sensor(humidity1)\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"1\"},{\"capacity\":\"2\",\"id\":\"Node-22\",\"nodeType\":\"Sensor(humidity2)\",\"operators\":\"NO-OPERATOR\",\"remainingCapacity\":\"2\"}]},\"planComputeTime\":\"12176\",\"queryId\":\"66052c23-f78d-4317-beef-c2d91985d14b\"}");

        Graph<ExecutionNode, ExecutionLink> executionPlanGraph = GraphBuilder.buildExecutionPlanGraphFromJson(executionPlanJsonObject);
        DOTExporter<ExecutionNode, ExecutionLink> exporter = new DOTExporter<>(ExecutionNode::toString);
        Writer writer = new StringWriter();
        exporter.exportGraph(executionPlanGraph, writer);

        String expectedExecutionPlanInDotFormat =
                "strict digraph G {\n" +
                        "  Node_19_SOURCEOP1_FILTEROP2_SINKSYS;\n" +
                        "  Node_0_SOURCESYS_SINKOP3;\n" +
                        "  Node_21_SOURCEOP1_SINKSYS;\n" +
                        "  Node_16_SOURCESYS_FILTEROP2_SINKSYS;\n" +
                        "  Node_13_SOURCESYS_SINKSYS;\n" +
                        "  Node_12_SOURCESYS_SINKSYS;\n" +
                        "  Node_8_SOURCESYS_SINKSYS;\n" +
                        "  Node_4_SOURCESYS_SINKSYS;\n" +
                        "  Node_1_SOURCESYS_SINKSYS;\n" +
                        "  Node_15_SOURCESYS_SINKSYS;\n" +
                        "  Node_10_SOURCESYS_SINKSYS;\n" +
                        "  Node_6_SOURCESYS_SINKSYS;\n" +
                        "  Node_3_SOURCESYS_SINKSYS;\n" +
                        "  Node_2_NOOPERATOR;\n" +
                        "  Node_5_NOOPERATOR;\n" +
                        "  Node_7_NOOPERATOR;\n" +
                        "  Node_9_NOOPERATOR;\n" +
                        "  Node_11_NOOPERATOR;\n" +
                        "  Node_14_NOOPERATOR;\n" +
                        "  Node_17_NOOPERATOR;\n" +
                        "  Node_18_NOOPERATOR;\n" +
                        "  Node_20_NOOPERATOR;\n" +
                        "  Node_22_NOOPERATOR;\n" +
                        "  Node_1_SOURCESYS_SINKSYS -> Node_0_SOURCESYS_SINKOP3;\n" +
                        "  Node_2_NOOPERATOR -> Node_0_SOURCESYS_SINKOP3;\n" +
                        "  Node_3_SOURCESYS_SINKSYS -> Node_0_SOURCESYS_SINKOP3;\n" +
                        "  Node_4_SOURCESYS_SINKSYS -> Node_1_SOURCESYS_SINKSYS;\n" +
                        "  Node_5_NOOPERATOR -> Node_2_NOOPERATOR;\n" +
                        "  Node_6_SOURCESYS_SINKSYS -> Node_3_SOURCESYS_SINKSYS;\n" +
                        "  Node_10_SOURCESYS_SINKSYS -> Node_6_SOURCESYS_SINKSYS;\n" +
                        "  Node_7_NOOPERATOR -> Node_4_SOURCESYS_SINKSYS;\n" +
                        "  Node_8_SOURCESYS_SINKSYS -> Node_4_SOURCESYS_SINKSYS;\n" +
                        "  Node_8_SOURCESYS_SINKSYS -> Node_5_NOOPERATOR;\n" +
                        "  Node_9_NOOPERATOR -> Node_6_SOURCESYS_SINKSYS;\n" +
                        "  Node_10_SOURCESYS_SINKSYS -> Node_5_NOOPERATOR;\n" +
                        "  Node_11_NOOPERATOR -> Node_7_NOOPERATOR;\n" +
                        "  Node_12_SOURCESYS_SINKSYS -> Node_8_SOURCESYS_SINKSYS;\n" +
                        "  Node_13_SOURCESYS_SINKSYS -> Node_12_SOURCESYS_SINKSYS;\n" +
                        "  Node_13_SOURCESYS_SINKSYS -> Node_11_NOOPERATOR;\n" +
                        "  Node_14_NOOPERATOR -> Node_12_SOURCESYS_SINKSYS;\n" +
                        "  Node_15_SOURCESYS_SINKSYS -> Node_10_SOURCESYS_SINKSYS;\n" +
                        "  Node_16_SOURCESYS_FILTEROP2_SINKSYS -> Node_15_SOURCESYS_SINKSYS;\n" +
                        "  Node_17_NOOPERATOR -> Node_15_SOURCESYS_SINKSYS;\n" +
                        "  Node_18_NOOPERATOR -> Node_9_NOOPERATOR;\n" +
                        "  Node_12_SOURCESYS_SINKSYS -> Node_7_NOOPERATOR;\n" +
                        "  Node_15_SOURCESYS_SINKSYS -> Node_18_NOOPERATOR;\n" +
                        "  Node_19_SOURCEOP1_FILTEROP2_SINKSYS -> Node_13_SOURCESYS_SINKSYS;\n" +
                        "  Node_20_NOOPERATOR -> Node_14_NOOPERATOR;\n" +
                        "  Node_21_SOURCEOP1_SINKSYS -> Node_16_SOURCESYS_FILTEROP2_SINKSYS;\n" +
                        "  Node_22_NOOPERATOR -> Node_17_NOOPERATOR;\n" +
                        "}\n";

        Assert.assertEquals(expectedExecutionPlanInDotFormat, writer.toString());
    }
}
