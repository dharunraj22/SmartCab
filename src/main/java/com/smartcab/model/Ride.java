package com.smartcab.model;

public class Ride {
    private final String rideId;
    private final Customer customer;
    private final Cab cab;
    private final Location pickupLocation;
    private final Location dropoffLocation;
    private double fare;
    private double estimatedTime;
    private RideStatus status;

    public Ride(String rideId, Customer customer, Cab cab, Location pickupLocation, Location dropoffLocation) {
        this.rideId = rideId;
        this.customer = customer;
        this.cab = cab;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = RideStatus.REQUESTED;
        calculateFare(5.0);
        calculateTime();
    }

    public String getRideId() {
        return rideId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cab getCab() {
        return cab;
    }

    public double getDistance() {
        double distance = pickupLocation.calculateDistance(dropoffLocation);
        return distance;
    }

    public void startRide() {
        if (status != RideStatus.REQUESTED) {
            throw new IllegalStateException("Ride cannot be started from current state: " + status);
        }
        status = RideStatus.ONGOING;
    }

    public void completeRide() {
        if (status != RideStatus.ONGOING) {
            throw new IllegalStateException("Ride must be ongoing to complete.");
        }
        status = RideStatus.COMPLETED;
        customer.deductFare(fare);
    }

    public RideStatus getStatus() {
        return status;
    }

    public double getFare() {
        return fare;
    }

    public void calculateFare(double ratePerKm) {
        double distance = getDistance();
        this.fare = Math.min(distance * ratePerKm, 50.0); // Cap fare at 50.0
    }

    public void calculateTime() {
        double distance = getDistance();
        estimatedTime =  distance / 40 * 60; // Assuming average speed of 40 km/h, return time in minutes
    }

    public String toString() {
        return "Ride{" +
                "rideId='" + rideId + '\'' +
                ", customer=" + customer +
                ", cab=" + cab +
                ", pickupLocation=" + pickupLocation +
                ", dropoffLocation=" + dropoffLocation +
                ", fare=" + fare +
                ", status=" + status +
                '}';
    }

}
