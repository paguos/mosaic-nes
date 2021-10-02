package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoCircle;
import org.eclipse.mosaic.lib.geo.GeoPoint;

public class RSULocationData extends LocationData{

    private final GeoCircle broadcastArea;

    public RSULocationData(String id, GeoPoint location, float broadcastRadius) {
        super(id, location);
        this.broadcastArea = new GeoCircle(location, broadcastRadius);
    }

    public GeoCircle getBroadcastArea() {
        return broadcastArea;
    }

}
