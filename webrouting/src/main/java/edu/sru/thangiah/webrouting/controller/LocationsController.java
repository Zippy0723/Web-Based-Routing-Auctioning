package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
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

import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.LocationsRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Locations.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/1/2022
 */

@Controller
public class LocationsController {

	private LocationsRepository locationsRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private ValidationServiceImp validationServiceImp;

	private static final Logger Logger = LoggerFactory.getLogger(LocationsController.class);

	/**
	 * Constructor for LocationsController. <br>
	 * Instantiates the locationsRepository
	 * @param locationsRepository Used to interact with locations in the database
	 */
	public LocationsController (LocationsRepository locationsRepository) {
		this.locationsRepository = locationsRepository;
	}

	/**
	 * Adds all of the locations to the "locations" model and redirects user to
	 * the locations page.
	 * @param model Used to add data to the model
	 * @return "locations"
	 */
	@RequestMapping({"/locations"})
	public String showLocationsList(Model model, HttpSession session) {

		try {
			model.addAttribute("error",session.getAttribute("error"));
		} catch(Exception e){
			//do nothing
		}
		session.removeAttribute("error");

		String redirectLocation = "/locations";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("currentPage","/locations"); //for sidebar highlight

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		if (user.getRole().toString().equals("CARRIER")) {

			model.addAttribute("locations", user.getCarrier().getLocations());

			try {
				model.addAttribute("successMessage",session.getAttribute("successMessage"));
			} catch (Exception e) {
				//do nothing
			}
			session.removeAttribute("successMessage");

			return "locations";
		}
		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

		model.addAttribute("locations", locationsRepository.findAll());

		return "locations";
	}


	/**
	 * Redirects user to the /uploadlocations page when clicking "Upload an excel file" button in the locations section of Carrier login
	 * @param model used to add data to the model
	 * @return "/uploadlocations"
	 */

	@RequestMapping({"/uploadlocations"})
	public String showAddLocationsExcel(Model model) {
		model.addAttribute("currentPage","/locations");
		return "/uploadlocations";
	}

	/**
	 * Finds a location using the id parameter and if found, redirects to the confirmation page
	 * Makes sure there are no dependencies before deleting. If there are, an error message is displayed
	 * @param id Stores the ID of the location to be deleted
	 * @param model Used to add data to the model
	 * @return "redirect:/locations"
	 */
	@GetMapping("/deletelocations/{id}")
	public String deleteLocation(@PathVariable("id") long id, Model model, HttpSession session ) {
		Locations location = locationsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid location Id:" + id));
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/locations");
		if (!location.getVehicles().isEmpty()) {
			session.setAttribute("error", "Unable to delete due to dependency conflict.");
			Logger.error("{} || attempted to delete location but was unable to due to dependecy conflict.",user.getUsername());
			model.addAttribute("locations", user.getCarrier().getLocations());

			return "redirect:" + (String) session.getAttribute("redirectLocation");
		}
		model.addAttribute("locations", location);

		return "/delete/deletelocationconfirm";
	}

	/**
	 * Finds a location using the id parameter and if found, deletes the location and redirects to locations page
	 * @param id ID of the location being deleted
	 * @param model Used to add data to the model
	 * @return "redirect:/locations"
	 */
	@GetMapping("/deletelocationconfirmation/{id}")
	public String deleteLocationConfirmation(@PathVariable("id") long id, Model model) {
		Locations location = locationsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid location Id:" + id));

		User loggedInUser = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		model.addAttribute("currentPage","/locations");
		Logger.info("{} || successfully deleted the location with ID {}.",loggedInUser.getUsername(), location.getId());

		locationsRepository.delete(location);
		return "redirect:/locations";
	}

	/**
	 * Finds a location using the id parameter and if found, adds the details of that location
	 * to the locations page
	 * @param id Stores the ID of the location to be viewed
	 * @param model Used to add data to the model
	 * @return "locations"
	 */
	@GetMapping("/viewlocation/{id}")
	public String viewLocation(@PathVariable("id") long id, Model model, HttpSession session) {
		Locations location = locationsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid location Id:" + id));

		model.addAttribute("locations", location);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/locations");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "locations";
	}

	@GetMapping("/editlocations/{id}")
	public String showLocationsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Locations locations = locationsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));


		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/locations");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		session.removeAttribute("message");

		model.addAttribute("locations", locations);

		return "/edit/edit-locations";
	}

	@PostMapping("edit-locations/{id}")
	public String locationsUpdateForm(@PathVariable("id") long id, Locations locations, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		List <Locations> carrierLocations = user.getCarrier().getLocations();

		Hashtable<String, String> hashtable = new Hashtable<>();

		hashtable.put("locationName", locations.getName().strip());
		hashtable.put("streetAddress1", locations.getStreetAddress1().strip());
		hashtable.put("streetAddress2", locations.getStreetAddress2().strip());
		hashtable.put("locationCity", locations.getCity().strip()); 
		hashtable.put("locationState", locations.getState().strip());
		hashtable.put("locationZip", locations.getZip().strip());
		hashtable.put("locationLatitude", "");
		hashtable.put("locationLongitude", "");
		hashtable.put("locationType", locations.getLocationType().strip());

		Locations result;

		result = validationServiceImp.validateLocationsForm(hashtable, session);


		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-locations";
		}

		result.setId(id);

		String locationName = result.getName().toString().strip();

		for(Locations check: carrierLocations) {
			String repoLocationName = check.getName().toString().strip();
			if(locationName.equals(repoLocationName) && (result.getId() != check.getId())) {
				Logger.info("{} || attempted to save a location with the same name as another location.",user.getUsername());
				model.addAttribute("message", "Another location already exists with that name.");
				return "/edit/edit-locations";
			}
		}

		locationsRepository.save(result);
		Logger.info("{} || successfully updated location with ID {}.", user.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
	}

}
