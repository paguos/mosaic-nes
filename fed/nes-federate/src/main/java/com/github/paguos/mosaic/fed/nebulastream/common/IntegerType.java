package com.github.paguos.mosaic.fed.nebulastream.common;

import com.github.paguos.mosaic.fed.utils.IOUtils;

import java.nio.ByteBuffer;
import java.util.Objects;

public class IntegerType extends DataType {

    private final BasicType type;

    public IntegerType(BasicType type) {
        this.type = type;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    public BasicType getType() {
        return type;
    }

    @Override
    public int getByteSize() {
        switch (type) {
            case INT8:
            case UINT8:
                return 1;
            case INT16:
            case UINT16:
                return 2;
            case INT32:
            case UINT32:
                return 4;
            case INT64:
            case UINT64:
                return 8;
            default:
                return 0;
        }
    }

    public static long parseLong(byte[] bytes) {
        IOUtils.reverseArray(bytes);
        return ByteBuffer.wrap(bytes).getLong();
    }

    @Override
    public String parseString(byte[] bytes) {
        IOUtils.reverseArray(bytes);
        ByteBuffer wrapped = ByteBuffer.wrap(bytes);

        switch (type) {
            case INT8:
                return String.format("%d", wrapped.get());
            case INT16:
                return String.format("%d", wrapped.getShort());
            case INT32:
                return String.format("%d", wrapped.getInt());
            case INT64:
                return String.format("%d", wrapped.getLong());
            default:
                return null;
        }
    }

    @Override
    public String toCpp() {
        return this.type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerType that = (IntegerType) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
