package com.smartcab.model;

public class Customer {
    
    private final String customerId;
    private String customerName;
    private String phoneNumber;
    private double walletBalance;
    private Location currentLocation;

    public Customer(String customerId, String customerName, String phoneNumber, double walletBalance, Location currentLocation) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.walletBalance = walletBalance;
        this.currentLocation = currentLocation;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void addBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to add must be non-negative.");
        }
        walletBalance += amount;
    }

    public void deductFare(double fare) {
        if (fare <= walletBalance) {
            walletBalance -= fare;
        } else {
            throw new IllegalArgumentException("Insufficient balance in wallet.");
        }
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", walletBalance=" + walletBalance +
                ", currentLocation=" + currentLocation +
                '}';    
    }

}
