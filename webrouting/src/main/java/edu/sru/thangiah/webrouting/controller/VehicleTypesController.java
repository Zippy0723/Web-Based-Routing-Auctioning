package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
import java.util.List;

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

import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Vehicle Types.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/1/2022
 */

@Controller
public class VehicleTypesController {
	
	private VehicleTypesRepository vehicleTypesRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserValidator userValidator;
	
	private static final Logger Logger = LoggerFactory.getLogger(VehicleTypesController.class);
	
	/**
	 * Constructor for VehicleTypesController. <br>
	 * Instantiates the vehicleTypesRepository
	 * @param vehicleTypesRepository Used to interact with the vehicle types in the database
	 */
	public VehicleTypesController(VehicleTypesRepository vehicleTypesRepository) {
		this.vehicleTypesRepository = vehicleTypesRepository;
	}
	
	/**
	 * Adds all of the vehicle types to the "vehicletypes" model and redirects user to
	 * the vehicletypes page.
	 * @param model Used to add data to the model
	 * @return "locations"
	 */
	@RequestMapping({"/vehicletypes"})
    public String showVehicleTypeList(Model model) {
        model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
        
        User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
        
        return "vehicletypes";
    }
	
	/**
	 * Redirects user to the /add/add-location page
	 * @param model Used to add data to the model
	 * @param vehicleTypes Used to store the information on the vehicle type being added
	 * @param result Ensures the information provided by the user is valid
	 * @return "/add/add-vehicletype"
	 */
	@RequestMapping({"/signupvehicletype"})
    public String showVehicleTypeSignUpForm(Model model, VehicleTypes vehicleTypes, BindingResult result) {
        
		User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
		
		return "/add/add-vehicletype";
	}
	
	/**
  	 * Adds a vehicle type to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the vehicle type is saved in the vehicleTypesRepository. and the user is redirect to /vehicletypes <br>
  	 * If there are errors, the user is redirected to the /add/add-vehicletype page.
  	 * @param vehicleTypes Information on the vehicle type being added
  	 * @param result Ensure the information provided by the user is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/vehicletypes" or "/add/add-vehicletype"
  	 */
	@RequestMapping({"/addvehicletypes"})
  	public String addVehicleType(@Validated VehicleTypes vehicleTypes, BindingResult result, Model model) {
		userValidator.addition(vehicleTypes, result);
  		if (result.hasErrors()) {
  			
  			User users = getLoggedInUser();
  	        List<Notification> notifications = new ArrayList<>();
  	        
  	        if(!(users == null)) {
  	            notifications = NotificationController.fetchUnreadNotifications(users);
  	        }
  	        
  	        model.addAttribute("notifications",notifications);
  			
  			return "/add/add-vehicletype";
		}
  		
  		User loggedInUser = getLoggedInUser();
  		
  		boolean deny = false;
  		
  		List<VehicleTypes> types = (List<VehicleTypes>) vehicleTypesRepository.findAll();
  		
  		for (VehicleTypes vt : types) {
  			if (vt.getType().equals(vehicleTypes.getType()) && vt.getSubType().equals(vehicleTypes.getSubType())) {
  				deny = true;
  				break;
  			}
  		}
  		
  		if (deny == true) {
  			model.addAttribute("error", "Error: Vehicle Type already exists.");
  			Logger.error("{} failed to update vehicle type because it already exists.", loggedInUser.getUsername());
  			model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
  			
  			User users = getLoggedInUser();
  	        List<Notification> notifications = new ArrayList<>();
  	        
  	        if(!(users == null)) {
  	            notifications = NotificationController.fetchUnreadNotifications(users);
  	        }
  	        
  	        model.addAttribute("notifications",notifications);
  			
  			return "vehicletypes";
  		}
  		
  		vehicleTypesRepository.save(vehicleTypes);
  		Logger.info("{} successfully saved the Vehicle type with ID {}.",loggedInUser.getUsername(), vehicleTypes.getId());
  		
  		User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
  		
  		return "redirect:/vehicletypes";
  	}
	
