package com.github.paguos.mosaic.fed.nebulastream.common;

public class DataTypeFactory {

    public static FixedChar createFixedChar(int size) {
        return new FixedChar(size);
    }

    public static DataType createType(BasicType basicType) {
        switch (basicType) {
            case BOOLEAN:
                return new BooleanType();
            case CHAR:
                return createFixedChar(1);
            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case UINT8:
            case UINT16:
            case UINT32:
            case UINT64:
                return new IntegerType(basicType);
            case FLOAT32:
            case FLOAT64:
                return new FloatType(basicType);
            default:
                return null;
        }
    }

}
