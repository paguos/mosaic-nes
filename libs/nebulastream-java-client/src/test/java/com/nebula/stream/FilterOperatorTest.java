package com.nebula.stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.FieldCompareTypeMismatchException;
import stream.nebula.exceptions.FieldIndexOutOfBoundException;
import stream.nebula.exceptions.FieldNotFoundException;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.operators.Predicate;
import stream.nebula.queryinterface.Query;

import java.util.ArrayList;

public class FilterOperatorTest {
    private static LogicalStream defaultLogical;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        fieldArrayList.add(new Field("id","UINT32"));
        fieldArrayList.add(new Field("value","UINT32"));
        fieldArrayList.add(new Field("stringField","CHAR[30]"));
        defaultLogical = new LogicalStream("default_logical", fieldArrayList);
    }


    @Test
    public void filterPredicateGenerateCorrectOperatorInCppCode() throws Exception {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).greaterThan(42));


        // Testing operators
        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\""+defaultLogical
                        .getFieldList().get(1).getName()+"\")>42);", query.generateCppCode());

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).greaterThanOrEqual(42));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\""+defaultLogical
                        .getFieldList().get(1).getName()+"\")>=42);", query.generateCppCode());

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).lessthanThan(42));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\""+defaultLogical
                        .getFieldList().get(1).getName()+"\")<42);", query.generateCppCode());

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).lessthanThanOrEqual(42));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\""+defaultLogical
                        .getFieldList().get(1).getName()+"\")<=42);", query.generateCppCode());

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).equal(42));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\""+defaultLogical
                        .getFieldList().get(1).getName()+"\")==42);", query.generateCppCode());
    }

    @Test
    public void filterPredicateWithFieldNameGenerateCorrectCppCode() throws FieldCompareTypeMismatchException,
            UnknownDataTypeException, FieldNotFoundException {
        Query query;

        String testFieldName = "value";

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(testFieldName).equal(42));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\""+testFieldName+"\")==42);"
                , query.generateCppCode());
    }

    @Test (expected = FieldNotFoundException.class)
    public void providingUnknownFieldNameProduceFieldNotFoundException() throws UnknownDataTypeException,
            FieldNotFoundException, FieldCompareTypeMismatchException {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField("non-existent-field").equal(42));
    }

    @Test(expected = FieldIndexOutOfBoundException.class)
    public void outOfBoundFieldIndexProduceFieldIndexOutOfBoundException() throws FieldCompareTypeMismatchException, UnknownDataTypeException, FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(99).equal(42));

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(-1).equal(42));
    }

    @Test(expected = FieldCompareTypeMismatchException.class)
    public void compareMismatchedFieldTypeProduceFieldCompareTypeMismatchException() throws FieldCompareTypeMismatchException, UnknownDataTypeException, FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                // compare field 1 (of type Integer/INT32) to 42L (long)
                .filter(Predicate.onField(1).equal(42L));

        query = new Query();
        query.from(defaultLogical)
                // compare field 2 (of type String/CHAR[]) to 42L (long)
                .filter(Predicate.onField(2).equal(42L));

        query = new Query();
        query.from(defaultLogical)
                // compare field "stringField" (of type String/CHAR[]) to 42L (long)
                .filter(Predicate.onField("stringField").equal(42L));
    }

    @Test
    public void multipleFilterPredicateUsingAnd() throws FieldCompareTypeMismatchException, UnknownDataTypeException, FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).greaterThan(100).and(Predicate.onField(1).greaterThan(200)));

        String cppCode = query.generateCppCode();
        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\"value\")>100 && Attribute(\"value\")>200);"
                , query.generateCppCode());
    }

    @Test
    public void multipleFilterPredicateUsingOr() throws FieldCompareTypeMismatchException, UnknownDataTypeException, FieldNotFoundException {
        Query query;

        query = new Query();
        query.from(defaultLogical)
                .filter(Predicate.onField(1).greaterThan(100).or(Predicate.onField(1).greaterThan(200)));

        String cppCode = query.generateCppCode();
        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\").filter(Attribute(\"value\")>100 || Attribute(\"value\")>200);"
                , query.generateCppCode());
    }
}
