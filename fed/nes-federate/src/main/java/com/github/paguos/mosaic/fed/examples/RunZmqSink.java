package com.github.paguos.mosaic.fed.examples;

import com.github.paguos.mosaic.fed.nebulastream.stream.ZmqSink;

import java.util.concurrent.ArrayBlockingQueue;

public class RunZmqSink {

    private static final String ZmqAddress = "tcp://localhost:5555";

    public static void main(String[] args) {
        ArrayBlockingQueue<String> receivedMessages = new ArrayBlockingQueue<>(200);
        Thread zmqThread = new Thread(new ZmqSink(ZmqAddress, receivedMessages));
        zmqThread.start();
    }
}
