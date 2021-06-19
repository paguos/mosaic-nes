package com.github.paguos.mosaic.fed.examples;

import com.github.paguos.mosaic.fed.catalog.SchemaCatalog;
import com.github.paguos.mosaic.fed.nebulastream.NesClient;
import com.github.paguos.mosaic.fed.nebulastream.stream.LogicalStream;
import com.github.paguos.mosaic.fed.nebulastream.stream.Schema;
import org.eclipse.mosaic.rti.api.InternalFederateException;

public class AddLogicalStream {

    public static void main(String[] args) throws InternalFederateException {
        NesClient client = new NesClient("localhost", "8081");
        Schema schema = SchemaCatalog.getMosaicNesSchema();
        client.addLogicalStream(new LogicalStream(schema));
    }

}
