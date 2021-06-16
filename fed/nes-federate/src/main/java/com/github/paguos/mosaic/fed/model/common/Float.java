package com.github.paguos.mosaic.fed.model.common;

public class Float extends DataType{

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public String toCpp() {
        return "FLOAT32";
    }
}
