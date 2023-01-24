package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Test class for the vehicles domain class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class VehiclesTest {
	private Vehicles vehicle;
    private long id;
	private String plateNumber;
	private String vinNumber;
	private String manufacturedYear;
	private VehicleTypes vehicleType;
	private Locations location;
	private Carriers carrier;
	private List<MaintenanceOrders> orders = new ArrayList<>();
	private List<Shipments> shipments = new ArrayList<>();
	private List<Driver> drivers = new ArrayList<>();
	private boolean test;
	private String isSeventeen;
	private String isEighteen;

	/**
	 * Sets up values for the vehicles test class
	 */
	@BeforeEach
	void setUp() throws Exception {
		test = false;
		vehicle = new Vehicles();
		id = 1L;
		vehicle.setId(id);
		plateNumber = "LDV 8169";
		vehicle.setPlateNumber(plateNumber);
		vinNumber = "123456789";
		vehicle.setVinNumber(vinNumber);
		manufacturedYear = "2015";
		vehicleType = new VehicleTypes();
		vehicleType.setId(id);
		vehicle.setVehicleType(vehicleType);
		vehicle.setManufacturedYear(manufacturedYear);
		location = new Locations();
		location.setId(id);
		vehicle.setLocation(location);
		carrier = new Carriers();
		carrier.setId(id);
		vehicle.setCarrier(carrier);
		orders = new ArrayList<>();
		vehicle.setOrders(orders);
		shipments = new ArrayList<>();
		vehicle.setShipments(shipments);
		drivers = new ArrayList<>();
		vehicle.setDrivers(drivers);
	}

	/**
	 * Tests the get id function
	 */
	@Test
	void testGetId() {
		assertEquals(id, vehicle.getId());
		assertNotNull(vehicle.getId());
	}

	/**
	 * Tests the set id function
	 */
	@Test
	void testSetId() {
		try {
			assertEquals(id, vehicle.getId());
			vehicle.setId(0);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get plate number function
	 */
	@Test
	void testGetPlateNumber() {
		assertEquals(plateNumber, vehicle.getPlateNumber());
		assertNotNull(vehicle.getPlateNumber());
	}

	/**
	 * Tests the set plate number function
	 */
	@Test
	void testSetPlateNumber() {
		try {
			assertEquals(plateNumber, vehicle.getPlateNumber());
			vehicle.setPlateNumber(null);
			assertEquals(null, vehicle.getPlateNumber());
			vehicle.setPlateNumber("123456789123");
			assertEquals("123456789123", vehicle.getPlateNumber());
			vehicle.setPlateNumber("1234567891234");
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
			assertNotEquals("1234567891234", vehicle.getPlateNumber());
		}
	}

	/**
	 * Tests the get vin number function
	 */
	@Test
	void testGetVinNumber() {
		assertEquals(vinNumber, vehicle.getVinNumber());
		assertNotNull(vehicle.getVinNumber());
	}

	/**
	 * Tests the set vin number function
	 */
	@Test
	void testSetVinNumber() {
		try {
			assertEquals(vinNumber, vehicle.getVinNumber());
			vehicle.setVinNumber(null);
			assertEquals(null, vehicle.getVinNumber());
			isSeventeen = RandomString.make(17);
			vehicle.setVinNumber(isSeventeen);
			assertEquals(isSeventeen, vehicle.getVinNumber());
			isEighteen = RandomString.make(18);
			vehicle.setVinNumber(isEighteen);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
			assertNotEquals(isEighteen, vehicle.getVinNumber());
		}
	}

	/**
	 * Tests the get manufactured year function
	 */
	@Test
	void testGetManufacturedYear() {
		assertEquals(manufacturedYear, vehicle.getManufacturedYear());
		assertNotNull(vehicle.getManufacturedYear());
	}

	/**
	 * Tests the set manufactured year function
	 */
	@Test
	void testSetManufacturedYear() {
		try {
			vehicle.setManufacturedYear(manufacturedYear);
			assertEquals(manufacturedYear, vehicle.getManufacturedYear());
			vehicle.setManufacturedYear(null);
			assertNull(vehicle.getManufacturedYear());
			isEighteen = RandomString.make(18);
			vehicle.setManufacturedYear(isEighteen);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
			assertNotEquals(isEighteen, vehicle.getManufacturedYear());
		}
	}

	/**
	 * Tests the get vehicle type function
	 */
	@Test
	void testGetVehicleType() {
		assertEquals(vehicleType, vehicle.getVehicleType());
		assertNotNull(vehicle.getVehicleType());
	}

	/**
	 * Tests the set vehicle type function
	 */
	@Test
	void testSetVehicleType() {
		assertEquals(vehicleType, vehicle.getVehicleType());
		vehicle.setVehicleType(null);
		assertNull(vehicle.getVehicleType());
	}

	/**
	 * Tests the get location function
	 */
	@Test
	void testGetLocation() {
		assertEquals(location, vehicle.getLocation());
		assertNotNull(vehicle.getLocation());
	}

	/**
	 * Tests the set location function
	 */
	@Test
	void testSetLocation() {
		assertEquals(location, vehicle.getLocation());
		vehicle.setLocation(null);
		assertNull(vehicle.getLocation());
	}

	/**
	 * Tests the get Carrier function
	 */
	@Test
	void testGetCarrier() {
		assertEquals(carrier, vehicle.getCarrier());
		assertNotNull(vehicle.getCarrier());
	}

	/**
	 * Tests the set carrier function
	 */
	@Test
	void testSetCarrier() {
		assertEquals(carrier, vehicle.getCarrier());
		vehicle.setCarrier(null);
		assertNull(vehicle.getCarrier());
	}

	/**
	 * Tests the get orders function
	 */
	@Test
	void testGetOrders() {
		assertEquals(orders, vehicle.getOrders());
		assertNotNull(vehicle.getOrders());
	}

	/**
	 * Tests the set orders function
	 */
	@Test
	void testSetOrders() {
		assertEquals(orders, vehicle.getOrders());
		vehicle.setOrders(null);
		assertNull(vehicle.getOrders());
	}

	/**
	 * Tests the get shipments function
	 */
	@Test
	void testGetShipments() {
		assertEquals(shipments, vehicle.getShipments());
		assertNotNull(vehicle.getShipments());
	}

	/**
	 * Tests the set shipments function
	 */
	@Test
	void testSetShipments() {
		assertEquals(shipments, vehicle.getShipments());
		vehicle.setShipments(null);
		assertNull(vehicle.getShipments());
	}

	/**
	 * Tests the get drivers function
	 */
	@Test
	void testGetDrivers() {
		assertEquals(drivers, vehicle.getDrivers());
		assertNotNull(vehicle.getDrivers());
	}

	/**
	 * Tests the set drivers function
	 */
	@Test
	void testSetDrivers() {
		assertEquals(drivers, vehicle.getDrivers());
		vehicle.setDrivers(null);
		assertNull(vehicle.getDrivers());
	}

}
