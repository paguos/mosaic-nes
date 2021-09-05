package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

import java.util.LinkedList;
import java.util.List;

public class LocationDirectory {

    private static VehicleLocationData FOCAL_VEHICLE = null;

    private static final List<RSULocationData> ROADSIDE_UNITS = new LinkedList<>();

    public static void register(RSULocationData RSULocationData) {
        ROADSIDE_UNITS.add(RSULocationData);
    }

    public static void register(VehicleLocationData vehicleLocationData) { FOCAL_VEHICLE = vehicleLocationData; }

    public static List<RSULocationData> getRoadsideUnits() {
        return ROADSIDE_UNITS;
    }

    /**
     * Find out if there are road side units near a given location
     * @param location a GeoPoint of reference
     * @return a lists of all the roadside units that are in range
     */
    public static List<RSULocationData> getRoadSideUnitsInRange(GeoPoint location) {
        List<RSULocationData> roadSideUnitsInRange = new LinkedList<>();
        for (RSULocationData rsu: ROADSIDE_UNITS) {
            if (rsu.getBroadcastArea().contains(location)) {
                roadSideUnitsInRange.add(rsu);
            }
        }
        return roadSideUnitsInRange;
    }

    public static VehicleLocationData getFocalVehicle() {
        return FOCAL_VEHICLE;
    }

}
