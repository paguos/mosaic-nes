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

public class ZeroMQReader implements Runnable {

    private final String zeroMQAddress;
    private final ArrayBlockingQueue<String> receivedMessages;
    private boolean schemaReceived;

    private Schema schema;
    private TupleParser tupleParser;

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
                byte[] envelopeBytes = socket.recv();
                byte[] messages = socket.recv();
                Envelope envelope = Envelope.parse(envelopeBytes);

                if (schemaReceived) {
                    int offset = 0;
                    while (offset < envelope.getTuplesCount() * schema.getByteSize()) {
                        byte[] tupleBytes = Arrays.copyOfRange(messages, offset, offset + schema.getByteSize());
                        receivedMessages.add(tupleParser.parseToString(tupleBytes));
                        offset += schema.getByteSize();
                    }
                } else {
                    byte[] schemaBytes = Arrays.copyOfRange(messages, 0, (int) envelope.getTuplesCount());
                    SerializableSchema serializableSchema = SerializableSchema.parseFrom(schemaBytes);
                    schema = Schema.parseFrom(serializableSchema);
                    tupleParser = new TupleParser(schema);
                    schemaReceived = true;
                }

            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
