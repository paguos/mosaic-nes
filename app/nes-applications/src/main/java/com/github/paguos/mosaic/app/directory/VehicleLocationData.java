package com.github.paguos.mosaic.app.directory;

import com.github.paguos.mosaic.fed.geo.GeoSquare;
import org.eclipse.mosaic.lib.geo.GeoPoint;

public class VehicleLocationData {

    private final String id;
    private final long rangeArea;
    private GeoPoint location;
    private GeoSquare movingRange;

    public VehicleLocationData(String id, GeoPoint location, long rangeArea) {
        this.id = id;
        this.location = location;
        this.rangeArea = rangeArea;
        this.movingRange = new GeoSquare(location, rangeArea);
    }

    public String getId() {
        return id;
    }

    public long getRangeArea() {
        return rangeArea;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public GeoSquare getMovingRange() {
        return movingRange;
    }

    public void updateLocation(GeoPoint location) {
        this.movingRange = new GeoSquare(location, rangeArea);
        this.location = location;
    }
}
