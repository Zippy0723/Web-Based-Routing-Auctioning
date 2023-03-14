package edu.sru.thangiah.webrouting.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
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
		XSSFSheet vehicleTypesWorksheet = workbook.createSheet("Vehicle Types");
		XSSFSheet locationsWorksheet = workbook.createSheet("Locations");
		XSSFSheet contactsWorksheet = workbook.createSheet("Contacts");
		XSSFSheet vehicleWorksheet = workbook.createSheet("Vehicles");
		XSSFSheet technicansWorksheet = workbook.createSheet("Technicans");
		XSSFSheet driversWorksheet = workbook.createSheet("Drivers");
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
	    
	    List<VehicleTypes> vehicleTypes = carrier.getVehicleTypes();
	    XSSFRow vehicleTypesHeaderRow = vehicleTypesWorksheet.createRow(0);
	    vehicleTypesHeaderRow.createCell(0).setCellValue("Type");
	    vehicleTypesHeaderRow.createCell(1).setCellValue("Sub Type");
	    vehicleTypesHeaderRow.createCell(2).setCellValue("Description");
	    vehicleTypesHeaderRow.createCell(3).setCellValue("Make");
	    vehicleTypesHeaderRow.createCell(4).setCellValue("Model");
	    vehicleTypesHeaderRow.createCell(5).setCellValue("Minimum Weight");
	    vehicleTypesHeaderRow.createCell(6).setCellValue("Maximumm Weight");
	    vehicleTypesHeaderRow.createCell(7).setCellValue("Capacity");
	    vehicleTypesHeaderRow.createCell(8).setCellValue("Maximum Range");
	    vehicleTypesHeaderRow.createCell(9).setCellValue("Restrictions");
	    vehicleTypesHeaderRow.createCell(10).setCellValue("Height");
	    vehicleTypesHeaderRow.createCell(11).setCellValue("Empty Weight");
	    vehicleTypesHeaderRow.createCell(12).setCellValue("Length");
	    vehicleTypesHeaderRow.createCell(13).setCellValue("Minimum Cubic Weight");
	    vehicleTypesHeaderRow.createCell(14).setCellValue("Maximum Cubic Weight");
	    
	    rowIndex = 1;
	    for (VehicleTypes type : vehicleTypes) {
	    	XSSFRow curRow = vehicleTypesWorksheet.createRow(rowIndex++);
	    	curRow.createCell(0).setCellValue(type.getType());
	    	curRow.createCell(1).setCellValue(type.getSubType());
	    	curRow.createCell(2).setCellValue(type.getDescription());
	    	curRow.createCell(3).setCellValue(type.getMake());
	    	curRow.createCell(4).setCellValue(type.getModel());
	    	curRow.createCell(5).setCellValue(type.getMinimumWeight());
	    	curRow.createCell(6).setCellValue(type.getMaximumWeight());
	    	curRow.createCell(7).setCellValue(type.getCapacity());
	    	curRow.createCell(8).setCellValue(type.getMaximumRange());
	    	curRow.createCell(9).setCellValue(type.getRestrictions());
	    	curRow.createCell(10).setCellValue(type.getHeight());
	    	curRow.createCell(11).setCellValue(type.getEmptyWeight());
	    	curRow.createCell(12).setCellValue(type.getLength());
	    	curRow.createCell(13).setCellValue(type.getMinimumWeight());
	    	curRow.createCell(14).setCellValue(type.getMaximumCubicWeight());
	    }
	    
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
	    
	    List<Technicians> technicians = carrier.getTechnicians();
	    XSSFRow technicansHeaderRow = technicansWorksheet.createRow(0);
	    technicansHeaderRow.createCell(0).setCellValue("Contact First + Last Name");
	    technicansHeaderRow.createCell(1).setCellValue("Skill Grade");
	    
	    rowIndex = 1;
	    for(Technicians tech : technicians) {
	    	XSSFRow curRow = technicansWorksheet.createRow(rowIndex++);
	    	curRow.createCell(0).setCellValue(tech.getContact().getFirstName() + " " + tech.getContact().getLastName());
	    	curRow.createCell(1).setCellValue(tech.getSkill_grade());
	    }
	    
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
	    for (int i = 0; i < vehicleTypesHeaderRow.getLastCellNum(); i++) {
	    	vehicleTypesWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < vehicleHeaderRow.getLastCellNum(); i++) {
	    	vehicleWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < driversHeaderRow.getLastCellNum(); i++) {
	    	driversWorksheet.autoSizeColumn(i);
	    }
	    for (int i = 0; i < technicansHeaderRow.getLastCellNum(); i++) {
	    	technicansWorksheet.autoSizeColumn(i);
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
	
	public void removeEntriesFromRepository( Hashtable<String,Object> dataBaseState) {
		
		if(dataBaseState.get("orders") !=  null){maintenanceOrdersRepository.deleteAll((List <MaintenanceOrders>) dataBaseState.get("orders"));}
    	if(dataBaseState.get("drivers") != null) {driverRepository.deleteAll((List <Driver>) dataBaseState.get("drivers"));}
    	if(dataBaseState.get("vehicles") != null) {vehiclesRepository.deleteAll((List <Vehicles>) dataBaseState.get("vehicles"));}
    	if(dataBaseState.get("technicans") != null) {techniciansRepository.deleteAll((List <Technicians>) dataBaseState.get("technicans"));}
    	if(dataBaseState.get("contacts") != null) {contactsRepository.deleteAll((List <Contacts>) dataBaseState.get("contacts"));}
    	if(dataBaseState.get("vehicleTypes") != null) {vehicleTypesRepository.deleteAll((List <VehicleTypes>) dataBaseState.get("vehicleTypes"));}
    	if(dataBaseState.get("locations") != null) {locationsRepository.deleteAll((List <Locations>) dataBaseState.get("locations"));}
    }
	
	public void addEntriesToRepsoitory(Hashtable<String,Object> dataBaseState) {
		
	   	contactsRepository.saveAll((List <Contacts>) dataBaseState.get("contacts"));
    	vehicleTypesRepository.saveAll((List <VehicleTypes>) dataBaseState.get("vehicleTypes"));
    	locationsRepository.saveAll((List <Locations>) dataBaseState.get("locations"));
    	techniciansRepository.saveAll((List <Technicians>) dataBaseState.get("technicans"));
    	vehiclesRepository.saveAll((List <Vehicles>) dataBaseState.get("vehicles"));
    	driverRepository.saveAll((List <Driver>) dataBaseState.get("drivers"));
    	maintenanceOrdersRepository.saveAll((List <MaintenanceOrders>) dataBaseState.get("orders"));
	}
	
	@PostMapping("/upload-carrier")
	public String LoadCarrierExcelForm(@RequestParam("file") MultipartFile excelData, HttpSession session, Model model){
		User user = getLoggedInUser();
		Carriers carrier = user.getCarrier();
		
		List<Contacts> currentContacts = carrier.getContacts();
        List<VehicleTypes> currentVehicleTypes = carrier.getVehicleTypes();
        List<Locations> currentLocations = carrier.getLocations();
        List<Technicians> currentTechnicians = carrier.getTechnicians();
        List<Vehicles> currentVehicles = carrier.getVehicles();
        List<MaintenanceOrders> currentOrders = carrier.getOrders();
        List<Driver> currentDrivers = carrier.getDrivers();
        
        Hashtable<String,Object> currentDataBaseState = new Hashtable<String,Object>();
        Hashtable<String,Object> newDataBaseState = new Hashtable<String,Object>();
        
        
        currentDataBaseState.put("contacts",currentContacts);
        currentDataBaseState.put("vehicleTypes",currentVehicleTypes);
        currentDataBaseState.put("locations",currentLocations);
        currentDataBaseState.put("technicans",currentTechnicians);
        currentDataBaseState.put("vehicles",currentVehicles);
        currentDataBaseState.put("orders",currentOrders);
        currentDataBaseState.put("drivers",currentDrivers);
        
        removeEntriesFromRepository(currentDataBaseState);
        
		Hashtable<String, Long> vehicleTypeNameToId = new Hashtable<>();
		Hashtable<String, Long> locationNameToId = new Hashtable<>();
		Hashtable<String, Long> contactsFullNameToId = new Hashtable<>();
		Hashtable<String, Long> techniciansContactFullNameToId = new Hashtable<>();
		Hashtable<String, Long> vehiclePlateAndVinToId = new Hashtable<>();
		
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		XSSFWorkbook workbook;
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
			
			
			newDataBaseState.put("contacts",contacts);
	        newDataBaseState.put("vehicleTypes",vehicleTypes);
	        newDataBaseState.put("locations",locations);
	        newDataBaseState.put("technicans",technicians);
	        newDataBaseState.put("vehicles",vehicles);
	        newDataBaseState.put("maintenanceOrders",maintenanceOrders);
	        newDataBaseState.put("drivers",drivers);
			
			
			//Start Vehicle Types
			vehicleTypes = validationServiceImp.validateVehicleTypesSheet(vehicleTypesSheet);
			
			if (vehicleTypes == null) {
				Logger.info("{} attempted to save Vehicle Types but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				workbook.close();
				return "redirect:" + redirectLocation;
			}
			
			
			for(VehicleTypes vehicleType: vehicleTypes) {
				vehicleTypesRepository.save(vehicleType);
				if(vehicleTypeNameToId.containsKey(vehicleType.getMake() + " " + vehicleType.getModel())){
					Logger.error("{} saved Vehicle Type with ID the same ID.",user.getUsername(),vehicleType.getId());
					removeEntriesFromRepository(newDataBaseState);
					addEntriesToRepsoitory(currentDataBaseState);
					workbook.close();
					return "redirect:" + redirectLocation;
				}
				vehicleTypeNameToId.put(vehicleType.getMake() + " " + vehicleType.getModel(), vehicleType.getId());
				Logger.info("{} saved Vehicle Type with ID {}.",user.getUsername(),vehicleType.getId());
			}
			
			//Start Locations
			locations = validationServiceImp.validateLocationsSheet(locationsSheet);
			
			if (locations == null) {
				Logger.info("{} attempted to save locations but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				workbook.close();
				return "redirect:" + redirectLocation;
			}

			for(Locations location: locations) {
				locationsRepository.save(location);
				if(locationNameToId.containsKey(location.getName())){
					Logger.error("{} saved Location with ID the same ID.",user.getUsername(),location.getId());
					removeEntriesFromRepository(newDataBaseState);
					addEntriesToRepsoitory(currentDataBaseState);
					workbook.close();
					return "redirect:" + redirectLocation;
				}
				
				locationNameToId.put(location.getName(), location.getId());
				Logger.info("{} saved Location with ID {}.",user.getUsername(),location.getId());
			
			}
			
			//Start Contacts
			contacts = validationServiceImp.validateContactsSheet(contactsSheet);
			
			if (contacts == null) {
				Logger.info("{} attempted to save Contacts but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				workbook.close();
				return "redirect:" + redirectLocation; 
			}
			
			for(Contacts contact: contacts) {
				contactsRepository.save(contact);
				if(contactsFullNameToId.containsKey(contact.getFirstName() + " " + contact.getLastName())){
					Logger.error("{} saved Contact with ID the same ID.",user.getUsername(),contact.getId());
					removeEntriesFromRepository(newDataBaseState);
					addEntriesToRepsoitory(currentDataBaseState);
					workbook.close();
					return "redirect:" + redirectLocation;
				}
				
				contactsFullNameToId.put(contact.getFirstName() + " " + contact.getLastName(), contact.getId());
				Logger.info("{} saved Contact with ID {}.",user.getUsername(),contact.getId());
			
			}
			
			//Start Vehicles
			vehicles = validationServiceImp.validateVehiclesSheet(vehiclesSheet, vehicleTypeNameToId, locationNameToId);
			
			if (vehicles == null) {
				Logger.info("{} attempted to save Vehicle but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				workbook.close();
				return "redirect:" + redirectLocation;
			}
			
			for(Vehicles vehicle: vehicles) {
				vehiclesRepository.save(vehicle);
				if(vehiclePlateAndVinToId.containsKey(vehicle.getPlateNumber() + " " + vehicle.getVinNumber())){
					Logger.error("{} saved Vehicle with ID the same ID.",user.getUsername(),vehicle.getId());
					removeEntriesFromRepository(newDataBaseState);
					addEntriesToRepsoitory(currentDataBaseState);
					workbook.close();
					return "redirect:" + redirectLocation;
				}
				
				vehiclePlateAndVinToId.put(vehicle.getPlateNumber() + " " + vehicle.getVinNumber(), vehicle.getId());
				Logger.info("{} saved Vehicle with ID {}.",user.getUsername(),vehicle.getId());
			
			}
			
			
			//Start Technicians
			technicians = validationServiceImp.validateTechniciansSheet(techniciansSheet, contactsFullNameToId);
			
			if (technicians == null) {
				Logger.info("{} attempted to save Technicians but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				workbook.close();
				return "redirect:" + redirectLocation; 
			}
			
			for(Technicians technician: technicians) {
				techniciansRepository.save(technician);
				if(techniciansContactFullNameToId.containsKey(technician.getContact().getFirstName() + " " + technician.getContact().getLastName())){
					Logger.error("{} saved Technician with ID the same ID.",user.getUsername(),technician.getId());
					removeEntriesFromRepository(newDataBaseState);
					addEntriesToRepsoitory(currentDataBaseState);
					workbook.close();
					return "redirect:" + redirectLocation;
				}
				
				techniciansContactFullNameToId.put(technician.getContact().getFirstName() + " " + technician.getContact().getLastName(), technician.getId());
				Logger.info("{} saved Technician with ID {}.",user.getUsername(),technician.getId());
			
			}
			
			
			//Start Drivers
			drivers = validationServiceImp.validateDriverSheet(driversSheet, vehiclePlateAndVinToId, contactsFullNameToId);
			
			if (drivers == null) {
				Logger.info("{} attempted to save Drivers but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				workbook.close();
				return "redirect:" + redirectLocation;
			}
			
			for(Driver driver: drivers) {
				driverRepository.save(driver);
				Logger.info("{} saved Driver with ID {}.",user.getUsername(),driver.getId());
			}
			
			
			//Start MaintenanceOrders
			maintenanceOrders = validationServiceImp.validateMaintenanceOrdersSheet(maintenanceOrdersSheet, vehiclePlateAndVinToId, techniciansContactFullNameToId);

			if (maintenanceOrders == null) {
				Logger.info("{} attempted to save Maintenance Orders but failed.",user.getUsername());
				removeEntriesFromRepository(newDataBaseState);
				addEntriesToRepsoitory(currentDataBaseState);
				return "redirect:" + redirectLocation; 
			}
			
			for(MaintenanceOrders maintenanceorder: maintenanceOrders) {
				maintenanceOrdersRepository.save(maintenanceorder);
				Logger.info("{} saved Maintenance Order with ID {}.",user.getUsername(), maintenanceorder.getId());
			}
			
			
			
		}
		catch(Exception e ) {
			e.printStackTrace();
			removeEntriesFromRepository(newDataBaseState);
			addEntriesToRepsoitory(currentDataBaseState);
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