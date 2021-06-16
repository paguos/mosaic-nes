package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.common.AttributeField;

import java.util.ArrayList;
import java.util.List;

public class Schema {

    private final List<AttributeField> fields;

    public Schema() {
        this.fields = new ArrayList<>();
    }

    public void addField(AttributeField field) {
        fields.add(field);
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
