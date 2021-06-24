package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.catalog.SchemaCatalog;
import com.github.paguos.mosaic.fed.nebulastream.msg.proto.SerializableSchema;
import com.github.paguos.mosaic.fed.nebulastream.common.AttributeField;
import com.github.paguos.mosaic.fed.nebulastream.common.BasicType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataTypeFactory;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NesSchemaTest {

    @Test
    public void createQnVSchema() {
        Schema schema = SchemaCatalog.getQnVSchema();
        assertEquals(28, schema.getByteSize());

        String expectedSchema = getQnvSchema();
        assertEquals(expectedSchema, schema.toCpp());
    }

    private String getQnvSchema(){
        String expectedSchema = "Schema::create()->addField(\"sensor_id\", ";
        expectedSchema += "DataTypeFactory::createFixedChar(8))->addField(createField(\"timestamp\", ";
        expectedSchema += "UINT64))->addField(createField(\"velocity\", FLOAT32))->addField(createField(\"quantity\", UINT64))";
        return expectedSchema;
    }

    @Test
    public void parseFromSerializable() throws InvalidProtocolBufferException {
        byte[] schemaBytes = SchemaFixtures.getSchemaBytes();
        SerializableSchema serializableSchema = SerializableSchema.parseFrom(schemaBytes);

        Schema schema = Schema.parseFrom(serializableSchema);

        assertEquals("mosaic_nes", schema.getName());
        assertEquals(5, schema.getFields().size());

        AttributeField expectedVehicleIdField = new AttributeField("vehicle_id", DataTypeFactory.createFixedChar(7));
        assertEquals(expectedVehicleIdField, schema.getFields().get(0));

        AttributeField expectedTimestampField = new AttributeField("timestamp", DataTypeFactory.createType(BasicType.INT64));
        assertEquals(expectedTimestampField, schema.getFields().get(1));

        AttributeField expectedLatitudeField = new AttributeField("latitude", DataTypeFactory.createType(BasicType.FLOAT64));
        assertEquals(expectedLatitudeField, schema.getFields().get(2));

        AttributeField expectedLongitudeField = new AttributeField("longitude", DataTypeFactory.createType(BasicType.FLOAT64));
        assertEquals(expectedLongitudeField, schema.getFields().get(3));

        AttributeField expectedSpeedField = new AttributeField("speed", DataTypeFactory.createType(BasicType.FLOAT64));
        assertEquals(expectedSpeedField, schema.getFields().get(4));
    }
}
