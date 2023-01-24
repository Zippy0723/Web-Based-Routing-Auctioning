package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
import java.util.List;

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

import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.Vehicles;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Vehicles.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/1/2022
 */

@Controller
public class VehiclesController {
	
	private VehiclesRepository vehiclesRepository;
	
	private VehicleTypesRepository vehicleTypesRepository;
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private UserValidator userValidator;
	
	/**
	 * Constructor for VehiclesController. <br>
	 * Instantiates the vehiclesRepository <br>
	 * Instantiates the vehicleTypesRepository
	 * @param vehiclesRepository Used to interact with the vehicles in the database
	 * @param vehicleTypesRepository Used to interact with the vehicle types in the database
	 */
	public VehiclesController(VehiclesRepository vehiclesRepository, VehicleTypesRepository vehicleTypesRepository) {
		this.vehiclesRepository = vehiclesRepository;
		this.vehicleTypesRepository = vehicleTypesRepository;;
	}
	
	/**
	 * Adds all of the vehicles to the "vehicles" model and redirects user to
	 * the vehicles page.
	 * @param model Used to add data to the model
	 * @return "vehicles"
	 */
	@RequestMapping({"/vehicles"})
    public String showVehicleList(Model model) {
		
		User user = getLoggedInUser();
		if (user.getRole().toString().equals("CARRIER")) {
			
			 model.addAttribute("vehicles", user.getCarrier().getVehicles());
			 return "vehicles";
		}
        model.addAttribute("vehicles", vehiclesRepository.findAll());
        return "vehicles";
    }
	
	/**
	 * Redirects user to the /add/add-vehicle page
	 * @param model Used to add data to the model
	 * @param vehicles Information on the vehicle being added
	 * @param result Ensures the information entered by the user is valid
	 * @return "/add/add-vehicle"
	 */
	@GetMapping({"/add-vehicle"})
    public String showLists(Model model, Vehicles vehicles, BindingResult result) {
		
		User user = getLoggedInUser();
		
			model.addAttribute("carriers", user.getCarrier());
			model.addAttribute("vehicleTypes", vehicleTypesRepository.findAll()); 
		    model.addAttribute("locations", user.getCarrier().getLocations()); 
	        return "/add/add-vehicle";
		
		/**
        model.addAttribute("vehicleTypes", vehicleTypesRepository.findAll()); 
        model.addAttribute("locations", locationsRepository.findAll());   
        model.addAttribute("carriers", carriersRepository.findAll());   
        return "/add/add-vehicle";
        */
    }
	
	/**
  	 * Adds a vehicle to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the vehicle is saved in the vehiclesRepository. and the user is redirect to /vehicles <br>
  	 * If there are errors, the user is redirected to the /add/add-vehicle page.
  	 * @param vehicles Information on the vehicle being added
  	 * @param result Ensures the information entered by the user is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/vehicles" or "/add/add-vehicle"
  	 */
	@RequestMapping({"/addvehicles"})
  	public String addVehicle(@Validated Vehicles vehicles, BindingResult result, Model model) {
		userValidator.addition(vehicles, result);
  		if (result.hasErrors()) {
  			return "/add/add-vehicle";
		}
  		
  		Boolean deny = false;
  		User user = getLoggedInUser();
  		List<Vehicles> checkVehicles = new ArrayList<>();
  		checkVehicles = (List<Vehicles>) vehiclesRepository.findAll();
  		
  		for(Vehicles check: checkVehicles) {
  			if(vehicles.getVinNumber().equals(check.getVinNumber().toString())
  					|| vehicles.getPlateNumber().equals(check.getPlateNumber())) {
  				deny = true;
  				break;
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Vehicle. Vehicle VIN or Plate Number already exists");
  			model.addAttribute("vehicles", user.getCarrier().getVehicles());
  			return "vehicles";
  		}
  		
  		vehiclesRepository.save(vehicles);
  		return "redirect:/vehicles";
  	}
	
	/**
  	 * Finds a vehicle using the id parameter and if found, redirects to confirmation page
  	 * If there are dependency issues, the vehicle is not deleted and an error is displayed to the user.
  	 * @param id ID of the vehicle being deleted
  	 * @param model  Used to add data to the model
  	 * @return "redirect:/vehicles"
  	 */
	@GetMapping("/deletevehicles/{id}")
    public String deleteVehicle(@PathVariable("id") long id, Model model) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        if (!vehicle.getOrders().isEmpty() || !vehicle.getShipments().isEmpty() || !vehicle.getDrivers().isEmpty()){
        	model.addAttribute("error", "Unable to delete due to dependency conflict.");
        	model.addAttribute("vehicles", vehiclesRepository.findAll());
       	 	return "vehicles";
        }
        model.addAttribute("vehicles", vehicle);
        return "/delete/deletevehicleconfirm";
    }
	
