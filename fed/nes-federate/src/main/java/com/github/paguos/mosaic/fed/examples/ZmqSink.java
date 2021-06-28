package com.github.paguos.mosaic.fed.examples;

import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQSink;

import java.util.concurrent.ArrayBlockingQueue;

public class ZmqSink {

    private static final String ZmqAddress = "tcp://localhost:5555";

    public static void main(String[] args) {
        ArrayBlockingQueue<String> receivedMessages = new ArrayBlockingQueue<>(200);
        Thread zmqThread = new Thread(new ZeroMQSink(ZmqAddress, receivedMessages));
        zmqThread.start();

        while (true) {
            if (receivedMessages.size() > 0) {
                System.out.println("Received!");
            }
        }
    }
}
