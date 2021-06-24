package com.github.paguos.mosaic.fed.nebulastream.catalog;

import com.github.paguos.mosaic.fed.nebulastream.common.BasicType;
import com.github.paguos.mosaic.fed.nebulastream.common.DataTypeFactory;
import com.github.paguos.mosaic.fed.nebulastream.stream.Schema;

public class SchemaCatalog {

    public static Schema getMosaicNesSchema() {
        Schema schema = new Schema("mosaic_nes");
        schema.addField("vehicle_id", DataTypeFactory.createFixedChar(7));
        schema.addField("timestamp", BasicType.INT64);
        schema.addField("latitude", BasicType.FLOAT64);
        schema.addField("longitude", BasicType.FLOAT64);
        schema.addField("speed", BasicType.FLOAT64);
        return schema;
    }

    public static Schema getQnVSchema() {
        Schema schema = new Schema("QnV");
        schema.addField("sensor_id", DataTypeFactory.createFixedChar(8));
        schema.addField("timestamp", BasicType.UINT64);
        schema.addField("velocity", BasicType.FLOAT32);
        schema.addField("quantity", BasicType.UINT64);
        return schema;
    }




}
