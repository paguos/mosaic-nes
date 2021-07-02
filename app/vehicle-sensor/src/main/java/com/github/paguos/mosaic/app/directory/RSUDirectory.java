package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

import java.util.LinkedList;
import java.util.List;

public class RSUDirectory {

    private static final List<GeoPoint> roadSideUnitsLocations = new LinkedList<>();

    public static void addLocation(GeoPoint location) {
        roadSideUnitsLocations.add(location);
    }

    public static List<GeoPoint> getLocations() {
        return roadSideUnitsLocations;
    }

}
