package com.smartcab.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Location class.
 */
public class LocationTest {

    @Test
    void testLocationInitialization() {
        Location loc = new Location(12.9716, 77.5946);
        double[] coordinates = loc.getLocation();

        assertEquals(12.9716, coordinates[0], 0.0001);
        assertEquals(77.5946, coordinates[1], 0.0001);
    }

    @Test
    void testUpdateLocation() {
        Location loc = new Location(10.0, 20.0);
        loc.updateLocation(11.0, 22.0);

        double[] coordinates = loc.getLocation();
        assertEquals(11.0, coordinates[0], 0.0001);
        assertEquals(22.0, coordinates[1], 0.0001);
    }

    @Test
    void testCalculateDistance() {
        Location loc1 = new Location(0, 0);
        Location loc2 = new Location(3, 4);
        double distance = loc1.calculateDistance(loc2);
        assertEquals(5.0, distance, 0.0001, "Distance between (0,0) and (3,4) should be 5");
    }

    @Test
    void testToString() {
        Location loc = new Location(10.12345, 20.54321);
        String result = loc.toString();

        assertTrue(result.contains("latitude=10.12345"));
        assertTrue(result.contains("longitude=20.54321"));
    }
}
