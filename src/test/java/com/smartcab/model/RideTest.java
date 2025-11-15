package com.smartcab.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RideTest {

    @Test
    void testRideInitializationAndFareCalculation() {
        // distance between (0,0) and (3,4) = 5.0
        Location pickup = new Location(0.0, 0.0);
        Location drop = new Location(3.0, 4.0);

        Customer customer = new Customer("C100", "Test", "9999999999", 100.0, pickup);
        Cab cab = new Cab("CAB100", "TestModel");

        Ride ride = new Ride("R100", customer, cab, pickup, drop);

        assertEquals(5.0, ride.getDistance(), 0.0001);
        // constructor uses ratePerKm = 5.0 => fare = 5 * 5 = 25 (and capped at 50, so 25)
        assertEquals(25.0, ride.getFare(), 0.001);
        assertEquals(RideStatus.REQUESTED, ride.getStatus());
    }

    @Test
    void testStartAndCompleteRideDeductsFare() {
        Location pickup = new Location(0.0, 0.0);
        Location drop = new Location(3.0, 4.0); // distance 5
        Customer customer = new Customer("C101", "Pay", "8888888888", 100.0, pickup);
        Cab cab = new Cab("CAB101", "ModelX");

        Ride ride = new Ride("R101", customer, cab, pickup, drop);

        // start
        ride.startRide();
        assertEquals(RideStatus.ONGOING, ride.getStatus());

        // complete
        double expectedFare = ride.getFare(); // 25.0 as above
        ride.completeRide();
        assertEquals(RideStatus.COMPLETED, ride.getStatus());

        // customer wallet reduced by fare
        assertEquals(100.0 - expectedFare, customer.getWalletBalance(), 0.001);
    }

    @Test
    void testInvalidStateTransitions() {
        Location pickup = new Location(0.0, 0.0);
        Location drop = new Location(3.0, 4.0);
        Customer customer = new Customer("C102", "State", "7777777777", 100.0, pickup);
        Cab cab = new Cab("CAB102", "ModelY");

        Ride ride = new Ride("R102", customer, cab, pickup, drop);

        // cannot complete before start
        assertThrows(IllegalStateException.class, ride::completeRide);

        // cannot start twice
        ride.startRide();
        assertThrows(IllegalStateException.class, ride::startRide);
    }

    @Test
    void testFareCapIsApplied() {
        // create locations with distance such that distance * ratePerKm > 50
        // distance = 11.0, ratePerKm = 5.0 -> 55.0 -> capped to 50.0
        Location pickup = new Location(0.0, 0.0);
        Location drop = new Location(11.0, 0.0);

        Customer customer = new Customer("C103", "CapTest", "6666666666", 200.0, pickup);
        Cab cab = new Cab("CAB103", "ModelZ");

        Ride ride = new Ride("R103", customer, cab, pickup, drop);

        assertTrue(ride.getDistance() > 10.9 && ride.getDistance() < 11.1);
        assertEquals(50.0, ride.getFare(), 0.001, "Fare should be capped at 50.0");
    }
}
