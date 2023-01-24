package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;

/**
 * Tests for the bids domain class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
@DataJpaTest
class BidsTest {
	
	Bids actualBid;
	
	Bids bid;
	
	Shipments ship;
	
	Carriers carr;
	
	@Mock
	BidsRepository bidrepo;
	
	@Mock
	CarriersRepository carrierRepo;
	
	@Mock
	ShipmentsRepository shipRepo;
	
	private String date;
	
	private String time;
	
	private String price;
	
	/**
	 * Sets up values for testing the class
	 * @throws Exception
	 */
	@BeforeEach
	 void setUp() throws Exception {
		bid = new Bids();
		actualBid = new Bids();
		Shipments ship = new Shipments();
		Carriers carr = new Carriers();
		carr.setId(1L);
		carr.setCarrierName("THANGIAH SHIPPING");
		carr.setScac("GZBO");
		carr.setLtl("Yes");
		carr.setFtl("Yes");
		carr.setPallets("24");
		carr.setWeight("40000");
		carrierRepo.save(carr);
		
		ship.setId(1L);
		ship.setClient("STBLLC");
		ship.setCarrier(carr);
		ship.setScac("GZBO");
		ship.setClientMode("FTL");
		ship.setShipDate("11-08-22");
		ship.setFreightbillNumber("1234567");
		ship.setPaidAmount("");
		ship.setFullFreightTerms("SHIPMENT AVAILABLE");
		ship.setCommodityClass("15");
		ship.setCommodityPieces("52");
		ship.setCommodityPaidWeight("1000");
		ship.setShipperCity("AVON");
		ship.setShipperState("OH");
		ship.setShipperZip("44011");
		ship.setConsigneeState("WV");
		ship.setConsigneeCity("Inwood");
		ship.setConsigneeZip("25428");
		shipRepo.save(ship);
		
		bid.setId(1L);
		bid.setShipment(ship);
		bid.setCarrier(carr);
		price = "800.00";
		date = "11-06-22";
		time = "10:08";
		
		bid.setPrice("800.00");
		bid.setDate("11-06-22");
		bid.setTime("10:08");
		
	}
	
	/**
	 * Tests the set ID method
	 */
	@Test
	void testSetId() {
		actualBid = bid;
		actualBid.setId(2L);
		bidrepo.save(actualBid);
		
		assertEquals(2L, actualBid.getId());
	}
	
	/**
	 * Tests the Get ID method
	 */
	@Test
	void testGetId() {
		assertNotEquals(2L, bid.getId());
	}
	
	/**
	 * Tests the get shipment method
	 */
	@Test
	void testGetShipment() {
		Shipments shipmentexpected = bid.getShipment();
		assertEquals(shipmentexpected, bid.getShipment());
	}

	/**
	 * Tests the set shipment method
	 */
	@Test
	void testSetShipment() {
		Shipments shipment = bid.getShipment();
		bid.setShipment(null);
		assertNotEquals(shipment,bid.getShipment());
	}

	/**
	 * Tests the get Carrier method
	 */
	@Test
	void testGetCarrier() {
		Carriers carrexpected = bid.getCarrier();
		assertEquals(carrexpected, bid.getCarrier());

	}

	/**
	 * Tests the set carrier method
	 */
	@Test
	void testSetCarrier() {
		Carriers carrier = bid.getCarrier();
		bid.setCarrier(null);
		assertNotEquals(carrier, bid.getCarrier());
	}

	/**
	 * Tests the Get price method
	 */
	@Test
	void testGetPrice() {
		assertEquals(price, bid.getPrice());
		
	}

	/**
	 * Tests the Set price method
	 */
	@Test
	void testSetPrice() {
		bid.setPrice("1.00");
		assertNotEquals(price,bid.getPrice());
	}

	/**
	 * Tests the Get date method
	 */
	@Test
	void testGetDate() {
		assertEquals(date, bid.getDate());
		
	}

	/**
	 * Tests the set date Method
	 */
	@Test
	void testSetDate() {
		bid.setDate("11-16-22");
		assertNotEquals(date, bid.getDate());
	}

	/**
	 * Tests the Get Time Method
	 */
	@Test
	void testGetTime() {
		assertEquals(time, bid.getTime());
	}

	/**
	 * Tests the set Time Method
	 */
	@Test
	void testSetTime() {
		bid.setTime("11:55");
		assertNotEquals(time, bid.getTime());
	}

}