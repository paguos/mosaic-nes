package com.nebula.stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.queryinterface.Query;

import java.util.ArrayList;


public class RESTClientTest {
    private static Query query;
    private static LogicalStream defaultLogical;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        fieldArrayList.add(new Field("id","UINT32"));
        fieldArrayList.add(new Field("value","UINT32"));
        defaultLogical = new LogicalStream("default_logical", fieldArrayList);
        query = new Query();
        query.from(defaultLogical)
                .print();
    }

    @Test
    public void generateCppCodeReturnCorrectCppCodeRepresentation() {
        String expectedCppRepresentation = "Query::from(\""+defaultLogical.getName()+"\")"
                + ".sink(PrintSinkDescriptor::create());";

        Assert.assertEquals(expectedCppRepresentation, query.generateCppCode());
    }
}
