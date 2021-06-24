package com.github.paguos.mosaic.fed.nebulastream.stream.zmq;

import com.github.paguos.mosaic.fed.nebulastream.msg.proto.SerializableSchema;
import com.github.paguos.mosaic.fed.nebulastream.stream.Schema;
import com.github.paguos.mosaic.fed.nebulastream.stream.SchemaParser;
import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class ZeroMQReader implements Runnable {

    private final String zeroMQAddress;
    private final ArrayBlockingQueue<String> receivedMessages;
    private boolean schemaReceived;

    private Schema schema;
    private SchemaParser schemaParser;

    public ZeroMQReader(String zeroMQAddress, ArrayBlockingQueue<String> receivedMessages) {
        this.zeroMQAddress = zeroMQAddress;
        this.receivedMessages = receivedMessages;
        this.schemaReceived = false;
    }

    @Override
    public void run() {

        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.PULL);
            socket.bind(zeroMQAddress);

            while (!Thread.currentThread().isInterrupted()) {
                String envelope = socket.recvStr();
                byte[] messages = socket.recv();

                if (schemaReceived) {
                    int currentIndex = 0;
                    while (currentIndex < messages.length) {
                        byte[] tupleBytes = Arrays.copyOfRange(messages, currentIndex, currentIndex + schema.getByteSize());
                        receivedMessages.add(schemaParser.parseTuple(tupleBytes));
                        currentIndex += schema.getByteSize();
                    }
                } else {
                    byte[] schemaBytes = Arrays.copyOfRange(messages, 0, 542);
                    SerializableSchema serializableSchema = SerializableSchema.parseFrom(schemaBytes);
                    schema = Schema.parseFrom(serializableSchema);
                    schemaParser = new SchemaParser(schema);
                    schemaReceived = true;
                }

            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
