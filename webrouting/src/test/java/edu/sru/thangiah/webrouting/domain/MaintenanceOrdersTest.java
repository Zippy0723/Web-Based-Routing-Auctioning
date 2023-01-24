package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Test Classes for Maintenance Orders Domain Class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class MaintenanceOrdersTest {
	
	private MaintenanceOrders mainOrder;

	private long id;
	
	private Technicians technician;
	
	private String scheduled_date;
	
	private String details;
	
	private String service_type_key;
	
	private String cost;
	
	private String status_key;
	
	private Vehicles vehicle;
	
	private String maintenance_type;
	
	private Carriers carrier;

	/**
	 * Sets up values for the Test Class of MaintenanceOrders
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		mainOrder = new MaintenanceOrders();
		vehicle = new Vehicles();
		carrier = new Carriers();
		id = 1L;
		mainOrder.setId(id);
		technician = new Technicians();
		technician.setId(id);
		mainOrder.setTechnician(technician);
		scheduled_date = "11-15-2022";
		mainOrder.setScheduled_date(scheduled_date);
		details = "Brake Replacement";
		mainOrder.setDetails(details);
		service_type_key = "Repair";
		mainOrder.setService_type_key(service_type_key);
		cost = "900.00";
		mainOrder.setCost(cost);
		status_key = "Pending";
		mainOrder.setStatus_key(status_key);
		vehicle.setId(id);
		mainOrder.setVehicle(vehicle);
		maintenance_type = "Brakes";
		mainOrder.setMaintenance_type(maintenance_type);
		carrier.setId(id);
		mainOrder.setCarrier(carrier);
	}

	/**
	 * Test for the Get Id function
	 */
	@Test
	void testGetId() {
		assertEquals(1L, mainOrder.getId());
	}

	/**
	 * Test for the Set Id function
	 */
	@Test
	void testSetId() {
		assertEquals(id, mainOrder.getId());
		assertNotNull(mainOrder.getId());
	}

	/**
	 * Tests the get technician method
	 */
	@Test
	void testGetTechnician() {
		assertEquals(technician, mainOrder.getTechnician());
		assertNotNull(mainOrder.getTechnician());
	}

	/**
	 * Tests the set technician method
	 */
	@Test
	void testSetTechnician() {
		assertEquals(technician, mainOrder.getTechnician());
	}

	/**
	 * Tests the get scheduled date method
	 */
	@Test
	void testGetScheduled_date() {
		assertEquals(scheduled_date, mainOrder.getScheduled_date());
		assertNotNull(mainOrder.getScheduled_date());
	}

	/**
	 * Tests the set scheduled date method
	 */
	@Test
	void testSetScheduled_date() {
		assertEquals(scheduled_date, mainOrder.getScheduled_date());
	}

	/**
	 * Tests the get details method
	 */
	@Test
	void testGetDetails() {
		assertEquals(details, mainOrder.getDetails());
		assertNotNull(mainOrder.getDetails());
	}

	/**
	 * Tests the set details method
	 */
	@Test
	void testSetDetails() {
		assertEquals(details, mainOrder.getDetails());
	}

	/**
	 * Tests the get service type key method
	 */
	@Test
	void testGetService_type_key() {
		assertEquals(service_type_key, mainOrder.getService_type_key());
		assertNotNull(mainOrder.getService_type_key());
	}

	/**
	 * Tests the set service type key method
	 */
	@Test
	void testSetService_type_key() {
		assertEquals(service_type_key, mainOrder.getService_type_key());
	}

	/**
	 * Tests the get cost method
	 */
	@Test
	void testGetCost() {
		assertEquals(cost, mainOrder.getCost());
		assertNotNull(mainOrder.getCost());
	}

	/**
	 * Tests the set cost method
	 */
	@Test
	void testSetCost() {
		assertEquals(cost, mainOrder.getCost());
	}

	/**
	 * Tests the get status key method
	 */
	@Test
	void testGetStatus_key() {
		assertEquals(status_key, mainOrder.getStatus_key());
		assertNotNull(mainOrder.getStatus_key());
	}

	/**
	 * Tests the set status key method
	 */
	@Test
	void testSetStatus_key() {
		assertEquals(status_key, mainOrder.getStatus_key());
	}

	/**
	 * Tests the get vehicle method
	 */
	@Test
	void testGetVehicle() {
		assertEquals(vehicle, mainOrder.getVehicle());
		assertNotNull(mainOrder.getVehicle());
	}	

	/**
	 * Tests the set vehicle method
	 */
	@Test
	void testSetVehicle() {
		assertEquals(vehicle, mainOrder.getVehicle());
	}

	/**
	 * Tests the get maintenance type method
	 */
	@Test
	void testGetMaintenance_type() {
		assertEquals(maintenance_type, mainOrder.getMaintenance_type());
		assertNotNull(mainOrder.getMaintenance_type());
	}

	/**
	 * Tests the set maintenance type method
	 */
	@Test
	void testSetMaintenance_type() {
		assertEquals(maintenance_type, mainOrder.getMaintenance_type());
	}

	/**
	 * Tests the get carrier method
	 */
	@Test
	void testGetCarrier() {
		assertEquals(carrier, mainOrder.getCarrier());
		assertNotNull(mainOrder.getCarrier());
	}

	/**
	 * Tests the set carrier method
	 */
	@Test
	void testSetCarrier() {
		assertEquals(carrier, mainOrder.getCarrier());
	}

}
