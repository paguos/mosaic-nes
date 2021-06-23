package com.github.paguos.mosaic.fed.examples;

import com.github.paguos.mosaic.fed.catalog.SchemaCatalog;
import com.github.paguos.mosaic.fed.nebulastream.stream.BufferBuilder;
import com.github.paguos.mosaic.fed.nebulastream.stream.zmq.ZeroMQWriter;

import java.util.concurrent.ArrayBlockingQueue;

public class ZmqSource {

    private static final String ZmqAddress = "tcp://127.0.0.1:5556";

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<byte[]> receivedMessages = new ArrayBlockingQueue<>(200);
        Thread zmqThread = new Thread(new ZeroMQWriter(ZmqAddress, receivedMessages));
        zmqThread.start();

        while (true) {
            byte[] test = BufferBuilder.createBuffer(SchemaCatalog.getMosaicNesSchema().getByteSize())
                    .fill("veh_1", 7)
                    .fill(75000000000L)
                    .fill(52.512683D)
                    .fill(13.320318D)
                    .fill(9.461893D)
                    .build();
            receivedMessages.add(test);
            Thread.sleep(500);
        }

    }
}
