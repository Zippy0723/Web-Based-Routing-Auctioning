package edu.sru.thangiah.webrouting.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.sru.thangiah.webrouting.domain.User;

/**
 * Test Class for testing the User Repositories.
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
@DataJpaTest
class UserRepositoryTest {
	
	@Autowired
	private UserRepository underTest;
	
	private User user;
	
	private User actualUser;
	
	/**
	 * Sets up the false user to test as well as instantiates the actual
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		user = new User();
		actualUser = new User();
		user.setUsername("bookshark13");
		user.setEmail("joshuajgearhart@gmail.com");
		user.setPassword("gangganggang");
		user.setOtpCode("abcdef");
		user.setVerificationCode("1234");
		underTest.save(user);
	}
	
	/**
	 * Tears down the repository after each test
	 */
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	
	/**
	 * Tests the find By Username function
	 */
	@Test
	void testFindByUsername() {
		
		if(underTest.findByUsername(user.getUsername()) != null) {
			actualUser = underTest.findByUsername(user.getUsername());
			assertEquals(user,actualUser);
		}
	}
	
	/**
	 * Tests the find By Username function if the username does not exist
	 */
	@Test
	void testFindByUsernameThatDoesntExist() {
		actualUser = underTest.findByUsername("beepBoop");
		assertNull(actualUser);
	}
	
	/**
	 * Tests the find By Verification function
	 */
	@Test
	void testFindByVerificationCode() {
		
		if(underTest.findByVerificationCode(user.getVerificationCode()) != null) {
			actualUser = underTest.findByVerificationCode(user.getVerificationCode());
			assertEquals(user,actualUser);
		}
	}
	
	/**
	 * Tests the find By VerificationCode function if the Verification code does not exist
	 */
	@Test
	void testFindByVerificationCodeThatDoesntExist() {
		actualUser = underTest.findByVerificationCode("4321");
		assertNull(actualUser);
	}
	
	/**
	 * Tests the find By Email function
	 */
	@Test
	void testFindByEmail() {
		if(underTest.findByEmail(user.getEmail()) != null) {
			actualUser = underTest.findByEmail(user.getEmail());
			assertEquals(user,actualUser);
		}
	}
	
	/**
	 * Tests the find By Email function if the email does not exist
	 */
	@Test
	void testFindByEmailThatDoesntExist() {
		actualUser = underTest.findByEmail("falseemail@gmail.com");
		assertNull(actualUser);
	}
	
	/**
	 * Tests the find By Otp Code function
	 */
	@Test
	void testFindByOtpCode() {
		if(underTest.findByOtpCode(user.getOtpCode()) != null) {
			actualUser = underTest.findByOtpCode(user.getOtpCode());
			assertEquals(user,actualUser);
		}
	}
	
	/**
	 * Tests the find By Otp Code function if the code does not exist
	 */
	@Test
	void testFindByOtpCodeThatDoesntExist() {
		actualUser = underTest.findByOtpCode("fedcba");
		assertNull(actualUser);
	}


	

}
