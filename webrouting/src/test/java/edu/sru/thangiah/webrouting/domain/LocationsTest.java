package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test Class for testing the locations Domain class
 * @author Josh
 *
 */
class LocationsTest {

	private String name;
	private Locations location;
	private String streetAdd1;
	private String streetAdd2;
	private String city;
	private String state;
	private String zip;
	private Long id;
	private String latitude;
	private String longitude;
	private String locationType;
	private List<Vehicles> vehicles;
	private Carriers carrier;
	/**
	 * Sets up values for each test to be used
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		location = new Locations();
		carrier = new Carriers();
		id = 1L;
		location.setId(id);
		city = "shippenville";
		location.setCity(city);
		streetAdd1 = "800 Greencrest Dr";
		location.setStreetAddress1(streetAdd1);
		streetAdd2 = "Apt #20";
		location.setStreetAddress2(streetAdd2);
		state = "PA";
		location.setState(state);
		zip = "16254";
		location.setZip(zip);
		latitude = "41.1911748";
		location.setLatitude(latitude);
		longitude = "-79.4164626";
		location.setLongitude(longitude);
		name = "The Office";
		location.setName(name);
		locationType = "Office";
		location.setLocationType(locationType);
		vehicles = new ArrayList<>();
		carrier.setId(id);
		location.setCarrier(carrier);
	}

	/**
	 * Test for get ID
	 */
	@Test
	void testGetId() {
		assertEquals(id, location.getId());
		assertNotNull(location.getId());
	}

	/**
	 * Test for setting ID
	 */
	@Test
	void testSetId() {
		location.setId(2L);
		assertNotEquals(id, location.getId());
		assertNotNull(location.getId());
	}

	/**
	 * Test for get name
	 */
	@Test
	void testGetName() {
		assertEquals(name, location.getName());
	}

	/**
	 * Test for set Name
	 */
	@Test
	void testSetName() {
		location.setName("Office");
		assertNotEquals(name, location.getName());
		assertNotNull(location.getName());
	}

	/**
	 * Test for the Get StreetAddress1 function
	 */
	@Test
	void testGetStreetAddress1() {
		assertEquals(streetAdd1, location.getStreetAddress1());
	}
	
	/**
	 * Test for the Set StreetAddress1 function
	 */
	@Test
	void testSetStreetAddress1() {
		location.setStreetAddress1("d");
		assertNotEquals(streetAdd1, location.getStreetAddress1());
	}
	
	/**
	 * Test for the Get StreetAddress2 function
	 */
	@Test
	void testGetStreetAddress2() {
		assertEquals(streetAdd2, location.getStreetAddress2());
	}
	
	/**
	 * Test for the Set StreetAddress2 function
	 */
	@Test
	void testSetStreetAddress2() {
		location.setStreetAddress2("c");
		assertNotEquals(streetAdd2, location.getStreetAddress2());
	}
	
	/**
	 * Test for the Get City function
	 */
	@Test
	void testGetCity() {
		assertEquals(city, location.getCity());
	}
	
	/**
	 * Test for the Set City function
	 */
	@Test
	void testSetCity() {
		location.setCity("b");
		assertNotEquals(city,location.getCity());
	}
	
	/**
	 * Test for the Get State function
	 */
	@Test
	void testGetState() {
		assertEquals(state, location.getState());
	}
	
	/**
	 * Test for the Set State function
	 */
	@Test
	void testSetState() {
		location.setState("a");
		assertNotEquals(state,location.getState());
	}
	
	/**
	 * Test for the Get ZIP function
	 */
	@Test
	void testGetZip() {
		assertEquals(zip, location.getZip());
	}
	
	/**
	 * Test for the Set ZIP function
	 */
	@Test
	void testSetZip() {
		location.setZip("1234");
		assertNotEquals(zip, location.getZip());
	}
	
	/**
	 * Tests the get latitude function
	 */
	@Test
	void testGetLatitude() {
		assertEquals(latitude, location.getLatitude());
		assertNotNull(location.getLatitude());
	}

	/**
	 * Tests the set latitude function
	 */
	@Test
	void testSetLatitude() {
		assertEquals(latitude, location.getLatitude());
	}

	/**
	 * Tests the get longitude function
	 */
	@Test
	void testGetLongitude() {
		assertEquals(longitude, location.getLongitude());
	}

	/**
	 * Tests the set longitude function
	 */
	@Test
	void testSetLongitude() {
		assertEquals(longitude, location.getLongitude());
	}

	/**
	 * Tests the get Carrier function
	 */
	@Test
	void testGetCarrier() {
		assertEquals(carrier, location.getCarrier());
		assertNotNull(location.getCarrier());
	}

	/**
	 * Tests the set Carrier Function
	 */
	@Test
	void testSetCarrier() {
		assertEquals(carrier, location.getCarrier());
	}

	/**
	 * Tests the get location type function
	 */
	@Test
	void testGetLocationType() {
		assertEquals(locationType, location.getLocationType());
		assertNotNull(location.getLocationType());
	}

	/**
	 * Tests the set locationType function
	 */
	@Test
	void testSetLocationType() {
		location.setLocationType("Building");
		assertNotEquals(locationType, location.getLocationType());
	}

	/**
	 * Tests the get vehicles function
	 */
	@Test
	void testGetVehicles() {
		assertEquals(vehicles, location.getVehicles());
		assertNotNull(location.getVehicles());
	}

	/**
	 * Tests the set vehicles function
	 */
	@Test
	void testSetVehicles() {
		location.setVehicles(null);
		assertNotEquals(vehicles, location.getVehicles());
	}

	/**
	 * Tests the to string function
	 */
	@Test
	void testToString() {
		assertNotNull(location.toString());
	}

}
