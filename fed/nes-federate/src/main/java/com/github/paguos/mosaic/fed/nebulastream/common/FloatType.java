package com.github.paguos.mosaic.fed.nebulastream.common;

import com.github.paguos.mosaic.fed.utils.IOUtils;

import java.nio.ByteBuffer;

public class FloatType extends DataType{

    private final BasicType type;

    public FloatType(BasicType type) {
        this.type = type;
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public int getByteSize() {
        switch (type) {
            case FLOAT32:
                return 4;
            case FLOAT64:
                return 8;
            default:
                return 0;
        }
    }

    @Override
    public String parseString(byte[] bytes) {
        IOUtils.reverseArray(bytes);
        ByteBuffer wrapped = ByteBuffer.wrap(bytes);

        switch (type) {
            case FLOAT32:
                return "" + wrapped.getFloat();
            case FLOAT64:
                return "" + wrapped.getDouble();
            default:
                return null;
        }
    }

    public BasicType getType() {
        return type;
    }

    @Override
    public String toCpp() {
        return this.type.name();
    }
}
