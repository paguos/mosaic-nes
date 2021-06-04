package com.nebula.stream;

import org.junit.Assert;
import org.junit.Test;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.operators.Predicate;
import stream.nebula.queryinterface.Query;

public class FilterOperatorTest {
    @Test
    public void filterPredicateGenerateCorrectOperatorInCppCode() throws Exception {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").greaterThan(42));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")>42);", query.generateCppCode());

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").greaterThanOrEqual(42));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")>=42);", query.generateCppCode());

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").lessThan(42));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")<42);", query.generateCppCode());

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").lessThanOrEqual(42));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")<=42);", query.generateCppCode());

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").equal(42));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")==42);", query.generateCppCode());
    }

    @Test (expected = EmptyFieldException.class)
    public void filterPredicateWithEmptyFieldThrowEmptyFieldException() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("")
                .filter(Predicate.onField("value").equal(42));
        query.generateCppCode();
    }

    @Test
    public void multipleFilterPredicateWithAnd() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").greaterThan(100).and(Predicate.onField("value").lessThan(200)));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")>100 && Attribute(\"value\")<200);"
                , query.generateCppCode());
    }

    @Test
    public void multipleFilterPredicateWithOr() throws EmptyFieldException {
        Query query;

        query = new Query();
        query.from("defaultLogical")
                .filter(Predicate.onField("value").greaterThan(100).or(Predicate.onField("value").lessThan(200)));
        Assert.assertEquals("Query::from(\"defaultLogical\").filter(Attribute(\"value\")>100 || Attribute(\"value\")<200);"
                , query.generateCppCode());
    }
}
