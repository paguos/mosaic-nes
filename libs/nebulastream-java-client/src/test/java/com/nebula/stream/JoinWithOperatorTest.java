package com.nebula.stream;

import org.junit.Assert;
import org.junit.Test;
import stream.nebula.exceptions.EmptyFieldException;
import stream.nebula.operators.EventTime;
import stream.nebula.operators.TimeMeasure;
import stream.nebula.operators.windowdefinition.TumblingWindow;
import stream.nebula.queryinterface.Query;

public class JoinWithOperatorTest {
    @Test
    public void joinWithGenerateCorrectCppCode() throws EmptyFieldException {
        // create the q1 Query
        Query q1 = new Query();
        q1.from("car");

        // create the q2 Query
        Query q2 = new Query();
        q2.from("truck");

        // join q1 and q2 on q1.id_car == q2.id_truck with tumbling window of 10ms on timestamp attribute
        q1.joinWith(q2, "id_car", "id_truck", TumblingWindow.of(new EventTime("timestamp"), TimeMeasure.milliseconds(10)));


        // assert that join operator generates the correct NES query
        Assert.assertEquals("Query::from(\"car\")" +
                        ".joinWith(Query::from(\"truck\")).where(Attribute(\"id_car\")).equalsTo(Attribute(\"id_truck\")).window(TumblingWindow::of(EventTime(Attribute(\"timestamp\")), Milliseconds(10)));"
                , q1.generateCppCode());
    }
}
