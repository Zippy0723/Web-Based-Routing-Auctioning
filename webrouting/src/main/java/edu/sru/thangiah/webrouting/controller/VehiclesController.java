package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.NotificationService;
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
	
	private CarriersRepository carriersRepository;
	
	private LocationsRepository locationsRepository;
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private NotificationService notificationService;
    
    private static final Logger Logger = LoggerFactory.getLogger(VehiclesController.class);
	
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
		
		User user = getLoggedInUser();
		if (user.getRole().toString().equals("CARRIER")) {
			
			 model.addAttribute("vehicles", user.getCarrier().getVehicles());
			 
			 User users = getLoggedInUser();
		        List<Notification> notifications = new ArrayList<>();
		        
		        if(!(users == null)) {
		            notifications = NotificationController.fetchUnreadNotifications(users);
		        }
		        
		        model.addAttribute("notifications",notifications);
			 
			 return "vehicles";
		}
        model.addAttribute("vehicles", vehiclesRepository.findAll());
        
        User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
        
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
    public String showLists(Model model, Vehicles vehicles, BindingResult result, HttpSession session) {
		
		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
			model.addAttribute("carriers", user.getCarrier());
			model.addAttribute("vehicleTypes", user.getCarrier().getVehicleTypes()); 
		    model.addAttribute("locations", user.getCarrier().getLocations()); 
		    model.addAttribute("currentPage","/vehicles");
		    
	        return "/add/add-vehicle";
		
		/**
        model.addAttribute("vehicleTypes", vehicleTypesRepository.findAll()); 
        model.addAttribute("locations", locationsRepository.findAll());   
        model.addAttribute("carriers", carriersRepository.findAll());   
        return "/add/add-vehicle";
        */
    }
	
	/**
     * Redirects user to the /upload-vehicles page when clicking "Upload an excel file" button in the vehicles section of Carrier login
     * @param model used to add data to the model
     * @return "/upload-vehicles"
     */
    
	@PostMapping("/upload-vehicles")
	public String LoadFromExcelData(@RequestParam("file") MultipartFile excelData){
		XSSFWorkbook workbook;
		try {
			User user = getLoggedInUser();
			workbook = new XSSFWorkbook(excelData.getInputStream());
	
		
			XSSFSheet worksheet = workbook.getSheetAt(0);
			
			List<Carriers> carriersList;
			carriersList = (List<Carriers>) carriersRepository.findAll();
			
			
			List<VehicleTypes> vehicleTypeList;
			vehicleTypeList = (List<VehicleTypes>) vehicleTypesRepository.findAll();
			
			List<Long> vehicleTypeIdList = new ArrayList<Long>();
			
			for (VehicleTypes v: vehicleTypeList) {
				vehicleTypeIdList.add(v.getId());
			}
			
			
			List<Locations> locationsList;
			locationsList = (List<Locations>) locationsRepository.findAll();
			
			List<Long> locationIdList = new ArrayList<Long>();
			
			for (Locations l: locationsList) {
				locationIdList.add(l.getId());
			}
			
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				
				
				 
				Vehicles vehicle = new Vehicles();
		        XSSFRow row = worksheet.getRow(i);
		        
		        if(row.getCell(0).getStringCellValue().isEmpty() || row.getCell(0)== null ) {
		        	break;
		        }
	    		
	    		
	    		String manufacturedYear = row.getCell(0).toString().strip();
			    String plateNumber = row.getCell(1).toString().strip();
			    String vinNumber = row.getCell(2).toString().strip();
			    
	    		String location = row.getCell(3).toString().strip();
	    		long locationID = Long.parseLong(location);			//May need to be in try catch
	    		
	    		String vehicleType = row.getCell(4).toString().strip();
	    		long vehicleTypeID = Long.parseLong(vehicleType);
	    	
	    		

	    		if (!(manufacturedYear.length() == 4) || !(manufacturedYear.matches("^[0-9]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a vehicle but the Manufactured Year must be 4 numeric characters.",user.getUsername());
	    			continue;
	    		}
	    		
	    		
	    		if(!(plateNumber.length() < 12 && plateNumber.length() > 0 && plateNumber.matches("^[a-zA-Z0-9.]+$"))) { 
	    			workbook.close();
	    			Logger.error("{} attempted to upload a vehicle but the Plate Number must be between 0 and 12 alphanumeric characters.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(vinNumber.length() < 17 && vinNumber.length() > 0) || !(vinNumber.matches("^[a-zA-Z0-9]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a vehicle but the Vin Number must be between 0 and 17 alphanumeric characters.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(vehicleTypeIdList.contains(vehicleTypeID))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a vehicle but the Vehicle Type ID does not exist.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(locationIdList.contains(locationID))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a vehicle but the Vehicle Type ID does not exist.",user.getUsername());
	    			continue;
	    		}
	    		
	    		
	    		
	    		vehicle.setManufacturedYear(manufacturedYear);
	    		vehicle.setVinNumber(vinNumber);
	    		vehicle.setVehicleType(vehicleTypesRepository.findById(vehicleTypeID).orElseThrow(() -> new IllegalArgumentException("Invalid vehicleType Id:" + vehicleTypeID)));
	    		vehicle.setLocation(locationsRepository.findById(locationID).orElseThrow(() -> new IllegalArgumentException("Invalid location Id:" + locationID)));
	    		
	    		
		        vehiclesRepository.save(vehicle);
		        Logger.info("{} successfully saved vehicle with ID {}.", user.getUsername(), vehicle.getId());
			 		
			 }
			 
			 workbook.close();
		 
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/pendingshipments";
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
  	public String addVehicle(@Validated Vehicles vehicles, BindingResult result, Model model, HttpSession session) {
		userValidator.addition(vehicles, result);
		User user = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(user, model);
  		model.addAttribute("currentPage","/vehicles");
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation")); 

  		if (result.hasErrors()) {			

  			return "/add/add-vehicle";
		}
  		
  		Boolean deny = false;
  		
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
  			Logger.error("{} was unable to add Vehicle because VIN or Plate Number already exists.", user.getUsername());
  			model.addAttribute("vehicles", user.getCarrier().getVehicles());
  			
  			User users = getLoggedInUser();
	        List<Notification> notifications = new ArrayList<>();
	        
	        if(!(users == null)) {
	            notifications = NotificationController.fetchUnreadNotifications(users);
	        }
	        
	        model.addAttribute("notifications",notifications);
  			
  			return "vehicles";
  		}
  		
  		vehiclesRepository.save(vehicles);
  		Logger.error("{} successfully added vehicle with ID {}.", user.getUsername(), vehicles.getId());
  		
  		User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
  		
  		return "redirect:" + redirectLocation;
  	}
	
	/**
  	 * Finds a vehicle using the id parameter and if found, redirects to confirmation page
  	 * If there are dependency issues, the vehicle is not deleted and an error is displayed to the user.
  	 * @param id ID of the vehicle being deleted
  	 * @param model  Used to add data to the model
  	 * @return "redirect:/vehicles"
  	 */
	@GetMapping("/deletevehicles/{id}")
    public String deleteVehicle(@PathVariable("id") long id, Model model, HttpSession session) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        model.addAttribute("currentPage","/vehicles");
        if (!vehicle.getOrders().isEmpty() || !vehicle.getShipments().isEmpty() || !vehicle.getDrivers().isEmpty()){
        	session.setAttribute("error", "Unable to delete due to dependency conflict.");
        	Logger.error("{} was unable to delete due to dependency conflict.", user.getUsername());
        	model.addAttribute("vehicles", vehiclesRepository.findAll());
        	
        	return "redirect:" + (String) session.getAttribute("redirectLocation");
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
  		User user = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(user, model);
  		model.addAttribute("currentPage","/vehicles");
  		User carrierUser = CarriersController.getUserFromCarrier(vehicle.getCarrier());
  		
  		if(user.getId() != carrierUser.getId()) {
  			notificationService.addNotification(carrierUser, 
  					"ALERT: Your vehicle with plate number " + vehicle.getPlateNumber() + " was deleted by " + user.getUsername(), false);
  		}
  		
  		Logger.info("{} successfully deleted the vehicle with ID {}." ,user.getUsername() ,vehicle.getId());
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
    public String viewVehicle(@PathVariable("id") long id, Model model, HttpSession session) {
  		
  		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        model.addAttribute("vehicles", vehicle);
        model.addAttribute("currentPage","/vehicles");
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "vehicles";
    }
	

    
	
	/**
  	 * Finds a vehicle using the id parameter and if found, adds all of the drivers of that vehicle
  	 * to the drivers page
  	 * @param id ID of the vehicle used to get the drivers
  	 * @param model Used to add data to the model
  	 * @return "drivers"
  	 */
  	@GetMapping("/viewvehicledrivers/{id}")
    public String viewVehicleDrivers(@PathVariable("id") long id, Model model, HttpSession session) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        model.addAttribute("currentPage","/drivers");
        model.addAttribute("drivers", vehicle.getDrivers());
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
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
    public String viewVehicleShipments(@PathVariable("id") long id, Model model, HttpSession session) {
        Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        
        model.addAttribute("shipments", vehicle.getShipments());
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        model.addAttribute("currentPage","/vehicles");
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "shipments";
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
