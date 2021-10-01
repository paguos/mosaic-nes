package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LocationDirectory {

    private static SinkLocationData sink = null;

    private static final Map<String, RSULocationData> roadside_units = new HashMap<>();

    private static final Map<String, VehicleLocationData> vehicles = new HashMap<>();

    public static void register(RSULocationData RSULocationData) {
        roadside_units.put(RSULocationData.getId(), RSULocationData);
    }

    public static void registerSink(SinkLocationData sinkLocationData) { sink = sinkLocationData; }

    public static void registerVehicle(VehicleLocationData vehicleLocationData) {
        vehicles.put(vehicleLocationData.getId(), vehicleLocationData);
    }

    public static String getSinkId() {
        return sink.getId();
    }

    /**
     * Find out if there are road side units near a given location
     * @param location a GeoPoint of reference
     * @return a lists of all the roadside units that are in range
     */
    public static List<RSULocationData> getRoadSideUnitsInRange(GeoPoint location) {
        List<RSULocationData> roadSideUnitsInRange = new LinkedList<>();
        for (RSULocationData rsu: roadside_units.values()) {
            if (rsu.getBroadcastArea().contains(location)) {
                roadSideUnitsInRange.add(rsu);
            }
        }
        return roadSideUnitsInRange;
    }

    /**
     * Checks if a given location is inside the moving range
     * @param location a GeoPoint of reference
     * @return true if the location is inside the moving sink range
     */
    public static boolean isLocationInRange(GeoPoint location) {
        if (sink == null) {
            return false;
        }

        return sink.getMovingRange().contains(location);
    }

    /**
     * Update the sink location
     * @param point GeoPoint of its current location
     */
    public static void updateSinkLocation(GeoPoint point) {
        sink.updateLocation(point);
        for (RSULocationData rsu: roadside_units.values()) {
            rsu.setEnabled(sink.getMovingRange().contains(rsu.getBroadcastArea()));
        }

        for(VehicleLocationData vehicle: vehicles.values()) {
            vehicle.setEnabled(sink.getMovingRange().contains(vehicle.getLocation()));
        }
    }

    public static void updateVehicleLocation(String id, GeoPoint point) {
        vehicles.get(id).setLocation(point);
    }

    /**
     * Checks if an RSU is enabled
     * @param rsuId id to identify a given RSU
     * @return true if enabled, false otherwise
     */
    public static boolean isRSUEnabled(String rsuId) {
        return roadside_units.get(rsuId).isEnabled();
    }

    public static boolean isVehicleEnabled(String vehicleId) {
        return vehicles.get(vehicleId).isEnabled();
    }

}
