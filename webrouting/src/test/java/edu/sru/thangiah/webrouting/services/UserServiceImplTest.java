package edu.sru.thangiah.webrouting.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;

/**
 * Test class for the UserServiceImpl Test Class
 * @author Josh	Gearhart	jjg1018@sru.edu
 *
 */
class UserServiceImplTest {
	
	@Mock
	UserRepository userRepository;
	
	private User user;
	
	private UserService userService;
	
	private User testUser;
	
	private String otp;
	
	/**
	 * Sets up values for the UserServiceImpl test class
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		userRepository =  mock(UserRepository.class);
		userService = new UserServiceImpl(userRepository);
		user = new User();
		user.setId(1L);
		user.setEmail("realEmail@gmail.com");
		user.setPassword("bookshark13");
		user.setUsername("bookshark13");
		user.setEnabled(false);
		List<Shipments> shipment;
		shipment = new ArrayList<>();
		user.setShipments(shipment);
		
		Carriers carrier;
		carrier = new Carriers();
		user.setCarrier(carrier);
		
		Role role;
		role = new Role();
		role.setId(1L);
		role.setName("Test");
		user.setRole(role);
		
	}
	
	/**
	 * Test for the save function
	 */
	@Test
	void testSave() {
		userService.save(user);
		verify(userRepository).save(user);
		
	}
	
	/**
	 * Test for the find by username function
	 */
	@Test
	void testFindByUsername() {
		testUser = new User();
		userRepository.save(user);
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		testUser = userService.findByUsername(user.getUsername());
		assertEquals(user, testUser);
	}

	/**
	 * Test for the find by OTP function
	 */
	@Test
	void testFindByOtp() {
		user.setOtpCode("1234");
		userRepository.save(user);
		when(userRepository.findByOtpCode(user.getOtpCode())).thenReturn(user);
		testUser = userService.findByOtp(user.getOtpCode());
		assertEquals(user, testUser);
	}

	/**
	 * Test for the find by email function
	 */
	@Test
	void testFindByEmail() {
		userRepository.save(user);
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		testUser = userService.findByEmail(user.getEmail());
		assertEquals(user, testUser);
	}

	/**
	 * Tests for the create OTP code function
	 */
	@Test
	void testCreateOtpCode() {
		String otp;
		otp = userService.createOtpCode();
		user.setOtpCode(otp);
		assertEquals(user.getOtpCode(), otp);
		
	}

	/**
	 * Tests for both conditions of the method, the first assertion tests the else, while the 2nd tests that if the users otp code is not equal to null it sets a new otp code.
	 */
	@Test
	void testAssignOtpCode() {
		userRepository.save(user);
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		userService.assignOtpCode("realEmail@gmail.com");
		userRepository.save(user);
		otp = user.getOtpCode();
		assertEquals(user.getOtpCode(), otp);
		userService.assignOtpCode("realEmail@gmail.com");
		userRepository.save(user);
		String otp2;
		otp2 = user.getOtpCode();
		assertNotEquals(otp2, otp);
	}

	/**
	 * Tests the reset password function for the UserService Implementation Test
	 */
	@Test
	void testResetPassword() {
		user.setOtpCode("1234");
		userService.resetPassword(user, "123456789");
		assertEquals(null, user.getOtpCode());
		assertNotEquals("bookshark13", "123456789");
		verify(userRepository).save(user);
	}

}
