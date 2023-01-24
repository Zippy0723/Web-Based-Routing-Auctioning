package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Carriers Domain
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class CarriersTest {

	private Carriers carrier;
	
	private String carrierName;
	
	private String scac;
	
	private String ltl;
	
	private String ftl;
	
	private String pallets;
	
	private String weight;
	
	private List<Contacts> contacts;
	
	private List<Shipments> shipments;
	
	private List<Bids> bids;
	
	private List<Locations> locations;
	
	private List<Vehicles> vehicles;
	
	private List<Driver> drivers;
	
	private List<MaintenanceOrders> orders;
	
	private Long id;
	
	/**
	 * Sets up values for testing the Carriers class
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		carrier = new Carriers();
		contacts = new ArrayList<>();
		shipments = new ArrayList<>();
		bids = new ArrayList<>();
		locations = new ArrayList<>();
		vehicles = new ArrayList<>();
		drivers = new ArrayList<>();
		orders = new ArrayList<>();
		
		
		carrierName = "THANGIAH SHIPPING";
		carrier.setCarrierName(carrierName);
		scac = "1234";
		carrier.setScac(scac);
		id = 1L;
		carrier.setId(id);
		carrier.setScac(scac);
		ltl = "Yes";
		carrier.setLtl(ltl);
		ftl = "Yes";
		carrier.setFtl(ftl);
		pallets = "24";
		carrier.setPallets(pallets);
		weight = "40000";
		carrier.setWeight(weight);
	}

	/**
	 * Test for the Get Id function
	 */
	@Test
	void testGetId() {
		assertEquals(1L, carrier.getId());
	}

	/**
	 * Tests the set id function
	 */
	@Test
	void testSetId() {
		carrier.setId(2L);
		assertNotEquals(id, carrier.getId());
	}

	/**
	 * Tests the get Carrier Name function
	 */
	@Test
	void testGetCarrierName() {
		assertEquals(carrierName, carrier.getCarrierName());
	}

	/**
	 * Tests the set carrier name function
	 */
	@Test
	void testSetCarrierName() {
		carrier.setCarrierName("WE SHIP 4 U");
		assertNotEquals(carrierName, carrier.getCarrierName());
	}

	/**
	 * Tests the get Scac Code function
	 */
	@Test
	void testGetScac() {
		assertEquals(scac, carrier.getScac());
	}

	/**
	 * Tests the Set scac code function
	 */
	@Test
	void testSetScac() {
		carrier.setScac("4321");
		assertNotEquals(scac, carrier.getScac());
	}

	/**
	 * Tests the GetLtl Function
	 */
	@Test
	void testGetLtl() {
		assertEquals(ltl, carrier.getLtl());
	}

	/**
	 * Tests the setLtl Function
	 */
	@Test
	void testSetLtl() {
		carrier.setLtl("NO");
		assertNotEquals(ltl, carrier.getLtl());
	}

	/**
	 * Tests the get Ftl Function
	 */
	@Test
	void testGetFtl() {
		assertEquals(ftl, carrier.getFtl());
	}

	/**
	 * Tests the Set ftl function
	 */
	@Test
	void testSetFtl() {
		carrier.setFtl("NO");
		assertNotEquals(ftl, carrier.getFtl());
	}

	/**
	 * Tests the Get pallets function
	 */
	@Test
	void testGetPallets() {
		
		assertEquals(pallets, carrier.getPallets());
	}

	/**
	 * Tests the set pallets function
	 */
	@Test
	void testSetPallets() {
		carrier.setPallets("12");
		assertNotEquals(pallets, carrier.getPallets());
	}

	/**
	 * Tests the get weight function
	 */
	@Test
	void testGetWeight() {
		assertEquals(weight, carrier.getWeight());
	}

	/**
	 * Tests the set weight function
	 */
	@Test
	void testSetWeight() {
		carrier.setWeight("50000");
		assertNotEquals(weight, carrier.getWeight());
	}

	/**
	 * Tests the get contacts function
	 */
	@Test
	void testGetContacts() {
		carrier.setContacts(contacts);
		List<Contacts> contactsActual;
		contactsActual = carrier.getContacts();
		assertEquals(carrier.getContacts(), contactsActual);
	}

	/**
	 * Tests the set contacts function
	 */
	@Test
	void testSetContacts() {
		carrier.setContacts(contacts);
		assertEquals(contacts, carrier.getContacts());
	}

	/**
	 * Tests the get shipments function
	 */
	@Test
	void testGetShipments() {
		carrier.setShipments(shipments);
		List<Shipments> shipmentsActual;
		shipmentsActual = carrier.getShipments();
		assertEquals(carrier.getShipments(), shipmentsActual);
	}

	/**
	 * Tests the set shipments function
	 */
	@Test
	void testSetShipments() {
		carrier.setShipments(shipments);
		assertEquals(shipments, carrier.getShipments());
		
	}

	/**
	 * Tests the get bids function
	 */
	@Test
	void testGetBids() {
		
		carrier.setBids(bids);
		List<Bids> bidsActual;
		bidsActual = carrier.getBids();
		assertEquals(carrier.getBids(), bidsActual);
	}

	/**
	 * Tests the set Bids Function
	 */
	@Test
	void testSetBids() {
		carrier.setBids(bids);
		assertEquals(bids, carrier.getBids());
	}

	/**
	 * Tests the get locations function
	 */
	@Test
	void testGetLocations() {
		carrier.setLocations(locations);
		List<Locations> locationActual;
		locationActual = carrier.getLocations();
		assertEquals(locations, locationActual);
	}

	/**
	 * Tests the Set Locations function
	 */
	@Test
	void testSetLocations() {
		carrier.setLocations(locations);
		assertEquals(locations, carrier.getLocations());
	}

	/**
	 * Tests the Get vehciles Function
	 */
	@Test
	void testGetVehicles() {
		carrier.setVehicles(vehicles);
		List<Vehicles> vehicleActual;
		vehicleActual = carrier.getVehicles();
		assertEquals(vehicles, vehicleActual);
	}

	/**
	 * Tests the Set Vehicles Function
	 */
	@Test
	void testSetVehicles() {
		carrier.setVehicles(vehicles);
		assertEquals(vehicles,carrier.getVehicles());
	}

	/**
	 * Tests the get drivers function
	 */
	@Test
	void testGetDrivers() {
		carrier.setDrivers(drivers);
		List<Driver> driversActual;
		driversActual = carrier.getDrivers();
		assertEquals(drivers, driversActual);
		
	}

	/**
	 * Tests the set drivers function
	 */
	@Test
	void testSetDrivers() {
		carrier.setDrivers(drivers);
		assertEquals(drivers, carrier.getDrivers());
	}

	/**
	 * Tests the Get Orders function
	 */
	@Test
	void testGetOrders() {
		carrier.setOrders(orders);
		List<MaintenanceOrders> ordersActual;
		ordersActual = carrier.getOrders();
		assertEquals(orders, ordersActual);
	}

	/**
	 * Tests the Set orders function
	 */
	@Test
	void testSetOrders() {
		carrier.setOrders(orders);
		assertEquals(orders, carrier.getOrders());
	}

	/**
	 * Tests the ToString Method
	 */
	@Test
	void testToString() {
		assertNotNull(carrier.toString());
	}

}