	/**
  	 * Finds a vehicle using the id parameter and if found, deletes the vehicle and redirects to vehicles page
  	 * @param id ID of the vehicle being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/vehicles"
  	 */
  	@GetMapping("/deletevehicleconfirmation/{id}")
    public String deleteVehicleConfirmation(@PathVariable("id") long id, Model model) {
  		Vehicles vehicle = vehiclesRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
  		
  		vehiclesRepository.delete(vehicle);
        return "redirect:/vehicles";
    }
	
	/**
  	 * Finds a vehicle using the id parameter and if found, adds the details of that vehicle
  	 * to the vehicles page
  	 * @param id ID of the vehicle being viewed
  	 * @param model Used to add data to the model
  	 * @return "vehicles"
  	 */
  	@GetMapping("/viewvehicle/{id}")
    public String viewVehicle(@PathVariable("id") long id, Model model) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        model.addAttribute("vehicles", vehicle);
        return "vehicles";
    }
	
	/**
  	 * Finds a vehicle using the id parameter and if found, adds the details of that vehicle
  	 * to a form and redirects the user to that update form. Also adds the vehicle types, locations, and carriers to the form.
  	 * @param id ID of the vehicle being edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-vehicle"
  	 */
	@GetMapping("/editvehicles/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
		 Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		 User user = getLoggedInUser();
		 
			
				model.addAttribute("carriers", user.getCarrier());
				model.addAttribute("vehicleTypes", vehicleTypesRepository.findAll()); 
			    model.addAttribute("locations", user.getCarrier().getLocations());
			    model.addAttribute("vehicles", vehicle);
			    return "/update/update-vehicle";
			}
        /**
		 model.addAttribute("vehicleTypes", vehicleTypesRepository.findAll()); 
	     model.addAttribute("locations", locationsRepository.findAll());   
	     model.addAttribute("carriers", carriersRepository.findAll());   
	     model.addAttribute("vehicles", vehicle);
        return "/update/update-vehicle";
        }
        */
    
	
	/**
  	 * Finds a vehicle using the id parameter and if found, adds all of the drivers of that vehicle
  	 * to the drivers page
  	 * @param id ID of the vehicle used to get the drivers
  	 * @param model Used to add data to the model
  	 * @return "drivers"
  	 */
  	@GetMapping("/viewvehicledrivers/{id}")
    public String viewVehicleDrivers(@PathVariable("id") long id, Model model) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        model.addAttribute("drivers", vehicle.getDrivers());
        return "drivers";
    }
  	
  	/**
  	 * Finds a carrier using the id parameter and if found, adds all of the shipments of that carrier
  	 * to the shipments page
  	 * @param id ID of the vehicle used to ge the shipments
  	 * @param model Used to add data to the model
  	 * @return "shipments"
  	 */
  	@GetMapping("/viewvehicleshipments/{id}")
    public String viewVehicleShipments(@PathVariable("id") long id, Model model) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        model.addAttribute("shipments", vehicle.getShipments());
        return "shipments";
    }
	
	/**
  	 * Updates a vehicle to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the vehicle is updated in the vehiclesRepository. and the user is redirect to /vehicles <br>
  	 * If there are errors, the user is redirected to the /update/update-vehicle page.
  	 * @param id ID of the vehicle being updated
  	 * @param vehicle Information on the vehicle being updated
  	 * @param result Ensures the information entered by the user is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/vehicles" or "/update/update-vehicle"
  	 */
	@PostMapping("/updatevehicle/{id}")
    public String updateVehicle(@PathVariable("id") long id, @Validated Vehicles vehicle, 
      BindingResult result, Model model) {
		userValidator.addition(vehicle, result);
        if (result.hasErrors()) {
        	vehicle.setId(id);
            return "/update/update-vehicle";
        }
        Boolean deny = false;
  		User user = getLoggedInUser();
  		List<Vehicles> checkVehicles = new ArrayList<>();
  		checkVehicles = (List<Vehicles>) vehiclesRepository.findAll();
  		
  		for(Vehicles check: checkVehicles) {
  			if(vehicle.getVinNumber().equals(check.getVinNumber().toString()) 
  					|| vehicle.getPlateNumber().equals(check.getPlateNumber())) {
  				if (vehicle.getId() != check.getId()) {
  					deny = true;
  	  				break;
  				}
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to update Vehicle. Vehicle VIN or Plate Number already exists");
  			model.addAttribute("vehicles", user.getCarrier().getVehicles());
  			return "vehicles";
  		}
  		
  		vehiclesRepository.save(vehicle);
  		return "redirect:/vehicles";
            
       
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
