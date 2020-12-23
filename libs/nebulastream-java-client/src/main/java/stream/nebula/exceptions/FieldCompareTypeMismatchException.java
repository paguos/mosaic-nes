package stream.nebula.exceptions;

import stream.nebula.utils.NESDataTypeUtil;

public class FieldCompareTypeMismatchException extends Exception {
    public FieldCompareTypeMismatchException(String comparedFieldName, String comparedFieldType, String value, String valueType) throws UnknownDataTypeException {
        super("Can not when compare field \""+comparedFieldName+"\" ("+comparedFieldType+"/"
                + NESDataTypeUtil.getNesDataType(comparedFieldType) +") to "
                +value+" ("+valueType+"/"+NESDataTypeUtil.getNesDataType(valueType)+")");
    }
}
