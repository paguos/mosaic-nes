package com.github.paguos.mosaic.fed.nebulastream.stream;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.concurrent.ArrayBlockingQueue;

public class ZmqSource implements Runnable {

    private final String zmqAddress;
    private final ArrayBlockingQueue<byte[]> messages;
    private boolean running;

    public ZmqSource(String zmqAddress, ArrayBlockingQueue<byte[]> messages) {
        this.zmqAddress = zmqAddress;
        this.messages = messages;
        this.running = true;
    }

    @Override
    public void run() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.PUSH);
            socket.connect(zmqAddress);

            while (running) {
                if (!messages.isEmpty()) {
                    byte[] envelope = new byte[16];
                    socket.send(envelope);
                    socket.send(messages.poll());
                }
            }
        }
    }

    public void terminate() {
        this.running = false;
    }
}
