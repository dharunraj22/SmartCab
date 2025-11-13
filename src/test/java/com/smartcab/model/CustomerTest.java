package com.smartcab.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void testCustomerInitialization() {
        Location location = new Location(12.9716, 77.5946);
        Customer customer = new Customer("C001", "Rahul", "9876543210", 100.0, location);

        assertEquals("C001", customer.getCustomerId());
        assertEquals(100.0, customer.getWalletBalance(), 0.001);
        assertEquals(location, customer.getCurrentLocation());
    }

    @Test
    void testAddBalance() {
        Location location = new Location(10.0, 20.0);
        Customer customer = new Customer("C002", "Priya", "9123456789", 200.0, location);

        customer.addBalance(50.0);
        assertEquals(250.0, customer.getWalletBalance(), 0.001);
    }

    @Test
    void testAddBalanceNegativeAmount() {
        Location location = new Location(10.0, 20.0);
        Customer customer = new Customer("C003", "Arun", "9999999999", 150.0, location);

        assertThrows(IllegalArgumentException.class, () -> customer.addBalance(-10.0));
    }

    @Test
    void testDeductFare() {
        Location location = new Location(12.0, 24.0);
        Customer customer = new Customer("C004", "Sita", "8888888888", 300.0, location);

        customer.deductFare(100.0);
        assertEquals(200.0, customer.getWalletBalance(), 0.001);
    }

    @Test
    void testDeductFareInsufficientBalance() {
        Location location = new Location(11.0, 22.0);
        Customer customer = new Customer("C005", "Karan", "7777777777", 50.0, location);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customer.deductFare(100.0));
        assertEquals("Insufficient balance in wallet.", exception.getMessage());
    }

    @Test
    void testSetCurrentLocation() {
        Location initial = new Location(10.0, 10.0);
        Location updated = new Location(11.1, 11.1);
        Customer customer = new Customer("C006", "Meena", "6666666666", 500.0, initial);

        customer.setCurrentLocation(updated);
        assertEquals(updated, customer.getCurrentLocation());
    }

    @Test
    void testToString() {
        Location loc = new Location(12.0, 22.0);
        Customer customer = new Customer("C007", "Vijay", "9999999999", 120.0, loc);

        String result = customer.toString();
        assertTrue(result.contains("C007"));
        assertTrue(result.contains("Vijay"));
        assertTrue(result.contains("walletBalance=120.0"));
    }
}
