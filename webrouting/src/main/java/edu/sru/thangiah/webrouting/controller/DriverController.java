package edu.sru.thangiah.webrouting.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.domain.Driver;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.DriverRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with contractors.
 * @author Ian Black		imb1007@sru.edu
 * @since 2/8/2022
 */

@Controller
public class DriverController {

	private DriverRepository driverRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ValidationServiceImp validationServiceImp;

	private static final Logger Logger = LoggerFactory.getLogger(DriverController.class);


	/**
	 * Constructor for DriverController
	 * @param driverRepository Instantiates the driver Repository
	 */
	
	public DriverController (DriverRepository driverRepository) {
		this.driverRepository = driverRepository;
	}

	/**
	 * Adds all of required attributes to the model to render the drivers page
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /drivers
	 */
	
	@RequestMapping({"/drivers"})
	public String showDriversList(Model model, HttpSession session) {
		try {
			model.addAttribute("error",session.getAttribute("error"));
		} catch(Exception e){
			//do nothing
		}
		session.removeAttribute("error");

		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

		String redirectLocation = "/drivers";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("currentPage","/drivers");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		if (user.getRole().toString().equals("CARRIER")) {

			model.addAttribute("drivers", user.getCarrier().getDrivers());

			return "drivers";
		}

		model.addAttribute("drivers", driverRepository.findAll());

		return "drivers";
	}

	/**
	 * Redirects user to the upload drivers page when clicking "Upload an excel file" button in the Drivers section of Carrier login
	 * @param model used to add data to the model
	 * @return /uploaddrivers
	 */

	@RequestMapping({"/uploaddrivers"})
	public String showAddDriversExcel(Model model) {
		model.addAttribute("currentPage","/drivers");
		return "/uploaddrivers";
	}

	/**
	 * Finds a driver using the id parameter and if found, redirects user to confirmation page for deleting a driver
	 * @param id Stores the ID of the driver to be deleted
	 * @param model used to load attributes into the Thymeleaf model
	 * @return /delete/deletedriverconfirm
	 */
	@GetMapping("/deletedriver/{id}")
	public String deleteDriver(@PathVariable("id") long id, Model model) {
		Driver drivers = driverRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
		model.addAttribute("drivers", drivers);
		model.addAttribute("currentPage","/drivers");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "/delete/deletedriverconfirm";
	}

	/**
	 * Finds a driver using the id parameter and if found, deletes the driver and redirects to drivers page
	 * @param id of the driver being deleted
	 * @param model used to load attributes into the Thymeleaf model
	 * @return redirects to /drivers
	 */
	@GetMapping("/deletedriverconfirmation/{id}")
	public String deleteDriverConfirmation(@PathVariable("id") long id, Model model) {
		Driver drivers = driverRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/drivers");
		Logger.info("{} || successfully deleted driver with ID {}.", user.getUsername(), drivers.getId());

		driverRepository.delete(drivers);
		return "redirect:/drivers";
	}

	/**
	 * Adds all of the required attributes to the model to render the drivers page
	 * @param id Stores the ID of the driver to be viewed
	 * @param model used to load attributes into the Thymeleaf model
	 * @return /drivers
	 */
	@GetMapping("/viewdriver/{id}")
	public String viewDriver(@PathVariable("id") long id, Model model) {
		Driver driver = driverRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));

		model.addAttribute("drivers", driver);

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/drivers");

		return "drivers";
	}

	/**
	 * Adds all of the required attributes to the model to render the edit drivers page
	 * @param id of the driver being edited 
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /edit/edit-drivers
	 */

	@GetMapping("/editdriver/{id}")
	public String showDriversEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Driver driver = driverRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));
		User user = userService.getLoggedInUser();

		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/drivers");
		
		if(!driver.getLisence_expiration().equals("")) {
		//This converts the date to a format that the page is expecting to load it into the date object form
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
	        Date date;
			date = inputFormat.parse(driver.getLisence_expiration());
	        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
	        String formattedDate = outputFormat.format(date);
	        driver.setLisence_expiration(formattedDate);
	        
		} catch (ParseException e) {
			
			System.out.println("Failed to convert date for the forms expected date");
		}
		}

		model.addAttribute("driver", driver); 

		model.addAttribute("vehicles", user.getCarrier().getVehicles());

		try {
			model.addAttribute("message",session.getAttribute("message"));
		}
		catch(Exception e){

		}
		session.removeAttribute("message");

		return "/edit/edit-drivers";

	}


	/**
	 * Receives and passes the new driver object off to be validated
	 * Once validated the object is saved to the drivers repository
	 * @param driver holds new driver object submitted by the user
	 * @param id of the driver being edited 
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirects to /drivers or /edit/edit-drivers
	 */
	
	@PostMapping("edit-drivers/{id}")
	public String driverUpdateForm(@PathVariable("id") long id, Driver driver, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User user = userService.getLoggedInUser();

		Driver temp = driverRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));

		Hashtable<String, String> hashtable = new Hashtable<>();
		
		if (!driver.getLisence_expiration().equals(""))
		{
			driver.setLisence_expiration(dateConverter(driver.getLisence_expiration()));	
		}

		hashtable.put("licenseNumber", driver.getLisence_number().strip());
		hashtable.put("licenseExpiration", driver.getLisence_expiration().strip());
		hashtable.put("licenseClass", driver.getLisence_class());

		driver.setContact(temp.getContact());

		Driver result;

		result = validationServiceImp.validateDriverForm(hashtable, session);


		if (result == null) {
			Logger.error("{} || attempted to edit a Driver but "+ session.getAttribute("message"),user.getUsername());
			return "redirect:/editdriver/"+id;
		}

		result.setId(id);
		result.setContact(driver.getContact());
		result.setVehicle(driver.getVehicle());


		driverRepository.save(result);
		Logger.info("{} || successfully updated driver with ID {}.", user.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
	}
	
	/**
	 * Converts date from date picker into the expect format for saving to the repositories
	 * @param originalDateString holds the original date
	 * @return newDateString
	 */
	
	String dateConverter(String originalDateString) {
		
		DateTimeFormatter originalDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		LocalDate originalDate = LocalDate.parse(originalDateString, originalDateFormatter);

		DateTimeFormatter newDateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		String newDateString = originalDate.format(newDateFormatter);
		
		return newDateString;
	}

}
