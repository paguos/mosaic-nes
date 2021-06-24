package com.github.paguos.mosaic.fed.nebulastream.stream;

import com.github.paguos.mosaic.fed.nebulastream.catalog.SchemaCatalog;
import com.github.paguos.mosaic.fed.nebulastream.common.BasicType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataTypeFactory;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class NesTupleParserTest {

    @Test
    public void testParseByte(){
        Schema schema = new Schema("INT8");
        schema.addField("test", BasicType.INT8);

        byte[] bytes = new byte[]{120};

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("120", tupleParser.parseToString(bytes));
    }

    @Test
    public void testParseShort(){
        Schema schema = new Schema("INT16");
        schema.addField("test", BasicType.INT16);

        byte[] shortBuffer = {-126, 0};

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("130", tupleParser.parseToString(shortBuffer));
    }

    @Test
    public void testParseInt(){
        Schema schema = new Schema("INT32");
        schema.addField("test", BasicType.INT32);

        byte[] integerBuffer = {-24, -128, 0, 0};

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("33000", tupleParser.parseToString(integerBuffer));
    }

    @Test
    public void testParseLong(){
        Schema schema = new Schema("INT64");
        schema.addField("test", BasicType.INT64);

        byte[] longBuffer = {0, 55, 23, -119, 0, 0, 0, 0};

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("2300000000", tupleParser.parseToString(longBuffer));
    }

    @Test
    public void testParseFloat(){
        Schema schema = new Schema("FLOAT32");
        schema.addField("test", BasicType.FLOAT32);

        byte [] floatBuffer = {106, 126, 0, 66};

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("32.12345", tupleParser.parseToString(floatBuffer));
    }

    @Test
    public void testParseDouble(){
        Schema schema = new Schema("FLOAT64");
        schema.addField("test", BasicType.FLOAT64);

        byte[] doubleBuffer = {-111, 35, 33, -70, 53, 63, 36, 64};

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("10.12345678", tupleParser.parseToString(doubleBuffer));
    }

    @Test
    public void testParseString(){
        Schema schema = new Schema("FIXED_CHARSET_7");
        schema.addField("test", DataTypeFactory.createFixedChar(7));

        String s = "veh_100";

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("veh_100", tupleParser.parseToString(s.getBytes()));
    }

    @Test
    public void testParseShorterString(){
        Schema schema = new Schema("FIXED_CHARSET_7");
        schema.addField("test", DataTypeFactory.createFixedChar(7));

        String s = "veh_1";

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("veh_1", tupleParser.parseToString(s.getBytes()));
    }

    @Test
    public void testParseCompleteSchema(){
        Schema schema = new Schema("FIXED_CHARSET_7");
        schema.addField("test", DataTypeFactory.createFixedChar(7));
        schema.addField("test", BasicType.INT32);
        schema.addField("test", BasicType.INT64);
        schema.addField("test", BasicType.FLOAT64);

        byte[] tupleBytes = new byte[schema.getByteSize()];

        String s = "veh_100";
        copy(s.getBytes(), tupleBytes, 0);

        byte[] integerBuffer = {16, 39, 0, 0};
        copy(integerBuffer, tupleBytes, 7);

        byte[] longBuffer = {0, 109, 124, 77, 0, 0, 0, 0};
        copy(longBuffer, tupleBytes, 11);

        byte[] doubleBuffer = {-20, -34, -118, -60, 4, -91, 42, 64};
        copy(doubleBuffer, tupleBytes, 19);

        TupleParser tupleParser = new TupleParser(schema);
        assertEquals("veh_100,10000,1300000000,13.322302", tupleParser.parseToString(tupleBytes));
    }

    @Test
    public void testParseMosaicSchema(){
        Schema mosaicSchema = SchemaCatalog.getMosaicNesSchema();

        byte[] tupleBytes = new byte[mosaicSchema.getByteSize()];

        String s = "veh_71";
        copy(s.getBytes(), tupleBytes, 0);

        byte[] timestamp = {0, 72, 47, 92, 49, 0, 0, 0};
        copy(timestamp, tupleBytes, 7);

        byte[] latitude = {-108, -65, 123, 71, -115, 65, 74, 64};
        copy(latitude, tupleBytes, 15);

        byte[] longitude = {-2, -100, -126, -4, 108, -92, 42, 64};
        copy(longitude, tupleBytes, 23);

        byte[] speed = {112, -105, -3, -70, -45, -75, 34, 64};
        copy(speed, tupleBytes, 31);

        TupleParser tupleParser = new TupleParser(mosaicSchema);
        assertEquals("veh_71,212000000000,52.512124,13.321144,9.355131", tupleParser.parseToString(tupleBytes));
    }

    private void copy(byte[] source, byte[] destination, int start) {
        int j = 0;
        for (int i = start; i < start + source.length; i++) {
            destination[i] = source[j++];
        }
    }



}
