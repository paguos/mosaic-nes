package com.github.paguos.mosaic.fed.nebulastream.stream;

import org.junit.Test;

import static org.junit.Assert.*;

public class EnvelopeTest {

    @Test
    public void payloadEnvelopeToBytes() {
        Envelope envelope = new Envelope(false, 100, 2500);

        byte[] expectedBytes = {0, 100, 0, 0, 0, 0, 0, 0, 0, -60, 9, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedBytes, envelope.toByteBuffer());
    }

    @Test
    public void metadataEnvelopeToBytes() {
        Envelope envelope = new Envelope(true, 542, 0);

        byte[] expectedBytes = {1, 30, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedBytes, envelope.toByteBuffer());
    }

    @Test
    public void parseFromBytes() {
        byte[] bytes = {0, 100, 0, 0, 0, 0, 0, 0, 0, -60, 9, 0, 0, 0, 0, 0, 0};
        Envelope envelope = Envelope.parse(bytes);

        assertFalse(envelope.isSchema());
        assertEquals(100, envelope.getTuplesCount());
        assertEquals(2500, envelope.getWatermark());
    }

    @Test
    public void parseSchemaEnvelope() {
        byte[] bytes = {1, 30, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Envelope envelope = Envelope.parse(bytes);

        assertTrue(envelope.isSchema());
        assertEquals(542, envelope.getTuplesCount());
        assertEquals(0, envelope.getWatermark());
    }
}
