package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing the technicians domain class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class TechniciansTest {
	
	private Technicians tech;
	
	private long id;
	
	private Contacts contact;
	
	private String skill_grade;
	
	private List<MaintenanceOrders> orders ;
	
	private boolean test;

	/**
	 * Sets up values for the Technicians tests
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		test = false;
		tech = new Technicians();
		id = 1L;
		contact = new Contacts();
		contact.setId(id);
		skill_grade = "A";
		orders = new ArrayList<>();
		tech.setId(id);
		tech.setContact(contact);
		tech.setSkill_grade(skill_grade);
		tech.setOrders(orders);
	}
/**
 * Tests the get id function
 */
	@Test
	void testGetId() {
		assertEquals(id, tech.getId());
		assertNotNull(tech.getId());
	}

	/**
	 * Tests the set id function
	 */
	@Test
	void testSetId() {
		try {
			assertEquals(id, tech.getId());
			tech.setId(null);
		}
		catch(Exception e) {
			test = true;
			assertTrue(test);
		}
	}

	/**
	 * Tests the get contact function
	 */
	@Test
	void testGetContact() {
		assertEquals(contact, tech.getContact());
		assertNotNull(tech.getContact());
	}

	/**
	 * Tests the set contact function
	 */
	@Test
	void testSetContact() {
		assertEquals(contact, tech.getContact());
		tech.setContact(null);
		assertEquals(null, tech.getContact());
	}

	/**
	 * Tests the get skill grade function
	 */
	@Test
	void testGetSkill_grade() {
		assertEquals(skill_grade, tech.getSkill_grade());
		assertNotNull(tech.getSkill_grade());
	}

	/**
	 * Tests the set skill grade function
	 */
	@Test
	void testSetSkill_grade() {
		assertEquals(skill_grade, tech.getSkill_grade());
		tech.setSkill_grade(null);
		assertEquals(null, tech.getSkill_grade());
	}

	/**
	 * Tests the get orders function
	 */
	@Test
	void testGetOrders() {
		assertEquals(orders, tech.getOrders());
		assertNotNull(tech.getOrders());
		
	}

	/**
	 * Tests the set orders function
	 */
	@Test
	void testSetOrders() {
		assertEquals(orders, tech.getOrders());
		tech.setOrders(null);
		assertEquals(null,tech.getOrders());
	}

}
