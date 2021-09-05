package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

public class VehicleLocationData {

    private final String id;
    private final GeoPoint location;

    public VehicleLocationData(String id, GeoPoint location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public GeoPoint getLocation() {
        return location;
    }
}
