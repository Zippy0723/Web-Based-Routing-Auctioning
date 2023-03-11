package edu.sru.thangiah.webrouting.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/dump-excel-carrier")
	public ResponseEntity<Resource> dumpExcelCarrier(Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet contactsWorksheet = workbook.createSheet("Contacts");
		XSSFSheet locationsWorksheet = workbook.createSheet("Locations");
		XSSFSheet vehicleWorksheet = workbook.createSheet("Vehicles");
		XSSFSheet vehicleTypesWorksheet = workbook.createSheet("Vehicle Types");
		XSSFSheet driversWorksheet = workbook.createSheet("Drivers");
		XSSFSheet technicansWorksheet = workbook.createSheet("Technicans");
		XSSFSheet maintenanceOrdersWorksheet = workbook.createSheet("Maintenance Orders");
				
		User user = getLoggedInUser();
		Carriers carrier = user.getCarrier();
		
		List<Contacts> contacts = carrier.getContacts();
		XSSFRow contactsHeaderRow = contactsWorksheet.createRow(0);
		contactsHeaderRow.createCell(0).setCellValue("First Name");
		contactsHeaderRow.createCell(1).setCellValue("Last Name");
		contactsHeaderRow.createCell(2).setCellValue("Middle Initial");
		contactsHeaderRow.createCell(3).setCellValue("Email");
		contactsHeaderRow.createCell(4).setCellValue("Street Address 1");
		contactsHeaderRow.createCell(5).setCellValue("Street Address 2");
		contactsHeaderRow.createCell(6).setCellValue("City");
		contactsHeaderRow.createCell(7).setCellValue("State");
		contactsHeaderRow.createCell(8).setCellValue("Zip Code");
		contactsHeaderRow.createCell(9).setCellValue("Primary Phone");
		contactsHeaderRow.createCell(10).setCellValue("Work Phone");
		
		int rowIndex = 1;
		for(Contacts contact : contacts) {
			XSSFRow curRow = contactsWorksheet.createRow(rowIndex++);
			curRow.createCell(0).setCellValue(contact.getFirstName());
			curRow.createCell(1).setCellValue(contact.getLastName());
			curRow.createCell(2).setCellValue(contact.getMiddleInitial());
			curRow.createCell(3).setCellValue(contact.getEmailAddress());
			curRow.createCell(4).setCellValue(contact.getStreetAddress1());
			curRow.createCell(5).setCellValue(contact.getStreetAddress2());
			curRow.createCell(6).setCellValue(contact.getCity());
			curRow.createCell(7).setCellValue(contact.getState());
			curRow.createCell(8).setCellValue(contact.getZip());
			curRow.createCell(9).setCellValue(contact.getPrimaryPhone());
			curRow.createCell(10).setCellValue(contact.getWorkPhone());
		}
		
		List<Locations> locations = carrier.getLocations();
		XSSFRow locationHeaderRow = locationsWorksheet.createRow(0);
		locationHeaderRow.createCell(0).setCellValue("Name");
		locationHeaderRow.createCell(1).setCellValue("Street Address 1");
		locationHeaderRow.createCell(2).setCellValue("Street Address 2");
		locationHeaderRow.createCell(3).setCellValue("City");
		locationHeaderRow.createCell(4).setCellValue("State");
		locationHeaderRow.createCell(5).setCellValue("Zip Code");
		locationHeaderRow.createCell(6).setCellValue("Latitude");
		locationHeaderRow.createCell(7).setCellValue("Longitude");
		locationHeaderRow.createCell(8).setCellValue("Location Type");
		
		rowIndex = 1;
		for(Locations location : locations) {
			XSSFRow curRow = locationsWorksheet.createRow(rowIndex++);
			curRow.createCell(0).setCellValue(location.getName());
			curRow.createCell(1).setCellValue(location.getStreetAddress1());
			curRow.createCell(2).setCellValue(location.getStreetAddress2());
			curRow.createCell(3).setCellValue(location.getCity());
			curRow.createCell(4).setCellValue(location.getState());
			curRow.createCell(5).setCellValue(location.getZip());
			curRow.createCell(6).setCellValue(location.getLatitude());
			curRow.createCell(7).setCellValue(location.getLongitude());
			curRow.createCell(8).setCellValue(location.getLocationType());
		}
		
		List<Vehicles> vehicles = carrier.getVehicles();
	    XSSFRow vehicleHeaderRow = vehicleWorksheet.createRow(0);
	    vehicleHeaderRow.createCell(0).setCellValue("Plate Number");
	    vehicleHeaderRow.createCell(1).setCellValue("VIN Number");
	    vehicleHeaderRow.createCell(2).setCellValue("Manufactured Year");
	    vehicleHeaderRow.createCell(3).setCellValue("Vehicle Type Make + Model");
	    vehicleHeaderRow.createCell(4).setCellValue("Location + Address");
	    
	    rowIndex = 1;
	    for(Vehicles vehicle : vehicles) {
	    	XSSFRow curRow = vehicleWorksheet.createRow(rowIndex++);
	    	curRow.createCell(0).setCellValue(vehicle.getPlateNumber());
	    	curRow.createCell(1).setCellValue(vehicle.getVinNumber());
	    	curRow.createCell(2).setCellValue(vehicle.getManufacturedYear());
	    	curRow.createCell(3).setCellValue(vehicle.getVehicleType().getMake() + " " + vehicle.getVehicleType().getModel());
	    	curRow.createCell(4).setCellValue(vehicle.getLocation().getName() + " " + vehicle.getLocation().getStreetAddress1());
	    }
	    
	    //vehicle types
	    
	    List<Driver> drivers = carrier.getDrivers();
	    XSSFRow driversHeaderRow = driversWorksheet.createRow(0);
	    driversHeaderRow.createCell(0).setCellValue("Contact First + Last Name");
	    driversHeaderRow.createCell(1).setCellValue("Vehicle Plate Number + VIN");
	    driversHeaderRow.createCell(2).setCellValue("License Number");
	    driversHeaderRow.createCell(3).setCellValue("License Expiration");
	    driversHeaderRow.createCell(4).setCellValue("License Class");
	    
	    rowIndex = 1;
	    for (Driver driver : drivers) {
	    	XSSFRow curRow = driversWorksheet.createRow(rowIndex++);
	    	curRow.createCell(0).setCellValue(driver.getContact().getFirstName() + " " + driver.getContact().getLastName());
	    	curRow.createCell(1).setCellValue(driver.getVehicle().getPlateNumber() + " " + driver.getVehicle().getVinNumber());
	    	curRow.createCell(2).setCellValue(driver.getLisence_number());
	    	curRow.createCell(3).setCellValue(driver.getLisence_expiration());
	    	curRow.createCell(4).setCellValue(driver.getLisence_class());
	    }
	    
	    //technicans
	    
	    List<MaintenanceOrders> maintenaneOrders = carrier.getOrders();
	    XSSFRow maintenaneOrdersHeaderRow = maintenanceOrdersWorksheet.createRow(0);
	    maintenaneOrdersHeaderRow.createCell(0).setCellValue("Technican First + Last Name");
	    maintenaneOrdersHeaderRow.createCell(1).setCellValue("Scheduled Date");
	    maintenaneOrdersHeaderRow.createCell(2).setCellValue("Details");
	    maintenaneOrdersHeaderRow.createCell(3).setCellValue("Service Type");
	    maintenaneOrdersHeaderRow.createCell(4).setCellValue("Cost");
	    maintenaneOrdersHeaderRow.createCell(5).setCellValue("Status");
	    maintenaneOrdersHeaderRow.createCell(6).setCellValue("Vehicle Plate Number + Vin");
	    maintenaneOrdersHeaderRow.createCell(7).setCellValue("Maintence Type");
	    
	    rowIndex = 1;
	    for (MaintenanceOrders order : maintenaneOrders) {
	    	XSSFRow curRow = maintenanceOrdersWorksheet.createRow(rowIndex++);
	    	curRow.createCell(0).setCellValue(order.getTechnician().getContact().getFirstName() + " " + order.getTechnician().getContact().getLastName());
	    	curRow.createCell(1).setCellValue(order.getScheduled_date());
	    	curRow.createCell(2).setCellValue(order.getDetails());
	    	curRow.createCell(3).setCellValue(order.getService_type_key());
	    	curRow.createCell(4).setCellValue(order.getCost());
	    	curRow.createCell(5).setCellValue(order.getStatus_key());
	    	curRow.createCell(6).setCellValue(order.getVehicle().getPlateNumber() + " " + order.getVehicle().getVinNumber());
	    	curRow.createCell(7).setCellValue(order.getMaintenance_type());
	    }
	    
	    for (int i = 0; i < contactsHeaderRow.getLastCellNum(); i++) {
	    	contactsWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < contactsHeaderRow.getLastCellNum(); i++) {
	    	locationsWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < vehicleHeaderRow.getLastCellNum(); i++) {
	    	vehicleWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < driversHeaderRow.getLastCellNum(); i++) {
	    	driversWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < maintenaneOrdersHeaderRow.getLastCellNum(); i++) {
	    	maintenanceOrdersWorksheet.autoSizeColumn(i);
	    }
	    
	    byte[] workbookBytes;
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        workbook.write(outputStream);
	        workbookBytes = outputStream.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        try {
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // Create a Resource object from the byte array
	    ByteArrayResource resource = new ByteArrayResource(workbookBytes);
	    
	    // Set the headers for the response
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehicles.xlsx");
	    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
	    
	    // Return a ResponseEntity with the resource and headers
	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(workbookBytes.length)
	            .body(resource);
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
