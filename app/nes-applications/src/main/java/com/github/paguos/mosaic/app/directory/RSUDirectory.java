package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

import java.util.LinkedList;
import java.util.List;

public class RSUDirectory {

    private static final List<RoadSideUnit> roadSideUnits = new LinkedList<>();

    public static void register(RoadSideUnit roadSideUnit) {
        roadSideUnits.add(roadSideUnit);
    }

    public static List<RoadSideUnit> getRoadSideUnits() {
        return roadSideUnits;
    }

    /**
     * Find out if there are road side units near a given location
     * @param location a GeoPoint of reference
     * @return a lists of all the road side units that are in range
     */
    public static List<RoadSideUnit> getRoadSideUnitsInRange(GeoPoint location) {
        List<RoadSideUnit> roadSideUnitsInRange = new LinkedList<>();
        for (RoadSideUnit rsu: roadSideUnits) {
            if (rsu.getBroadcastArea().contains(location)) {
                roadSideUnitsInRange.add(rsu);
            }
        }
        return roadSideUnitsInRange;
    }

}
