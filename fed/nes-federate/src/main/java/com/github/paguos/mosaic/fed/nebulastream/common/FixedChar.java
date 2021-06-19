package com.github.paguos.mosaic.fed.nebulastream.common;

import java.util.Objects;

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
    public int getByteSize() {
        return size;
    }

    @Override
    public String parseString(byte[] bytes) {
        return new String(bytes).trim();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedChar fixedChar = (FixedChar) o;
        return size == fixedChar.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }
}
