package com.nebula.stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.model.logicalstream.Field;
import stream.nebula.model.logicalstream.LogicalStream;
import stream.nebula.operators.EventTime;
import stream.nebula.operators.TimeMeasure;
import stream.nebula.operators.windowdefinition.TumblingWindow;
import stream.nebula.queryinterface.Query;

import java.util.ArrayList;

public class JoinWithOperatorTest {
    private static LogicalStream car;
    private static LogicalStream truck;

    @BeforeClass
    public static void init() throws UnknownDataTypeException {
        ArrayList<Field> carFieldList = new ArrayList<>();
        carFieldList.add(new Field("id_car","UINT32"));
        carFieldList.add(new Field("value_car","UINT32"));
        carFieldList.add(new Field("timestamp","UINT64"));

        ArrayList<Field> truckFieldList = new ArrayList<>();
        truckFieldList.add(new Field("id_truck","UINT32"));
        truckFieldList.add(new Field("value_truck","UINT32"));
        carFieldList.add(new Field("timestamp","UINT64"));

        car = new LogicalStream("car", carFieldList);
        truck = new LogicalStream("truck", truckFieldList);
    }

    @Test
    public void joinWithGenerateCorrectCppCode() {
        // create the q1 Query
        Query q1 = new Query();
        q1.from(car);

        // create the q2 Query
        Query q2 = new Query();
        q2.from(truck);

        // join q1 and q2 on q1.id_car == q2.id_truck with tumbling window of 10ms on timestamp attribute
        q1.joinWith(q2, "id_car", "id_truck", TumblingWindow.of(new EventTime("timestamp"), TimeMeasure.milliseconds(10)));

        // assert that join operator generates the correct NES query
        Assert.assertEquals("Query::from(\"car\")" +
                        ".joinWith(Query::from(\"truck\"), Attribute(\"id_car\"), Attribute(\"id_truck\"), TumblingWindow::of(EventTime(Attribute(\"timestamp\")), Milliseconds(10)));"
                , q1.generateCppCode());
    }
}
