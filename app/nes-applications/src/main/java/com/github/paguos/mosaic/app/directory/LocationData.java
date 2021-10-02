package com.github.paguos.mosaic.app.directory;

import org.eclipse.mosaic.lib.geo.GeoPoint;

public abstract class LocationData {

    private final String id;
    private GeoPoint location;
    private boolean enabled;

    public LocationData(String id, GeoPoint location) {
        this.id = id;
        this.location = location;
        this.enabled = false;
    }

    public String getId() {
        return id;
    }

    public GeoPoint getLocation() {
        return location;
    }

    protected void setLocation(GeoPoint location) {
        this.location = location;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
