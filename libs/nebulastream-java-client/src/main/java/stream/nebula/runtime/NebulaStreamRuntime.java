package stream.nebula.runtime;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.json.JSONObject;
import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.executioplan.ExecutionLink;
import stream.nebula.model.executioplan.ExecutionNode;
import stream.nebula.model.query.Query;
import stream.nebula.model.queryplan.LogicalQuery;
import stream.nebula.model.topology.TopologyEntry;
import stream.nebula.model.topology.TopologyLink;
import stream.nebula.utils.GraphBuilder;
import stream.nebula.utils.HttpDeleteWithBody;

import java.io.IOException;
import java.util.*;

public class NebulaStreamRuntime {

    private static NebulaStreamRuntime runtime = null;
    private NebulaStreamConfig config;

    private NebulaStreamRuntime() {

    }

    public static NebulaStreamRuntime getRuntime() {
        if (runtime == null) {
            runtime = new NebulaStreamRuntime();
        }
        return runtime;
    }

    public NebulaStreamConfig getConfig() {
        if (config == null) {
            config = new NebulaStreamConfig();
        }
        return config;
    }

    public boolean checkConnection() throws IOException {
        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/connectivity/check");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            return responseJson.getBoolean("success");
        } else {
            return false;
        }
    }

    public int executeQuery(String queryString, String strategyName) throws IOException, RESTExecption {
        Integer queryId = null;
        // Check if config is not null
        assert config != null;

        // Check if inputQuery is not empty
        assert queryString.length() > 0;

        // Send the query to NES REST Server using BottomUp strategy
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("strategyName", strategyName);
        requestBodyMap.put("userQuery", queryString);

        // System.out.println(new JSONObject(requestBodyMap).toString());

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/execute-query");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            String responseString = EntityUtils.toString(response.getEntity());
            JSONObject responseJson = new JSONObject(responseString);
            return responseJson.getInt("queryId");
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public Graph<TopologyEntry, TopologyLink> getNesTopology() throws IOException, RESTExecption {
        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/topology");

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));

            return GraphBuilder.buildTopologyGraphFromJson(responseJson);
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public Graph<LogicalQuery, DefaultEdge> getQueryPlan(Integer queryId) throws IOException, RESTExecption {

        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/query-plan?queryId=" + queryId);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            System.out.printf(responseJson.toString());

            return GraphBuilder.buildQueryPlanGraphFromJson(responseJson);
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public Graph<ExecutionNode, ExecutionLink> getExecutionPlan(Integer queryId) throws IOException, RESTExecption {


        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/execution-plan?queryId=" + queryId);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            Graph<TopologyEntry, TopologyLink> topology = NebulaStreamRuntime.getRuntime().getNesTopology();

            return GraphBuilder.buildExecutionPlanGraphFromJson(responseJson, topology);
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public ArrayList<Query> getAllRegisteredQueries() throws IOException, RESTExecption {
        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/queryCatalog/allRegisteredQueries");

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if(response.getStatusLine().getStatusCode()==200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));

            Iterator<String> keys = responseJson.keys();
            ArrayList<Query> queryArrayList = new ArrayList<>();

            while (keys.hasNext()) {
                String key = keys.next();
                queryArrayList.add(new Query(key, responseJson.getString(key)));
            }

            return queryArrayList;
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public ArrayList<Query> getRegisteredQueryWithStatus(String status) throws Exception {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("status", status);

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/queryCatalog/queries");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));

            Iterator<String> keys = responseJson.keys();
            ArrayList<Query> queryArrayList = new ArrayList<>();

            while (keys.hasNext()) {
                String key = keys.next();
                queryArrayList.add(new Query(key, responseJson.getString(key)));
            }
            return queryArrayList;

        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }


    }

    public boolean deleteQuery(int queryId) throws IOException, RESTExecption {
        HttpDelete request = new HttpDelete("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/stop-query?queryId=" + queryId);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public List<String> getAvailableLogicalStreams() throws UnknownDataTypeException, IOException, RESTExecption {
        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/streamCatalog/allLogicalStream");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonResponse = EntityUtils.toString(response.getEntity());

            List<String> availableLogicalStream = new ArrayList<>();
            JSONObject availableLogicalStreamJsonObject = new JSONObject(jsonResponse);
            Iterator<String> keys = availableLogicalStreamJsonObject.keys();

            while (keys.hasNext()) {
                String currentLogicalStreamName = keys.next();
                availableLogicalStream.add(currentLogicalStreamName);
            }
            return availableLogicalStream;
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public boolean addLogicalStream(String streamName, String schema) throws IOException, RESTExecption {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("streamName", streamName);
        requestBodyMap.put("schema", schema);

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/streamCatalog/addLogicalStream");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            return responseJson.getBoolean("Success");
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public boolean updateLogicalStream(String streamName, String schema) throws IOException, RESTExecption {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("streamName", streamName);
        requestBodyMap.put("schema", schema);

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/streamCatalog/updateLogicalStream");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            return responseJson.getBoolean("Success");
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public boolean deleteLogicalStream(String streamName) throws IOException, RESTExecption {

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("streamName", streamName);

        HttpDeleteWithBody request = new HttpDeleteWithBody("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/streamCatalog/deleteLogicalStream");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            return responseJson.getBoolean("Success");
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }


}
