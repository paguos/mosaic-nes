package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.common.IntegerType;

import java.util.Arrays;

public class Envelope {

    private final long tuplesCount;
    private final long watermark;

    public Envelope(long tuplesCount, long watermark) {
        this.tuplesCount = tuplesCount;
        this.watermark = watermark;
    }

    public long getTuplesCount() {
        return tuplesCount;
    }

    public long getWatermark() {
        return watermark;
    }

    public byte[] toByteBuffer() {
        return BufferBuilder.createBuffer(16)
                .fill(tuplesCount)
                .fill(watermark)
                .build();
    }

    public static Envelope parse(byte[] bytes) {
        long tuplesCount = IntegerType.parseLong(
                Arrays.copyOfRange(bytes, 0, bytes.length / 2));
        long watermark = IntegerType.parseLong(
                Arrays.copyOfRange(bytes, bytes.length / 2, bytes.length ));
        return new Envelope(tuplesCount, watermark);
    }
}
