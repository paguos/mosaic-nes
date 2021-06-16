package com.github.paguos.mosaic.fed.nebulastream.common;

public class IntegerType extends DataType {

    private BasicType type;

    public IntegerType(BasicType type) {
        this.type = type;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public String toCpp() {
        return this.type.name();
    }
}
