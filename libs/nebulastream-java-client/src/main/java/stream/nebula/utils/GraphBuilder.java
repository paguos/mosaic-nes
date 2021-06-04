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
            TopologyEntry currentEntry = new TopologyEntry(currentNode.getInt("id"),
                    currentNode.getString("ip_address"), currentNode.getFloat("available_resources"));
            topology.addVertex(currentEntry);
        }

        JSONArray edges = getTopologyResponseJsonObject.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject currentEdges = edges.getJSONObject(i);
            Integer source = currentEdges.getInt("source");
            Integer target = currentEdges.getInt("target");
            TopologyLink currentLink = new TopologyLink(source, target);
            // Find the source and target from the graph by id
            TopologyEntry sourceNode = topology.vertexSet().stream()
                    .filter(topologyEntry -> topologyEntry.getId().equals(source))
                    .findFirst().orElse(null);
            TopologyEntry targetNode = topology.vertexSet().stream()
                    .filter(topologyEntry -> topologyEntry.getId().equals(target))
                    .findFirst().orElse(null);
            topology.addEdge(sourceNode, targetNode, currentLink);
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
                    currentNode.getString("name"), currentNode.getString("nodeType"));
            queryPlanGraph.addVertex(currentLogicalQuery);
        }

        // Parsing the edges and add to the graph
        JSONArray edges = getQueryplanJsonObject.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject currentEdges = edges.getJSONObject(i);

            LogicalQuery source = queryPlanGraph.vertexSet().stream()
                    .filter(logicalQuery -> logicalQuery.getName().equals(currentEdges.getString("source")))
                    .findFirst().orElse(null);
            LogicalQuery target = queryPlanGraph.vertexSet().stream()
                    .filter(logicalQuery -> logicalQuery.getName().equals(currentEdges.getString("target")))
                    .findFirst().orElse(null);
            queryPlanGraph.addEdge(source, target);
        }
        return queryPlanGraph;
    }


    public static Graph<ExecutionNode, ExecutionLink> buildExecutionPlanGraphFromJson(JSONObject executionPlanJsonObject, Graph<TopologyEntry, TopologyLink> topology) {

        Graph<ExecutionNode, ExecutionLink> executionPlanGraph = new DirectedAcyclicGraph<>(ExecutionLink.class);
        // Parsing the nodes and add to the graph
        JSONArray nodes = executionPlanJsonObject.getJSONArray("executionNodes");
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject currentNode = nodes.getJSONObject(i);
            JSONObject currentQuery = currentNode.getJSONArray("ScheduledQueries").getJSONObject(0);

            String operators = "";

            JSONArray queryOperators = currentQuery.getJSONArray("querySubPlans");
            for (int j = 0; j < queryOperators.length(); j++) {
                String responseOperator = queryOperators.getJSONObject(j).getString("operator");

                String[] operator = responseOperator.split("\\n");
                for (int k = 0; k < operator.length; k++) {
                    if (k == 0) {
                        operators = operators.concat(operator[k].replaceAll("\\s+",""));
                    } else {
                        operators = operators.concat("_" + operator[k].replaceAll("\\s+",""));
                    }
                }
            }

            ExecutionNode currentExecutionNode = new ExecutionNode(currentNode.getInt("executionNodeId"),
                    currentNode.getInt("topologyNodeId"),
                    currentNode.getString("topologyNodeIpAddress"),
                    operators);
            executionPlanGraph.addVertex(currentExecutionNode);
        }

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject currentNodeI = nodes.getJSONObject(i);
            ExecutionNode source = executionPlanGraph.vertexSet().stream()
                    .filter(executionNode -> executionNode.getId().equals(currentNodeI.getInt("executionNodeId")))
                    .findFirst().orElse(null);
            for (int j = 0; j < nodes.length(); j++) {
                JSONObject currentNodeJ = nodes.getJSONObject(j);

                ExecutionNode target = executionPlanGraph.vertexSet().stream()
                        .filter(executionNode -> executionNode.getId().equals(currentNodeJ.getInt("executionNodeId")))
                        .findFirst().orElse(null);

                // Find the source and target from the graph by id
                TopologyEntry sourceNode = topology.vertexSet().stream()
                        .filter(topologyEntry -> topologyEntry.getId().equals(currentNodeI.getInt("topologyNodeId")))
                        .findFirst().orElse(null);
                TopologyEntry targetNode = topology.vertexSet().stream()
                        .filter(topologyEntry -> topologyEntry.getId().equals(currentNodeJ.getInt("topologyNodeId")))
                        .findFirst().orElse(null);

                if(topology.containsEdge(sourceNode, targetNode)) {
                    ExecutionLink currentLink = new ExecutionLink(source.getId(), target.getId());
                    executionPlanGraph.addEdge(source, target,currentLink);
                }
            }
        }
        return executionPlanGraph;
    }

    public static String cleanNodeId(String id) {
        return id.replace("-", "_").replace("(", "").replace(")", "");
    }

}
