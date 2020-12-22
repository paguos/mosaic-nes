package stream.nebula.utils;

import stream.nebula.exceptions.UnknownDataTypeException;

public class NESDataTypeUtil {
    // Available basic data types taken from IoTDB's DataType.hpp
    // https://github.com/nebulastream/IoTDB/blob/master/iotdb/include/API/Types/DataTypes.hpp
    public static String nesDataTypes = ("CHAR\\[\\d+\\]|INTEGER|\\(Float\\)|INT8|UINT8|INT16|UINT16|INT32|UINT32|INT64|UINT64|FLOAT32" +
            "|FLOAT64|BOOLEAN|CHAR|Char|DATE|VOID_TYPE");

    public static Class getJavaDataType(String nesDataTypes) throws UnknownDataTypeException {
        switch (nesDataTypes){
            case "INT8":
                return Byte.class;
            case "INT16":
                return Short.class;
            case "INT32":
                return Integer.class;
            case "INT64":
                return Long.class;
            case "UINT8":
                return Byte.class;
            case "UINT16":
                return Short.class;
            case "UINT32":
                return Integer.class;
            case "INTEGER":
                return Integer.class;
            case "UINT64":
                return Long.class;
            case "FLOAT32":
                return Float.class;
            case "(Float)":
                return Float.class;
            case "FLOAT64":
                return Double.class;
            case "BOOLEAN":
                return Boolean.class;
            case "CHAR":
                return Character.class;
            case "Char":
                return Character.class;
            case "VOID_TYPE":
                return Void.class;
            default:
                if (nesDataTypes.matches("CHAR\\[\\d+\\]")){
                    return String.class;
                } else {
                    throw new UnknownDataTypeException(nesDataTypes);

                }
        }
    }

    public static String getNesDataType(String javaDataTypeName) throws UnknownDataTypeException {
        switch (javaDataTypeName){
            case "Byte":
                return "INT8/UINT8";
            case "Short":
                return "INT16/UINT16";
            case "Integer":
                return "INT32/UINT32";
            case "Long":
                return "INT64/UINT64";
            case "Float":
                return "FLOAT32";
            case "Double":
                return "FLOAT64";
            case "Boolean":
                return "BOOLEAN";
            case "Character":
                return "CHAR";
            case "Void":
                return "VOID_TYPE";
            case "String":
                return "CHAR[]";
            default:
                throw new UnknownDataTypeException(javaDataTypeName);
        }
    }
}
