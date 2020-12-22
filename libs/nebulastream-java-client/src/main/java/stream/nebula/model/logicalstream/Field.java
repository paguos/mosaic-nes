package stream.nebula.model.logicalstream;

import stream.nebula.exceptions.UnknownDataTypeException;
import stream.nebula.utils.NESDataTypeUtil;

public class Field {
    private final String name;
    private final String type;
    private final Class javaDataType;

    public Class getJavaDataType() {
        return javaDataType;
    }

    public Field(String name, String type) throws UnknownDataTypeException {
        this.name = name;
        this.type = type;
        this.javaDataType = NESDataTypeUtil.getJavaDataType(type);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
