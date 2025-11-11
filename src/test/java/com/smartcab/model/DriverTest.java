package com.smartcab.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Driver class.
 */
public class DriverTest {

    @Test
    void testDriverInitialization() {
        Cab cab = new Cab("CAB001", "Honda City");
        Location loc = new Location(12.9716, 77.5946);
        Driver driver = new Driver(
                "D001", "Arun", cab, "LIC1234",
                4.8, "9876543210", DriverStatus.ACTIVE, loc
        );

        assertEquals("D001", driver.toString().contains("D001") ? "D001" : null);
        assertEquals(cab, driver.getCab());
        assertEquals(DriverStatus.ACTIVE, driver.getStatus());
        assertEquals(loc, driver.getCurrentLocation());
    }

    @Test
    void testSetStatus() {
        Cab cab = new Cab("CAB002", "Maruti Swift");
        Location loc = new Location(10.0, 20.0);
        Driver driver = new Driver("D002", "Karthik", cab, "LIC6789", 4.5, "9123456789", DriverStatus.ACTIVE, loc);

        driver.setStatus(DriverStatus.OFFLINE);
        assertEquals(DriverStatus.OFFLINE, driver.getStatus());
    }

    @Test
    void testSetCurrentLocation() {
        Cab cab = new Cab("CAB003", "Tata Indica");
        Location initialLoc = new Location(10.0, 20.0);
        Location newLoc = new Location(13.0, 25.0);
        Driver driver = new Driver("D003", "Vijay", cab, "LIC4321", 4.7, "9876512345", DriverStatus.ACTIVE, initialLoc);

        driver.setCurrentLocation(newLoc);
        assertEquals(newLoc, driver.getCurrentLocation());
    }

    @Test
    void testToString() {
        Cab cab = new Cab("CAB004", "Toyota Etios");
        Location loc = new Location(11.0, 22.0);
        Driver driver = new Driver("D004", "Suresh", cab, "LIC9999", 4.2, "9999999999", DriverStatus.INACTIVE, loc);

        String str = driver.toString();
        assertTrue(str.contains("driverId='D004'"));
        assertTrue(str.contains("status=INACTIVE"));
        assertTrue(str.contains("Toyota Etios"));
        assertTrue(str.contains("Location"));
    }
}
