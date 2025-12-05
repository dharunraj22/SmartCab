package com.smartcab.service;
import com.smartcab.model.Ride;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.smartcab.model.Customer;
import com.smartcab.model.Location;
import com.smartcab.model.Driver;
import com.smartcab.model.DriverStatus;


public class RideService {
    
    private DriverService driverService;
    private Map<String, Ride> rides = new HashMap<>();

    public RideService(DriverService driverService) {
        this.driverService = driverService;
    }

    public Ride requestRide(Customer customer, Location pickup, Location drop) {
        if( customer == null || pickup == null || drop == null) {
            throw new IllegalArgumentException("Customer and locations must be provided");
        }
        if(pickup.equals(drop)) {
            throw new IllegalArgumentException("Pickup and drop locations cannot be the same");
        }

        Driver driver = driverService.findNearestDriver(pickup);
        if(driver == null || driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new IllegalStateException("No available drivers at the moment");
        }

        String rideId = UUID.randomUUID().toString();
        Ride ride = new Ride(rideId, customer, driver.getCab(), pickup, drop);
        double fare = ride.getFare();
        if(customer.getWalletBalance() < fare) {
            throw new IllegalStateException("Insufficient balance in customer's wallet");
        }

        boolean available = driverService.markDriverBusy(driver.getDriverId());
        if(!available) {
            throw new IllegalStateException("Driver is no longer unavailable");
        }

        rides.put(ride.getRideId(), ride);
        return ride;
    }

}