	/**
  	 * Finds a vehicle type using the id parameter and if found, redirects to confirmation page
  	 * If there are dependency issues, the vehicle is not deleted and an error is displayed to the user.
  	 * @param id ID of the vehicle type being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/vehicletypes"
  	 */
	@GetMapping("/deletevehicletype/{id}")
    public String deleteVehicleType(@PathVariable("id") long id, Model model) {
        VehicleTypes vehicleTypes = vehicleTypesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));
        
        User loggedInUser = getLoggedInUser();
        if(!vehicleTypes.getVehicles().isEmpty()) {
        	model.addAttribute("error", "Unable to delete due to dependency conflict.");
        	Logger.error("{} failed to delete the vehicle type due to dependency conflict.", loggedInUser.getUsername());
        	model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
        	
        	User users = getLoggedInUser();
            List<Notification> notifications = new ArrayList<>();
            
            if(!(users == null)) {
                notifications = NotificationController.fetchUnreadNotifications(users);
            }
            
            model.addAttribute("notifications",notifications);
            
        	return "vehicletypes";
        }
        model.addAttribute("vehicletypes", vehicleTypes);
        
        User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
        
        return "/delete/deletevehicletypeconfirm";
    }
	
	/**
  	 * Finds a vehicle type using the id parameter and if found, deletes the vehicle type and redirects to vehicle types page
  	 * @param id ID of the vehicle type being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/contacts"
  	 */
  	@GetMapping("/deletevehicletypeconfirmation/{id}")
    public String deleteContactConfirmation(@PathVariable("id") long id, Model model) {
  		VehicleTypes vehicleTypes = vehicleTypesRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));
  		
  		User loggedInUser = getLoggedInUser();
  		Logger.info("{} successfully deleted the Vehicle type with ID {}.", loggedInUser.getUsername(),vehicleTypes.getId());
  		vehicleTypesRepository.delete(vehicleTypes);
        return "redirect:/vehicletypes";
    }
	
	/**
  	 * Finds a location using the id parameter and if found, adds the details of that location
  	 * to the locations page
  	 * @param id ID of the vehicle type being viewed 
  	 * @param model Used to add data to the model
  	 * @return "locations"
  	 */
  	@GetMapping("/viewvehicletype/{id}")
    public String viewVehicleType(@PathVariable("id") long id, Model model) {
        VehicleTypes vehicleType = vehicleTypesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));
        
        model.addAttribute("vehicletypes", vehicleType);
        
        User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
        
        return "vehicletypes";
    }
	
	/**
  	 * Finds a vehicle type using the id parameter and if found, adds the details of that vehicle type
  	 * to a form and redirects the user to that update form.
  	 * @param id ID of the vehicle type being edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-vehicletype"
  	 */
	@GetMapping("/editvehicletype/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
		VehicleTypes vehicleTypes = vehicleTypesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vechile type Id:" + id));
        
		model.addAttribute("vehicleTypes", vehicleTypes);
		
		User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
		
        return "/update/update-vehicletype";
    }
	
	/**
  	 * Updates a vehicle type to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the vehicle type is updated in the vehicleTypesRepository. and the user is redirect to /vehicletypes <br>
  	 * If there are errors, the user is redirected to the /update/update-vehicletype page.
  	 * @param id ID of the vehicle type being updated
  	 * @param vehicleType Information on the vehicle type being updated
  	 * @param result Ensures the information entered by the user is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/vehicletypes" or "/update/update-vehicletype"
  	 */
	@PostMapping("/updatevehicletype/{id}")
    public String updateVehicleType(@PathVariable("id") long id, @Validated VehicleTypes vehicleType, 
      BindingResult result, Model model) {
		userValidator.addition(vehicleType, result);
		
		User loggedInUser = getLoggedInUser();
		
        if (result.hasErrors()) {
        	vehicleType.setId(id);
            return "/update/update-vehicletype";
        }
        
    	boolean deny = false;
  		
  		List<VehicleTypes> types = (List<VehicleTypes>) vehicleTypesRepository.findAll();
  		
  		for (VehicleTypes vt : types) {
  			if (vt.getType().equals(vehicleType.getType()) && vt.getSubType().equals(vehicleType.getSubType())) {
  				if (vt.getId() != vehicleType.getId()) {
  					deny = true;
  					break;
  				}
  			}
  		}
  		
  		if (deny == true) {
  			model.addAttribute("error", "Error: Vehicle Type already exists.");
  			Logger.error("{} failed to update vehicle Type because the Vehicle type already exists.", loggedInUser.getUsername());
  			model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
  			return "vehicletypes";
  		}
        
        vehicleTypesRepository.save(vehicleType);
        Logger.info("{} successfully updated the Vehicle Type with ID {}.",loggedInUser.getUsername(),vehicleType.getId());
        return "redirect:/vehicletypes";
    }
	
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
