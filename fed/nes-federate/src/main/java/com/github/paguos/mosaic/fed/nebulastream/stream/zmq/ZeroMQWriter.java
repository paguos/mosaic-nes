package com.github.paguos.mosaic.fed.nebulastream.stream.zmq;

import com.github.paguos.mosaic.fed.nebulastream.stream.BufferBuilder;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.concurrent.ArrayBlockingQueue;

public class ZeroMQWriter implements Runnable {

    private final String zmqAddress;
    private final ArrayBlockingQueue<byte[]> messages;
    private boolean running;

    public ZeroMQWriter(String zmqAddress, ArrayBlockingQueue<byte[]> messages) {
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
                    byte[] envelope = BufferBuilder.createBuffer(16)
                            .fill(1)
                            .fill(0)
                            .build();
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
