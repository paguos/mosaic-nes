package com.github.paguos.mosaic.fed.nebulastream.stream.zmq;

import com.github.paguos.mosaic.fed.nebulastream.msg.proto.SerializableSchema;
import com.github.paguos.mosaic.fed.nebulastream.stream.Envelope;
import com.github.paguos.mosaic.fed.nebulastream.stream.Schema;
import com.github.paguos.mosaic.fed.nebulastream.stream.TupleParser;
import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class ZeroMQSink implements Runnable {

    private final String zeroMQAddress;
    private final ArrayBlockingQueue<String> receivedMessages;
    private boolean schemaReceived;
    private volatile boolean running;

    private Schema schema;
    private TupleParser tupleParser;

    public ZeroMQSink(String zeroMQAddress, ArrayBlockingQueue<String> receivedMessages) {
        this.zeroMQAddress = zeroMQAddress;
        this.receivedMessages = receivedMessages;
        this.running = true;
        this.schemaReceived = false;
    }

    @Override
    public void run() {

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.PULL);
        socket.bind(zeroMQAddress);

        while (running) {
            byte[] envelopeBytes = socket.recv();
            byte[] messages = socket.recv();
            Envelope envelope = Envelope.parse(envelopeBytes);

            if (schemaReceived) {
                int offset = 0;
                while (offset < envelope.getTuplesCount() * schema.getByteSize() && offset < messages.length) {
                    byte[] tupleBytes = Arrays.copyOfRange(messages, offset, offset + schema.getByteSize());
                    receivedMessages.add(tupleParser.parseToString(tupleBytes));
                    offset += schema.getByteSize();
                }
            } else {
                byte[] schemaBytes = Arrays.copyOfRange(messages, 0, (int) envelope.getTuplesCount());
                try {
                    SerializableSchema serializableSchema = SerializableSchema.parseFrom(schemaBytes);
                    schema = Schema.parseFrom(serializableSchema);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

                tupleParser = new TupleParser(schema);
                schemaReceived = true;
            }
        }

        socket.close();
        context.close();
    }

    public void terminate() {
        this.running = false;
    }
}
