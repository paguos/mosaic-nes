package com.github.paguos.mosaic.fed.nebulastream.common;

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
    public String toCpp() {
        return this.type.name();
    }
}
