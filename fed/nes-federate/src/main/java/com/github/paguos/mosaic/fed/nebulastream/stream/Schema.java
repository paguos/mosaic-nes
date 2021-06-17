package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.common.AttributeField;
import com.github.paguos.mosaic.fed.nebulastream.common.BasicType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class Schema {

    private final String name;
    private final List<AttributeField> fields;

    public Schema(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addField (String name, BasicType type) {
        fields.add(new AttributeField(name, DataTypeFactory.createType(type)));
    }

    public void addField(String name, DataType type) {
        fields.add(new AttributeField(name, type));
    }

    public int getByteSize(){
        int length = 0;
        for (AttributeField f: fields) {
            length+= f.getDataType().getByteSize();
        }

        return length;
    }

    public List<AttributeField> getFields() {
        return fields;
    }

    public String toCpp() {
        StringBuilder builder = new StringBuilder();
        builder.append("Schema::create()");

        for (AttributeField field:
             this.fields) {
            builder.append(String.format("->addField(%s)", field.toCpp()));
        }


        return builder.toString();
    }


}
