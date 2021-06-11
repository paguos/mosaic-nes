package com.nebula.stream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.Aggregation;
import stream.nebula.operators.EventTime;
import stream.nebula.operators.TimeMeasure;
import stream.nebula.operators.windowdefinition.SlidingWindow;
import stream.nebula.operators.windowdefinition.TumblingWindow;
import stream.nebula.queryinterface.Query;

public class WindowByKeyOperatorTest {
    @Test
    public void windowOperatorGenerateCorrectCppCode() throws Exception {
        Query query;

        // Test Tumbling Window
        query = new Query();
        query.from("defaultLogical")
                .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum("value"));

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".window(TumblingWindow::of(EventTime(Attribute(\"timestamp\")), Milliseconds(10))).byKey(Attribute(\"id\")).apply(Sum(Attribute(\"value\")));"
                , query.generateCppCode());

        // Test Sliding Window
        query = new Query();
        query.from("defaultLogical")
                .windowByKey("id", SlidingWindow.of(new EventTime("timestamp"), TimeMeasure.minutes(1), TimeMeasure.seconds(30)), Aggregation.sum("value"));

        Assert.assertEquals("Query::from(\"defaultLogical\")" +
                        ".window(SlidingWindow::of(EventTime(Attribute(\"timestamp\")), Minutes(1), Seconds(30))).byKey(Attribute(\"id\")).apply(Sum(Attribute(\"value\")));"
                , query.generateCppCode());

    }

    @Test
    public void testUsingEmptyKey() throws EmptyFieldException {
        Exception exception = assertThrows(EmptyFieldException.class, () -> {
            Query query;
            query = new Query();
            query.from("defaultLogical")
                    .windowByKey("", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum("value"));
        });

        String expectedMessage = "Window key cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUsingEmptyAggregationField() throws EmptyFieldException {
        Exception exception = assertThrows(EmptyFieldException.class, () -> {
            Query query;
            query = new Query();
            query.from("defaultLogical")
                    .windowByKey("id", TumblingWindow.of(new EventTime("timestamp"),TimeMeasure.milliseconds(10)), Aggregation.sum(""));
        });

        String expectedMessage = "Aggregation cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
