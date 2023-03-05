package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
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
    private SecurityService securityService;
    
    @Autowired
    private UserValidator userValidator;
	
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
		
		String redirectLocation = "/locations";         
		session.setAttribute("redirectLocation", redirectLocation);         
		model.addAttribute("redirectLocation", redirectLocation);

		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		if (user.getRole().toString().equals("CARRIER")) {
			
			 model.addAttribute("locations", user.getCarrier().getLocations());
			 
			 return "locations";
		}
		
        model.addAttribute("locations", locationsRepository.findAll());
        
        return "locations";
    }
	
	/**
	 * Redirects user to the /add/add-location page
	 * @param model Used to add data to the model
	 * @param location Stores the information on the location
	 * @param result Ensures the user inputs are valid
	 * @return "/add/add-location"
	 */
	@GetMapping({"/add-location"})
    public String showCarriersList(Model model, Locations location, BindingResult result, HttpSession session) {
		
		User user = getLoggedInUser();
		
		model.addAttribute("carriers", user.getCarrier());  
        model = NotificationController.loadNotificationsIntoModel(user, model);
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        
	    return "/add/add-location";
		
    }
	
	/**
  	 * Adds a location to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the location is saved in the locationsRepository. and the user is redirect to /locations <br>
  	 * If there are errors, the user is redirected to the /add/add-location page.
  	 * @param location Stores the information on the location
  	 * @param result Ensures the user inputs are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/locations" or "/add/add-location"
  	 */
	@RequestMapping({"/addlocations"})
  	public String addLocation(@Validated Locations location, BindingResult result, Model model, HttpSession session) {
		userValidator.addition(location, result);
		
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		
  		if (result.hasErrors()) {
  			return "/add/add-location";
  		}
  		Boolean deny = false;
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
  		List<Locations> checkLocation = new ArrayList<>();
  		checkLocation = (List<Locations>) locationsRepository.findAll();
  		
  		for(Locations check: checkLocation) {
  			if(location.getStreetAddress1().toString().equals(check.getStreetAddress1().toString())  ||
  					location.getName().equals(check.getName())) {
  				deny = true;
  				break;
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Location. Location address or name already exists");
  			Logger.error("{} was unable to add location {} because the address or name already exists.", user.getUsername(), location.getName());
  			model.addAttribute("locations", user.getCarrier().getLocations());
  			return "/locations";
			 
  		}
  		locationsRepository.save(location);
  		Logger.info("{} successfully saved location with ID {}.", user.getUsername(), location.getId());
  		return "redirect:" + redirectLocation;
  	}
	
	/**
     * Redirects user to the /uploadlocations page when clicking "Upload an excel file" button in the locations section of Carrier login
     * @param model used to add data to the model
     * @return "/uploadlocations"
     */
    
    @RequestMapping({"/uploadlocations"})
    public String showAddLocationsExcel(Model model) {
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
    public String deleteLocation(@PathVariable("id") long id, Model model) {
        Locations location = locationsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid location Id:" + id));
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        if (!location.getVehicles().isEmpty()) {
        	model.addAttribute("error", "Unable to delete due to dependency conflict.");
        	Logger.error("Unable to delete location due to dependecy conflict.");
        	model.addAttribute("locations", user.getCarrier().getLocations());
        	
        	return "locations";
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
  		
  		User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		Logger.info("{} successfully deleted the location with ID {}.",loggedInUser.getUsername(), location.getId());
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
    public String viewLocation(@PathVariable("id") long id, Model model) {
        Locations location = locationsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid location Id:" + id));
        
        model.addAttribute("locations", location);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "locations";
    }
	
	/**
  	 * Finds a location using the id parameter and if found, adds the details of that location
  	 * to a form and redirects the user to that update form. Also adds the contractors to the form.
  	 * @param id Stores the ID of the location to be edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-location"
  	 */
	@GetMapping("/editlocations/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Locations location = locationsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Location Id:" + id));
		
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
    	model.addAttribute("carriers", user.getCarrier());
		model.addAttribute("locations", location);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		return "/update/update-location";
		
    }
	
	/**
  	 * Updates a location to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the location is updated in the locationsRepository. and the user is redirected to /locations <br>
  	 * If there are errors, the user is redirected to the /update/update-location page.
  	 * @param id Stores the ID of the location to be updated
  	 * @param location Stores the information on the location
  	 * @param result Ensures the user inputs are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/locations" or "/update/update-location"
  	 */
	@PostMapping("/updatelocation/{id}")
    public String updateLocation(@PathVariable("id") String idString, @Validated Locations location, 
      BindingResult result, Model model,HttpSession session) {
		
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		
		User user = getLoggedInUser();
	    model = NotificationController.loadNotificationsIntoModel(user, model);

		long id = Long.parseLong(idString);
		userValidator.addition(location, result);
        if (result.hasErrors()) {
        	location.setId(id);
            return "/update/update-location";
        }
        
        Boolean deny = false;
       
  		List<Locations> checkLocation = new ArrayList<>();
  		checkLocation = (List<Locations>) locationsRepository.findAll();
  		
  		for(Locations check: checkLocation) {
  			if(location.getStreetAddress1().toString().equals(check.getStreetAddress1().toString()) && location.getId() != check.getId() ) {
  				deny = true;
  				break;
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to update Location. Location address already exists");
  			Logger.error("{} attempted to update location. Update failed due to location already existing.", user.getUsername());
  			model.addAttribute("locations", user.getCarrier().getLocations());
  			return "/locations";
			 
  		}
        locationsRepository.save(location);
        Logger.info("{} successfully updated location with ID {}.", user.getUsername(), location.getId());
        return "redirect:" + redirectLocation;
    }
	
	/**
	 * Returns the user that is currently logged into the system. <br>
	 * If there is no user logged in, null is returned.
	 * @return user2 or null
	 */
	public User getLoggedInUser() {
    	if (securityService.isAuthenticated()) {
    		org.springframework.security.core.userdetails.User user = 
    				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		
    		User user2 = userService.findByUsername(user.getUsername());
    		
    		return user2;
    	}
    	else {
    		return null;
    	}
    }

}
