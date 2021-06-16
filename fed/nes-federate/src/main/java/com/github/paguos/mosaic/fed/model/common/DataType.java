package com.github.paguos.mosaic.fed.model.common;

public abstract class DataType {

    public boolean isFixedChar() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isInteger(){
        return false;
    }

    public abstract  String toCpp();

}
