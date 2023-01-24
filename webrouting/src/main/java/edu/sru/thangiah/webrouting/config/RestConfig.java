package edu.sru.thangiah.webrouting.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
* Class for Configuring the Rest Template
* @author Josh Gearhart jjg1018@sru.edu
* @since 9/6/2022
*/

@Configuration
public class RestConfig {
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}