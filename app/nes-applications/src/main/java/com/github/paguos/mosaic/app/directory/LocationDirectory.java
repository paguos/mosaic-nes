package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LocationDirectory {

    private static VehicleLocationData SINK = null;

    private static final Map<String, RSULocationData> ROADSIDE_UNITS = new HashMap<>();

    public static void register(RSULocationData RSULocationData) {
        ROADSIDE_UNITS.put(RSULocationData.getId(), RSULocationData);
    }

    public static void register(VehicleLocationData vehicleLocationData) { SINK = vehicleLocationData; }

    public static String getSinkId() {
        return SINK.getId();
    }

    /**
     * Find out if there are road side units near a given location
     * @param location a GeoPoint of reference
     * @return a lists of all the roadside units that are in range
     */
    public static List<RSULocationData> getRoadSideUnitsInRange(GeoPoint location) {
        List<RSULocationData> roadSideUnitsInRange = new LinkedList<>();
        for (RSULocationData rsu: ROADSIDE_UNITS.values()) {
            if (rsu.getBroadcastArea().contains(location)) {
                roadSideUnitsInRange.add(rsu);
            }
        }
        return roadSideUnitsInRange;
    }

    /**
     * Update the sink location
     * @param point GeoPoint of its current location
     */
    public static void updateSinkLocation(GeoPoint point) {
        SINK.updateLocation(point);
        for (RSULocationData rsu: ROADSIDE_UNITS.values()) {
            rsu.setEnabled(SINK.getMovingRange().contains(rsu.getBroadcastArea()));
        }
    }

    /**
     * Checks if an RSU is enabled
     * @param rsuId id to identify a given RSU
     * @return true if enabled, false otherwise
     */
    public static boolean isRSUEnabled(String rsuId) {
        return ROADSIDE_UNITS.get(rsuId).isEnabled();
    }

}
