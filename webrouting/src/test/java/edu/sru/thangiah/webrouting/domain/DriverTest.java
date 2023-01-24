package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Driver Domain Class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class DriverTest {
	private Driver driver;
	
	private Carriers carrier;

	private long id;
	
	private Contacts contact;
	
	private Vehicles vehicle;
	
	private String lisence_number;
	
	private String lisence_expir;
	
	private String lisence_class;
	/**
	 * Sets up values to be used for the driver test cases
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		
		driver = new Driver();
		carrier = new Carriers();
		carrier.setId(1L);
		driver.setCarrier(carrier);
		vehicle = new Vehicles();
		vehicle.setId(1L);
		driver.setVehicle(vehicle);
		contact = new Contacts();
		contact.setId(1L);
		driver.setContact(contact);
		id = 1L;
		driver.setId(id);
		lisence_number= "LDV-8169";
		driver.setLisence_number(lisence_number);
		lisence_expir = "2024-11-14";
		driver.setLisence_expiration(lisence_expir);
		lisence_class = "A";
		driver.setLisence_class(lisence_class);
		
	}

	/**
	 * Tests the Get Id function
	 */
	@Test
	void testGetId() {
		
		assertEquals(1L, driver.getId());
	}

	/**
	 * Tests the set Id function
	 */
	@Test
	void testSetId() {
		driver.setId(2L);
		assertNotEquals(id, driver.getId());
	}

	/**
	 * Tests the Get contact function
	 */
	@Test
	void testGetContact() {
		 assertEquals(contact,driver.getContact());
	}

	/**
	 * Tests the set contact function
	 */
	@Test
	void testSetContact() {
		driver.setContact(null);
		assertNotEquals(contact, driver.getContact());
	}

	/**
	 * Tests the Get carrier function
	 */
	@Test
	void testGetCarrier() {
		assertEquals(carrier, driver.getCarrier());
	}

	/**
	 * Tests the set carrier function
	 */
	@Test
	void testSetCarrier() {
		driver.setCarrier(null);
		assertNotEquals(carrier, driver.getCarrier());
	}

	/**
	 * Tests the Get vehicle function
	 */
	@Test
	void testGetVehicle() {
		assertEquals(vehicle, driver.getVehicle());
	}

	/**
	 * Tests the set vehicle function
	 */
	@Test
	void testSetVehicle() {
		driver.setVehicle(null);
		assertNotEquals(vehicle, driver.getVehicle());
	}

	/**
	 * Tests the Get lisencenumber function
	 */
	@Test
	void testGetLisence_number() {
		assertEquals(lisence_number, driver.getLisence_number());
	}

	/**
	 * Tests the set lisencenumber function
	 */
	@Test
	void testSetLisence_number() {
		driver.setLisence_number("811114");
		assertNotEquals(lisence_number, driver.getLisence_number());
	}

	/**
	 * Tests the get lisenceexpiration function
	 */
	@Test
	void testGetLisence_expiration() {
		assertEquals(lisence_expir, driver.getLisence_expiration());
	}

	/**
	 * Tests the set lisence expiration function
	 */
	@Test
	void testSetLisence_expiration() {
		driver.setLisence_expiration("09/20/2020");
		assertNotEquals(lisence_expir, driver.getLisence_expiration());
	}

	/**
	 * Tests the get lisence class function
	 */
	@Test
	void testGetLisence_class() {
		assertEquals(lisence_class,driver.getLisence_class());
	}

	/**
	 * Tests the set lisence class function
	 */
	@Test
	void testSetLisence_class() {
		driver.setLisence_class("O");
		assertNotEquals(lisence_class, driver.getLisence_class());
	}

	/**
	 * Tests the to string function
	 */
	@Test
	void testToString() {
		assertNotNull(driver.toString());
	}

}
