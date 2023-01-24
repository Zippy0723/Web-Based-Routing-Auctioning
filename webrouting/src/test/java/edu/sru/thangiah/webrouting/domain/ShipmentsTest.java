package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Test class for the Shipments Domain Class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class ShipmentsTest {
	
	private Shipments shipment;
	
	private long id;
	
	private String client;
	
	private Carriers carrier;
	
	private String scac;
	
	private String clientMode;
	
	private String shipDate;
	
	private String freightbillNumber;
	
	private String paidAmount;
	
	private String fullFreightTerms;
	
	private String commodityClass;
	
	private String commodityPieces;
	
	private String commodityPaidWeight;
	
	private String shipperCity;
	
	private String shipperState;
	
	private String shipperZip;
	
	private String shipperLatitude;
	
	private String shipperLongitude;
	
	private String consigneeCity;
	
	private String consigneeState;
	
	private String consigneeZip;
	
	private String consigneeLatitude;
	
	private String consigneeLongitude;
	
	private Vehicles vehicle;
	
	private User user;
	
	private List<Bids> bids;
	
	private boolean test;

	/**
	 * Sets up values for the Shipments Test classes
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		test = false;
		id = 1L;
		bids = new ArrayList<>();
		user = new User();
		vehicle = new Vehicles();
		carrier = new Carriers();
		shipment = new Shipments();
		user.setId(id);
		vehicle.setId(id);
		carrier.setId(id);
		shipment.setId(id);
		shipment.setBids(bids);
		shipment.setCarrier(carrier);
		shipment.setUser(user);
		shipment.setVehicle(vehicle);
		client = "STBLLC";
		shipment.setClient(client);
		scac = "1234";
		shipment.setScac(scac);
		clientMode = "FTL";
		shipment.setClientMode(clientMode);
		shipDate = "2022-04-17";
		shipment.setShipDate(shipDate);
		freightbillNumber = "3587239";
		shipment.setFreightbillNumber(freightbillNumber);
		paidAmount = "2000.00";
		shipment.setPaidAmount(paidAmount);
		fullFreightTerms = "SHIPMENT AVAILABLE";
		shipment.setFullFreightTerms(fullFreightTerms);
		commodityClass="15";
		shipment.setCommodityClass(commodityClass);
		commodityPieces = "52";
		shipment.setCommodityPieces(commodityPieces);
		commodityPaidWeight="1000";
		shipment.setCommodityPaidWeight(commodityPaidWeight);
		shipperCity = "AVON";
		shipment.setShipperCity(shipperCity);
		shipperState = "OH";
		shipment.setShipperState(shipperState);
		shipperZip = "44011";
		shipment.setShipperZip(shipperZip);
		shipperLatitude = "41.4517093";
		shipment.setShipperLatitude(shipperLatitude);
		shipperLongitude = "-82.0356225";
		shipment.setShipperLongitude(shipperLongitude);
		consigneeCity = "Inwood";
		shipment.setConsigneeCity(consigneeCity);
		consigneeState = "WV";
		shipment.setConsigneeState(consigneeState);
		consigneeZip ="25428";
		shipment.setConsigneeZip(consigneeZip);
		consigneeLatitude="39.3578781";
		shipment.setConsigneeLatitude(consigneeLatitude);
		consigneeLongitude="78.03999994";
		shipment.setConsigneeLongitude(consigneeLongitude);
	}

	/**
	 * Test for the get id function
	 */
	@Test
	void testGetId() {
		assertEquals(id, shipment.getId());
		assertNotNull(shipment.getId());
	}

	/**
	 * Test for the set id 
	 */
	@Test
	void testSetId() {
		try {
		assertEquals(id, shipment.getId());
		shipment.setId(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get client function
	 */
	@Test
	void testGetClient() {
		assertEquals(client, shipment.getClient());
		assertNotNull(shipment.getClient());
	}

	/**
	 * Tests the set client function
	 */
	@Test
	void testSetClient() {
		try {
			assertEquals(client,shipment.getClient());
			shipment.setClient(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get carrier function
	 */
	@Test
	void testGetCarrier() {
		assertEquals(carrier, shipment.getCarrier());
		assertNotNull(shipment.getCarrier());
	}

	/**
	 * Tests the set carrier function
	 */
	@Test
	void testSetCarrier() {
		assertEquals(carrier, shipment.getCarrier());
		shipment.setCarrier(null);
		assertNull(shipment.getCarrier());
	}
	/**
	 * Tests the get scac function
	 */
	@Test
	void testGetScac() {
		assertEquals(scac, shipment.getScac());
		assertNotNull(shipment.getScac());
	}

	/**
	 * Tests the set scac function
	 */
	@Test
	void testSetScac() {
		try {
			assertEquals(scac,shipment.getScac());
			shipment.setScac(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	
	}

	/**
	 * Tests the get client mode function
	 */
	@Test
	void testGetClientMode() {
		assertEquals(clientMode, shipment.getClientMode());
		assertNotNull(shipment.getClientMode());
	}

	/**
	 * Tests the set client mode function
	 */
	@Test
	void testSetClientMode() {
		try {
			assertEquals(clientMode, shipment.getClientMode());
			shipment.setClientMode(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get ship date function
	 */
	@Test
	void testGetShipDate() {
		assertEquals(shipDate, shipment.getShipDate());
		assertNotNull(shipment.getShipDate());
	}

	/**
	 * Tests the set ship date function
	 */
	@Test
	void testSetShipDate() {
		try {
			assertEquals(shipDate, shipment.getShipDate());
			shipment.setShipDate(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get freight bill number function
	 */
	@Test
	void testGetFreightbillNumber() {
		assertEquals(freightbillNumber, shipment.getFreightbillNumber());
		assertNotNull(shipment.getFreightbillNumber());
	}

	/**
	 * Tests the set freight bill number function
	 */
	@Test
	void testSetFreightbillNumber() {
		try {
			assertEquals(freightbillNumber, shipment.getFreightbillNumber());
			shipment.setFreightbillNumber(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get paid amount function
	 */
	@Test
	void testGetPaidAmount() {
		assertEquals(paidAmount, shipment.getPaidAmount());
		assertNotNull(shipment.getPaidAmount());
	}

	/**
	 * Tests the set paid amount function
	 */
	@Test
	void testSetPaidAmount() {
		try {
			assertEquals(paidAmount, shipment.getPaidAmount());
			shipment.setPaidAmount(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get full freight terms function
	 */
	@Test
	void testGetFullFreightTerms() {
		assertEquals(fullFreightTerms, shipment.getFullFreightTerms());
		assertNotNull(shipment.getFullFreightTerms());
	}

	/**
	 * Tests the set full freight terms function
	 */
	@Test
	void testSetFullFreightTerms() {
		try {
			assertEquals(fullFreightTerms, shipment.getFullFreightTerms());
			shipment.setFullFreightTerms(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get commodity class function
	 */
	@Test
	void testGetCommodityClass() {
		assertEquals(commodityClass, shipment.getCommodityClass());
		assertNotNull(shipment.getCommodityClass());
	}

	/**
	 * Tests the set commodity class function
	 */
	@Test
	void testSetCommodityClass() {
		try {
			assertEquals(commodityClass, shipment.getCommodityClass());
			shipment.setCommodityClass(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get commodity pieces function
	 */
	@Test
	void testGetCommodityPieces() {
		assertEquals(commodityPieces, shipment.getCommodityPieces());
		assertNotNull(shipment.getCommodityPieces());
	}

	/**
	 * Tests the set commodity pieces function
	 */
	@Test
	void testSetCommodityPieces() {
		try {
			assertEquals(commodityPieces, shipment.getCommodityPieces());
			shipment.setCommodityPieces(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get commodity paid weight function
	 */
	@Test
	void testGetCommodityPaidWeight() {
		assertEquals(commodityPaidWeight, shipment.getCommodityPaidWeight());
		assertNotNull(shipment.getCommodityPaidWeight());
	}

	/**
	 * Tests the set commodity paid weight function
	 */
	@Test
	void testSetCommodityPaidWeight() {
		try {
			assertEquals(commodityPaidWeight, shipment.getCommodityPaidWeight());
			shipment.setCommodityPaidWeight(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
		
	}

	/**
	 * Tests the get shipper city function
	 */
	@Test
	void testGetShipperCity() {
		assertEquals(shipperCity, shipment.getShipperCity());
		assertNotNull(shipment.getShipperCity());
	}

	/**
	 * Tests the set shhipper city function
	 */
	@Test
	void testSetShipperCity() {
		try {
			assertEquals(shipperCity, shipment.getShipperCity());
			shipment.setShipperCity(null);
		}
	catch(Exception e) {
		test = true;
		assertTrue(test);
		}
	}

	/**
	 * Tests the get shipper state function
	 */
	@Test
	void testGetShipperState() {
		assertEquals(shipperState, shipment.getShipperState());
		assertNotNull(shipment.getShipperState());
	}

	/**
	 * Tests the set shipper state function
	 */
	@Test
	void testSetShipperState() {
		try {
			assertEquals(shipperState, shipment.getShipperState());
			shipment.setShipperState(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get shipper zip function
	 */
	@Test
	void testGetShipperZip() {
		assertEquals(shipperZip, shipment.getShipperZip());
		assertNotNull(shipment.getShipperZip());
	}

	/**
	 * Tests the set shipper zip function
	 */
	@Test
	void testSetShipperZip() {
		try {
			assertEquals(shipperZip, shipment.getShipperZip());
			shipment.setShipperZip(shipperZip);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get shipper latitude function
	 */
	@Test
	void testGetShipperLatitude() {
		assertEquals(shipperLatitude, shipment.getShipperLatitude());
		assertNotNull(shipment.getShipperLatitude());
	}

	/**
	 * Tests the set shipper latitude function
	 */
	@Test
	void testSetShipperLatitude() {
			assertEquals(shipperLatitude, shipment.getShipperLatitude());
			shipment.setShipperLatitude(null);
			assertEquals(null, shipment.getShipperLatitude());
	}

	/**
	 * Tests the get shipper longitude function
	 */
	@Test
	void testGetShipperLongitude() {
		assertEquals(shipperLongitude, shipment.getShipperLongitude());
		assertNotNull(shipment.getShipperLongitude());
	}

	/**
	 * Tests the set shipper longitude function
	 */
	@Test
	void testSetShipperLongitude() {
		assertEquals(shipperLongitude, shipment.getShipperLongitude());
		shipment.setShipperLongitude("180.000");
		assertEquals("180.000", shipment.getShipperLongitude());
	}

	/**
	 * Tests the get consignee city function
	 */
	@Test
	void testGetConsigneeCity() {
		assertEquals(consigneeCity, shipment.getConsigneeCity());
		assertNotNull(shipment.getConsigneeCity());
	}

	/**
	 * Tests the set consignee city function
	 */
	@Test
	void testSetConsigneeCity() {
		try {
			assertEquals(consigneeCity, shipment.getConsigneeCity());
			shipment.setConsigneeCity(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get consignee state function
	 */
	@Test
	void testGetConsigneeState() {
		assertEquals(consigneeState, shipment.getConsigneeState());
		assertNotNull(shipment.getConsigneeState());
	}

	/**
	 * Tests the set consignee state function
	 */
	@Test
	void testSetConsigneeState() {
		try {
			assertEquals(consigneeState, shipment.getConsigneeState());
			shipment.setConsigneeCity(null);
		}
		catch(Exception e) {
			test = true; 
			assertTrue(test);
		}
	}

	/**
	 * Tests the get consignee zip function
	 */
	@Test
	void testGetConsigneeZip() {
		assertEquals(consigneeZip, shipment.getConsigneeZip());
		assertNotNull(shipment.getConsigneeZip());
	}

	/**
	 * Tests the set consignee zip function
	 */
	@Test
	void testSetConsigneeZip() {
		try {
			assertEquals(consigneeZip, shipment.getConsigneeZip());
			shipment.setConsigneeZip(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get consignee latitude function
	 */
	@Test
	void testGetConsigneeLatitude() {
		assertEquals(consigneeLatitude, shipment.getConsigneeLatitude());
		assertNotNull(shipment.getConsigneeLatitude());
	}

	/**
	 * Tests the set consignee latitude function
	 */
	@Test
	void testSetConsigneeLatitude() {
		assertEquals(consigneeLatitude, shipment.getConsigneeLatitude());
		shipment.setConsigneeLatitude("90.1000");
		assertEquals("90.1000", shipment.getConsigneeLatitude());
	}

	/**
	 * Tests the get consignee longitude function
	 */
	@Test
	void testGetConsigneeLongitude() {
		assertEquals(consigneeLongitude, shipment.getConsigneeLongitude());
		assertNotNull(shipment.getConsigneeLongitude());
	}

	/**
	 * Tests the set consignee longitude function
	 */
	@Test
	void testSetConsigneeLongitude() {
		assertEquals(consigneeLongitude, shipment.getConsigneeLongitude());
		shipment.setConsigneeLongitude("0.00000");
		assertEquals("0.00000", shipment.getConsigneeLongitude());
	}

	/**
	 * Tests the get vehicle function
	 */
	@Test
	void testGetVehicle() {
		assertEquals(vehicle, shipment.getVehicle());
		assertNotNull(shipment.getVehicle());
	}

	/**
	 * Tests the set  vehicle function
	 */
	@Test
	void testSetVehicle() {
		try {
			assertEquals(vehicle, shipment.getVehicle());
			shipment.setVehicle(null);
		}
		catch(Exception e) {
			test =true; 
			assertTrue(test);
		}
	}

	/**
	 * Tests the get user  function
	 */
	@Test
	void testGetUser() {
		assertEquals(user, shipment.getUser());
		assertNotNull(shipment.getUser());
	}

	/**
	 * Tests the set  user function
	 */
	@Test
	void testSetUser() {
		try {
			assertEquals(user, shipment.getUser());
			shipment.setUser(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get bids  function
	 */
	@Test
	void testGetBids() {
		assertEquals(bids, shipment.getBids());
		assertNotNull(shipment.getBids());
	}

	/**
	 * Tests the set bids  function
	 */
	@Test
	void testSetBids() {
		assertEquals(bids, shipment.getBids());
		shipment.setBids(null);
		assertEquals(null, shipment.getBids());
	}

}
