package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.common.BasicType;
import com.github.paguos.mosaic.fed.nebulastream.common.BooleanType;
import com.github.paguos.mosaic.fed.nebulastream.common.IntegerType;

import java.util.Arrays;

public class Envelope {

    private final boolean isSchema;
    private final long tuplesCount;
    private final long watermark;

    public Envelope(boolean isSchema, long tuplesCount, long watermark) {
        this.isSchema = isSchema;
        this.tuplesCount = tuplesCount;
        this.watermark = watermark;
    }

    public boolean isSchema() {
        return isSchema;
    }

    public long getTuplesCount() {
        return tuplesCount;
    }

    public long getWatermark() {
        return watermark;
    }

    public byte[] toByteBuffer() {
        return BufferBuilder.createBuffer(17)
                .fill(isSchema)
                .fill(tuplesCount)
                .fill(watermark)
                .build();
    }

    public static Envelope parse(byte[] bytes) {
        int offset = 0;

        BooleanType flagType = new BooleanType();
        IntegerType dataType = new IntegerType(BasicType.UINT64);

        boolean isSchema = BooleanType.parse(bytes[0]);
        offset += flagType.getByteSize();

        long tuplesCount = IntegerType.parseLong(
                Arrays.copyOfRange(bytes, offset, offset + dataType.getByteSize()));

        offset += dataType.getByteSize();
        long watermark = IntegerType.parseLong(
                Arrays.copyOfRange(bytes, offset, offset + dataType.getByteSize()));

        return new Envelope(isSchema, tuplesCount, watermark);
    }
}
