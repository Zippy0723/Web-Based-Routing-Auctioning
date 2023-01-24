package edu.sru.thangiah.webrouting.captcha;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Used to test the RecieveCaptchaTest
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class RecieveCaptchaTest {

	RecieveCaptcha recieveCap;
	
	boolean success;
	
	String challenge_ts;
	
	String hostname;
	
	String[] errorCodes;
	
	LocalDateTime test;
	
	String toString;
	/**
	 * Sets up values for the RecieveCaptchaTests
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		recieveCap = new RecieveCaptcha();
		errorCodes = new String[6];
		recieveCap.setSuccess(success=true);
		test = LocalDateTime.now();
		toString = test.toString();
		hostname = "localhost";
	}
	
	/**
	 * Tests the isSuccessMethod
	 */
	@Test
	void testIsSuccess() {
		assertEquals(true, recieveCap.isSuccess());
	}

	/**
	 * Tests the setSuccess Method
	 */
	@Test
	void testSetSuccess() {
		recieveCap.setSuccess(false);
		assertFalse(recieveCap.isSuccess());
	}

	/**
	 * Tests GetChallenge_ts Method
	 */
	@Test
	void testGetChallenge_ts() {
		String test;
		recieveCap.setChallenge_ts(toString);
		test = recieveCap.getChallenge_ts();
		assertEquals(test, recieveCap.getChallenge_ts());
	}

	/**
	 * Test the setChallenge_ts Method
	 */
	@Test
	void testSetChallenge_ts() {
		recieveCap.setChallenge_ts(toString);
		assertEquals(toString, recieveCap.getChallenge_ts());
	}
	/**
	 * Tests the getHostName method
	 */
	@Test
	void testGetHostName() {
		recieveCap.setHostName(hostname);
		String test1;
		test1=recieveCap.getHostName();
		assertEquals(hostname, test1);
	}

	/**
	 * Tests the SetHostName Method
	 */
	@Test
	void testSetHostName() {
		recieveCap.setHostName(hostname);
		assertEquals(hostname, recieveCap.getHostName());
	}

	/**
	 * Tests theGetErrorCode Method
	 */
	@Test
	void testGetErrorCodes() {
		String error1;
		error1 = "missing-input-secret";
		errorCodes[0]= error1;
		recieveCap.setErrorCodes(errorCodes);
		String[] testErrorCodes = new String[6];
		testErrorCodes= recieveCap.getErrorCodes();
		assertEquals(recieveCap.getErrorCodes(), testErrorCodes);
	}

	/**
	 * Tests the SetErrorCodes Method
	 */
	@Test
	void testSetErrorCodes() {
		String error1;
		error1 = "missing-input-secret";
		errorCodes[0]= error1;
		recieveCap.setErrorCodes(errorCodes);
		String[] actualErrors = new String[6];
		actualErrors = errorCodes;
		String actualError = new String();
		int i = 0;
		while(i < actualErrors.length) {
			if(actualErrors[i] !=null) {
				actualError = actualErrors[i];
				i++;
			}
			else {
				i++;
			}
		}
		assertEquals(errorCodes[0], actualError);
	}

}
