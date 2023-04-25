package edu.sru.thangiah.webrouting;

import java.security.spec.RSAPublicKeySpec;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * Web Routing and Auctioning Application
 * 
 * Originally Created By
 * Patrick Blair
 * Anthony Christe
 * 
 * Updated Starting 2/6/2013
 * Alex McCraken
 * Mitch Nemitz
 * Kelly Smith
 * 
 * Update Starting 1/20/2022
 * Fady Aziz		faa1002@sru.edu
 * Ian Black		imb1007@sru.edu
 * Logan Kirkwood	llk1005@sru.edu
 * 
 * Update Starting 9/6/2022
 * Joshua Gearhart	jjg1018@sru.edu                                      
 * Nick Bushee		nab1017@sru.edu
 * 
 * Update Started 1/20/2023
 * Thomas Haley		tjh1019@sru.edu 
 * Dakota Myers		drm1022@sru.edu
 * Beth Orgovan		bro0700@sru.edu 
 * Sinchana Kori	ssk1022@sru.edu 
 */

/**
 * Used to start the Spring Application
 * 
 * @author Thomas Haley		tjh1019@sru.edu 
 * @author Dakota Myers		drm1022@sru.edu
 * @author Beth Orgovan		bro0700@sru.edu 
 * @author Sinchana Kori	ssk1022@sru.edu 
 *
 */

@SpringBootApplication
@EnableAsync
@EntityScan(basePackages = {"edu.sru.thangiah.webrouting.domain"})
@EnableScheduling
public class WebroutingApplication {

	/**
	 * Main method for the program. Starts Spring Application
	 * @param args Arguments for the main function
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebroutingApplication.class, args);
		System.out.println("Webrouting Started!");
		
	}

}

/*
 PORT 8080 IN USE
 LINUX:
 netstat -lnp | grep 8080
 kill -9 PID
 
 WINDOWS:
 netstat -ano | findstr 8080
 taskkill /F /pid PID
 
 ALTERNATIVE: In Eclipse Download Spring Boot Tools
 Start and Stop project as you would a java application
*/