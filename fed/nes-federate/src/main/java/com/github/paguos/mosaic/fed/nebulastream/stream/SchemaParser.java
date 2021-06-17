package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.common.AttributeField;
import com.github.paguos.mosaic.fed.nebulastream.common.DataType;

import java.util.Arrays;

public class SchemaParser {

    private final Schema schema;

    public SchemaParser(Schema schema) {
        this.schema = schema;
    }

    public String parseTuple(byte[] bytes) {
        int currentIndex = 0;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < schema.getFields().size(); i++) {
            AttributeField field = schema.getFields().get(i);
            DataType type = field.getDataType();

            byte[] fieldBytes = Arrays.copyOfRange(bytes, currentIndex, currentIndex + type.getByteSize());
            stringBuilder.append(type.parseString(fieldBytes));

            if (i != schema.getFields().size() - 1) {
                stringBuilder.append(",");
            }

            currentIndex += field.getDataType().getByteSize();
        }

        return stringBuilder.toString();
    }

}
