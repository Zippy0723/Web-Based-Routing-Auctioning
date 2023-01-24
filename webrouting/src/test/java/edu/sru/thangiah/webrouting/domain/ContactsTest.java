package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Testing the Contacts Domain Class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class ContactsTest {
	
	private Contacts contact;

	private long id;
	
	private String firstName;
	
	private String lastName;
	
	private String middleInit;
	
	private String emailAddress;
	
	private String streetAdd1;
	
	private String streetAdd2;
	
	private String city;
	
	private String state;
	
	private String zip;
	
	private String primaryPhone;
	
	private String workPhone;
	
	private Carriers carrier;
	
	private List<Driver> drivers;
	
	private List<Technicians> technicians;
	
	/**
	 * Set up function for values to be tested by test Methods
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		id = 1L;
		firstName = "kirstin";
		lastName = "theiss";
		middleInit = "M";
		emailAddress = "kmt20092kt@gmail.com";
		streetAdd1 = "800 GreenCrest Drive";
		streetAdd2 = "Apt #20";
		city = "shippenville";
		state = "PA";
		zip = "16254";
		primaryPhone = "(814)-319-2289";
		workPhone = "867-5309";
		contact = new Contacts();
		contact.setId(id);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setMiddleInitial(middleInit);
		contact.setEmailAddress(emailAddress);
		contact.setStreetAddress1(streetAdd1);
		contact.setStreetAddress2(streetAdd2);
		contact.setCity(city);
		contact.setState(state);
		contact.setZip(zip);
		contact.setPrimaryPhone(primaryPhone);
		contact.setWorkPhone(workPhone);
		
		carrier = new Carriers();
		carrier.setId(1L);
		contact.setCarrier(carrier);
		
		drivers = new ArrayList<>();
		contact.setDrivers(drivers);
		technicians = new ArrayList<>();
		contact.setTechnicians(technicians);
		
		
	}

	/**
	 * Test for the Get Id function
	 */
	@Test
	void testGetId() {
		assertEquals(1L, contact.getId());
	}

	/**
	 * Test for the Set Id function
	 */
	@Test
	void testSetId() {
		assertNotNull(contact.getId());
	}
	
	/**
	 * Test for the Get First Name function
	 */
	@Test
	void testGetFirstName() {
		assertEquals(firstName, contact.getFirstName());
	}
	
	/**
	 * Test for the Set First Name function
	 */
	@Test
	void testSetFirstName() {
		contact.setFirstName("abc");
		assertNotEquals(firstName, contact.getFirstName());
	}
	
	/**
	 * Test for the Get Last Name function
	 */
	@Test
	void testGetLastName() {
		assertEquals(lastName, contact.getLastName());
	}
	
	/**
	 * Test for the Set Last Name function
	 */
	@Test
	void testSetLastName() {
		contact.setLastName("thangiah");
		assertNotEquals(lastName, contact.getLastName());
	}
	
	/**
	 * Test for the Get MiddileInitial function
	 */
	@Test
	void testGetMiddleInitial() {
		assertEquals(middleInit, contact.getMiddleInitial());
	}
	
	/**
	 * Test for the Set MiddleInitial function
	 */
	@Test
	void testSetMiddleInitial() {
		contact.setMiddleInitial("b");
		assertNotEquals(middleInit, contact.getMiddleInitial());
	}
	
	/**
	 * Test for the Get Email Address function
	 */
	@Test
	void testGetEmailAddress() {
		assertEquals(emailAddress, contact.getEmailAddress());
	}
	
	/**
	 * Test for the Set EmailAddress function
	 */
	@Test
	void testSetEmailAddress() {
		contact.setEmailAddress("realmail@mail.com");
		assertNotEquals(emailAddress, contact.getEmailAddress());
	}
	
	/**
	 * Test for the Get StreetAddress1 function
	 */
	@Test
	void testGetStreetAddress1() {
		assertEquals(streetAdd1, contact.getStreetAddress1());
	}
	
	/**
	 * Test for the Set StreetAddress1 function
	 */
	@Test
	void testSetStreetAddress1() {
		contact.setStreetAddress1("gazebo lane");
		assertNotEquals(streetAdd1, contact.getStreetAddress1());
	}
	
	/**
	 * Test for the Get StreetAddress2 function
	 */
	@Test
	void testGetStreetAddress2() {
		assertEquals(streetAdd2, contact.getStreetAddress2());
	}
	
	/**
	 * Test for the Set StreetAddress2 function
	 */
	@Test
	void testSetStreetAddress2() {
		contact.setStreetAddress2("apt #20");
		assertNotEquals(streetAdd2, contact.getStreetAddress2());
	}
	
	/**
	 * Test for the Get City function
	 */
	@Test
	void testGetCity() {
		assertEquals(city, contact.getCity());
	}
	
	/**
	 * Test for the Set City function
	 */
	@Test
	void testSetCity() {
		contact.setCity("tionesta");
		assertNotEquals(city,contact.getCity());
	}
	
	/**
	 * Test for the Get State function
	 */
	@Test
	void testGetState() {
		assertEquals(state, contact.getState());
	}
	
	/**
	 * Test for the Set State function
	 */
	@Test
	void testSetState() {
		contact.setState("HI");
		assertNotEquals(state,contact.getState());
	}
	
	/**
	 * Test for the Get ZIP function
	 */
	@Test
	void testGetZip() {
		assertEquals(zip, contact.getZip());
	}
	
	/**
	 * Test for the Set ZIP function
	 */
	@Test
	void testSetZip() {
		contact.setZip("16214");
		assertNotEquals(zip, contact.getZip());
	}
	
	/**
	 * Test for the Get PrimaryPhone function
	 */
	@Test
	void testGetPrimaryPhone() {
		assertEquals(primaryPhone, contact.getPrimaryPhone());
	}
	
	/**
	 * Test for the Set PrimaryPhone function
	 */
	@Test
	void testSetPrimaryPhone() {
		contact.setPrimaryPhone("1233121234");
		assertNotEquals(primaryPhone, contact.getPrimaryPhone());
	}
	
	/**
	 * Test for the Get WorkPhone function
	 */
	@Test
	void testGetWorkPhone() {
		assertEquals(workPhone, contact.getWorkPhone());
	}
	
	/**
	 * Test for the Set WorkPhone function
	 */
	@Test
	void testSetWorkPhone() {
		contact.setWorkPhone("8143192289");
		assertNotEquals(workPhone, contact.getWorkPhone());
	}
	
	/**
	 * Test for the Get Carrier function
	 */
	@Test
	void testGetCarrier() {
		assertEquals(carrier, contact.getCarrier());
	}
	
	/**
	 * Test for the Set Carrier function
	 */
	@Test
	void testSetCarrier() {
		Carriers actualCarrier = new Carriers();
		actualCarrier.setId(2L);
		contact.setCarrier(actualCarrier);
		assertNotEquals(carrier, contact.getCarrier());
	}
	
	/**
	 * Test for the Get Drivers function
	 */
	@Test
	void testGetDrivers() {
		assertEquals(drivers, contact.getDrivers());
	}
	
	/**
	 * Test for the Set Drivers function
	 */
	@Test
	void testSetDrivers() {
		contact.setDrivers(null);
		assertNotEquals(drivers, contact.getDrivers());
	}
	
	/**
	 * Test for the Get Technicians function
	 */
	@Test
	void testGetTechnicians() {
		assertEquals(technicians, contact.getTechnicians());
	}
	
	/**
	 * Test for the Set Technicians function
	 */
	@Test
	void testSetTechnicians() {
		contact.setTechnicians(null);
		assertNotEquals(technicians, contact.getTechnicians());
	}
	
	/**
	 * Test for the toString function
	 */
	@Test
	void testToString() {
		assertNotNull(contact.toString());
	}

}
