package edu.sru.thangiah.webrouting.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.validation.BindingResult;

import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.UserServiceImpl;
import net.bytebuddy.utility.RandomString;


/**
 * Testing the User Validator Class
 * @author Josh Gearhart 	jjg1018@sru.edu
 *
 */
@DataJpaTest
 class UserValidatorTest {
	
		private UserValidator userValidator;

		private String upperTest;
		
		private String username;
		
		private String lowerTest;
		
		private String email;
		
		private String password;
		
		private String confirmPassword;
		
		
		private UserService userService;
		
		private User user;
		
		private boolean test;
		
		@Mock
		private UserRepository userRepository;
		
		@Mock
		private BindingResult result;
		
		private User issueUser;
		
		private long id;
		
		
	
	/**
	 * Sets up values for the tests to run
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		upperTest = new String();
		lowerTest = new String();
		userService = new UserServiceImpl(userRepository);
		userValidator = new UserValidator(userService);
		result = mock(BindingResult.class);
		user = new User();
		issueUser = new User();
		test = false;
		id = 1L;
		user.setId(id);
		username = "bookshark13";
		user.setUsername(username);
		password = "bookshark13";
		user.setPassword(password);
		confirmPassword = "bookshark13";
		user.setConfirmPassword(confirmPassword);
		email = "joshuajgearhart@gmail.com";
		user.setEmail(email);
		issueUser.setId(id + 1L);
		issueUser.setEmail("joshuajgearhartgmail.com");
		issueUser.setPassword(password);
		issueUser.setConfirmPassword(confirmPassword);
		userService.save(user);
		userService.save(issueUser);
	}
	/**
	 * Makes sure that the userRepository is clean for each test
	 */
	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}
	
	//Supports Omitted as its a function that comes from the Validator and goes unused.

	/**
	 * Test for the validateEmail function, checks that no errors exist 1st and passes
	 * 2nd tests that the email contains an @ symbol 
     * 3rd tests that the email is not duplicate
     * 4th tests that the email is not null
	 */
	@Test
	void testValidateEmail() {
		user.setUpdateEmail(user.getEmail());
		userService.save(user);
		userValidator.validateEmail(user, result);
		if(result.hasErrors()) {
			assertFalse(test);
			fail("Test has failed because errors were present");
		}
		else {
			test = true;
			assertTrue(test);
			test = false;
		}
		issueUser.setUpdateEmail(user.getEmail());
		userService.save(issueUser);
		userValidator.validateEmail(issueUser, result);
		if(result.hasErrors()) {
			assertFalse(test);
			test = true;
		}
		else {
			issueUser.setEmail(email); 
			userService.save(issueUser);
			issueUser.setUpdateEmail(user.getEmail());
			userService.save(issueUser);
		}
		userValidator.validateEmail(issueUser, result);
		if(result.hasErrors()) {
			assertTrue(test);
			test = false;
		}
		else {
			issueUser.setEmail("");
			userService.save(issueUser);
			issueUser.setUpdateEmail(user.getEmail());
			userService.save(issueUser);
		}
		userValidator.validateEmail(issueUser, result);
		if(result.hasErrors()) {
			assertFalse(test);
		}
	}

	/**
	 * Test for the test ValidatePassword function
	 * 1st tests proper conditions
	 * then tests for a empty password and next an empty confirmPassword
	 * next tests for non matching password and confirm password
	 * after that tests for max password length
	 */
	@Test
	void testValidatePassword() {
		upperTest = RandomString.make(33);
		lowerTest = RandomString.make(7);
		
    userValidator.validatePassword(user, result);
    	if(result.hasErrors()) {
    		assertFalse(test);
    		fail("Test has failed before errors were present");
    	}
    	else {
    		test = true;
    		assertTrue(test);
    		test = false;
    	}
    	issueUser.setPassword("");
    	userService.save(issueUser);
    	userValidator.validatePassword(issueUser, result);
    		if(result.hasErrors()) {
    			assertFalse(test);
    			test = true;
    		}
    	issueUser.setPassword("123456789");
    	issueUser.setConfirmPassword("");
    	userService.save(issueUser);
    	userValidator.validatePassword(issueUser, result);
    		if(result.hasErrors()) {
    			assertTrue(test);
    			test = false;
    		}
    		issueUser.setConfirmPassword("12345678910");
    		userService.save(issueUser);
    		userValidator.validatePassword(issueUser, result);
    			if(result.hasErrors()) {
    				assertFalse(test);
    				test = true;
    			}
    	issueUser.setPassword(upperTest);
    	issueUser.setConfirmPassword(upperTest);
    	userService.save(issueUser);
    	userValidator.validatePassword(issueUser, result);
    		if(result.hasErrors()) {
    			assertTrue(test);
    			test = false;
    		}
    	password = RandomString.make(32);
    	confirmPassword = password;
    	issueUser.setPassword(password);
    	issueUser.setConfirmPassword(confirmPassword);
    	userValidator.validatePassword(issueUser, result);
    		if(result.hasErrors()) {
    			assertFalse(test);
    			test = true;
    		}	
    	password = RandomString.make(31);
       	confirmPassword = password;
       	issueUser.setPassword(password);
       	issueUser.setConfirmPassword(confirmPassword);
       	userValidator.validatePassword(issueUser, result);
        		if(result.hasErrors()) {
        			assertTrue(test);
        			test = false;
        		}	
    	issueUser.setPassword(lowerTest);
    	issueUser.setConfirmPassword(lowerTest);
    	userService.save(issueUser);
    	userValidator.validatePassword(issueUser, result);
		if(result.hasErrors()) {
			assertFalse(test);
			test = true;
		}
		password = RandomString.make(8);
		confirmPassword = password;
		issueUser.setPassword(password);
		issueUser.setConfirmPassword(confirmPassword);
		userValidator.validatePassword(issueUser, result);
		if(result.hasErrors()) {
			assertTrue(test);
			test = false;
		}
		password = RandomString.make(9);
		confirmPassword = password;
		issueUser.setPassword(password);
		issueUser.setConfirmPassword(confirmPassword);
		userValidator.validatePassword(issueUser, result);
		if(result.hasErrors()) {
			assertFalse(test);
			test = true;
		}
	}
	/**
	 * Testing Validate function only testing for username, as previous functions are the same.
	 */
	@Test
	void testValidate() {
		userValidator.validate(user, result);
		if(result.hasErrors()) {
    		assertFalse(test);
    		fail("Test has failed before errors were present");
    	}
    	else {
    		test = true;
    		assertTrue(test);
    		test = false;
    	}
		upperTest = RandomString.make(33);
		issueUser.setUsername(upperTest);
		userRepository.save(issueUser);
		if(result.hasErrors()) {
			assertFalse(test);
			test = true;
		}
		lowerTest = RandomString.make(7);
		issueUser.setUsername(lowerTest);
		if(result.hasErrors()) {
			assertTrue(test);
			test = false;
		}
		upperTest = RandomString.make(32);
		issueUser.setUsername(upperTest);
		userRepository.save(issueUser);
		if(result.hasErrors()) {
			assertFalse(test);
			test = true;
		}
		lowerTest = RandomString.make(8);
		issueUser.setUsername(lowerTest);
		if(result.hasErrors()) {
			assertTrue(test);
			test = false;
		}
		upperTest = RandomString.make(31);
		issueUser.setUsername(upperTest);
		userRepository.save(issueUser);
		if(result.hasErrors()) {
			assertFalse(test);
			test = true;
		}
		lowerTest = RandomString.make(9);
		issueUser.setUsername(lowerTest);
		if(result.hasErrors()) {
			assertTrue(test);
			test = false;
		}
	}


}
