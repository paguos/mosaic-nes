package com.github.paguos.mosaic.fed.geo;

import org.eclipse.mosaic.lib.geo.*;
import org.eclipse.mosaic.lib.math.Vector3d;
import org.eclipse.mosaic.lib.transform.GeoProjection;

public class GeoSquare {

    private final GeoPoint center;
    private final double area;

    public GeoSquare(GeoPoint center, double area) {
        this.center = center;
        this.area = area;
    }

    public GeoPoint getNorthBound() {
        return GeoProjection.getInstance().getGeoCalculator().pointFromDirection(
                getCenter(), new Vector3d(0, 0, -getDistanceToBound()), new MutableGeoPoint()
        );
    }

    public GeoPoint getSouthBound() {
        return  GeoProjection.getInstance().getGeoCalculator().pointFromDirection(
                getCenter(), new Vector3d(0, 0, getDistanceToBound()), new MutableGeoPoint()
        );
    }

    public GeoPoint getEastBound() {
        return GeoProjection.getInstance().getGeoCalculator().pointFromDirection(
                getCenter(), new Vector3d(getDistanceToBound(), 0, 0), new MutableGeoPoint()
        );
    }

    public GeoPoint getWestBound() {
        return GeoProjection.getInstance().getGeoCalculator().pointFromDirection(
                getCenter(), new Vector3d(-getDistanceToBound(), 0, 0), new MutableGeoPoint()
        );

    }


    public GeoPoint getCenter() {
        return this.center;
    }

    public double getDistanceToBound() {
        return this.area / 4;
    }
}