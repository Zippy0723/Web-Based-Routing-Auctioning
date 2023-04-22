package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.domain.Vehicles;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.LocationsRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.ApiServiceImpl;
import edu.sru.thangiah.webrouting.services.NotificationService;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
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

	private CarriersRepository carriersRepository;

	private LocationsRepository locationsRepository;
	
	private ShipmentsRepository shipmentsRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private ValidationServiceImp validationServiceImp;

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ApiServiceImpl apiService;

	private static final Logger Logger = LoggerFactory.getLogger(VehiclesController.class);

	/**
	 * 
	 * @param vehiclesRepository Instantiates the vehicles Repository
	 * @param vehicleTypesRepository Instantiates the vehicletypes Repository
	 * @param shipmentsRepository Instantiates the shipments Repository
	 */
	public VehiclesController(VehiclesRepository vehiclesRepository, VehicleTypesRepository vehicleTypesRepository, ShipmentsRepository shipmentsRepository) {
		this.vehiclesRepository = vehiclesRepository;
		this.vehicleTypesRepository = vehicleTypesRepository;
		this.shipmentsRepository = shipmentsRepository;
	}

	/**
	 * Adds all of the required attributes to the model to render the vehicles page
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /vehicles
	 */
	
	@RequestMapping({"/vehicles"})
	public String showVehicleList(Model model, HttpSession session) {

		String redirectLocation = "/vehicles";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("currentPage","/vehicles");

		try {
			model.addAttribute("error",session.getAttribute("error"));
		} catch(Exception e) {
			//do nothing
		}
		session.removeAttribute("error");

		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

		User user = userService.getLoggedInUser();
		if (user.getRole().toString().equals("CARRIER")) {

			model.addAttribute("vehicles", user.getCarrier().getVehicles());

			User users = userService.getLoggedInUser();
			List<Notification> notifications = new ArrayList<>();

			if(!(users == null)) {
				notifications = NotificationController.fetchUnreadNotifications(users);
			}

			model.addAttribute("notifications",notifications);

			return "vehicles";
		}
		model.addAttribute("vehicles", vehiclesRepository.findAll());

		User users = userService.getLoggedInUser();
		List<Notification> notifications = new ArrayList<>();

		if(!(users == null)) {
			notifications = NotificationController.fetchUnreadNotifications(users);
		}

		model.addAttribute("notifications",notifications);

		return "vehicles";
	}

	/**
	 * Finds a vehicle using the id parameter and if found, redirects to confirmation page
	 * If there are dependency issues, the vehicle is not deleted and an error is displayed to the user.
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /delete/deletevehicleconfirm
	 */
	
	@GetMapping("/deletevehicles/{id}")
	public String deleteVehicle(@PathVariable("id") long id, Model model, HttpSession session) {
		Vehicles vehicle = vehiclesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/vehicles");
		if (!vehicle.getOrders().isEmpty() || !vehicle.getShipments().isEmpty() || !vehicle.getDrivers().isEmpty()){
			session.setAttribute("error", "Unable to delete due to dependency conflict.");
			Logger.error("{} || was unable to delete due to dependency conflict.", user.getUsername());
			model.addAttribute("vehicles", vehiclesRepository.findAll());

			return "redirect:" + (String) session.getAttribute("redirectLocation");
		}
		model.addAttribute("vehicles", vehicle);

		return "/delete/deletevehicleconfirm";
	}

	/**
	 * Finds a vehicle using the id parameter and if found, deletes the vehicle and redirects to vehicles page
	 * @param id of the vehicle being deleted
	 * @param model used to load attributes into the Thymeleaf model
	 * @return redirects to /vehicles
	 */
	
	@GetMapping("/deletevehicleconfirmation/{id}")
	public String deleteVehicleConfirmation(@PathVariable("id") long id, Model model) {
		Vehicles vehicle = vehiclesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/vehicles");
		User carrierUser = CarriersController.getUserFromCarrier(vehicle.getCarrier());

		if(user.getId() != carrierUser.getId()) {
			notificationService.addNotification(carrierUser, 
					"ALERT: Your vehicle with plate number " + vehicle.getPlateNumber() + " was deleted by " + user.getUsername(), false);
		}

		Logger.info("{} || successfully deleted the vehicle with ID {}." ,user.getUsername() ,vehicle.getId());
		vehiclesRepository.delete(vehicle);
		return "redirect:/vehicles";
	}

	/**
	 * Finds a vehicle using the id parameter and if found, adds the details of that vehicle to the vehicles page
	 * @param id of the vehicle being viewed
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /vehicles
	 */
	
	@GetMapping("/viewvehicle/{id}")
	public String viewVehicle(@PathVariable("id") long id, Model model, HttpSession session) {

		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		Vehicles vehicle = vehiclesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));

		model.addAttribute("vehicles", vehicle);
		model.addAttribute("currentPage","/vehicles");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "vehicles";
	}

	/**
	 * Finds a vehicle using the id parameter and if found, adds all of the drivers of that vehicle to the drivers page
	 * @param id of the vehicle being viewed
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /drivers
	 */
	@GetMapping("/viewvehicledrivers/{id}")
	public String viewVehicleDrivers(@PathVariable("id") long id, Model model, HttpSession session) {
		Vehicles vehicle = vehiclesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));

		model.addAttribute("currentPage","/drivers");
		model.addAttribute("drivers", vehicle.getDrivers());
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "drivers";
	}

	/**
	 * Finds a carrier using the id parameter and if found, adds all of the shipments of that carrier to the shipments page
	 * @param id of the vehicle used to ge the shipments
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /shipments
	 */
	
	@GetMapping("/viewvehicleshipments/{id}")
	public String viewVehicleShipments(@PathVariable("id") long id, Model model, HttpSession session) {
		Vehicles vehicle = vehiclesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));

		model.addAttribute("shipments", vehicle.getShipments());
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/vehicles");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "shipments";
	}

	/**
	 * Adds all of the required attributes to the model to render the edit vehicles page
	 * @param id of the vehicle being edited 
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /edit/edit-vehicles
	 */

	@GetMapping("/editvehicles/{id}")
	public String showVehiclesEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Vehicles vehicle = vehiclesRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		User user = userService.getLoggedInUser();
		model.addAttribute("currentPage","/vehicles");

		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));

		model.addAttribute("vehicleTypes", user.getCarrier().getVehicleTypes()); 
		model.addAttribute("locations", user.getCarrier().getLocations());
		model.addAttribute("vehicles", vehicle);

		session.removeAttribute("message");

		return "/edit/edit-vehicles";

	}


	/**
	 * Receives the new vehicles object and passes it off to be validated
	 * Once valid the object is saved to the vehicles repository
	 * @param id of the vehicle being edited
	 * @param vehicle holds the new vehicle object submitted by the user
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirects to /vehicles or /edit/edit-vehicles
	 */
	
	@PostMapping("edit-vehicles/{id}")
	public String vehicleUpdateForm(@PathVariable("id") long id, Vehicles vehicle, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("vehicleTypes", user.getCarrier().getVehicleTypes()); 
		model.addAttribute("locations", user.getCarrier().getLocations());
		List <Vehicles> carrierVehicles = user.getCarrier().getVehicles();

		Hashtable<String, String> hashtable = new Hashtable<>();

		hashtable.put("plate", vehicle.getPlateNumber().strip());
		hashtable.put("vin", vehicle.getVinNumber().strip());
		hashtable.put("manufacturedYear", vehicle.getManufacturedYear().strip());

		Vehicles result;

		result = validationServiceImp.validateVehiclesForm(hashtable, session);


		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-vehicles";
		}

		result.setId(id);
		result.setVehicleType(vehicle.getVehicleType());
		result.setLocation(vehicle.getLocation());

		String plateVin = result.getPlateNumber().strip() + " " + result.getVinNumber().strip();

		for(Vehicles check: carrierVehicles) {
			String repoPlateVin = check.getPlateNumber() + " " + check.getVinNumber().strip();
			if(repoPlateVin.equals(plateVin) && (result.getId() != check.getId())) {
				Logger.info("{} || attempted to save a vehicle with the plate and vin as another vehicle type.",user.getUsername());
				model.addAttribute("message", "Another vehicle exists with that vin and plate.");
				return "/edit/edit-vehicles";
			}
		}

		vehiclesRepository.save(result);
		Logger.info("{} || successfully updated vehicle with ID {}.", user.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
	}
	
	/**
	 * Adds all of the required attributes to the model to render the assign vehicles page
	 * @param shipmentId holds the id of the shipment being assigned
	 * @param model used to load attributes into the Thymeleaf model
	 * @return /assignvehicle
	 */
	
	@RequestMapping("assignvehicle/{id}")
	public String assignVehicle(@PathVariable("id") long shipmentId, Model model) {
		User user = userService.getLoggedInUser();
		model.addAttribute("currentPage","/shipments");
		List<Vehicles> vehicles = (List<Vehicles>) user.getCarrier().getVehicles();
		Shipments shipment = shipmentsRepository.findById(shipmentId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + shipmentId));
		
		model.addAttribute("shipment",shipment);
		model.addAttribute("vehicles",vehicles);
		
		
		model = NotificationController.loadNotificationsIntoModel(user, model);
		return "assignvehicle";
	}
	
	/**
	 * Attaches a vehicle to a shipment
	 * @param shipmentId holds the id of the shipment being assigned
	 * @param vehicle holds the vehicle being assigned
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirectLocation
	 */
	
	@PostMapping("/assignvehicletransaction/{id}")
	public String assignVehicle(@PathVariable("id") long shipmentId, @RequestParam("vehicle") Vehicles vehicle, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		Shipments shipment = shipmentsRepository.findById(shipmentId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + shipmentId));
		
		shipment.setVehicle(vehicle);
		shipmentsRepository.save(shipment);
		
		return "redirect:" + redirectLocation;
	}
	
	/**
	 * Finds the closest vehicle to the shipment location based on location
	 * @param shipmentId holds the id of the shipment being assigned
	 * @return minKey
	 * @throws NumberFormatException
	 * @throws UnsupportedEncodingException
	 */
	
	@GetMapping("getbestvehicle/{shipmentId}")
	@ResponseBody
	public Long getBestVehicleId(@PathVariable("shipmentId") long shipmentId) throws NumberFormatException, UnsupportedEncodingException { //gets the vehicles that's location is closests to the shipments start location.
		Shipments shipment = shipmentsRepository.findById(shipmentId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + shipmentId));
		String startingLat = shipment.getShipperLatitude();
		String startingLng = shipment.getShipperLongitude();
		User user = userService.getLoggedInUser();
		List<Vehicles> vehicles = (List<Vehicles>) user.getCarrier().getVehicles();
		HashMap<Long,Double> distances = new HashMap<Long,Double>();
		
		for(Vehicles v : vehicles) {
			distances.put(v.getId(),Double.parseDouble(apiService.fetchDistanceBetweenCoordinates(startingLat, startingLng, v.getLocation().getLatitude(), v.getLocation().getLongitude()).replaceAll(",", "")));
		}
		
		long minKey = 0;
		Double minValue = Double.MAX_VALUE;

		for (Map.Entry<Long, Double> entry : distances.entrySet()) {
		    long key = entry.getKey();
		    Double value = entry.getValue();
		    if (value < minValue) {
		        minKey = key;
		        minValue = value;
		    }
		}
		
		return (Long)minKey;
	}
}
