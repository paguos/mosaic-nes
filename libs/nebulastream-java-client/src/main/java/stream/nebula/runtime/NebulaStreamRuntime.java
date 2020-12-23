package stream.nebula.runtime;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import stream.nebula.model.executioplan.ExecutionLink;
import stream.nebula.model.executioplan.ExecutionNode;
import stream.nebula.model.physicalstream.PhysicalStream;
import stream.nebula.model.query.Query;
import stream.nebula.model.queryplan.LogicalQuery;
import stream.nebula.model.topology.TopologyEntry;
import stream.nebula.model.topology.TopologyLink;
import stream.nebula.utils.GraphBuilder;
import stream.nebula.utils.HttpDeleteWithBody;
import stream.nebula.utils.HttpGetWithBody;
import stream.nebula.utils.NESDataTypeUtil;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String executeQuery(String queryString, String strategyName) throws IOException, RESTExecption {
        // Check if config is not null
        assert config != null;

        // Check if inputQuery is not empty
        assert queryString.length() > 0;

        // Send the query to NES REST Server using BottomUp strategy
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("strategyName", strategyName);
        requestBodyMap.put("userQuery", queryString);

        System.out.println(new JSONObject(requestBodyMap).toString());

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/execute-query");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity());
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public Graph<TopologyEntry, TopologyLink> getNesTopology() throws IOException, RESTExecption {
        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/nes-topology");

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            return GraphBuilder.buildTopologyGraphFromJson(responseJson);
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public Graph<LogicalQuery, DefaultEdge> getQueryPlan(String inputQuery) throws IOException, RESTExecption {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("userQuery", inputQuery);

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/query-plan");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            Graph<LogicalQuery, DefaultEdge> queryPlan = new DefaultDirectedGraph<>(DefaultEdge.class);
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));

            return GraphBuilder.buildQueryPlanGraphFromJson(responseJson);
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public Graph<ExecutionNode, ExecutionLink> getExecutionPlan(String inputQuery, String strategyName) throws IOException, RESTExecption {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("strategyName", strategyName);
        requestBodyMap.put("userQuery", inputQuery);

        HttpPost request = new HttpPost("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/execution-plan");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));

            return GraphBuilder.buildExecutionPlanGraphFromJson(responseJson);
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

        Map<String, Integer> requestBodyMap = new HashMap<>();
        requestBodyMap.put("queryId", queryId);

        HttpDeleteWithBody request = new HttpDeleteWithBody("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/query/stop-query");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }
    }

    public LogicalStream getLogicalStream(String name) throws Exception {
        List<LogicalStream> availableLogicalStream = getAvailableLogicalStreams();

        for (LogicalStream logicalStream: availableLogicalStream) {
            if (logicalStream.getName().equals(name)) {
                return logicalStream;
            }
        }
        throw new Exception("Cannot find logical stream '"+name+"' in the available logical stream");
    }

    public List<LogicalStream> getAvailableLogicalStreams() throws UnknownDataTypeException, IOException, RESTExecption {
        HttpGet request = new HttpGet("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/streamCatalog/allLogicalStream");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            String jsonResponse = EntityUtils.toString(response.getEntity());

            List<LogicalStream> availableLogicalStream = new ArrayList<>();
            JSONObject availableLogicalStreamJsonObject = new JSONObject(jsonResponse);
            Iterator<String> keys = availableLogicalStreamJsonObject.keys();

            while (keys.hasNext()) {
                String currentKey = keys.next();
                String currentValue = availableLogicalStreamJsonObject.getString(currentKey);
                currentValue = currentValue.substring(0, currentValue.length() - 1);
                List<String> fieldStringList = Arrays.asList(currentValue.split(NESDataTypeUtil.nesDataTypes));
                //remove whitespace in field's name
                fieldStringList.replaceAll(String::trim);

                StringBuilder fieldAsDelimiter = new StringBuilder();
                for (String field : fieldStringList) {
                    fieldAsDelimiter.append(field).append("|");
                }
                fieldAsDelimiter = new StringBuilder(fieldAsDelimiter.substring(0, fieldAsDelimiter.length() - 1));
                // Remove colon in after the field's name
                fieldStringList.replaceAll(f -> f.substring(0, f.length() - 1).replace(" ",""));

                List<String> fieldTypeStringList = Arrays.asList(currentValue.split(fieldAsDelimiter.toString()));
                fieldTypeStringList = fieldTypeStringList.subList(1, fieldTypeStringList.size());
                //remove whitespace in data type
                fieldTypeStringList.replaceAll(String::trim);

                //field name list
                Iterator<String> fieldStringListIterator = fieldStringList.iterator();
                //field data type list
                Iterator<String> fieldTypeStringListIterator = fieldTypeStringList.iterator();

                List<Field> fieldList = new ArrayList<>();
                while (fieldStringListIterator.hasNext() && fieldTypeStringListIterator.hasNext()) {
                    fieldList.add(new Field(fieldStringListIterator.next(), fieldTypeStringListIterator.next()));
                }
                availableLogicalStream.add(new LogicalStream(currentKey, fieldList));
            }
            return availableLogicalStream;
        } else {
            throw new RESTExecption(response.getStatusLine().getStatusCode());
        }

    }

    public ArrayList<PhysicalStream> getAllPhysicalStream(String logicalStreamName) throws Exception {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("streamName", logicalStreamName);

        HttpGetWithBody request = new HttpGetWithBody("http://" + config.getHost() + ":" + config.getPort() + "/v1/nes/streamCatalog/allPhysicalStream");
        request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            Pattern MY_PATTERN = Pattern.compile("\\b\\w*[=]\\w*\\b");
            String responseString = EntityUtils.toString(response.getEntity());
            if(responseString.equalsIgnoreCase("\"No Physical Stream Found.\"")){
                throw new RESTExecption(204);
            } else {
                JSONObject responseJson = new JSONObject(responseString);
                ArrayList<PhysicalStream> physicalStreamArrayList = new ArrayList<>();
                for (Object physicalStreamString : responseJson.getJSONArray("Physical Streams")) {
                    Matcher m = MY_PATTERN.matcher((CharSequence) physicalStreamString);
                    StringBuilder row = new StringBuilder();

                    ArrayList<String> rowContent = new ArrayList<>();
                    while (m.find()) {
                        rowContent.add(m.group().split("=")[1]);
                    }
                    // Assuming the order of fields in the physicl_stream_string does not change
                    PhysicalStream physicalStream = new PhysicalStream(
                            rowContent.get(0),
                            rowContent.get(1),
                            rowContent.get(2),
                            rowContent.get(3),
                            Integer.parseInt(rowContent.get(4)),
                            Integer.parseInt(rowContent.get(5)),
                            rowContent.get(5)
                    );
                    physicalStreamArrayList.add(physicalStream);
                }
                return physicalStreamArrayList;
            }


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
