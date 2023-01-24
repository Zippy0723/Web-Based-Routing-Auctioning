package edu.sru.thangiah.webrouting.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * Test for the RestConfig class
 * @author Josh Gearhart	jjg1018@sru.edu
 *
 */
class RestConfigTest {

	private RestConfig restConfig;
	
	private RestTemplateBuilder builder;
	
	@Mock
	private RestTemplate restTemp;
	
	/**
	 * Sets up values for the test
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		restConfig = new RestConfig();
		builder = new RestTemplateBuilder();
	}

	/**
	 * Tests the rest Template method
	 */
	@Test
	void testRestTemplate() {
		restTemp = restConfig.restTemplate(builder);
		assertNotNull(restTemp);
	}

}
