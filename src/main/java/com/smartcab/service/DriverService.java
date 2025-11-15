package com.smartcab.service;

import com.smartcab.model.Driver;
import com.smartcab.model.DriverStatus;
import com.smartcab.model.Location;

import java.util.*;

public class DriverService {

    private Map<String, Driver> drivers = new HashMap<>();

    public void registerDriver(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        if (drivers.containsKey(driver.getDriverId())) {
            throw new IllegalArgumentException("Driver already registered: " + driver.getDriverId());
        }
        drivers.put(driver.getDriverId(), driver);
    }

    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }

    public void updateDriverStatus(String driverId, DriverStatus status) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.setStatus(status);
        }
    }

    public void updateDriverLocation(String driverId, Location location) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.setCurrentLocation(location);
        }
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers.values()) {
            if (driver.getStatus() == DriverStatus.AVAILABLE) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    public Driver findNearestDriver(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        List<Driver> availableDrivers = getAvailableDrivers();
        Driver nearestDriver = null;
        double minDist = Double.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            Location driverLocation = driver.getCurrentLocation();
            if (driverLocation == null) continue;

            double currDist = location.calculateDistance(driverLocation);
            if (currDist < minDist) {
                minDist = currDist;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    public void markDriverBusy(String driverId) {
        updateDriverStatus(driverId, DriverStatus.BUSY);
    }

    public void markDriverFree(String driverId) {
        updateDriverStatus(driverId, DriverStatus.AVAILABLE);
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers.values());
    }
}
