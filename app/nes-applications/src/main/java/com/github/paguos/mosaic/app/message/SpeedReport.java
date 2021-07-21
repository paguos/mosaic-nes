package com.github.paguos.mosaic.app.message;

import org.eclipse.mosaic.lib.geo.GeoPoint;

public class SpeedReport {

    private final long timestamp;
    private final String vehicleId;
    private final GeoPoint vehiclePosition;
    private final double vehicleSpeed;

    public SpeedReport(long timestamp, String vehicleId, GeoPoint vehiclePosition, double vehicleSpeed) {
        this.timestamp = timestamp;
        this.vehicleId = vehicleId;
        this.vehiclePosition = vehiclePosition;
        this.vehicleSpeed = vehicleSpeed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public GeoPoint getVehiclePosition() {
        return vehiclePosition;
    }

    public double getVehicleSpeed() {
        return vehicleSpeed;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("id=").append(vehicleId);
        sb.append(",timestamp=").append(timestamp);
        sb.append(",latitude=").append(vehiclePosition.getLatitude());
        sb.append(",longitude=").append(vehiclePosition.getLongitude());
        sb.append(",speed=").append(vehiclePosition);
        sb.append('}');
        return sb.toString();
    }

}
