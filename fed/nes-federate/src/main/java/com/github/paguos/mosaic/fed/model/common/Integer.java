package com.github.paguos.mosaic.fed.model.common;

public class Integer extends DataType {

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public String toCpp() {
        return "UINT64";
    }
}
