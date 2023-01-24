package edu.sru.thangiah.webrouting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

/*
 * Fleet Management Software
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
 */

/**
 * Used to start the Spring Application
 * 
 * @author Joshua Gearhart	jjg1018@sru.edu 
 * @author Nick Bushee		nab1017@sru.edu
 *
 */

@SpringBootApplication
@EnableAsync
@EntityScan(basePackages = {"edu.sru.thangiah.webrouting.domain"})
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