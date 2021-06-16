package com.github.paguos.mosaic.fed.nebulastream;

import com.github.paguos.mosaic.fed.nebulastream.common.AttributeField;
import com.github.paguos.mosaic.fed.nebulastream.common.DataTypeFactory;
import com.github.paguos.mosaic.fed.nebulastream.stream.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NesSchemaTest {

    @Test
    public void createQnVSchema() {
        Schema schema = new Schema();
        schema.addField(new AttributeField("sensor_id", DataTypeFactory.createFixedChar(8)));
        schema.addField(new AttributeField("timestamp", DataTypeFactory.createInteger()));
        schema.addField(new AttributeField("velocity", DataTypeFactory.createFloat()));
        schema.addField(new AttributeField("quantity", DataTypeFactory.createInteger()));

        String expectedSchema = getQnvSchema();
        assertEquals(expectedSchema, schema.toCpp());
    }

    private String getQnvSchema(){
        String expectedSchema = "Schema::create()->addField(\"sensor_id\", ";
        expectedSchema += "DataTypeFactory::createFixedChar(8))->addField(createField(\"timestamp\", ";
        expectedSchema += "UINT64))->addField(createField(\"velocity\", FLOAT32))->addField(createField(\"quantity\", UINT64))";
        return expectedSchema;
    }
}
