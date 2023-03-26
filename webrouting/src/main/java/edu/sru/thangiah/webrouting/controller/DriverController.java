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

import edu.sru.thangiah.webrouting.domain.Driver;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.DriverRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;

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
    private SecurityService securityService;
    
    private static final Logger Logger = LoggerFactory.getLogger(DriverController.class);
    
	/**
	 * Constructor for DriverController. <br>
	 * Instantiates the driverRepository
	 * @param driverRepository Used to interact with drivers in the database
	 */
	public DriverController (DriverRepository driverRepository) {
		this.driverRepository = driverRepository;
	}
	
	/**
	 * Adds all of the drivers to the "drivers" model and redirects user to
	 * the drivers page.
	 * @param model Used to add data to the model
	 * @return "drivers"
	 */
	@RequestMapping({"/drivers"})
    public String showDriversList(Model model, HttpSession session) {
        try {
            model.addAttribute("error",session.getAttribute("error"));
        } catch(Exception e){
            //do nothing
        }
        session.removeAttribute("error");
        
		String redirectLocation = "/drivers";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		if (user.getRole().toString().equals("CARRIER")) {
			
			 model.addAttribute("drivers", user.getCarrier().getDrivers());
			 
			 return "drivers";
		}
		
		model.addAttribute("drivers", driverRepository.findAll());
		
        return "drivers";
    }
	
	/**
	 * Redirects user to the /add/add-driver page <br>
	 * Adds all of the carriers, contacts, and vehicles to the model
	 * @param model Used to add data to the model
	 * @param drivers Stores the information for the driver that is being added
	 * @param result Ensures the input from the user are valid
	 * @return "/add/add-driver"
	 */
	@GetMapping({"/add-driver"})
    public String showLists(Model model, Driver drivers, BindingResult result, HttpSession session) {
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("carriers", user.getCarrier());
		model.addAttribute("contacts", user.getCarrier().getContacts());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());
		
        return "/add/add-driver";
    }
	
	/**
  	 * Adds a driver to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the driver is saved in the driverRepository. and the user is redirect to /drivers <br>
  	 * If there are errors, the user is redirected to the /add/add-driver page.
  	 * @param drivers Stores the information of the driver that is to be added
  	 * @param result Ensures the inputs from the user are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/drivers" or "/add/add-driver"
  	 */
	@RequestMapping({"/adddriver"})
  	public String addDriver(@Validated Driver drivers, BindingResult result, Model model, HttpSession session) {
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        String redirectLocation = (String) session.getAttribute("redirectLocation");
        model.addAttribute("redirectLocation", redirectLocation);
  		if (result.hasErrors()) {
  			return "/add/add-driver";
		}
  		
  		Boolean deny = false;
  		List<Driver> checkDrivers = new ArrayList<>();
  		checkDrivers = (List<Driver>) driverRepository.findAll();
  		
  		for(Driver check: checkDrivers) {
  			if(drivers.getContact().toString().equals(check.getContact().toString()) ) {
  				deny = true;
  				break;
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Driver. Lisence number already exists or Contact already in use");
  			Logger.error("{} was unable to add a driver because lisence number already exists or contact already in use for driver with ID {}.", user.getUsername(), drivers.getId());
  			model.addAttribute("drivers", user.getCarrier().getDrivers());
  			
  			return "drivers";
  		}
  		
  		driverRepository.save(drivers);
  		Logger.info("{} sucessfully added new driver with ID {}.", user.getUsername(), drivers.getId());
  		
  		return "redirect:" + redirectLocation;
  	}
	
	/**
     * Redirects user to the /uploaddrivers page when clicking "Upload an excel file" button in the Drivers section of Carrier login
     * @param model used to add data to the model
     * @return "/uploaddrivers"
     */
    
    @RequestMapping({"/uploaddrivers"})
    public String showAddDriversExcel(Model model) {
 	   return "/uploaddrivers";
    }
	
	/**
  	 * Finds a driver using the id parameter and if found, redirects user to confrimation page
  	 * @param id Stores the ID of the driver to be deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/drivers"
  	 */
	@GetMapping("/deletedriver/{id}")
    public String deleteDriver(@PathVariable("id") long id, Model model) {
        Driver drivers = driverRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
        model.addAttribute("drivers", drivers);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "/delete/deletedriverconfirm";
    }
    
    /**
  	 * Finds a driver using the id parameter and if found, deletes the driver and redirects to drivers page
  	 * @param id ID of the driver being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/drivers"
  	 */
  	@GetMapping("/deletedriverconfirmation/{id}")
    public String deleteDriverConfirmation(@PathVariable("id") long id, Model model) {
  		Driver drivers = driverRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
  		
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
  		Logger.info("{} successfully deleted driver with ID {}.", user.getUsername(), drivers.getId());
  		driverRepository.delete(drivers);
  	    return "redirect:/drivers";
    }
	
	/**
  	 * Finds a driver using the id parameter and if found, adds the details of that driver
  	 * to the drivers page
  	 * @param id Stores the ID of the driver to be viewed
  	 * @param model Used to add data to the model
  	 * @return "drivers"
  	 */
  	@GetMapping("/viewdriver/{id}")
    public String viewDriver(@PathVariable("id") long id, Model model) {
        Driver driver = driverRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
        
        model.addAttribute("drivers", driver);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "drivers";
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
