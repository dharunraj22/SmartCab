package com.smartcab.model;


public class Driver {
    
    private final String driverId;
    private String driverName;
    private Cab cab;
    private final String licenseNo;
    private double rating;
    private String contactNo;
    private DriverStatus status;
    private Location currentLocation;

    public Driver(String driverId, String driverName, Cab cab, String licenseNo,
                  double rating, String contactNo, DriverStatus status, Location currentLocation) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.cab = cab;
        this.licenseNo = licenseNo;
        this.rating = rating;
        this.contactNo = contactNo;
        this.status = status;
        this.currentLocation = currentLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Cab getCab() {
        return cab;
    }

    public void updateRating(double newRating) {
        if (newRating >= 1.0 && newRating <= 5.0) {
            this.rating = (this.rating + newRating) / 2;
        }
    }

    public String getDriverId() {
        return driverId;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", cab=" + cab +
                ", licenseNo='" + licenseNo + '\'' +
                ", rating=" + rating +
                ", contactNo='" + contactNo + '\'' +
                ", status=" + status +
                ", currentLocation=" + currentLocation +
                '}';
    }

}
