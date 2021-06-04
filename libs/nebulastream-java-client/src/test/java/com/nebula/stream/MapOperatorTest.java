package com.nebula.stream;

import org.junit.Assert;
import org.junit.Test;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.Map;
import stream.nebula.operators.Operation;
import stream.nebula.queryinterface.Query;

public class MapOperatorTest {
    @Test
    public void mapOnFieldName() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical").map(Map.onField("value1").assign(Operation.add("value1","value2")));

        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"value1\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());
    }

    @Test
    public void mapOperationWithConstantAssignment() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical").map(Map.onField("value1").assign(Operation.constant(10)));

        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"value1\") = 10);"
                , query.generateCppCode());
    }

    @Test (expected = EmptyFieldException.class)
    public void mapWithEmptyFieldThrowEmptyFieldException() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical").map(Map.onField("").assign(Operation.add("value1","value2")));

        query.generateCppCode();
    }

    @Test
    public void mapOperationOnNewField() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical").map(Map.onField("newfield").assign(Operation.add("value1","value2")));

        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"newfield\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());
    }

    @Test
    public void mapOperationOperators() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical").map(Map.onField("newfield").assign(Operation.add("value1","value2")));
        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"newfield\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from("defaultLogical").map(Map.onField("newfield").assign(Operation.substract("value1","value2")));
        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"newfield\") = Attribute(\"value1\") - Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from("defaultLogical").map(Map.onField("newfield").assign(Operation.multiply("value1","value2")));
        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"newfield\") = Attribute(\"value1\") * Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from("defaultLogical").map(Map.onField("newfield").assign(Operation.divide("value1","value2")));
        Assert.assertEquals("Query::from(\"defaultLogical\").map(Attribute(\"newfield\") = Attribute(\"value1\") / Attribute(\"value2\"));"
                , query.generateCppCode());
    }
}
