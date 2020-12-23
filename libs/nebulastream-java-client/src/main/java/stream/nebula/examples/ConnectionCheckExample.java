package stream.nebula.examples;

import stream.nebula.runtime.NebulaStreamRuntime;

import java.util.List;

/**
 * This example demonstrate how to check if connection to NES Coordinator is working.
 **/

public class ConnectionCheckExample {
    public static void main(String[] args) throws Exception {

        // Configure network connection to NES REST server
        NebulaStreamRuntime ner = NebulaStreamRuntime.getRuntime();
        ner.getConfig().setHost("localhost")
                .setPort("8081");

        // Execute the query to NES system. Additionally print the response to get the query-id.
        boolean response = ner.checkConnection();
        System.out.println("RESPONSE:\n"+response);
    }
}
