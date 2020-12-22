package stream.nebula.utils;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.json.JSONArray;
import org.json.JSONObject;
import stream.nebula.model.executioplan.ExecutionLink;
import stream.nebula.model.executioplan.ExecutionNode;
import stream.nebula.model.queryplan.LogicalQuery;
import stream.nebula.model.topology.TopologyEntry;
import stream.nebula.model.topology.TopologyLink;

public class GraphBuilder {
    public static Graph<TopologyEntry, TopologyLink> buildTopologyGraphFromJson(JSONObject getTopologyResponseJsonObject) {
        // Instantiating graph object
        Graph<TopologyEntry, TopologyLink> topology = new DirectedAcyclicGraph<>(TopologyLink.class);

        // Parsing nodes and add to the graph
        JSONArray nodes = getTopologyResponseJsonObject.getJSONArray("nodes");
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject currentNode = nodes.getJSONObject(i);
            TopologyEntry currentEntry = new TopologyEntry(currentNode.getString("id"),
                    currentNode.getString("title"), currentNode.getString("nodeType"),
                    currentNode.getFloat("capacity"), currentNode.getFloat("remainingCapacity"));
            topology.addVertex(currentEntry);
        }

        JSONArray edges = getTopologyResponseJsonObject.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject currentEdges = edges.getJSONObject(i);
            TopologyLink currentLink = new TopologyLink(currentEdges.getFloat("linkCapacity"),
                    currentEdges.getFloat("linkLatency"));
            // Find the source and target from the graph by id
            TopologyEntry source = topology.vertexSet().stream()
                    .filter(topologyEntry -> topologyEntry.getId().equals(currentEdges.getString("source")))
                    .findFirst().orElse(null);
            TopologyEntry target = topology.vertexSet().stream()
                    .filter(topologyEntry -> topologyEntry.getId().equals(currentEdges.getString("target")))
                    .findFirst().orElse(null);
            topology.addEdge(source, target, currentLink);
        }
        return topology;
    }

    public static Graph<LogicalQuery, DefaultEdge> buildQueryPlanGraphFromJson(JSONObject getQueryplanJsonObject) {
        Graph<LogicalQuery, DefaultEdge> queryPlanGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        // Parsing the nodes and add to the graph
        JSONArray nodes = getQueryplanJsonObject.getJSONArray("nodes");
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject currentNode = nodes.getJSONObject(i);
            LogicalQuery currentLogicalQuery = new LogicalQuery(currentNode.getString("id"),
                    currentNode.getString("title"), currentNode.getString("nodeType"));
            queryPlanGraph.addVertex(currentLogicalQuery);
        }

        // Parsing the edges and add to the graph
        JSONArray edges = getQueryplanJsonObject.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject currentEdges = edges.getJSONObject(i);

            LogicalQuery source = queryPlanGraph.vertexSet().stream()
                    .filter(logicalQuery -> logicalQuery.getId().equals(currentEdges.getString("source")))
                    .findFirst().orElse(null);
            LogicalQuery target = queryPlanGraph.vertexSet().stream()
                    .filter(logicalQuery -> logicalQuery.getId().equals(currentEdges.getString("target")))
                    .findFirst().orElse(null);
            queryPlanGraph.addEdge(source, target);
        }
        return queryPlanGraph;
    }


    public static Graph<ExecutionNode, ExecutionLink> buildExecutionPlanGraphFromJson(JSONObject executionPlanJsonObject) {

        Graph<ExecutionNode, ExecutionLink> executionPlanGraph = new DirectedAcyclicGraph<>(ExecutionLink.class);
        // Parsing the nodes and add to the graph
        JSONArray nodes = executionPlanJsonObject.getJSONObject("executionGraph").getJSONArray("nodes");
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject currentNode = nodes.getJSONObject(i);
            ExecutionNode currentExecutionNode = new ExecutionNode(currentNode.getString("id"),
                    currentNode.getString("nodeType"), currentNode.getString("operators"),
                    currentNode.getFloat("remainingCapacity"));
            executionPlanGraph.addVertex(currentExecutionNode);
        }
        JSONArray edges = executionPlanJsonObject.getJSONObject("executionGraph").getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject currentEdges = edges.getJSONObject(i);

            ExecutionNode source = executionPlanGraph.vertexSet().stream()
                    .filter(executionNode -> executionNode.getId().equals(currentEdges.getString("source")))
                    .findFirst().orElse(null);
            ExecutionNode target = executionPlanGraph.vertexSet().stream()
                    .filter(executionNode -> executionNode.getId().equals(currentEdges.getString("target")))
                    .findFirst().orElse(null);
            ExecutionLink currentLink = new ExecutionLink(currentEdges.getFloat("linkCapacity"),
                    currentEdges.getFloat("linkLatency"));
            executionPlanGraph.addEdge(source, target,currentLink);
        }
        return executionPlanGraph;
    }

    public static String cleanNodeId(String id) {
        return id.replace("-", "_").replace("(", "").replace(")", "");
    }

}
