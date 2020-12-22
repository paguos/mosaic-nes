package com.nebula.stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.FieldIndexOutOfBoundException;
import stream.nebula.exceptions.FieldNotFoundException;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.operators.Map;
import stream.nebula.operators.Operation;
import stream.nebula.queryinterface.Query;

import java.util.ArrayList;

public class MapOperatorTest {
    private static LogicalStream defaultLogical;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        fieldArrayList.add(new Field("id","UINT32"));
        fieldArrayList.add(new Field("value1","UINT32"));
        fieldArrayList.add(new Field("value2","UINT32"));
        fieldArrayList.add(new Field("stringField","CHAR[30]"));
        defaultLogical = new LogicalStream("default_logical", fieldArrayList);
    }

    @Test
    public void mapOnFieldIndex() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField(1).assign(Operation.add(1,2)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"value1\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());
    }

    @Test
    public void mapOnFieldName() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField("value1").assign(Operation.add(1,2)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"value1\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());
    }

    @Test
    public void mapOperationWithFieldName() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField("value1").assign(Operation.add("value1","value2")));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"value1\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());
    }

    @Test
    public void mapOperationWithConstantAssignment() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField("value1").assign(Operation.constant(10)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"value1\") = 10);"
                , query.generateCppCode());
    }

    @Test (expected = FieldIndexOutOfBoundException.class)
    public void mapOnOutOfBoundFieldIndexThrowException() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField(99).assign(Operation.add("value1","value2")));

        query.generateCppCode();
    }

    @Test (expected = FieldIndexOutOfBoundException.class)
    public void mapOperationOnOutOfBoundLeftFieldIndexThrowException() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField(1).assign(Operation.add(99,2)));

        query.generateCppCode();
    }

    @Test (expected = FieldIndexOutOfBoundException.class)
    public void mapOperationOnOutOfBoundRightFieldIndexThrowException() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField(1).assign(Operation.add(1,99)));

        query.generateCppCode();
    }

    @Test (expected = FieldNotFoundException.class)
    public void mapOperationOnUnknownLeftFieldThrowException() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField(1).assign(Operation.add("value99","value2")));

        query.generateCppCode();
    }

    @Test (expected = FieldNotFoundException.class)
    public void mapOperationOnUnknownRightFieldThrowException() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField(1).assign(Operation.add("value1","value99")));

        query.generateCppCode();
    }

    @Test
    public void mapOperationOnNewField() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.add("value1","value2")));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());
    }

    @Test
    public void mapOperationOperators() throws FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.add(1,2)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.add("value1","value2")));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") + Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.substract(1,2)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") - Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.substract("value1","value2")));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") - Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.multiply(1,2)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") * Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.multiply("value1","value2")));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") * Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.divide(1,2)));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") / Attribute(\"value2\"));"
                , query.generateCppCode());

        query = new Query();
        query.from(defaultLogical).map(Map.onField("newfield").assign(Operation.divide("value1","value2")));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").map(Attribute(\"newfield\") = Attribute(\"value1\") / Attribute(\"value2\"));"
                , query.generateCppCode());
    }
}
