package com.smartcab.model;

public class Location {

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double[] getLocation() {
        return new double[]{latitude, longitude};
    }

    public void updateLocation(double newLatitude, double newLongitude) {
        this.latitude = newLatitude;
        this.longitude = newLongitude;
    }

    @Override
    public boolean equals(Object obj) {
        return this.latitude == obj.latitude && this.longitude == obj.longitude;
    }

    public double calculateDistance(Location other) {
        double latDiff = this.latitude - other.latitude;
        double lonDiff = this.longitude - other.longitude;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    } 

    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
