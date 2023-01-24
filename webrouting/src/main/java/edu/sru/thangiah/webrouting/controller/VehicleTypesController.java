package edu.sru.thangiah.webrouting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
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
	private UserValidator userValidator;
	
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
  			return "/add/add-vehicletype";
		}
  		
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
  			model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
  			return "vehicletypes";
  		}
  		
  		vehicleTypesRepository.save(vehicleTypes);
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
        
        if(!vehicleTypes.getVehicles().isEmpty()) {
        	model.addAttribute("error", "Unable to delete due to dependency conflict.");
        	model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
        	return "vehicletypes";
        }
        model.addAttribute("vehicletypes", vehicleTypes);
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
  			model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());
  			return "vehicletypes";
  		}
            
        vehicleTypesRepository.save(vehicleType);
        return "redirect:/vehicletypes";
    }

}
