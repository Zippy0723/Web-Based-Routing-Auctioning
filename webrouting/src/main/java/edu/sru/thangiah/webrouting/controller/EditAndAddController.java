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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.sru.thangiah.webrouting.controller.ExcelController;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Driver;
import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.MaintenanceOrders;
import edu.sru.thangiah.webrouting.domain.Role;
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
	
	@GetMapping("/edittechnician/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Technicians technician = techniciansRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Technician Id:" + id));
	     model.addAttribute("technicians", technician);
	     model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
	     model.addAttribute("currentPage","/technicians");
	     
	     User user = getLoggedInUser();
	     model = NotificationController.loadNotificationsIntoModel(user, model);
	     
        return "/edit/edit-technicians";
    }
	
  	@GetMapping("/editcontact/{id}")
    public String showContactsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
        Contacts contacts = contactsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        
        
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        model.addAttribute("currentPage","/contacts");
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
        model.addAttribute("currentPage","/locations");
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
		model.addAttribute("currentPage","/vehicletypes");
        
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
		 model.addAttribute("currentPage","/vehicles");
		 
		 model = NotificationController.loadNotificationsIntoModel(user, model);
		 model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		 
		 model.addAttribute("vehicleTypes", user.getCarrier().getVehicleTypes()); 
		 model.addAttribute("locations", user.getCarrier().getLocations());
		 model.addAttribute("vehicles", vehicle);
		 
		 session.removeAttribute("message");
			    
		 return "/edit/edit-vehicles";
		 
	}
	
	@GetMapping("/editdriver/{id}")
    public String showDriversEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Driver driver = driverRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));
		 User user = getLoggedInUser();
		 
		 model = NotificationController.loadNotificationsIntoModel(user, model);
		 model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		 model.addAttribute("currentPage","/drivers");
		 
		 model.addAttribute("driver", driver); 

		 model.addAttribute("vehicles", user.getCarrier().getVehicles());
		 
		 session.removeAttribute("message");
			    
		 return "/edit/edit-drivers";
		 
	}

	@GetMapping("/editshippers/{id}")
    public String showUserEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		User userForm = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		 User user = getLoggedInUser();
		 
		 model = NotificationController.loadNotificationsIntoModel(user, model);
		 model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		 model.addAttribute("currentPage","/users");
		 
		 model.addAttribute("user", userForm);

		 session.removeAttribute("message");
			    
		 return "/edit/edit-shippers";
		 
	}
	
	@GetMapping("/editorder/{id}")
    public String showOrdersEditForm(@PathVariable("id") long id, Model model, HttpSession session ) {
		MaintenanceOrders maintenanceOrder = maintenanceOrdersRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
		User user = getLoggedInUser();
		
		model.addAttribute("currentPage","/maintenanceorders");
		model.addAttribute("technicians", techniciansRepository.findAll());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());
	    model.addAttribute("maintenanceOrders", maintenanceOrder);
	    model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        model = NotificationController.loadNotificationsIntoModel(user, model);
	    
        session.removeAttribute("message");
        
        return "/edit/edit-orders";
    }
	
	@GetMapping("/editshipment/{id}")
    public String showShipmentsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
        
	    User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);

	    model.addAttribute("shipments", shipment);
	    model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
	    model.addAttribute("currentPage","/shipments");
	    
	    session.removeAttribute("message");

       return "/edit/edit-shipments";
        
    }
	
	@PostMapping("/updateshipment/{id}")
    public String updateShipment(@PathVariable("id") long id, Shipments shipment, 
    		Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		
		Shipments temp = shipmentsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        
        Hashtable<String, String> hashtable = new Hashtable<>();
        
		hashtable.put("clientName", shipment.getClient().strip());
		hashtable.put("clientMode", shipment.getClientMode().strip());
		hashtable.put("date", shipment.getShipDate().strip());
		hashtable.put("commodityClass", shipment.getCommodityClass().strip());
		hashtable.put("commodityPieces", shipment.getCommodityPieces().strip());
		hashtable.put("commodityPaidWeight", shipment.getCommodityPaidWeight().strip());
		hashtable.put("shipperCity", shipment.getShipperCity().strip());
		hashtable.put("shipperState", shipment.getShipperState().strip());
		hashtable.put("shipperZip", shipment.getShipperZip().strip());
		hashtable.put("shipperLatitude", shipment.getShipperLatitude().strip());
		hashtable.put("shipperLongitude", shipment.getShipperLongitude().strip());
		hashtable.put("consigneeCity", shipment.getConsigneeCity().strip());
		hashtable.put("consigneeState", shipment.getConsigneeState().strip());
		hashtable.put("consigneeZip", shipment.getConsigneeZip().strip());
		hashtable.put("consigneeLatitude", shipment.getConsigneeLatitude().strip());
		hashtable.put("consigneeLongitude", shipment.getConsigneeLongitude().strip());
        
		Shipments result;
        
		result = validationServiceImp.validateShipmentForm(hashtable, session);
        
		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-shipments";
		}
        
			result.setScac(temp.getScac());
			result.setFullFreightTerms(temp.getFullFreightTerms());
			result.setPaidAmount(temp.getPaidAmount());
			result.setFreightbillNumber(temp.getFreightbillNumber());
			result.setId(id);
			result.setCarrier(temp.getCarrier());
			result.setUser(user);
        
        
		  shipmentsRepository.save(result);
	      Logger.info("{} || successfully updated the shipment with ID {}",user.getUsername(), result.getId());
	      return "redirect:" + redirectLocation;
        
	}
	
	
	@PostMapping("/editorders/{id}")
    public String updateOrder(@PathVariable("id") long id, MaintenanceOrders maintenanceOrder, 
      Model model, HttpSession session) {
		
       
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        String redirectLocation = (String) session.getAttribute("redirectLocation");
        model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
        
        model.addAttribute("technicians", techniciansRepository.findAll());
		model.addAttribute("vehicles", loggedInUser.getCarrier().getVehicles());
	    model.addAttribute("maintenanceOrders", maintenanceOrder);
        
	    Hashtable<String, String> hashtable = new Hashtable<>();
	    
	    MaintenanceOrders result;
	    
	    
	    
	    hashtable.put("date", maintenanceOrder.getScheduled_date().strip());
		hashtable.put("details", maintenanceOrder.getDetails().strip());
		hashtable.put("serviceType", maintenanceOrder.getService_type_key().strip());
		hashtable.put("cost", maintenanceOrder.getCost().strip());
		hashtable.put("status", maintenanceOrder.getStatus_key().strip());
		hashtable.put("type", maintenanceOrder.getMaintenance_type().strip());
		
		result = validationServiceImp.validateMaintenanceOrderForm(hashtable, session);
		
		
		
		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-orders";
		}

		result.setVehicle(maintenanceOrder.getVehicle());
		result.setTechnician(maintenanceOrder.getTechnician());
		result.setId(id);

  		
	    maintenanceOrdersRepository.save(result);
        Logger.info("{} || successfully updated the maintenance order with ID {}",loggedInUser.getUsername(), maintenanceOrder.getId());
        return "redirect:" + redirectLocation;
    }
	
	@PostMapping("/edittechnicians/{id}")
    public String updateTechnician(@PathVariable("id") long id, Technicians technician, 
       Model model, HttpSession session) {
		 User user = getLoggedInUser();
		 model = NotificationController.loadNotificationsIntoModel(user, model);
		 String redirectLocation = (String) session.getAttribute("redirectLocation");
		 model.addAttribute("redirectLocation", session.getAttribute("redirectLocation")); 
		 Technicians result = techniciansRepository.findById(id)
		          .orElseThrow(() -> new IllegalArgumentException("Invalid Technician Id:" + id));
		
		 String skillGrade = technician.getSkill_grade().strip();
		 
		 if (!(skillGrade.length() <= 12 && skillGrade.length() > 0) || !(skillGrade.matches("^[a-zA-Z0-9.]+$"))) {
				Logger.error("{} || attempted to edit a Technician but the Skill Grade was not between 1 and 12 alphanumeric characters long.",user.getUsername());
				model.addAttribute("message", "Skill Grade was not between 1 and 12 alphanumeric characters.");
				return "/edit/edit-technicians";	
			}

  		result.setSkill_grade(skillGrade);
  		
        techniciansRepository.save(result);
        Logger.info("{} || successfully updated Technician with ID {}", user.getUsername(), result.getId());
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
  				Logger.info("{} || attempted to save a location with the same name as another location.",user.getUsername());
  				model.addAttribute("message", "Another location already exists with that name.");
  				return "/edit/edit-locations";
  	  		}
  		}
		
        locationsRepository.save(result);
  		Logger.info("{} || successfully updated location with ID {}.", user.getUsername(), result.getId());
  		
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
  				Logger.info("{} || attempted to save a vehicle type with the same make and model as another vehicle type.",user.getUsername());
  				model.addAttribute("message", "Another vehicle type exists with that make and model.");
  				return "/edit/edit-vehicletypes";
  	  		}
  		}
		
        vehicleTypesRepository.save(result);
  		Logger.info("{} || successfully updated vehicle type with ID {}.", user.getUsername(), result.getId());
  		
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
  				Logger.info("{} || attempted to save a vehicle with the plate and vin as another vehicle type.",user.getUsername());
  				model.addAttribute("message", "Another vehicle exists with that vin and plate.");
  				return "/edit/edit-vehicles";
  	  		}
  		}
		
        vehiclesRepository.save(result);
  		Logger.info("{} || successfully updated vehicle with ID {}.", user.getUsername(), result.getId());

  		return "redirect:" + redirectLocation;
  	}
  	
  	@PostMapping("edit-contact/{id}")
  	public String contactsUpdateForm(@PathVariable("id") long id, Contacts contacts, Model model, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        List<Contacts> carrierContacts = user.getCarrier().getContacts();
        
        model.addAttribute("contacts", contacts);

        
        Hashtable<String, String> hashtable = new Hashtable<>();
		
        hashtable.put("firstName", contacts.getFirstName().strip());
		hashtable.put("lastName", contacts.getLastName().strip());
		hashtable.put("middleInitial", contacts.getMiddleInitial().strip());
		hashtable.put("emailAddress", contacts.getEmailAddress().strip());
		hashtable.put("streetAddress1", contacts.getStreetAddress1().strip());
		hashtable.put("streetAddress2", contacts.getStreetAddress2().strip());
		hashtable.put("contactCity", contacts.getCity().strip()); 
		hashtable.put("contactState", contacts.getState().strip());
		hashtable.put("contactZip", contacts.getZip().strip());
		hashtable.put("primaryPhone", contacts.getPrimaryPhone().strip());
		hashtable.put("workPhone", contacts.getWorkPhone().strip());
	
		Contacts result;
		
		result = validationServiceImp.validateContactForm(hashtable, session);
		
		
		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-contacts";
		}
		
		String fullName = result.getFirstName() + " " + result.getLastName();

		
		result.setId(id);
		
		for(Contacts check: carrierContacts) {
			String carrierContactsFullName = check.getFirstName() + " " + check.getLastName();
  			if(fullName.equals(carrierContactsFullName) && (result.getId() != check.getId())) {
  				Logger.info("{} || attempted to save a contact with the same name as an existing contact.",user.getUsername());
  				model.addAttribute("message", "Another contact exists with that name.");
  				return "/edit/edit-contacts";
  	  		}
		}
				
        contactsRepository.save(result);
  		Logger.info("{} || successfully updated contact with ID {}.", user.getUsername(), result.getId());

  		return "redirect:" + redirectLocation;
  	}
  	
  	@PostMapping("edit-drivers/{id}")
  	public String driverUpdateForm(@PathVariable("id") long id, Driver driver, Model model, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User user = getLoggedInUser();
  		
  		Driver temp = driverRepository.findById(id)
  				.orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));
  		
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
		model.addAttribute("vehicles", user.getCarrier().getVehicles());
		model.addAttribute("driver", driver);

        
        Hashtable<String, String> hashtable = new Hashtable<>();
		
		hashtable.put("licenseNumber", driver.getLisence_number().strip());
		hashtable.put("licenseExpiration", driver.getLisence_expiration().strip());
		hashtable.put("licenseClass", driver.getLisence_class());
	
		driver.setContact(temp.getContact());
		
		Driver result;
		
		result = validationServiceImp.validateDriverForm(hashtable, session);
		
		
		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-drivers";
		}
        
		result.setId(id);
		result.setContact(driver.getContact());
		result.setVehicle(driver.getVehicle());
		
		
        driverRepository.save(result);
  		Logger.info("{} || successfully updated driver with ID {}.", user.getUsername(), result.getId());

  		return "redirect:" + redirectLocation;
  	}
  	
  	@PostMapping("edit-shipper/{id}")
  	public String shipperUpdateForm(@PathVariable("id") long id, User user, Model model, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        User result = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
        
        List <User> repoUsers =  userRepository.findAll();
        
        
        String username = user.getUsername().strip();
        String emailAddress = user.getEmail().strip();
        
        
        if(!(emailAddress.length() <= 64 && emailAddress.length() > 0) || !(emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){
			Logger.error("{} || attempted to edit a shipper but the email address must be between 1 and 64 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Email must be between 1 and 64 alphanumeric characters.");
			return "/edit/edit-shippers";	
		}
        
		if (!(username.length() <= 32 && username.length() > 0) || !(username.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} || attempted to edit a shipper but the username was not between 1 and 32 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Username must be between 1 and 32 alphanumeric characters.");
			return "/edit/edit-shippers";	
		}	
		
		for(User check: repoUsers) {
			String repoUsername = check.getUsername().strip();
  			if(username.equals(repoUsername) && id != check.getId()) {
  				Logger.error("{} || attempted to save a shipper with the same username as another user.",loggedInUser.getUsername());
  				model.addAttribute("message", "Another user already exists with that username.");
  				return "/edit/edit-shippers";
  	  		}
  		}
		
		for(User check: repoUsers) {
			String repoEmailAddress = check.getEmail().strip();
  			if(emailAddress.equals(repoEmailAddress) && id != check.getId()) {
  				Logger.error("{} || attempted to save a shipper with the same email as another user.",loggedInUser.getUsername());
  				model.addAttribute("message", "Another user already exists with that email.");
  				return "/edit/edit-shippers";
  	  		}
  		}
		
		result.setEmail(emailAddress);
		result.setUsername(username);
		result.setAuctioningAllowed(user.getAuctioningAllowed());
		result.setEnabled(user.isEnabled());

        userRepository.save(result);
  		Logger.error("{} || successfully updated a shipper with ID {}.", loggedInUser.getUsername(), result.getId());
  		
  		return "redirect:" + redirectLocation;
  	}
  	
  	
  	
  	@GetMapping("/addcarrier")
    public String showCarrierAddForm(User user, Model model, HttpSession session) {
		 model.addAttribute("userForm", new User());
		 model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
	     model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
		 

		 session.removeAttribute("message");
			    
		 return "/add/add-carrier";
  	}
  	
  	
  	@PostMapping("addcarrierform")
  	public String carrierAddForm(@ModelAttribute("userForm") User userForm, Model model,
    		String carrierName, String scac, boolean ltl, boolean ftl, String pallets, String weight, HttpSession session) {
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        
        List <User> repoUsers =  userRepository.findAll();
        List <Carriers> repoCarriers =  (List<Carriers>) carriersRepository.findAll();
        

        
        
        String username = userForm.getUsername().strip();
        String emailAddress = userForm.getEmail().strip();
        String password = userForm.getPassword().strip();

        
        if(!(emailAddress.length() <= 64 && emailAddress.length() > 0) || !(emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){
			Logger.error("{} || attempted to add a carrier but the email address must be between 1 and 64 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Email must be between 1 and 64 alphanumeric characters.");
			return "/add/add-carrier";	
		}
        
		if (!(username.length() <= 32 && username.length() > 0) || !(username.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} || attempted to add a carrier but the username was not between 1 and 32 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Username must be between 1 and 32 alphanumeric characters.");
			return "/add/add-carrier";	
		}	
		
		for(User check: repoUsers) {
			String repoUsername = check.getUsername().strip();
  			if(username.equals(repoUsername)) {
  				Logger.error("{} || attempted to add a carrier with the same username as another user.",loggedInUser.getUsername());
  				model.addAttribute("message", "Another user already exists with that username.");
  				return "/add/add-carrier";
  	  		}
  		}
		
		for(User check: repoUsers) {
			String repoEmailAddress = check.getEmail().strip();
  			if(emailAddress.equals(repoEmailAddress)) {
  				Logger.error("{} || attempted to add a carrier with the same email as another user.",loggedInUser.getUsername());
  				model.addAttribute("message", "Another user already exists with that email.");
  				return "/add/add-carrier";
  	  		}
		}
  			
		for(Carriers check: repoCarriers) {
			String repoCarrierName = check.getCarrierName().strip();
  			if(carrierName.equals(repoCarrierName)) {
  				Logger.error("{} || attempted to save a carrier with the same carrier name as another carrier.",loggedInUser.getUsername());
  				model.addAttribute("message", "Another carrier already exists with that carrier name.");
  				return "/add/add-carrier";
  	  		}
  		}
		
		for(Carriers check: repoCarriers) {
			String repoScac = check.getScac().strip();
  			if(scac.equals(repoScac)) {
  				Logger.error("{} || attempted to save a carrier with the same scac as another carrier.",loggedInUser.getUsername());
  				model.addAttribute("message", "Another carrier already exists with that scac.");
  				return "/add/add-carrier";
  	  		}
  		}
		
		if(!(scac.length() <= 4 && scac.length() >= 2) || !(scac.matches("^[a-zA-Z0-9]+$"))) {
				Logger.error("{} || attempted to add a carrier but the scac was not between 2 and 4 alphanumeric characters.",loggedInUser.getUsername());
				model.addAttribute("message", "Scac was not between 2 and 4 alphanumeric characters.");
				return "/add/add-carrier";
			}
		
  	
		if(!(pallets.length() <= 32 && pallets.length() > 0) || !(pallets.matches("^[0-9]+$"))) {
			Logger.error("{} || attempted to add a carrier but the pallets must be between 1 and 32 numeric chracters.",loggedInUser.getUsername());
			model.addAttribute("message", "Pallets must be between 1 and 32 numeric characters.");
			return "/add/add-carrier";
		}
		
		if(!(weight.length() <= 32 && weight.length() > 0) || !(weight.matches("^[0-9]+$"))) {
			Logger.error("{} || attempted to add a carrier but the weight must be between 1 and 32 numeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Weight must be be between 1 and 32 numeric characters.");
			return "/add/add-carrier";
		}
		
		
		User result = new User();
		Carriers carrierResult = new Carriers();
		
		Role role = new Role();
		
		role.setId(3);
		
		
		result.setEmail(emailAddress);
		result.setUsername(username);
		result.setPassword(password);
		result.setAuctioningAllowed(userForm.getAuctioningAllowed());
		result.setEnabled(userForm.isEnabled());
		result.setRole(role);
		
		
		carrierResult.setLtl(ltl);
		carrierResult.setFtl(ftl);
		carrierResult.setCarrierName(carrierName);
		carrierResult.setPallets(pallets);
		carrierResult.setScac(scac);
		carrierResult.setWeight(weight);
		
		

		carriersRepository.save(carrierResult);
		result.setCarrier(carrierResult);
        userRepository.save(result);
  		Logger.info("{} || successfully saved a carrier with ID {}.", loggedInUser.getUsername(), result.getId());
  		
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