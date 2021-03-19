package com.nebula.stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.FieldIndexOutOfBoundException;
import stream.nebula.exceptions.FieldNotFoundException;
import stream.nebula.exceptions.InvalidAggregationFieldException;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.operators.Aggregation;
import stream.nebula.operators.EventTime;
import stream.nebula.operators.TimeMeasure;
import stream.nebula.operators.windowdefinition.SlidingWindow;
import stream.nebula.operators.windowdefinition.TumblingWindow;
import stream.nebula.queryinterface.Query;

import java.util.ArrayList;

public class WindowByKeyOperatorTest {
    private static LogicalStream defaultLogical;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        fieldArrayList.add(new Field("id","UINT32"));
        fieldArrayList.add(new Field("value","UINT32"));
        fieldArrayList.add(new Field("charval","CHAR"));
        defaultLogical = new LogicalStream("default_logical", fieldArrayList);
    }

    @Test
    public void windowOperatorGenerateCorrectCppCode() throws Exception {
        Query query;

        // Test Tumbling Window
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum(1));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\")" +
                        ".windowByKey(Attribute(\"id\"), TumblingWindow::of(EventTime(Attribute(\"timestamp\")), Milliseconds(10)), " +
                        "Sum(Attribute(\"value\")));"
                , query.generateCppCode());

        // Test Sliding Window
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("id", SlidingWindow.of(new EventTime("timestamp"), TimeMeasure.minutes(1), TimeMeasure.seconds(30)), Aggregation.sum(1));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\")" +
                        ".windowByKey(Attribute(\"id\"), SlidingWindow::of(EventTime(Attribute(\"timestamp\")), Minutes(1), Seconds(30)), Sum(Attribute(\"value\")));"
                , query.generateCppCode());

        // Test Aggregation using fieldName
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum("value"));

        Assert.assertEquals("Query::from(\""+defaultLogical.getName()+"\")" +
                        ".windowByKey(Attribute(\"id\"), TumblingWindow::of(EventTime(Attribute(\"timestamp\")), Milliseconds(10)), Sum(Attribute(\"value\")));"
                , query.generateCppCode());
    }

    @Test (expected = FieldNotFoundException.class)
    public void providingNonExistentKeyFieldProduceFieldNotFoundException() throws FieldIndexOutOfBoundException, InvalidAggregationFieldException, FieldNotFoundException {
        Query query;
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("non-existent-field", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum("non-existent-field"));
    }

    @Test (expected = InvalidAggregationFieldException.class)
    public void aggregatingNonNumberFieldProduceInvalidAggregationFieldException() throws InvalidAggregationFieldException, FieldNotFoundException {
        Query query;
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum(2));
    }

    @Test (expected = FieldIndexOutOfBoundException.class)
    public void aggregatingOutOfBoundFieldProduceFieldIndexOutOfBoundException() throws FieldIndexOutOfBoundException, InvalidAggregationFieldException, FieldNotFoundException {
        Query query;
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum(99));
    }

    @Test (expected = FieldNotFoundException.class)
    public void aggregatingNonExistentFieldProduceFieldNotFoundException() throws FieldIndexOutOfBoundException, InvalidAggregationFieldException, FieldNotFoundException {
        Query query;
        query = new Query();
        query.from(defaultLogical)
                .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum("non-existent-field"));
    }
}
