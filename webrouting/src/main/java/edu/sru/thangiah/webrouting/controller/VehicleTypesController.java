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

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
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

	@Autowired
	private ValidationServiceImp validationServiceImp;

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
	public String showVehicleTypeList(Model model, HttpSession session) {
		User user = getLoggedInUser();
		Carriers carrier = user.getCarrier();
		String redirectLocation = "/vehicletypes";

		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("vehicletypes", carrier.getVehicleTypes());
		model.addAttribute("currentPage","/vehicletypes");

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

		model = NotificationController.loadNotificationsIntoModel(user, model);

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
	public String showVehicleTypeSignUpForm(Model model, VehicleTypes vehicleTypes, BindingResult result, HttpSession session) {

		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/vehicletypes");
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
	public String addVehicleType(@Validated VehicleTypes vehicleTypes, BindingResult result, Model model, HttpSession session) {
		userValidator.addition(vehicleTypes, result);
		User loggedInUser = getLoggedInUser();
		Carriers carrier = loggedInUser.getCarrier();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation")); 
		model.addAttribute("currentPage","/vehicletypes");

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
			Logger.error("{} || failed to update vehicle type because it already exists.", loggedInUser.getUsername());
			model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());

			return "vehicletypes";
		}

		vehicleTypes.setCarrier(carrier);
		vehicleTypesRepository.save(vehicleTypes);
		Logger.info("{} || successfully saved the Vehicle type with ID {}.",loggedInUser.getUsername(), vehicleTypes.getId());

		return "redirect:" + redirectLocation;
	}


	/**
	 * Redirects user to the /uploadvehicletypes page when clicking "Upload an excel file" button in the Vehicle Types section of Carrier login
	 * @param model used to add data to the model
	 * @return "/uploadvehicletypes"
	 */

	@RequestMapping({"/uploadvehicletypes"})
	public String showAddVehicleTypesExcel(Model model) {
		model.addAttribute("currentPage","/vehicletypes");
		return "/uploadvehicletypes";
	}

	/**
	 * Finds a vehicle type using the id parameter and if found, redirects to confirmation page
	 * If there are dependency issues, the vehicle is not deleted and an error is displayed to the user.
	 * @param id ID of the vehicle type being deleted
	 * @param model Used to add data to the model
	 * @return "redirect:/vehicletypes"
	 */
	@GetMapping("/deletevehicletype/{id}")
	public String deleteVehicleType(@PathVariable("id") long id, Model model, HttpSession session) {
		VehicleTypes vehicleTypes = vehicleTypesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));

		User loggedInUser = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		model.addAttribute("currentPage","/vehicletypes");
		if(!vehicleTypes.getVehicles().isEmpty()) {
			session.setAttribute("error", "Unable to delete due to dependency conflict.");
			Logger.error("{} || failed to delete the vehicle type due to dependency conflict.", loggedInUser.getUsername());
			model.addAttribute("vehicletypes", vehicleTypesRepository.findAll());

			return "redirect:" + (String) session.getAttribute("redirectLocation");
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

		User loggedInUser = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		model.addAttribute("currentPage","/vehicletypes");
		Logger.info("{} || successfully deleted the Vehicle type with ID {}.", loggedInUser.getUsername(),vehicleTypes.getId());

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
	public String viewVehicleType(@PathVariable("id") long id, Model model, HttpSession session) {
		VehicleTypes vehicleType = vehicleTypesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));

		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));

		model.addAttribute("vehicletypes", vehicleType);
		model.addAttribute("currentPage","/vehicletypes");

		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "vehicletypes";
	}

	@GetMapping("/editvehicletypes/{id}")
	public String showVehicleTypesEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		VehicleTypes vehicleType = vehicleTypesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));

		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));

		model.addAttribute("vehicleTypes", vehicleType);
		model.addAttribute("currentPage","/vehicletypes");

		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		session.removeAttribute("message");

		return "/edit/edit-vehicletypes";
	}


	@PostMapping("edit-vehicletypes/{id}")
	public String vehicleTypeUpdateForm(@PathVariable("id") long id, VehicleTypes vehicleType, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		List <VehicleTypes> carrierVehicleTypes = user.getCarrier().getVehicleTypes();

		Hashtable<String, String> hashtable = new Hashtable<>();

		hashtable.put("type", vehicleType.getType().strip());
		hashtable.put("subType", vehicleType.getSubType());
		hashtable.put("description", vehicleType.getDescription().strip());
		hashtable.put("make", vehicleType.getMake().strip());
		hashtable.put("model", vehicleType.getModel().strip());
		hashtable.put("minimumWeight", vehicleType.getMinimumWeight());
		hashtable.put("maximumWeight", vehicleType.getMaximumWeight());
		hashtable.put("capacity", vehicleType.getCapacity());
		hashtable.put("maximumRange", vehicleType.getMaximumRange());
		hashtable.put("restrictions", vehicleType.getRestrictions().strip());
		hashtable.put("height", vehicleType.getHeight());
		hashtable.put("emptyWeight", vehicleType.getEmptyWeight());
		hashtable.put("length", vehicleType.getLength());
		hashtable.put("minimumCubicWeight", vehicleType.getMinimumCubicWeight());
		hashtable.put("maximumCubicWeight", vehicleType.getMaximumCubicWeight());

		VehicleTypes result;

		result = validationServiceImp.validateVehicleTypesForm(hashtable, session);


		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-vehicletypes";
		}

		result.setId(id);

		String makeModel = result.getMake().strip() + " " + result.getModel().strip();

		for(VehicleTypes check: carrierVehicleTypes) {
			String repoMakeModel = check.getMake().strip() + " " + check.getModel().strip();
			if(repoMakeModel.equals(makeModel) && (result.getId() != check.getId())) {
				Logger.info("{} || attempted to save a vehicle type with the same make and model as another vehicle type.",user.getUsername());
				model.addAttribute("message", "Another vehicle type exists with that make and model.");
				return "/edit/edit-vehicletypes";
			}
		}

		vehicleTypesRepository.save(result);
		Logger.info("{} || successfully updated vehicle type with ID {}.", user.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
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
