package stream.nebula.examples;

import stream.nebula.operators.Aggregation;
import stream.nebula.operators.EventTime;
import stream.nebula.operators.TimeMeasure;
import stream.nebula.operators.windowdefinition.SlidingWindow;
import stream.nebula.operators.windowdefinition.TumblingWindow;
import stream.nebula.queryinterface.Query;
import stream.nebula.runtime.NebulaStreamRuntime;
import stream.nebula.model.logicalstream.LogicalStream;

import java.util.List;

/**
 * This example demonstrate how to use window oeprator on NES Java Client. Currently, there are two kinds of window
 * operator supported:
 * 1. Tumbling Window
 * 2. Sliding Window
 *
 * Window operator can be added to an InputQuery object by calling window() method. This method expect two parameters:
 * 1. An implementation of WindowType abstract class (either TumblingWindow or SlidingWindow).
 * 2. Aggregation function to be applied to each window.
 *
 * We can specify the time parameter used in the window by calling its static of() method. The of() method of
 * TumblingWindow accept a single parameter of Time object that define the length of the window. Meanwhile, the of()
 * method of SlidingWindow expect two parameters: the length of the window and the sliding between window.
 *
 * The time parameter can be specified by calling statics method of different kinds of time measurement (e.g Time.hour().
 * Currently it support milliseconds(), seconds(), minutes(), and hours().
 *
 * The aggregation to be applied to each window can be chosen from the static functions of Aggregation class. Currently,
 * it support sum() aggregation function and expect a single parameter of fieldName.
 */


public class WindowExample {
    public static void main(String[] args) throws Exception {
        // Configure network connection to NES REST server
        NebulaStreamRuntime ner = NebulaStreamRuntime.getRuntime();
        ner.getConfig().setHost("localhost")
                .setPort("8081");

        // Get a list of available logical stream and choose one
        List<LogicalStream> availableLogicalStream = ner.getAvailableLogicalStreams();
        LogicalStream defaultLogical = availableLogicalStream.get(0);

        Query query;

        // Example of using event time tumbling window of 10 seconds and then apply sum aggregation to each window
        query = new Query();
        query.from(defaultLogical)
                .window(TumblingWindow.of(new EventTime("timestamp"), TimeMeasure.seconds(10)), Aggregation.sum(1));
        System.out.println(query.generateCppCode());

        // Example of using event time sliding window of 1 minutes with 30 seconds slide and then apply sum aggregation to
        // each window
        query = new Query();
        query.from(defaultLogical)
                .window(SlidingWindow.of(new EventTime("timestamp"), TimeMeasure.minutes(1), TimeMeasure.seconds(30)), Aggregation.sum(1));
        System.out.println(query.generateCppCode());


    }
}
