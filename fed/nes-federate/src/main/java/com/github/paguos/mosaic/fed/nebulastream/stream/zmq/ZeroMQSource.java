package com.github.paguos.mosaic.fed.nebulastream.stream.zmq;

import com.github.paguos.mosaic.fed.nebulastream.stream.Envelope;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.concurrent.ArrayBlockingQueue;

public class ZeroMQSource implements Runnable {

    private final String zmqAddress;
    private final ArrayBlockingQueue<byte[]> messages;
    private boolean running;

    public ZeroMQSource(String zmqAddress, ArrayBlockingQueue<byte[]> messages) {
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
                    Envelope envelope = new Envelope(false, 1, 0);
                    socket.send(envelope.toByteBuffer());
                    socket.send(messages.poll());
                }
            }

            socket.close();
        }
    }

    public void terminate() {
        this.running = false;
    }
}
