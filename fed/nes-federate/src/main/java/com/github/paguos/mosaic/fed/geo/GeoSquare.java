package com.github.paguos.mosaic.fed.geo;

import org.eclipse.mosaic.lib.geo.*;
import org.eclipse.mosaic.lib.math.MathUtils;
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

    public double getArea() {
        return area;
    }

    public GeoPoint getCenter() {
        return this.center;
    }

    public double getDistanceToBound() {
        return Math.sqrt(this.area) / 2.0;
    }

    public boolean contains(GeoPoint point) {
        if ( point.getLongitude() < getWestBound().getLongitude()) { return false; }
        if ( point.getLongitude() > getEastBound().getLongitude()) { return false; }
        if ( point.getLatitude() > getNorthBound().getLatitude()) { return false; }
        if ( point.getLatitude() < getSouthBound().getLatitude()) { return false; }

        return true;
    }

    public boolean contains(GeoCircle circle) {
        CartesianPoint circleCenter = circle.getCenter().toCartesian();
        double x = MathUtils.clamp(circleCenter.getX(), getWestBound().toCartesian().getX(), getEastBound().toCartesian().getX());
        double y = MathUtils.clamp(circleCenter.getY(), getSouthBound().toCartesian().getY(), getNorthBound().toCartesian().getY());

        CartesianPoint ref = CartesianPoint.xy(x, y);
        return (circleCenter.distanceTo(ref) <= circle.getRadius());
    }
}
