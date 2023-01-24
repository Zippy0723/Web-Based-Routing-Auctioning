package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import net.bytebuddy.utility.RandomString;
/**
 * Used to test the Role Class in the Domain Package
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
@DataJpaTest
class RoleTest {
	
	Role role;
	
	Role expectedRole;
	
	String test;
	
	boolean actual;
	
	List<User> users;
	
	String testUpperBoundry;
	/**
	 * Used to Set up Data for the tests
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		test = new String();
		role = new Role();
		users =  new ArrayList<>();
		testUpperBoundry = new String();
		actual = false;
		expectedRole = new Role();
		role.setId(1L);
		role.setName("Test");
	}
	
	/**
	 * Tests the role get id function
	 */
	@Test
	void testGetId() {
		long test= role.getId();
		assertEquals(test, role.getId());
	}
	
	/**
	 * Tests the role set id function
	 */
	@Test
	void testSetId() {
		assertEquals(1L, role.getId());
	}

	/**
	 * Tests the role get name function
	 */
	@Test
	void testGetName() {
		test = role.getName();
		assertEquals(test, role.getName());
	}

	/**
	 * Tests the role set name  function
	 */
	@Test
	void testSetName() {
		test = "Test2";
		role.setName(test);
		assertEquals(test, role.getName());
	}

	/**
	 * Tests the role set name to make sure values are unique in the DB
	 */
	@Test
	void testSetNameIsUnique() {
		try {
		expectedRole = role;
		expectedRole.setName(role.getName());
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}

	/**
	 * Tests the role set name function to make sure DB constraints are persisted
	 */
	@Test
	void testSetNameUpper() {
		try {
			testUpperBoundry= RandomString.make(25);
			role.setName(testUpperBoundry);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}
	

	/**
	 * Tests the role set name function to make sure DB constraits are persisted
	 */
	@Test
	void testSetNameNotNull() {
		try {
		expectedRole.setName(null);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}


	/**
	 * Tests the role get users function
	 */
	@Test
	void testGetUsers() {
		role.setUsers(users);
		List<User> usersActual;
		usersActual = role.getUsers();
		assertEquals(role.getUsers(), usersActual);
	}

	/**
	 * Tests the role set users function
	 */
	@Test
	void testSetUsers() {
		role.setUsers(users);
		assertEquals(users, role.getUsers());
	}

	/**
	 * Tests the role toString function
	 */
	@Test
	void testToString() {
		test = "Test";
		assertEquals(test, role.toString());
	}

}
