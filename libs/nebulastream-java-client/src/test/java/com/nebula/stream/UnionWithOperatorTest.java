package com.nebula.stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.queryinterface.Query;

import java.util.ArrayList;

public class UnionWithOperatorTest {
    private static LogicalStream car;
    private static LogicalStream truck;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        fieldArrayList.add(new Field("id","UINT32"));
        fieldArrayList.add(new Field("value","UINT32"));

        car = new LogicalStream("car", fieldArrayList);
        truck = new LogicalStream("truck", fieldArrayList);

    }

    @Test
    public void unionWithGenerateCorrectCppCode() {
        // create the q1 Query
        Query q1 = new Query();
        q1.from(car);

        // create the q2 Query
        Query q2 = new Query();
        q2.from(truck);

        // union q1 and q2
        q1.unionWith(q2);
        
        // assert that union operator generates the correct NES query
        Assert.assertEquals("Query::from(\"car\")" +
                        ".unionWith(Query::from(\"truck\"));"
                , q1.generateCppCode());
    }
}
