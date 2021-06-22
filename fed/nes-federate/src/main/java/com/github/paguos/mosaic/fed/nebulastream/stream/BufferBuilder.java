package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.utils.IOUtils;

import java.nio.ByteBuffer;

public class BufferBuilder {

    private final byte[] buffer;
    private int offset;

    private BufferBuilder(int bufferSize) {
        this.buffer = new byte[bufferSize];
        this.offset = 0;
    }

    public static BufferBuilder createBuffer(int bufferSize) {
        return new BufferBuilder(bufferSize);
    }

    public BufferBuilder fill(byte aByte) {
        buffer[offset++] = aByte;
        return this;
    }

    public BufferBuilder fill(byte[] bytes) {
        for (byte aByte : bytes) {
            buffer[offset++] = aByte;
        }
        return this;
    }

    public BufferBuilder fill(short aShort) {
        byte[] shortBytes = ByteBuffer.allocate(2).putShort(aShort).array();
        IOUtils.reverseArray(shortBytes);
        this.fill(shortBytes);
        return this;
    }

    public BufferBuilder fill(int aInt) {
        byte[] intBytes = ByteBuffer.allocate(4).putInt(aInt).array();
        IOUtils.reverseArray(intBytes);
        this.fill(intBytes);
        return this;
    }

    public BufferBuilder fill(long aLong) {
        byte[] longBytes = ByteBuffer.allocate(8).putLong(aLong).array();
        IOUtils.reverseArray(longBytes);
        this.fill(longBytes);
        return this;
    }

    public BufferBuilder fill(float aFloat) {
        byte[] floatBytes = ByteBuffer.allocate(4).putFloat(aFloat).array();
        IOUtils.reverseArray(floatBytes);
        this.fill(floatBytes);
        return this;
    }

    public BufferBuilder fill(double aDouble) {
        byte[] doubleBytes = ByteBuffer.allocate(8).putDouble(aDouble).array();
        IOUtils.reverseArray(doubleBytes);
        this.fill(doubleBytes);
        return this;
    }

    public BufferBuilder fill(String str, int desiredLength) {
        byte[] sBytes = new byte[desiredLength];
        System.arraycopy(str.getBytes(), 0, sBytes, 0, str.length());
        this.fill(sBytes);
        return this;
    }

    public byte[] build() {
        return buffer;
    }

}
