package com.github.paguos.mosaic.fed.nebulastream.common;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeField field = (AttributeField) o;
        return name.equals(field.name) && dataType.equals(field.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataType);
    }
}
