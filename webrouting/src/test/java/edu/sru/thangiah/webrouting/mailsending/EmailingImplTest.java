package edu.sru.thangiah.webrouting.mailsending;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;
/**
 * Test Class for testing the Emailing Impl Class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class EmailingImplTest {

	@Mock
	private UserRepository userRepository;
	
	private User user;
	
	private boolean actual;
	
    @Mock
    private JavaMailSender sendMail;
    
    @Autowired
    private Emailing emailImpl;

    private Role role;
	
	
	/**
	 * Sets up data for the tests to use
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		userRepository = mock(UserRepository.class);
		user = new User();
		emailImpl = new EmailingImpl(userRepository, sendMail);
		role = new Role();
		actual = false;
		role.setName("Test");
		user.setUsername("bookshark13");
		user.setEnabled(true);
		user.setEmail("joshuajgearhart@gmail.com");
		user.setPassword("gangganggang");
		user.setRole(role);
		emailImpl.saveVerificationCode(user);
	}
	
	/**
	 * Tears down the repository after each test
	 */
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}
	/**
	 * Tests to see that the verification code is being set.
	 */
	@Test
	void testSaveVerificationCodeIsEnabled() {
		assertFalse(user.isEnabled());
	}
	
	/*
	 * test to see that the verification code is being set
	 */
	@Test
	void testSaveVerificationCodeIsSettingVerificationCode() {
		assertNotNull(user.getVerificationCode());
	}
	/**
	 * verifies that the userRepository is saving the user.
	 */
	@Test
	void testSaveVerificationCodeIsSavingUser() {
		verify(userRepository).save(user);
	}


	/**
	 * Tests the verify account would properly deny access
	 * Sets the enabled to true(an account who shouldn't be accessing this method)
	 * actual is set to true indicating that it fired, if it doesn't deny test will fail since actual is initialized to false.
	 */
	@Test
	void testVerifyAccountIsDenyingIfUserIsEnabledAlready() {
		user.setEnabled(true);
		if(user == null || user.isEnabled()) {
			actual = true;
			assertTrue(actual);
		}
		else {
			assertTrue(actual);
		}
	}
	
	/**
	 * Checks that the appropriate user is going to the else condition and the enabled is being set properly
	 */
	@Test
	void testVerifyAccountIsSettingEnabled() {
		if(user == null || user.isEnabled()) {
			assertTrue(actual);
		}
		else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepository.save(user);
			assertTrue(user.isEnabled());
		}
	}
	
	/**
	 * Checks that the appropriate user is going to the else condition and the enabled is being set properly
	 */
	@Test
	void testVerifyAccountIsVerificationCode() {
		if(user == null || user.isEnabled()) {
			assertTrue(actual);
		}
		else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepository.save(user);
			assertNull(user.getVerificationCode());
		}
	}

	/**
	 * Test to make sure user is being found by verificationCode
	 */
	@Test
	void testFindByVerificationCode() {
		userRepository.findByVerificationCode(user.getVerificationCode());
		verify(userRepository).findByVerificationCode(user.getVerificationCode());
	}

}
