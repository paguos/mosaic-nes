package com.nebula.stream;

import org.junit.Assert;
import org.junit.Test;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.queryinterface.Query;

public class UnionWithOperatorTest {
    @Test
    public void unionWithGenerateCorrectCppCode() throws EmptyFieldException {
        // create the q1 Query
        Query q1 = new Query();
        q1.from("car");

        // create the q2 Query
        Query q2 = new Query();
        q2.from("truck");

        // union q1 and q2
        q1.unionWith(q2);
        
        // assert that union operator generates the correct NES query
        Assert.assertEquals("Query::from(\"car\")" +
                        ".unionWith(Query::from(\"truck\"));"
                , q1.generateCppCode());
    }
}
