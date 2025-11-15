package com.smartcab.service;

import com.smartcab.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DriverServiceTest {

    @Test
    void testRegisterDriverSuccess() {
        DriverService service = new DriverService();
        Driver driver = new Driver("D001", "John", new Cab("C1", "Toyota"), "LIC001", 4.5, "99999", DriverStatus.AVAILABLE, new Location(10, 10));

        service.registerDriver(driver);

        assertEquals(driver, service.getDriver("D001"));
    }

    @Test
    void testRegisterDuplicateDriverThrowsException() {
        DriverService service = new DriverService();
        Driver driver1 = new Driver("D001", "Alice", new Cab("C2", "Honda"), "LIC002", 4.2, "88888", DriverStatus.AVAILABLE, new Location(5, 5));
        Driver driver2 = new Driver("D001", "Bob", new Cab("C3", "Suzuki"), "LIC003", 3.9, "77777", DriverStatus.AVAILABLE, new Location(6, 6));

        service.registerDriver(driver1);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.registerDriver(driver2)
        );
    }

    @Test
    void testUpdateDriverStatus() {
        DriverService service = new DriverService();
        Driver driver = new Driver("D002", "Mark", new Cab("C5", "Tesla"), "LIC005", 4.8, "66666", DriverStatus.AVAILABLE, new Location(2, 2));

        service.registerDriver(driver);
        service.updateDriverStatus("D002", DriverStatus.BUSY);

        assertEquals(DriverStatus.BUSY, driver.getStatus());
    }

    @Test
    void testUpdateDriverLocation() {
        DriverService service = new DriverService();
        Driver driver = new Driver("D003", "Tom", new Cab("C6", "Ford"), "LIC006", 4.6, "55555", DriverStatus.AVAILABLE, new Location(1, 1));

        service.registerDriver(driver);
        Location newLocation = new Location(20, 20);
        service.updateDriverLocation("D003", newLocation);

        assertEquals(newLocation, driver.getCurrentLocation());
    }

    @Test
    void testGetAvailableDrivers() {
        DriverService service = new DriverService();

        Driver d1 = new Driver("D004", "Amy", new Cab("C7", "BMW"), "LIC007", 4.5, "11111", DriverStatus.AVAILABLE, new Location(3, 3));
        Driver d2 = new Driver("D005", "Sam", new Cab("C8", "Audi"), "LIC008", 4.1, "22222", DriverStatus.BUSY, new Location(4, 4));

        service.registerDriver(d1);
        service.registerDriver(d2);

        List<Driver> availableDrivers = service.getAvailableDrivers();

        assertEquals(1, availableDrivers.size());
        assertEquals(d1, availableDrivers.get(0));
    }

    @Test
    void testFindNearestDriver() {
        DriverService service = new DriverService();

        Location customerLoc = new Location(0, 0);

        Driver d1 = new Driver("D006", "Ray", new Cab("C9", "Hyundai"), "LIC009", 4.3, "33333", DriverStatus.AVAILABLE, new Location(3, 4)); // dist = 5
        Driver d2 = new Driver("D007", "Max", new Cab("C10", "Kia"), "LIC010", 4.0, "44444", DriverStatus.AVAILABLE, new Location(1, 1));   // dist = ~1.41

        service.registerDriver(d1);
        service.registerDriver(d2);

        Driver nearest = service.findNearestDriver(customerLoc);

        assertEquals(d2, nearest);
    }

    @Test
    void testFindNearestDriverIgnoresDriversWithoutLocation() {
        DriverService service = new DriverService();

        Location customerLoc = new Location(0, 0);

        Driver d1 = new Driver("D008", "NullLoc", new Cab("C11", "Tata"), "LIC011", 3.5, "55555", DriverStatus.AVAILABLE, null);
        Driver d2 = new Driver("D009", "ValidLoc", new Cab("C12", "Mahindra"), "LIC012", 4.0, "66666", DriverStatus.AVAILABLE, new Location(2, 2));

        service.registerDriver(d1);
        service.registerDriver(d2);

        Driver nearest = service.findNearestDriver(customerLoc);

        assertEquals(d2, nearest);
    }

    @Test
    void testFindNearestDriverThrowsIfLocationIsNull() {
        DriverService service = new DriverService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.findNearestDriver(null)
        );
    }

    @Test
    void testMarkDriverBusy() {
        DriverService service = new DriverService();
        Driver driver = new Driver("D010", "BusyTest", new Cab("C13", "Skoda"), "LIC013", 4.9, "99999", DriverStatus.AVAILABLE, new Location(7, 7));

        service.registerDriver(driver);
        service.markDriverBusy("D010");

        assertEquals(DriverStatus.BUSY, driver.getStatus());
    }

    @Test
    void testMarkDriverFree() {
        DriverService service = new DriverService();
        Driver driver = new Driver("D011", "FreeTest", new Cab("C14", "MG"), "LIC014", 4.6, "88888", DriverStatus.BUSY, new Location(9, 9));

        service.registerDriver(driver);
        service.markDriverFree("D011");

        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
    }
}
