package com.github.paguos.mosaic.app.directory;

import com.github.paguos.mosaic.fed.geo.GeoSquare;
import org.eclipse.mosaic.lib.geo.GeoPoint;

public class SinkLocationData extends LocationData {

    private final long rangeArea;
    private GeoSquare movingRange;

    public SinkLocationData(String id, GeoPoint location, long rangeArea) {
        super(id, location);
        this.rangeArea = rangeArea;
        this.movingRange = new GeoSquare(location, rangeArea);
        setEnabled(true);
    }

    public GeoSquare getMovingRange() {
        return movingRange;
    }

    public void updateLocation(GeoPoint location) {
        this.movingRange = new GeoSquare(location, rangeArea);
        setLocation(location);
    }
}
