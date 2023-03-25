package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.sru.thangiah.webrouting.controller.ExcelController;
import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Driver;
import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.MaintenanceOrders;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.Technicians;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.domain.Vehicles;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ContactsRepository;
import edu.sru.thangiah.webrouting.repository.DriverRepository;
import edu.sru.thangiah.webrouting.repository.LocationsRepository;
import edu.sru.thangiah.webrouting.repository.MaintenanceOrdersRepository;
import edu.sru.thangiah.webrouting.repository.RoleRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
import edu.sru.thangiah.webrouting.web.UserValidator;



@Controller
public class EditAndAddController {
	

	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
	@Autowired
	private UserValidator userValidator;

	private CarriersRepository carriersRepository;
	
	private ShipmentsRepository shipmentsRepository;
	
	private VehiclesRepository vehiclesRepository;
	
	private BidsRepository bidsRepository;
	
	private VehicleTypesRepository vehicleTypesRepository;
	
	private LocationsRepository	locationsRepository;
	
	private ContactsRepository contactsRepository;
	
	private UserRepository userRepository;
	
	private TechniciansRepository techniciansRepository;
	
	private DriverRepository driverRepository;
	
	private RoleRepository roleRepository;
	
	private MaintenanceOrdersRepository maintenanceOrdersRepository;
	
	private ValidationServiceImp validationServiceImp;
	
