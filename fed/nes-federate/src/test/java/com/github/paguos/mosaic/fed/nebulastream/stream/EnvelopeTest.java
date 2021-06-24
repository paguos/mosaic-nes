package com.github.paguos.mosaic.fed.nebulastream.stream;

import org.junit.Test;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EnvelopeTest {

    @Test
    public void envelopeToBytes() {
        Envelope envelope = new Envelope(100, 2500);

        byte[] expectedBytes = {100, 0, 0, 0, 0, 0, 0, 0, -60, 9, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedBytes, envelope.toByteBuffer());
    }

    @Test
    public void parseFromBytes() {
        byte[] bytes = {100, 0, 0, 0, 0, 0, 0, 0, -60, 9, 0, 0, 0, 0, 0, 0};
        Envelope envelope = Envelope.parse(bytes);

        assertEquals(100, envelope.getTuplesCount());
        assertEquals(2500, envelope.getWatermark());
    }
}
