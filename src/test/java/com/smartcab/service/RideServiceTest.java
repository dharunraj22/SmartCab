package com.smartcab.service;

import com.smartcab.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class RideServiceTest {

    private DriverService driverService;
    private RideService rideService;

    @BeforeEach
    void setup() {
        driverService = new DriverService();
        rideService = new RideService(driverService);
    }

    private Driver makeDriver(String id, double lat, double lon) {
        return new Driver(
                id,
                "Driver-" + id,
                new Cab("CAB-" + id, "Model-" + id),
                "LIC-" + id,
                4.5,
                "99999",
                DriverStatus.AVAILABLE,
                new Location(lat, lon)
        );
    }

    private Customer makeCustomer(double wallet) {
        return new Customer("C1", "Cust", "9999", wallet, new Location(0, 0));
    }


    // ------------------------------------------------------------
    // REQUEST RIDE TESTS
    // ------------------------------------------------------------

    @Test
    void testRequestRideSuccess() {
        Driver d = makeDriver("D1", 1, 1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);

        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));

        assertNotNull(ride);
        assertEquals(RideStatus.REQUESTED, ride.getStatus());
        assertEquals(d.getDriverId(), ride.getDriver().getDriverId());
        assertEquals(DriverStatus.BUSY, driverService.getDriver("D1").getStatus());
    }

    @Test
    void testRequestRideInsufficientWallet() {
        Driver d = makeDriver("D1", 1, 1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(0.1); // insufficient

        assertThrows(IllegalStateException.class,
                () -> rideService.requestRide(c, new Location(0,0), new Location(10,10)));
    }

    @Test
    void testPickupEqualsDropThrows() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);

        Location same = new Location(5,5);
        assertThrows(IllegalArgumentException.class,
                () -> rideService.requestRide(c, same, same));
    }

    @Test
    void testNoAvailableDrivers() {
        Customer c = makeCustomer(100);

        assertThrows(IllegalStateException.class,
                () -> rideService.requestRide(c, new Location(0,0), new Location(1,1)));
    }

    @Test
    void testNearestDriverSelected() {
        Driver d1 = makeDriver("D1", 10,10); // far
        Driver d2 = makeDriver("D2", 1,1);   // near

        driverService.registerDriver(d1);
        driverService.registerDriver(d2);

        Customer c = makeCustomer(100);

        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(5,5));
        assertEquals("D2", ride.getDriver().getDriverId());
    }


    // ------------------------------------------------------------
    // START RIDE TESTS
    // ------------------------------------------------------------

    @Test
    void testStartRideSuccess() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);

        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));

        assertTrue(rideService.startRide(ride.getRideId()));
        assertEquals(RideStatus.ONGOING, ride.getStatus());
    }

    @Test
    void testStartRideInvalidState() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);
        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));
        rideService.startRide(ride.getRideId()); // starts OK

        // second start should fail
        assertFalse(rideService.startRide(ride.getRideId()));
    }

    @Test
    void testStartRideNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> rideService.startRide("NO-RIDE"));
    }


    // ------------------------------------------------------------
    // COMPLETE RIDE TESTS
    // ------------------------------------------------------------

    @Test
    void testCompleteRideSuccess() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);
        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));
        rideService.startRide(ride.getRideId());

        assertTrue(rideService.completeRide(ride.getRideId()));
        assertEquals(RideStatus.COMPLETED, ride.getStatus());
        assertEquals(DriverStatus.AVAILABLE, d.getStatus());
    }

    @Test
    void testCompleteRideNotOngoing() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);
        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));

        // ride is still REQUESTED
        assertFalse(rideService.completeRide(ride.getRideId()));
    }

    @Test
    void testCompleteRideNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> rideService.completeRide("bad-id"));
    }


    // ------------------------------------------------------------
    // CANCEL RIDE TESTS
    // ------------------------------------------------------------

    @Test
    void testCancelRideSuccess() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);
        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));

        assertTrue(rideService.cancelRide(ride.getRideId()));
        assertEquals(RideStatus.CANCELLED, ride.getStatus());
        assertEquals(DriverStatus.AVAILABLE, d.getStatus());
    }

    @Test
    void testCancelRideInvalidState() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c = makeCustomer(100);
        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));
        rideService.startRide(ride.getRideId()); // make ONGOING

        assertFalse(rideService.cancelRide(ride.getRideId()));
    }

    @Test
    void testCancelRideNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> rideService.cancelRide("unknown"));
    }


    // ------------------------------------------------------------
    // CONCURRENCY TEST
    // ------------------------------------------------------------

    @Test
    void testConcurrentDriverAssignment() throws InterruptedException {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);

        Customer c1 = makeCustomer(100);
        Customer c2 = makeCustomer(100);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        Future<Ride> f1 = executor.submit(() -> {
            latch.await();
            return rideService.requestRide(c1, new Location(0,0), new Location(2,2));
        });

        Future<Ride> f2 = executor.submit(() -> {
            latch.await();
            return rideService.requestRide(c2, new Location(0,0), new Location(2,2));
        });

        latch.countDown();

        Ride r1 = null;
        Ride r2 = null;

        try {
            r1 = f1.get();
        } catch (Exception ignored) {}

        try {
            r2 = f2.get();
        } catch (Exception ignored) {}

        executor.shutdown();

        // Only ONE ride should successfully get the driver
        int successCount = 0;
        if (r1 != null) successCount++;
        if (r2 != null) successCount++;

        assertEquals(1, successCount, "Exactly one ride must succeed in driver assignment");
    }


    // ------------------------------------------------------------
    // GET RIDE
    // ------------------------------------------------------------

    @Test
    void testGetRide() {
        Driver d = makeDriver("D1", 1,1);
        driverService.registerDriver(d);
        Customer c = makeCustomer(100);
        Ride ride = rideService.requestRide(c, new Location(0,0), new Location(2,2));

        Ride fetched = rideService.getRide(ride.getRideId());
        assertEquals(ride, fetched);
    }
}