	private static final Logger Logger = LoggerFactory.getLogger(EditAndAddController.class);
	
	
	public EditAndAddController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository, 
			VehicleTypesRepository vehicleTypesRepository,LocationsRepository	locationsRepository, ContactsRepository contactsRepository, TechniciansRepository techniciansRepository,
			DriverRepository driverRepository, MaintenanceOrdersRepository maintenanceOrdersRepository, ValidationServiceImp validationServiceImp, UserRepository userRepository, RoleRepository roleRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
		this.vehiclesRepository = vehiclesRepository;
		this.bidsRepository = bidsRepository;
		this.vehicleTypesRepository = vehicleTypesRepository;
		this.locationsRepository = locationsRepository;
		this.contactsRepository = contactsRepository;
		this.techniciansRepository = techniciansRepository;
		this.driverRepository = driverRepository;
		this.maintenanceOrdersRepository = maintenanceOrdersRepository;
		this.validationServiceImp = validationServiceImp;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		
	}
	

	
  	@GetMapping("/editcontact/{id}")
    public String showContactsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
        Contacts contacts = contactsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        
        
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        session.removeAttribute("message");
        
        model.addAttribute("contacts", contacts);
        
        return "/edit/edit-contacts";
    }
  	
  	
  	@GetMapping("/editlocations/{id}")
    public String showLocationsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
        Locations locations = locationsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        
        
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        session.removeAttribute("message");
        
        model.addAttribute("locations", locations);
        
        return "/edit/edit-locations";
    }

  	@GetMapping("/editvehicletypes/{id}")
    public String showVehicleTypesEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
  		VehicleTypes vehicleType = vehicleTypesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type Id:" + id));
              
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
              
		model.addAttribute("vehicleTypes", vehicleType);
        
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        session.removeAttribute("message");
    
        return "/edit/edit-vehicletypes";
    }
	
	@GetMapping("/editvehicles/{id}")
    public String showVehiclesEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Vehicles vehicle = vehiclesRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		 User user = getLoggedInUser();
		 
		 model = NotificationController.loadNotificationsIntoModel(user, model);
		 model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		 
		 model.addAttribute("vehicleTypes", user.getCarrier().getVehicleTypes()); 
		 model.addAttribute("locations", user.getCarrier().getLocations());
		 model.addAttribute("vehicles", vehicle);
		 
		 session.removeAttribute("message");
			    
		 return "/edit/edit-vehicles";
		 
	}

	@GetMapping("/editusers/{id}")
    public String showUserEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		User userForm = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		 User user = getLoggedInUser();
		 
		 model = NotificationController.loadNotificationsIntoModel(user, model);
		 model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		 
		 String role = userForm.getRole().toString();
		 
		 model.addAttribute("user", userForm);
		 model.addAttribute("role", role);
		 model.addAttribute("availableRoles", roleRepository.findAll());
		 

		 session.removeAttribute("message");
			    
		 return "/edit/edit-user";
		 
	}
  	
  	@PostMapping("edit-contact/{id}")
  	public String contactUpdateForm(@PathVariable("id") long id, Contacts contact, Model model, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        List <Contacts> carrierContacts =  user.getCarrier().getContacts();
        
        Hashtable<String, String> hashtable = new Hashtable<>();
		
		hashtable.put("firstName", contact.getFirstName().strip());
		hashtable.put("lastName", contact.getLastName().strip());
		hashtable.put("middleInitial", contact.getMiddleInitial().strip());
		hashtable.put("emailAddress", contact.getEmailAddress().strip());
		hashtable.put("streetAddress1", contact.getStreetAddress1().strip());
		hashtable.put("streetAddress2", contact.getStreetAddress2().strip());
		hashtable.put("contactCity", contact.getCity().strip()); 
		hashtable.put("contactState", contact.getState().strip());
		hashtable.put("contactZip", contact.getZip().strip());
		hashtable.put("primaryPhone", contact.getPrimaryPhone().strip());
		hashtable.put("workPhone", contact.getWorkPhone().strip());

		Contacts result;
		
		result = validationServiceImp.validateContactForm(hashtable, session);
		
		
		if (result == null) {
			Logger.info("{} attempted to update Contact but failed.",user.getUsername());
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-contacts";
		}
        
		result.setId(id);
		
		String fullName = result.getFirstName().toString().strip() + " " + result.getLastName().toString().strip();
		
		for(Contacts check: carrierContacts) {
			String repoFullName = check.getFirstName().toString().strip() + " " + check.getLastName().toString().strip();
  			if(fullName.equals(repoFullName) && (result.getId() != check.getId())) {
  				Logger.info("{} attempted to save a contact with the same name as another contact.",user.getUsername());
  				model.addAttribute("message", "Another contact already exists with that name.");
  				return "/edit/edit-contacts";
  	  		}
  		}
		
        contactsRepository.save(result);
  		Logger.info("{} successfully updated contact with ID {}.", user.getUsername(), result.getId());
  		
  		return "redirect:" + redirectLocation;
  	}
        
  	
  	@PostMapping("edit-locations/{id}")
  	public String locationsUpdateForm(@PathVariable("id") long id, Locations locations, Model model, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        List <Locations> carrierLocations = user.getCarrier().getLocations();
        
        Hashtable<String, String> hashtable = new Hashtable<>();
		
        hashtable.put("locationName", locations.getName().strip());
		hashtable.put("streetAddress1", locations.getStreetAddress1().strip());
		hashtable.put("streetAddress2", locations.getStreetAddress2().strip());
		hashtable.put("locationCity", locations.getCity().strip()); 
		hashtable.put("locationState", locations.getState().strip());
		hashtable.put("locationZip", locations.getZip().strip());
		hashtable.put("locationLatitude", locations.getLatitude().strip());
		hashtable.put("locationLongitude", locations.getLongitude().strip());
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
  				Logger.info("{} attempted to save a location with the same name as another location.",user.getUsername());
  				model.addAttribute("message", "Another location already exists with that name.");
  				return "/edit/edit-locations";
  	  		}
  		}
		
        locationsRepository.save(result);
  		Logger.info("{} successfully updated location with ID {}.", user.getUsername(), result.getId());
  		
  		return "redirect:" + redirectLocation;
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
  				Logger.info("{} attempted to save a vehicle type with the same make and model as another vehicle type.",user.getUsername());
  				model.addAttribute("message", "Another vehicle type exists with that make and model.");
  				return "/edit/edit-vehicletypes";
  	  		}
  		}
		
        vehicleTypesRepository.save(result);
  		Logger.info("{} successfully updated vehicle type with ID {}.", user.getUsername(), result.getId());
  		
  		return "redirect:" + redirectLocation;
  	}
  	
  	@PostMapping("edit-vehicles/{id}")
  	public String vehicleUpdateForm(@PathVariable("id") long id, Vehicles vehicle, Model model, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
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
  				Logger.info("{} attempted to save a vehicle with the plate and vin as another vehicle type.",user.getUsername());
  				model.addAttribute("message", "Another vehicle exists with that vin and plate.");
  				return "/edit/edit-vehicles";
  	  		}
  		}
		
        vehiclesRepository.save(result);
  		Logger.info("{} successfully updated vehicle with ID {}.", user.getUsername(), result.getId());

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