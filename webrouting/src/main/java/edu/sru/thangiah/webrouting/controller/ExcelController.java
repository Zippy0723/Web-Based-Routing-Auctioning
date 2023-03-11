package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Carriers;
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
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
import edu.sru.thangiah.webrouting.web.UserValidator;

@Controller
public class ExcelController {
	
	private ValidationServiceImp validationServiceImp;

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
	
	private TechniciansRepository techniciansRepository;
	
	private DriverRepository driverRepository;
	
	private MaintenanceOrdersRepository maintenanceOrdersRepository;
	
	private static final Logger Logger = LoggerFactory.getLogger(ExcelController.class);
	
	
	public ExcelController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository, 
			VehicleTypesRepository vehicleTypesRepository,ValidationServiceImp validationServiceImp,LocationsRepository	locationsRepository, ContactsRepository contactsRepository, TechniciansRepository techniciansRepository,
			DriverRepository driverRepository, MaintenanceOrdersRepository maintenanceOrdersRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
		this.vehiclesRepository = vehiclesRepository;
		this.bidsRepository = bidsRepository;
		this.validationServiceImp = validationServiceImp;
		this.vehicleTypesRepository = vehicleTypesRepository;
		this.locationsRepository = locationsRepository;
		this.contactsRepository = contactsRepository;
		this.techniciansRepository = techniciansRepository;
		this.driverRepository = driverRepository;
		this.maintenanceOrdersRepository = maintenanceOrdersRepository;
		
	}
	
	
	/**
  	 * Reads an excel file containing shipments and adds it to the shipments repository. <br>
  	 * After the file is uploaded and added to the database, user is redirected to the created shipments page
  	 * @param excelData Excel file that is being added to the database
  	 * @return "redirect: ${redirectLocation}"
  	 */
	@PostMapping("/upload-shipment")
	public String LoadShipmentExcelForm(@RequestParam("file") MultipartFile excelData, HttpSession session, Model model){
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		XSSFWorkbook workbook;
		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		
		try {
			workbook = new XSSFWorkbook(excelData.getInputStream());
			XSSFSheet worksheet = workbook.getSheetAt(0);
			
			List<Shipments> shipments = validationServiceImp.validateShipmentSheet(worksheet);
			
			if (shipments == null) {
				Logger.info("{} attempted to save shipments but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			for(Shipments s: shipments) {
			shipmentsRepository.save(s);
			Logger.info("{} saved shipment with ID {}.",user.getUsername(),s.getId());
			}
			
		}
		catch(Exception e ) {
			e.printStackTrace();
		}
		
		return "redirect:" + redirectLocation;	
	}
	
	
	@PostMapping("/upload-carrier")
	public String LoadCarrierExcelForm(@RequestParam("file") MultipartFile excelData, HttpSession session, Model model){
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		XSSFWorkbook workbook;
		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		
		try {
			workbook = new XSSFWorkbook(excelData.getInputStream());
			XSSFSheet vehicleTypesSheet = workbook.getSheetAt(0);
			XSSFSheet locationsSheet = workbook.getSheetAt(1);
			XSSFSheet contactsSheet = workbook.getSheetAt(2);
			XSSFSheet vehiclesSheet = workbook.getSheetAt(3);
			XSSFSheet techniciansSheet = workbook.getSheetAt(4);
			XSSFSheet driversSheet = workbook.getSheetAt(5);
			XSSFSheet maintenanceOrdersSheet = workbook.getSheetAt(6);
			
			
			List<VehicleTypes> vehicleTypes = new ArrayList<>();
			List<Locations> locations = new ArrayList<>();
			List<Contacts> contacts = new ArrayList<>();
			List<Vehicles> vehicles = new ArrayList<>();
			List<Technicians> technicians = new ArrayList<>();
			List<Driver> drivers = new ArrayList<>();
			List<MaintenanceOrders> maintenanceOrders = new ArrayList<>();
			
			vehicleTypes = validationServiceImp.validateVehicleTypesSheet(vehicleTypesSheet);
			
			if (vehicleTypes == null) {
				Logger.info("{} attempted to save Vehicle Types but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			/*
			locations = validationServiceImp.validateLocationsSheet(locationsSheet);
			
			if (locations == null) {
				Logger.info("{} attempted to save Vehicle Types but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			contacts = validationServiceImp.validateContactsSheet(contactsSheet);
			
			if (contacts == null) {
				Logger.info("{} attempted to save Contacts but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			vehicles = validationServiceImp.validateVehiclesSheet(vehiclesSheet);
			
			if (vehicles == null) {
				Logger.info("{} attempted to save Vehicle but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			technicians = validationServiceImp.validateTechniciansSheet(techniciansSheet);
			
			if (technicians == null) {
				Logger.info("{} attempted to save Technicians but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			drivers = validationServiceImp.validateDriverSheet(driversSheet);
			
			if (drivers == null) {
				Logger.info("{} attempted to save Drivers but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			maintenanceOrders = validationServiceImp.validateMaintenanceOrdersSheet(maintenanceOrdersSheet);
			
			if (maintenanceOrders == null) {
				Logger.info("{} attempted to save Maintenance Orders but failed.",user.getUsername());
				return "redirect:" + redirectLocation; 
			}
			
			*/
			
			//Validation for dependencies
			
			
			for(VehicleTypes vehicleType: vehicleTypes) {
				vehicleTypesRepository.save(vehicleType);
				Logger.info("{} saved Vehicle Type with ID {}.",user.getUsername(),vehicleType.getId());
			}
			
			/*
			for(Locations location: locations) {
				locationsRepository.save(location);
			Logger.info("{} saved Location with ID {}.",user.getUsername(),location.getId());
			}

			for(Contacts contact: contacts) {
				contactsRepository.save(contact);
			Logger.info("{} saved Contact with ID {}.",user.getUsername(),contact.getId());
			}
			
			for(Vehicles vehicle: vehicles) {
				vehiclesRepository.save(vehicle);
			Logger.info("{} saved Vehicle with ID {}.",user.getUsername(),vehicle.getId());
			}
			
			for(Technicians technician: technicians) {
				techniciansRepository.save(technician);
			Logger.info("{} saved Technician with ID {}.",user.getUsername(),technician.getId());
			}
			
			for(Driver driver: drivers) {
				driverRepository.save(driver);
			Logger.info("{} saved Driver with ID {}.",user.getUsername(),driver.getId());
			}
			
			for(MaintenanceOrders maintenanceorder: maintenanceOrders) {
				maintenanceOrdersRepository.save(maintenanceorder);
			Logger.info("{} saved Maintenance Order with ID {}.",user.getUsername(), maintenanceorder.getId());
			}
			*/
		}
		catch(Exception e ) {
			e.printStackTrace();
			return "redirect:" + redirectLocation;
		}
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
