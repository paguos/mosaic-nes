package com.github.paguos.mosaic.fed.nebulastream.common;

public abstract class DataType {

    public boolean isBoolean() {
        return false;
    }

    public boolean isFixedChar() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isInteger(){
        return false;
    }

    public abstract int getByteSize();

    public abstract String parseString(byte[] bytes);

    public abstract  String toCpp();

}
