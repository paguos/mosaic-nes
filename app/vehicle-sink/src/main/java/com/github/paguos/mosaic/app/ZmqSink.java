package com.github.paguos.mosaic.app;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.concurrent.ArrayBlockingQueue;

public class ZmqSink implements Runnable {

    private final String zmqAddress;
    private final ArrayBlockingQueue<String> receivedMessages;


    public ZmqSink(String zmqAddress, ArrayBlockingQueue<String> receivedMessages) {
        this.zmqAddress = zmqAddress;
        this.receivedMessages = receivedMessages;
    }

    @Override
    public void run() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.PULL);
            socket.bind(zmqAddress);

            while (!Thread.currentThread().isInterrupted()) {
                ZMsg strings = ZMsg.recvMsg(socket);
                receivedMessages.add(strings.toString());
            }
        }
    }
}
