package com.smartcab.service;
import com.smartcab.model.Ride;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Comparator;
import java.util.List;

import com.smartcab.model.Customer;
import com.smartcab.model.Location;
import com.smartcab.model.Driver;
import com.smartcab.model.RideStatus;
import com.smartcab.model.DriverStatus;


public class RideService {
    
    private DriverService driverService;
    private Map<String, Ride> rides = new ConcurrentHashMap<>();

    public RideService(DriverService driverService) {
        this.driverService = driverService;
    }

    public Ride requestRide(Customer customer, Location pickup, Location drop) {
        if (customer == null || pickup == null || drop == null) {
            throw new IllegalArgumentException("Customer and locations must be provided");
        }
        if (pickup.equals(drop)) {
            throw new IllegalArgumentException("Pickup and drop locations cannot be the same");
        }

        double fare = Ride.estimateFare(pickup, drop, 5);
        if (customer.getWalletBalance() < fare) {
            throw new IllegalStateException("Insufficient balance in customer's wallet");
        }

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        availableDrivers.sort(Comparator.comparingDouble(d -> {
            Location loc = d.getCurrentLocation();
            return loc == null ? Double.MAX_VALUE : pickup.calculateDistance(loc);
        }));

        for (Driver d : availableDrivers) {
            boolean assigned = false;
            Ride ride = null;

            try {
                assigned = driverService.markDriverBusy(d.getDriverId());
                if (assigned) {
                    String rideId = UUID.randomUUID().toString();
                    ride = new Ride(rideId, customer, d, d.getCab(), pickup, drop);
                    rides.put(ride.getRideId(), ride);
                    return ride;
                }
            } catch (Exception e) {
                if (assigned) {
                    try {
                        driverService.markDriverFree(d.getDriverId());
                    } catch (Exception ex) {
                        System.err.println("Failed to free driver after ride assignment failure: " + ex.getMessage());
                    }
                }
                throw new IllegalStateException("Failed to assign driver: " + e.getMessage(), e);
            }
        }
        throw new IllegalStateException("Failed to assign any available driver");
    }

    public boolean startRide(String rideId) {
        Ride ride = rides.get(rideId);
        if(ride == null) {
            throw new IllegalArgumentException("Ride not found: " + rideId);
        }
        synchronized (ride) {
            if(ride.getStatus() != RideStatus.REQUESTED) {
                return false;
            }

            if(ride.getDriver() == null) {
                Driver driver = ride.getDriver();
                if(driver.getStatus() != DriverStatus.BUSY) {
                    System.err.println("Driver not marked busy for ride: " + rideId);
                }
            }
            ride.startRide();
            System.out.println("Ride " + rideId + " started.");
            return true;
        }
    }

    public boolean completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if(ride == null) {
            throw new IllegalArgumentException("Provided rideId not found: " + rideId);
        }
        synchronized (ride) {
            if(ride.getStatus() != RideStatus.ONGOING) {
                return false;
            }
            try {
            ride.completeRide();
            } catch (Exception e) {
                System.err.println("Failed to complete ride (payment or state): " + e.getMessage());
                throw new IllegalStateException("Failed to complete ride due to payment/state issue.", e);
            }

            try {
                driverService.markDriverFree(ride.getDriver().getDriverId());
            } catch (Exception e) {
                System.err.println("Warning: failed to free driver " + ride.getDriver().getDriverId()
                    + " after completing ride " + rideId + ": " + e.getMessage());
            }

            System.out.println("Ride " + rideId + " completed.");
            return true;
        }
    }

    public boolean cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if(ride == null) {
            throw new IllegalArgumentException("Ride not found: " + rideId);    
        }
        synchronized (ride) {
            try {
                driverService.markDriverFree(ride.getDriver().getDriverId());
                ride.setStatus(RideStatus.CANCELLED);
                System.out.println("Ride " + rideId + " cancelled.");
                return true;
            } catch (Exception e) {
                System.err.println("Failed to cancel ride " + rideId + ": " + e.getMessage());
                return false;
            }
        }
        
    }

    public Ride getRide(String RideId) {
        return rides.get(RideId);
    }

    public void listRides() {
        for(Ride ride : rides.values()) {
            System.out.println(ride);
        }
    }

}