package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoCircle;
import org.eclipse.mosaic.lib.geo.GeoPoint;

public class RoadSideUnit {

    private final String id;
    private final GeoPoint location;
    private final GeoCircle broadcastArea;

    public RoadSideUnit(String id, GeoPoint location, float broadcastRadius) {
        this.id = id;
        this.location = location;
        this.broadcastArea = new GeoCircle(location, broadcastRadius);
    }

    public String getId() {
        return id;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public GeoCircle getBroadcastArea() {
        return broadcastArea;
    }

}
