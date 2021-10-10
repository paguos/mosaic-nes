package com.github.paguos.mosaic.fed.nebulastream.query;

import org.eclipse.mosaic.lib.geo.GeoPoint;
import org.eclipse.mosaic.lib.transform.GeoProjection;
import org.eclipse.mosaic.lib.transform.Wgs84Projection;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.queryinterface.Query;

import static org.junit.Assert.assertEquals;

public class RangeQueryTest {

    @BeforeClass
    public static void setup() {
        GeoPoint origin = GeoPoint.latLon(52.5047675, 13.3156317);
        if (!GeoProjection.isInitialized()) {
            GeoProjection.initialize(new Wgs84Projection(origin));
        }
    }

    @Test
    public void validRangeQuery() throws EmptyFieldException {
        GeoPoint position = GeoPoint.latLon(52.5128417, 13.3213595);
        Query query = new RangeQuery(position, 1000).from("test-stream");

        String expectedQuery = "Query::from(\"test-stream\").filter(" + "Attribute(\"latitude\")<=52.51298373611774" +
                " && Attribute(\"latitude\")>=52.512699663882266" +
                " && Attribute(\"longitude\")<=13.321592888014166" +
                " && Attribute(\"longitude\")>=13.321126111985834" +
                ");";
        assertEquals(expectedQuery, query.generateCppCode());
    }

}
