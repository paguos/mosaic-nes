package stream.nebula.examples;

import stream.nebula.operators.Map;
import stream.nebula.operators.Operation;
import stream.nebula.operators.Predicate;
import stream.nebula.operators.sink.PrintSink;
import stream.nebula.queryinterface.Query;
import stream.nebula.runtime.NebulaStreamRuntime;

/**
 * This example demonstrates basics usage of InputQuery of NES Java Client. It covers basic network configuration,
 * creating InputQuery and apply org.nebula.stream.operators to the InputQuery instance and finally send the query to
 * the NES system.
 *
 * The first thing we need to do is to define the host and port of the NES system. Since in this example, we run NES
 * system on the same machine, we going to set host to localhost. Note that we also set the port to NES REST
 * default port, which is 8081.
 *
 * Next, we are going to create a query. This can be done by creating an instance of InputQuery. To specify the
 * stream to use, we specify it as a parameter for from() operator. We then apply org.nebula.stream.operators to this
 * query instance. In this example, we are then going to apply filter and print the operator.
 *
 * Finally, we call execute() that takes the network configuration (NES REST host and port). We can print the response
 * to see the query-id returned by the NES system.
 **/

public class QueryExample {
    public static void main(String[] args) throws Exception {

        // Configure network connection to NES REST server
        NebulaStreamRuntime ner = NebulaStreamRuntime.getRuntime();
        ner.getConfig().setHost("localhost")
                .setPort("8081");

        // Create an instance of Query that assign the d5 field with the value of d3*d4
        Query query = new Query().from("ysb")
                .filter(Predicate.onField("user_id").greaterThanOrEqual(1).and(Predicate.onField("user_id").lessThan(10000)))
                .map(Map.onField("d5").assign(Operation.multiply("d3","d4")))
                .sink(new PrintSink());

        // Print out the generated C++ code
        System.out.println(query.generateCppCode());

        // Execute the query to NES system. Additionally print the response to get the query-id.
        int response = ner.executeQuery(query.generateCppCode(), "BottomUp");
        System.out.println("RESPONSE:\n"+response);
    }
}
