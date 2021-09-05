package com.github.paguos.mosaic.fed.geo;

import org.eclipse.mosaic.lib.geo.CartesianPoint;
import org.eclipse.mosaic.lib.geo.GeoPoint;
import org.eclipse.mosaic.lib.transform.GeoProjection;
import org.eclipse.mosaic.lib.transform.Wgs84Projection;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeoSquareTest {

    @BeforeClass
    public static void setup() {
        GeoPoint origin = GeoPoint.latLon(52.5047675, 13.3156317);

        if (!GeoProjection.isInitialized()) {
            GeoProjection.initialize(new Wgs84Projection(origin));
        }
    }

    @Test
    public void getBounds() {
        GeoPoint center = GeoPoint.latLon(52.5128417, 13.3213595);
        GeoSquare square = new GeoSquare(center, 10000);

        GeoPoint north = square.getNorthBound();
        assertEquals(center.getLongitude(), north.getLongitude(), 0.0);
        assertTrue(north.getLatitude() > center.getLatitude());

        GeoPoint south = square.getSouthBound();
        assertEquals(center.getLongitude(), south.getLongitude(), 0.0);
        assertTrue(south.getLatitude() < center.getLatitude());

        GeoPoint east = square.getEastBound();
        assertEquals(center.getLatitude(), east.getLatitude(), 0.0);
        assertTrue(east.getLongitude() > center.getLongitude());

        GeoPoint west = square.getWestBound();
        assertEquals(center.getLatitude(), west.getLatitude(), 0.0);
        assertTrue(west.getLongitude() < center.getLongitude());
    }

    @Test
    public void distanceToBound() {
        GeoPoint center = GeoPoint.latLon(52.5128417, 13.3213595);
        GeoSquare square = new GeoSquare(center, 10000);

        assertEquals(50, square.getDistanceToBound(), 0.0);
    }

    @Test
    public void testContains() {
        GeoPoint center = GeoPoint.latLon(52.5128417, 13.3213595);
        GeoSquare square = new GeoSquare(center, 10000);

        assertTrue(square.contains(center));

        CartesianPoint a = center.toCartesian();

        GeoPoint inside = CartesianPoint.xy(a.getX() + 20, a.getY() + 20).toGeo();
        assertTrue(square.contains(inside));

        GeoPoint outside = CartesianPoint.xy(a.getX() + 100, a.getY() + 500).toGeo();
        assertFalse(square.contains(outside));
    }

}
