package com.github.paguos.mosaic.fed.nebulastream;

import com.github.paguos.mosaic.fed.nebulastream.stream.LogicalStream;
import org.eclipse.mosaic.rti.api.InternalFederateException;
import stream.nebula.exceptions.RESTExecption;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.queryinterface.Query;
import stream.nebula.runtime.NebulaStreamRuntime;

import java.io.IOException;
import java.util.List;

public class NesClient {

    private final NebulaStreamRuntime nebulaStreamRuntime;

    public NesClient(String host, String port) {
        this.nebulaStreamRuntime = NebulaStreamRuntime.getRuntime();
        this.nebulaStreamRuntime.getConfig().setHost(host).setPort(port);
    }

    public void addLogicalStream(LogicalStream logicalStream) throws InternalFederateException {
        try {
            nebulaStreamRuntime.addLogicalStream(logicalStream.getName(), logicalStream.getSchema().toCpp());
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

}
