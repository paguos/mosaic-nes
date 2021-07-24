package com.github.paguos.mosaic.fed.nebulastream.common;

public class BooleanType extends DataType {

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public int getByteSize() {
        return 1;
    }

    public static boolean parse(byte b) {
        return b!=0;
    }

    @Override
    public String parseString(byte[] bytes) {
        if (bytes[0] == 1) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public String toCpp() {
        return null;
    }

}
