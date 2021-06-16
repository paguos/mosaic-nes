package com.github.paguos.mosaic.fed.nebulastream.common;

public class FixedChar extends DataType{

    private final int size;

    protected FixedChar(int size) {
        this.size = size;
    }

    @Override
    public boolean isFixedChar() {
        return true;
    }

    @Override
    public String toCpp() {
        return String.format("DataTypeFactory::createFixedChar(%d)", this.size);
    }

    @Override
    public String toString() {
        return "FixedChar{" +
                "size=" + size +
                '}';
    }
}
