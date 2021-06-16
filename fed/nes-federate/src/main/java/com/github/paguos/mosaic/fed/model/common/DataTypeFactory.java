package com.github.paguos.mosaic.fed.model.common;

public class DataTypeFactory {

    public static FixedChar createFixedChar(int size) {
        return new FixedChar(size);
    }

    public static Float createFloat() {
        return new Float();
    }

    public static Integer createInteger() {
        return new Integer();
    }

}
