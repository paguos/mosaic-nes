package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.msg.SerializableDataType;
import com.github.paguos.mosaic.fed.msg.SerializableField;
import com.github.paguos.mosaic.fed.msg.SerializableSchema;
import com.github.paguos.mosaic.fed.nebulastream.common.AttributeField;
import com.github.paguos.mosaic.fed.nebulastream.common.BasicType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataTypeFactory;
import com.google.protobuf.InvalidProtocolBufferException;

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

    public static Schema parseFrom(SerializableSchema serializableSchema) throws InvalidProtocolBufferException {
        String schemaName = serializableSchema.getFields(0).getName().split("\\$")[0];
        Schema schema = new Schema(schemaName);

        for (SerializableField field: serializableSchema.getFieldsList()){
            String fieldName = field.getName().split("\\$")[1];

            byte[] fieldDetailsBytes = field.getType().getDetails().getValue().toByteArray();
            String typeName = field.getType().getType().name();

            if (typeName.equals("INTEGER")) {
                SerializableDataType.IntegerDetails details = SerializableDataType.IntegerDetails.parseFrom(fieldDetailsBytes);
                long bits = details.getBits();
                if (bits == 8) {
                    schema.addField(fieldName, BasicType.INT8);
                } else if (bits == 16) {
                    schema.addField(fieldName, BasicType.INT16);
                } else if (bits == 32) {
                    schema.addField(fieldName, BasicType.INT32);
                } else if (bits == 64) {
                    schema.addField(fieldName, BasicType.INT64);
                }
            } else if (typeName.equals("FLOAT")) {
                SerializableDataType.FloatDetails details = SerializableDataType.FloatDetails.parseFrom(fieldDetailsBytes);
                long bits = details.getBits();
                if (bits == 32) {
                    schema.addField(fieldName, BasicType.FLOAT32);
                } else if (bits == 64) {
                    schema.addField(fieldName, BasicType.FLOAT64);
                }
            } else if (typeName.equals("ARRAY")) {
                SerializableDataType.ArrayDetails details = SerializableDataType.ArrayDetails.parseFrom(fieldDetailsBytes);
                schema.addField(fieldName, DataTypeFactory.createFixedChar((int) details.getDimensions()));
            }
        }

        return schema;
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
