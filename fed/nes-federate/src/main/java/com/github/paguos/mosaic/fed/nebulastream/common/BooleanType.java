package com.github.paguos.mosaic.fed.nebulastream.common;

public class BooleanType extends DataType {

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public String toCpp() {
        return null;
    }

}
