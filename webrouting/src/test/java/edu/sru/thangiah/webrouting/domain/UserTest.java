package edu.sru.thangiah.webrouting.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import net.bytebuddy.utility.RandomString;

/**
 * Unit testing for Testing the User Domain Class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
@DataJpaTest
class UserTest {
	
	User user;
	
	User actualUser;
	
	String actualEmail;
	
	boolean actual;
	
	String actualUname;
	
	String actualPass;
	
	String upperBoundryTest;
	
	/**
	 * Sets up the users being tested
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		user = new User();
		actualUser = new User();
		actualEmail = new String();
		actualUname = new String();
		actualPass = new String();
		upperBoundryTest = new String();
		actual=false;
		user.setId(1L);
		user.setEmail("realEmail@gmail.com");
		user.setPassword("bookshark13");
		user.setUsername("bookshark13");
		user.setEnabled(false);
		
	}
	
	/**
	 * Used to Test the GetId function of a user
	 */
	@Test
	void testGetId() {
		assertEquals(1L, user.getId());
	}

	/**
	 * Used to Test the setId function of a user
	 */
	@Test
	void testSetId() {
		actualUser.setId(2L);
		assertNotEquals(user.getId(), actualUser.getId());
	}
	
	/**
	 * Used to Test the GetUsername function of a user
	 */
	@Test
	void testGetUsername() {
		actualUname = user.getUsername();
		assertEquals(user.getUsername(), actualUname);
	}


	/**
	 * Used to Test the SetUsername function of a user
	 */
	@Test
	void testSetUsername() {
		assertEquals("bookshark13", user.getUsername());
	}

	/**
	 * Used to test that a user cannot have a null username
	 */
	@Test
	void testsetNullUsername() {
		try {
		actualUser = user;
		actualUser.setUsername(null);
		}
		catch(Exception e){
			actual = true;
			assertTrue(actual);
		}
	}
	
	/**
	 * Used to test that user cannot have a Username Larger than the DB constraints
	 */
	@Test
	void testSetUpperUsername() {
		upperBoundryTest = RandomString.make(33); 
		try {
			actualUser = user;
			actualUser.setUsername(upperBoundryTest);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}
	/**
	 * Used to Test the GetPassword function of a user
	 */
	@Test
	void testGetPassword() {
		actualPass = user.getPassword();
		assertEquals(actualPass, user.getPassword());
	}

	/**
	 * Used to Test the SetPassword function of a user
	 */
	@Test
	void testSetPassword() {
		assertEquals("bookshark13", user.getPassword());
	}
	
	/**
	 * Used to test that user cannot have a Password Larger than the DB constraints
	 */
	@Test
	void testSetUpperPassword() {
		upperBoundryTest = RandomString.make(33); 
		try {
			actualUser = user;
			actualUser.setPassword(upperBoundryTest);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}
	/**
	 * Used to test that a user cannot have a null Password
	 */
	@Test
	void testsetNullPassword() {
		try {
		actualUser = user;
		actualUser.setPassword(null);
		}
		catch(Exception e){
			actual = true;
			assertTrue(actual);
		}
	}

	/**
	 * Used to Test the GetEmail function of a user
	 */
	@Test
	void testGetEmail() {
		actualEmail=user.getEmail();
		assertEquals(actualEmail, user.getEmail());
	}

	/**
	 * Used to Test the SetEmail function of a user
	 */
	@Test
	void testSetEmail() {
		actualEmail = "realEmail@gmail.com";
		assertEquals(actualEmail, user.getEmail());
	}
	
	/**
	 * Used to test that user cannot have a email Larger than the DB constraints
	 */
	@Test
	void testSetUpperEmail() {
		upperBoundryTest = RandomString.make(65); 
		try {
			actualUser = user;
			actualUser.setEmail(upperBoundryTest);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}
	/**
	 * Used to test that a user cannot have a null Email
	 */
	@Test
	void testsetNullEmail() {
		try {
		actualUser = user;
		actualUser.setEmail(null);
		}
		catch(Exception e){
			actual = true;
			assertTrue(actual);
		}
	}
	/**
	 * Used to Test the GetRole function of a user
	 */
	@Test
	void testGetRole() {
		Role role;
		role = new Role();
		role.setId(0);
		role.setName("Test");
		user.setRole(role);
		Role expectedRole = user.getRole();
		assertEquals(expectedRole, user.getRole());
	}

	/**
	 * Used to Test the SetRole function of a user
	 */
	@Test
	void testSetRole() {
		Role role;
		role = new Role();
		role.setId(0);
		role.setName("Test");
		user.setRole(role);
		assertEquals(role, user.getRole());
	}
	
	/**
	 * Used to test that a user cannot have a null Role
	 */
	@Test
	void testsetNullRole() {
		try {
		actualUser = user;
		actualUser.setRole(null);
		}
		catch(Exception e){
			actual = true;
			assertTrue(actual);
		}
	}

	/**
	 * Used to Test the GetCarrier function of a user
	 */
	@Test
	void testGetCarrier() {
		Carriers carrier;
		carrier = new Carriers();
		user.setCarrier(carrier);
		Carriers expectedCarrier = user.getCarrier();
		assertEquals(expectedCarrier, user.getCarrier());
	}

	/**
	 * Used to Test the SetCarrier function of a user
	 */
	@Test
	void testSetCarrier() {
		Carriers carrier;
		carrier = new Carriers();
		user.setCarrier(carrier);
		assertEquals(carrier, user.getCarrier());
	}
	

	/**
	 * Used to Test the GetShipments function of a user
	 */
	@Test
	void testGetShipments() {
		List<Shipments> shipment;
		shipment = new ArrayList<>();
		user.setShipments(shipment);
		List<Shipments> expectShip = user.getShipments();
		assertEquals(expectShip, user.getShipments());
	}


	/**
	 * Used to Test the SetShipments function of a user
	 */
	@Test
	void testSetShipments() {
		List<Shipments> shipment;
		shipment = new ArrayList<>();
		user.setShipments(shipment);
		assertEquals(shipment, user.getShipments());
	}

	/**
	 * Used to Test the ToString function of a user
	 */
	@Test
	void testToString() {
		String userName;
		userName = user.toString();
		assertEquals(userName, user.toString());
	}

	/**
	 * Used to Test the GetVerificationCode function of a user
	 */
	@Test
	void testGetVerificationCode() {
		user.setVerificationCode("1234");
		String verificationCode = user.getVerificationCode();
		assertEquals(verificationCode, user.getVerificationCode());
		
	}

	/**
	 * Used to Test the SetVerificationCode function of a user
	 */
	@Test
	void testSetVerificationCode() {
		String verifyCode = RandomString.make(64);
		user.setVerificationCode(verifyCode);
		assertEquals(user.getVerificationCode(), verifyCode);
	}

	/**
	 * Used to test that user cannot have a verification code Larger than the DB constraints
	 */
	@Test
	void testSetUpperVerificationCode() {
		upperBoundryTest = RandomString.make(65); 
		try {
			actualUser = user;
			actualUser.setVerificationCode(upperBoundryTest);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}
	/**
	 * Used to Test the IsEnabled function of a user
	 */
	@Test
	void testIsEnabled() {
		boolean enable;
		enable = user.isEnabled();
		assertEquals(user.isEnabled(), enable);
	}

	/**
	 * Used to Test the SetEnabled function of a user
	 */
	@Test
	void testSetEnabled() {
		assertEquals(false, user.isEnabled());
	}
	

	/**
	 * Used to Test the GetOtpCode function of a user
	 */
	@Test
	void testGetOtpCode() {
		String otpCode;
		otpCode = RandomString.make(6);
		user.setOtpCode(otpCode);
		String expectedOtpCode = user.getOtpCode();
		assertEquals(expectedOtpCode, otpCode);
	}

	/**
	 * Used to Test the SetOtpCode function of a user
	 */
	@Test
	void testSetOtpCode() {
		String otpCode;
		otpCode = RandomString.make(6);
		user.setOtpCode(otpCode);
		assertEquals(user.getOtpCode(), otpCode);
	}
	
	/**
	 * Used to test that user cannot have a OtpCode Larger than the DB constraints
	 */
	@Test
	void testSetUpperOtpCode() {
		upperBoundryTest = RandomString.make(7); 
		try {
			actualUser = user;
			actualUser.setOtpCode(upperBoundryTest);
		}
		catch(Exception e) {
			actual = true;
			assertTrue(actual);
		}
	}

	/**
	 * Used to Test the GetConfirmPassword function of a user
	 */
	@Test
	void testGetConfirmPassword() {
		String confirmPass = "1234";
		user.setConfirmPassword(confirmPass);
		assertEquals(user.getConfirmPassword(), confirmPass);
	}


	/**
	 * Used to Test the SetConfirmPassword function of a user
	 */
	@Test
	void testSetConfirmPassword() {
		String confirmPass = "bookshark13";
		user.setConfirmPassword(confirmPass);
		assertEquals(user.getConfirmPassword(), confirmPass);
	}
	
	/**
	 * Used to test that a user cannot have a null Email
	 */
	@Test
	void testsetNullConfirmPassword() {
		try {
		actualUser = user;
		actualUser.setEmail(null);
		}
		catch(Exception e){
			actual = true;
			assertTrue(actual);
		}
	}

}
