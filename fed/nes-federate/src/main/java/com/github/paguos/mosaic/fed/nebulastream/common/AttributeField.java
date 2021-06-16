package com.github.paguos.mosaic.fed.nebulastream.common;

public class AttributeField {

    private final String name;
    private final DataType dataType;

    public AttributeField(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String toCpp(){
        if (dataType.isFixedChar()) {
            return String.format("\"%s\", %s", this.name, dataType.toCpp());
        } else {
            return String.format("createField(\"%s\", %s)", this.name, dataType.toCpp());
        }
    }

    @Override
    public String toString() {
        return "AttributeField{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                '}';
    }

}
