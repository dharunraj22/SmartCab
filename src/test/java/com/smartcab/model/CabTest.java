package com.smartcab.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CabTest {
    
    @Test
    void testCabCreation() {
        Cab cab = new Cab("ABC123", "Toyota Prius");
        assertEquals("ABC123", cab.getCabNo());
        assertEquals("Toyota Prius", cab.getCabModel());
        assertTrue(cab.isAvailable(), "Cabs should be available by default");
    }

    @Test
    void testSetAvailability() {
        Cab cab = new Cab("XYZ789", "Honda Civic");
        cab.setAvailable(false);
        assertFalse(cab.isAvailable(), "Cab should not be available after setting to false");
        cab.setAvailable(true);
        assertTrue(cab.isAvailable(), "Cab should be available after setting to true");
    }

    @Test
    void testToString() {
        Cab cab = new Cab("LMN456", "Ford Focus");
        String expectedString = "Cab{cabNo='LMN456', cabModel='Ford Focus', isAvailable=true}";
        assertEquals(expectedString, cab.toString(), "toString() output mismatch");
    }
}
