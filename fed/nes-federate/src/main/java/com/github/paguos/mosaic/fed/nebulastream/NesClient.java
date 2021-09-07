package com.github.paguos.mosaic.fed.nebulastream;

import com.github.paguos.mosaic.fed.nebulastream.stream.LogicalStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.mosaic.lib.geo.GeoPoint;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import org.json.JSONObject;
import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.queryinterface.Query;
import stream.nebula.runtime.NebulaStreamRuntime;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NesClient {

    private final NebulaStreamRuntime nebulaStreamRuntime;

    public NesClient(String host, String port) {
        this.nebulaStreamRuntime = NebulaStreamRuntime.getRuntime();
        this.nebulaStreamRuntime.getConfig().setHost(host).setPort(port);
    }

    public void addLogicalStream(LogicalStream logicalStream) throws InternalFederateException {
        try {
            nebulaStreamRuntime.addLogicalStream(logicalStream.getSchema().getName(), logicalStream.getSchema().toCpp());
        } catch (IOException | RESTExecption e) {
            throw new InternalFederateException(e.getMessage());
        }
    }

    public boolean deleteQuery(int queryId) throws InternalFederateException {
        try {
            return nebulaStreamRuntime.deleteQuery(queryId);
        } catch (IOException | RESTExecption e) {
            throw new InternalFederateException(e.getMessage());
        }
    }

    public int executeQuery(Query query) throws InternalFederateException {
        try {
            return nebulaStreamRuntime.executeQuery(query.generateCppCode(), "BottomUp");
        } catch (IOException | RESTExecption e) {
            throw new InternalFederateException(e.getMessage());
        }
    }

    public List<String> getAvailableLogicalStreams() throws InternalFederateException {
        try {
            return nebulaStreamRuntime.getAvailableLogicalStreams();
        } catch (UnknownDataTypeException | IOException | RESTExecption e) {
            throw new InternalFederateException(e.getMessage());
        }
    }

    public int getTopologyNodeCount() throws InternalFederateException {
        try {
            return nebulaStreamRuntime.getNesTopology().vertexSet().size();
        } catch (IOException | RESTExecption e) {
            throw new InternalFederateException(e.getMessage());
        }
    }

    public void updateLocation(String nodeId, GeoPoint location) throws InternalFederateException {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("nodeId", nodeId);
        requestBodyMap.put("latitude", location.getLatitude());
        requestBodyMap.put("longitude", location.getLongitude());

        try {
            HttpPost request = new HttpPost("http://" + nebulaStreamRuntime.getConfig().getHost() + ":" + nebulaStreamRuntime.getConfig().getPort() + "/v1/nes/geo/location");
            request.setEntity(new StringEntity(new JSONObject(requestBodyMap).toString()));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(request);
        } catch (IOException e) {
            throw new InternalFederateException(e.getMessage());
        }

    }

}
