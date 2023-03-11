package edu.sru.thangiah.webrouting.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.web.UserValidator;



@Service
public class ValidationServiceImp {
	

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
	
	private static final Logger Logger = LoggerFactory.getLogger(ValidationServiceImp.class);
	
	
	public ValidationServiceImp (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository, 
			VehicleTypesRepository vehicleTypesRepository,LocationsRepository	locationsRepository, ContactsRepository contactsRepository, TechniciansRepository techniciansRepository,
			DriverRepository driverRepository, MaintenanceOrdersRepository maintenanceOrdersRepository) {
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
		
	}
	
	
	public List<Shipments> validateShipmentSheet(XSSFSheet worksheet){
		
		List <Shipments> result = new ArrayList<>();
		
		try {
			User user = getLoggedInUser();
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				Boolean isNull = false;
				 
				Shipments shipment = new Shipments();
		        XSSFRow row = worksheet.getRow(i);
		        
		        for (int j = 0; j<16; j++) {
		        	if (row.getCell(j)== null || row.getCell(j).toString().equals("")) {
		        		isNull = true;
		        	}
		        }
		        if (isNull == true) {
		        	break;
		        }
		        
		        
		        String scac = "";											//Not Set By Upload
	    		String freightBillNumber = "0.00";							//Not Set By Upload
	    		String paidAmount = "0.00";									//Not Set By Upload
	    		String fullFreightTerms = "PENDING"; 						//Not Set By Upload (ALWAYS PENDING)
	    		
	    		
	    		String clientName = row.getCell(0).toString().strip();
			    String clientMode = row.getCell(1).toString().strip();
			    String date = row.getCell(2).toString();
	    		String commodityClass = row.getCell(3).toString().strip();
	    		String commodityPieces = row.getCell(4).toString().strip();
	    		String commodityPaidWeight = row.getCell(5).toString().strip();
	    		String shipperCity = row.getCell(6).toString().strip();
	    		String shipperState = row.getCell(7).toString().strip();
	    		String shipperZip = row.getCell(8).toString().strip();
	    		String shipperLatitude = row.getCell(9).toString().strip();
	    		String shipperLongitude = row.getCell(10).toString().strip();
	    		String consigneeCity = row.getCell(11).toString().strip();
	    		String consigneeState = row.getCell(12).toString().strip();
	    		String consigneeZip = row.getCell(13).toString().strip();
	    		String consigneeLatitude = row.getCell(14).toString().strip();
	    		String consigneeLongitude = row.getCell(15).toString().strip();
	    		
	    		
	    		commodityClass = commodityClass.substring(0, commodityClass.length() - 2);
	    		commodityPieces = commodityPieces.substring(0, commodityPieces.length() - 2);
	    		commodityPaidWeight = commodityPaidWeight.substring(0, commodityPaidWeight.length() - 2);
	    		shipperZip = shipperZip.substring(0, shipperZip.length() - 2);
	    		shipperLatitude = shipperLatitude.substring(0, shipperLatitude.length() - 2);
	    		shipperLongitude = shipperLongitude.substring(0, shipperLongitude.length() - 2);
	    		consigneeZip = consigneeZip.substring(0, consigneeZip.length() - 2);
	    		consigneeLatitude = consigneeLatitude.substring(0, consigneeLatitude.length() - 2);
	    		consigneeLongitude = consigneeLongitude.substring(0, consigneeLongitude.length() - 2);
	    		
	    		
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("clientName", clientName);
	    		hashtable.put("clientMode", clientMode);
	    		hashtable.put("date", date);
	    		hashtable.put("commodityClass", commodityClass);
	    		hashtable.put("commodityPieces", commodityPieces);
	    		hashtable.put("commodityPaidWeight", commodityPaidWeight);
	    		hashtable.put("shipperCity", shipperCity);
	    		hashtable.put("shipperState", shipperState);
	    		hashtable.put("shipperZip", shipperZip);
	    		hashtable.put("shipperLatitude", shipperLatitude);
	    		hashtable.put("shipperLongitude", shipperLongitude);
	    		hashtable.put("consigneeCity", consigneeCity);
	    		hashtable.put("consigneeState", consigneeState);
	    		hashtable.put("consigneeZip", consigneeZip);
	    		hashtable.put("consigneeLatitude", consigneeLatitude);
	    		hashtable.put("consigneeLongitude", consigneeLongitude);
	    		
	    		
	    		hashtable.put("scac", scac);
	    		hashtable.put("freightBillNumber", freightBillNumber);
	    		hashtable.put("paidAmount", paidAmount);
	    		hashtable.put("fullFreightTerms", fullFreightTerms);
	    		
	    		
	    		shipment = validateShipment(hashtable);
	    		if (shipment == null) {
	    			continue;								//Change this to return null if you want the upload to fail if any are incorrect
	    		}
	    		
	    		shipment.setCarrier(null);					//THIS IS DEFAULT
	    		shipment.setVehicle(null);					//THIS IS DEFAULT
		        shipment.setUser(user);						//THIS USER
		        
		        result.add(shipment);
			 		
			 }
			
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public Shipments validateShipment(Hashtable<String, String> hashtable) {
		
		List<String> acceptedFreightTerms = Arrays.asList("PENDING", "AVAILABLE SHIPMENT", "AWAITING ACCEPTANCE", "BID ACCEPTED", "FROZEN");
		List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
		List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
		
		User user = getLoggedInUser();
		
		String clientName = (String) hashtable.get("clientName");
		String clientMode = (String)hashtable.get("clientMode");
		String date = (String)hashtable.get("date");
		String commodityClass = (String)hashtable.get("commodityClass");
		String commodityPieces = (String)hashtable.get("commodityPieces");
		String commodityPaidWeight = (String)hashtable.get("commodityPaidWeight");
		String shipperCity = (String)hashtable.get("shipperCity");
		String shipperState = (String)hashtable.get("shipperState");
		String shipperZip = (String)hashtable.get("shipperZip");
		String shipperLatitude = (String)hashtable.get("shipperLatitude");
		String shipperLongitude = (String)hashtable.get("shipperLongitude");
		String consigneeCity = (String)hashtable.get("consigneeCity");
		String consigneeState = (String)hashtable.get("consigneeState");
		String consigneeZip = (String)hashtable.get("consigneeZip");
		String consigneeLatitude = (String)hashtable.get("consigneeLatitude");
		String consigneeLongitude = (String)hashtable.get("consigneeLongitude");
		String freightBillNumber = (String)hashtable.get("freightBillNumber");
		String paidAmount = (String)hashtable.get("paidAmount");
		String scac = (String)hashtable.get("scac");
		String fullFreightTerms = (String)hashtable.get("fullFreightTerms");
		
		
		if(!(acceptedFreightTerms.contains(fullFreightTerms))) {
			Logger.error("{} attempted to upload a shipment but the Full Freight Terms must be an accepted term.",user.getUsername());
			return null;
		}
		
		if(!(scac.length() <= 4 && scac.length() >= 2) || !(scac.matches("^[a-zA-Z0-9]+$"))) {
			if (!scac.equals("")) {
					Logger.error("{} attempted to upload a shipment but the SCAC must be between 2 and 4 characters long or empty.",user.getUsername());
					return null;
				}	
			}
		
		if(!(paidAmount.length() <= 16 && paidAmount.length() > 0) || !(freightBillNumber.matches("^[0-9]*\\.?[0-9]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Paid Amount must be between 0 and 16 numbers long.",user.getUsername());
			return null;
		}
		
		if(!(freightBillNumber.length() <= 32 && freightBillNumber.length() > 0) || !(freightBillNumber.matches("^[0-9]*\\.?[0-9]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Freight Bill Number must be between 0 and 32 numbers long.",user.getUsername());
			return null;
		}
		
		if (!(clientName.length() <= 64 && clientName.length() > 0) || !(clientName.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Client Name must be between 0 and 64 characters and alphanumeric.",user.getUsername());
			return null;
		}
		
		if(!(clientMode.equals("LTL") || clientMode.equals("FTL"))) {
			Logger.error("{} attempted to upload a shipment but the Client Mode must be LTL or FTL.",user.getUsername());
			return null;
		}
		
		
		if(!(date.length() <= 12 && date.length() > 0 && date.matches("^\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}$"))) { 
			Logger.error("{} attempted to upload a shipment but the Date must be between 0 and 12 characters and formated MM/DD/YYYY.",user.getUsername());
			return null;
		}
		
		
		if(!(commodityClass.length() <= 12 && commodityClass.length() > 0) || !(commodityClass.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Commodity Class must be between 0 and 12 characters and alphanumeric.",user.getUsername());
			return null;
		}
		
		if(!(commodityPieces.length() <= 64 && commodityPieces.length() > 0) || !(commodityPieces.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Commodity Pieces must be between 0 and 64 characters long and numeric.",user.getUsername());
			return null;
		}
		
		if(!(commodityPaidWeight.length() <= 16 && commodityPaidWeight.length() > 0) || !(commodityPaidWeight.matches("^[0-9.]*\\.?[0-9.]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Commodity Paid Weight must be between 0 and 16 characters long and numeric.",user.getUsername());
			return null;
		}
		
		if(!(shipperCity.length() <= 64 && shipperCity.length() > 0) || !(shipperCity.matches("^[a-zA-Z]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Shipper City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
			return null;
		}
		
		if(!(states.contains(shipperState) || stateAbbreviations.contains(shipperState))) {
			Logger.error("{} attempted to upload a shipment but the Shipper State must be a state or state abbreviation.",user.getUsername());
			return null;
		}
		
		if(!(shipperZip.length() <= 12 && shipperZip.length() > 0) || !(shipperZip.matches("^[0-9.]+$"))){
			Logger.error("{} attempted to upload a shipment but the Shipper Zip must be between 0 and 12 characters and is numeric.",user.getUsername());
			return null;
		}
		
		if(!(shipperLatitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))) {
			Logger.error("{} attempted to upload a shipment but the Shipper Latitude must be between 90 and -90 up to 7 decimal places.",user.getUsername());
			return null;
		}
		
		if(!(shipperLongitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))) {
			Logger.error("{} attempted to upload a shipment but the Shipper Longitude must be between 0 and 12 characters.",user.getUsername());
			return null;
		}
		
		if(!(consigneeCity.length() <= 64 && consigneeCity.length() > 0) || !( consigneeCity.matches("^[a-zA-Z]+$"))) {
			Logger.error("{} attempted to upload a shipment but the Consignee City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
			return null;
		}
		
		if(!(states.contains(consigneeState) || stateAbbreviations.contains(consigneeState))) {
			Logger.error("{} attempted to upload a shipment but the Consignee State must be a state or state abbreviation.",user.getUsername());
			return null;
		}
		
		if(!(consigneeZip.length() <= 12 && consigneeZip.length() > 0) || !(consigneeZip.matches("^[0-9.]+$"))){
			Logger.error("{} attempted to upload a shipment but the Consignee Zip must be between 0 and 12 characters and is alphabetic.",user.getUsername());
			return null;
		}
		
		if(!(consigneeLatitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))) {
			Logger.error("{} attempted to upload a shipment but the Consignee Latitude must be between 90 and -90 up to 7 decimal places.",user.getUsername());
			return null;
		}
		
		if(!(consigneeLongitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))) {
			Logger.error("{} attempted to upload a shipment but the Consignee Longitude must be between 180 and -180 up to 7 decimal places.",user.getUsername());
			return null;
		}
		Shipments shipment = new Shipments();
		
		shipment.setClient(clientName);
		shipment.setClientMode(clientMode);
		shipment.setShipDate(date);
		shipment.setCommodityClass(commodityClass);
		shipment.setCommodityPieces(commodityPieces);
		shipment.setCommodityPaidWeight(commodityPaidWeight);
		shipment.setShipperCity(shipperCity);
		shipment.setShipperState(shipperState);
		shipment.setShipperZip(shipperZip);
		shipment.setShipperLatitude(consigneeLongitude);
		shipment.setShipperLongitude(shipperLongitude);
		shipment.setConsigneeCity(consigneeCity);
		shipment.setConsigneeState(consigneeState);
		shipment.setConsigneeZip(consigneeZip);
		shipment.setConsigneeLatitude(consigneeLatitude);
		shipment.setConsigneeLongitude(consigneeLongitude);
		
		shipment.setFreightbillNumber(freightBillNumber);
		shipment.setPaidAmount(paidAmount);
		shipment.setFullFreightTerms(fullFreightTerms);
		shipment.setScac(scac);
		
		return shipment;	
		
	}
	
	
	public List<VehicleTypes> validateVehicleTypesSheet(XSSFSheet worksheet){
		List <VehicleTypes> result = new ArrayList<>();
		try {
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				Boolean isNull = false;
				 
				VehicleTypes VehicleType = new VehicleTypes();
		        XSSFRow row = worksheet.getRow(i);
		        
		        for (int j = 0; j<15; j++) {
		        	if (row.getCell(j)== null || row.getCell(j).toString().equals("")) {
		        		isNull = true;
		        	}
		        }
		        if (isNull == true) {
		        	break;
		        }
		        
		        String type = row.getCell(0).toString().strip();
			    String subType = row.getCell(1).toString().strip();
	    		String description = row.getCell(2).toString().strip();
			    String make = row.getCell(3).toString().strip();
			    String model = row.getCell(4).toString();
	    		String minimumWeight = row.getCell(5).toString().strip();
	    		String maximumWeight = row.getCell(6).toString().strip();
	    		String capacity = row.getCell(7).toString().strip();
	    		String maximumRange = row.getCell(8).toString().strip();
	    		String restrictions = row.getCell(9).toString().strip();
	    		String height = row.getCell(10).toString().strip();
	    		String emptyWeight = row.getCell(11).toString().strip();
	    		String length = row.getCell(12).toString().strip();
	    		String minimumCubicWeight = row.getCell(13).toString().strip();
	    		String maximumCubicWeight = row.getCell(14).toString().strip();

	    		
	    		minimumWeight = minimumWeight.substring(0, minimumWeight.length() - 2);
	    		maximumWeight = maximumWeight.substring(0, maximumWeight.length() - 2);
	    		capacity = capacity.substring(0, capacity.length() - 2);
	    		maximumRange = maximumRange.substring(0, maximumRange.length() - 2);
	    		height = height.substring(0, height.length() - 2);
	    		emptyWeight = emptyWeight.substring(0, emptyWeight.length() - 2);
	    		length = length.substring(0, length.length() - 2);
	    		minimumCubicWeight = minimumCubicWeight.substring(0, minimumCubicWeight.length() - 2);
	    		maximumCubicWeight = maximumCubicWeight.substring(0, maximumCubicWeight.length() - 2);
	    		
	    		
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("type", type);
	    		hashtable.put("subType", subType);
	    		hashtable.put("description", description);
	    		hashtable.put("make", make);
	    		hashtable.put("model", model);
	    		hashtable.put("minimumWeight", minimumWeight);
	    		hashtable.put("maximumWeight", maximumWeight);
	    		hashtable.put("capacity", capacity);
	    		hashtable.put("maximumRange", maximumRange);
	    		hashtable.put("restrictions", restrictions);
	    		hashtable.put("height", height);
	    		hashtable.put("emptyWeight", emptyWeight);
	    		hashtable.put("length", length);
	    		hashtable.put("minimumCubicWeight", minimumCubicWeight);
	    		hashtable.put("maximumCubicWeight", maximumCubicWeight);
	    		
	    		VehicleType = validateVehicleTypes(hashtable);
	    		if (VehicleType == null) {
	    			return null;								
	    		}
	    		
		        result.add(VehicleType);
			 		
			 	}
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public VehicleTypes validateVehicleTypes(Hashtable<String, String> hashtable) {
		
		User user = getLoggedInUser();
		
		String type = (String) hashtable.get("type");
	    String subType = (String) hashtable.get("subType");
		String description = (String) hashtable.get("description");
	    String make = (String) hashtable.get("make");
	    String model = (String) hashtable.get("model");
		String minimumWeight = (String) hashtable.get("minimumWeight");
		String maximumWeight = (String) hashtable.get("maximumWeight");
		String capacity = (String) hashtable.get("capacity");
		String maximumRange = (String) hashtable.get("maximumRange");
		String restrictions = (String) hashtable.get("restrictions");
		String height = (String) hashtable.get("height");
		String emptyWeight = (String) hashtable.get("emptyWeight");
		String length = (String) hashtable.get("length");
		String minimumCubicWeight = (String) hashtable.get("minimumCubicWeight");
		String maximumCubicWeight = (String) hashtable.get("maximumCubicWeight");
		
		if (!(type.length() <= 32 && type.length() > 0) || !(type.matches("^[a-zA-Z]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Type was not between 1 and 32 alphabetic characters long.",user.getUsername());
			return null;	
		}
		
		if (!(subType.length() <= 32 && subType.length() > 0) || !(subType.matches("^[a-zA-Z]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Sub Type was not between 1 and 32 characters long.",user.getUsername());
			return null;	
		}
	
		if (!(description.length() <= 64 && description.length() > 0) || !(description.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Description was not between 1 and 64 characters long.",user.getUsername());
			return null;	
		}
		
		if(!(make.length() <= 32 && make.length() > 0) || !(make.matches("^[a-zA-Z0-9.-]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Make was not between 1 and 32 characters long.",user.getUsername());
			return null;
		}
		
		if(!(model.length() <= 32 && model.length() > 0) || !(model.matches("^[a-zA-Z0-9.-]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Make was not between 1 and 32 characters long.",user.getUsername());
			return null;
		}
		
		if (!(minimumWeight.length() <= 16 && minimumWeight.length() > 0) || !(minimumWeight.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Minimum Weight was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		if (!(maximumWeight.length() <= 16 && maximumWeight.length() > 0) || !(maximumWeight.matches("^[0-9.]+$"))) { 
			Logger.error("{} attempted to upload a Vehicle Type but the Maximum Weight was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		if(!(capacity.length() <= 16 && capacity.length() > 0) || !(capacity.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Capacity was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		if(!(maximumRange.length() <= 16 && maximumRange.length() > 0) || !(maximumRange.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Maximum Range was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		if(!(restrictions.length() <= 128 && restrictions.length() > 0) || !(restrictions.matches("^[a-zA-Z0-9./]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Restrictions was not between 1 and 128 alphanumeric characters long.",user.getUsername());
			return null;
		}
		
		if(!(height.length() <= 16 && height.length() > 0) || !(height.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Height was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		if(!(emptyWeight.length() <= 16 && emptyWeight.length() > 0) || !(emptyWeight.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Empty Weight was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		
		if(!(length.length() <= 16 && length.length() > 0) || !( length.matches("^[0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle Type but the Length was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		
		if(!(minimumCubicWeight.length() <= 16 && minimumCubicWeight.length() > 0) || !(minimumCubicWeight.matches("^[0-9.]+$"))){
			Logger.error("{} attempted to upload a Vehicle Type but the Minimum Cubic Weight was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}
		
		if(!(maximumCubicWeight.length() <= 16 && maximumCubicWeight.length() > 0) || !(maximumCubicWeight.matches("^[0-9.]+$"))){
			Logger.error("{} attempted to upload a Vehicle Type but the Maximum Cubic Weight was not between 1 and 16 numeric characters long.",user.getUsername());
			return null;
		}

		VehicleTypes vehicleType = new VehicleTypes();
		
	try {
		vehicleType.setType(type);
		vehicleType.setSubType(subType);
		vehicleType.setDescription(description);
		vehicleType.setMake(make);
		vehicleType.setModel(model);
		vehicleType.setMinimumWeight(Integer.parseInt(minimumWeight));
		vehicleType.setMaximumWeight(Integer.parseInt(maximumWeight));
		vehicleType.setCapacity(capacity);
		vehicleType.setMaximumRange(Integer.parseInt(maximumRange));
		vehicleType.setRestrictions(restrictions);
		vehicleType.setHeight(Integer.parseInt(height));
		vehicleType.setEmptyWeight(Integer.parseInt(emptyWeight));
		vehicleType.setLength(Integer.parseInt(length));
		vehicleType.setMinimumCubicWeight(Integer.parseInt(minimumCubicWeight));
		vehicleType.setMaximumCubicWeight(Integer.parseInt(maximumCubicWeight));
	}
	catch(Exception e) {
		e.printStackTrace();
		System.out.println("Failed to parseInt on Vehicle Types");
		return null;
	}

		return vehicleType;
	}
	

	public List<Contacts> validateContactsSheet(XSSFSheet worksheet){
		List <Contacts> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
	}
	public List<Technicians> validateTechniciansSheet(XSSFSheet worksheet){
		List <Technicians> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
	}
	public List<Vehicles> validateVehiclesSheet(XSSFSheet worksheet, Hashtable<String, Long> vehicleTypeNameToId, Hashtable<String, Long> locationNameToId){
		List <Vehicles> result = new ArrayList<>();
		try {
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				Boolean isNull = false;
				 
				Vehicles vehicle = new Vehicles();
		        XSSFRow row = worksheet.getRow(i);
		        
		        for (int j = 0; j<5; j++) {
		        	if (row.getCell(j)== null || row.getCell(j).toString().equals("")) {
		        		isNull = true;
		        	}
		        }
		        if (isNull == true) {
		        	break;
		        }
		        
		        String plate = row.getCell(0).toString().strip();
			    String vin = row.getCell(1).toString().strip();
	    		String manufacturedYear = row.getCell(2).toString().strip();
			    String vehicleTypeMakeModel = row.getCell(3).toString().strip();
			    String locationName = row.getCell(4).toString().strip();
			    
			    System.out.println("THIS:" + manufacturedYear);
	    		/*
	    		minimumWeight = minimumWeight.substring(0, minimumWeight.length() - 2);
	    		maximumWeight = maximumWeight.substring(0, maximumWeight.length() - 2);
	    		capacity = capacity.substring(0, capacity.length() - 2);
	    		maximumRange = maximumRange.substring(0, maximumRange.length() - 2);
	    		height = height.substring(0, height.length() - 2);
	    		emptyWeight = emptyWeight.substring(0, emptyWeight.length() - 2);
	    		length = length.substring(0, length.length() - 2);
	    		minimumCubicWeight = minimumCubicWeight.substring(0, minimumCubicWeight.length() - 2);
	    		maximumCubicWeight = maximumCubicWeight.substring(0, maximumCubicWeight.length() - 2);
	    		
	    		*/
			    
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("plate", plate);
	    		hashtable.put("vin", vin);
	    		hashtable.put("manufacturedYear", manufacturedYear);
	    		hashtable.put("vehicleTypeMakeModel", vehicleTypeMakeModel);
	    		hashtable.put("locationName", locationName);

	    		
	    		vehicle = validateVehicles(hashtable, vehicleTypeNameToId, locationNameToId );
	    		if (vehicle == null) {
	    			return null;								
	    		}
	    		
		        result.add(vehicle);
			 		
			 	}
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public Vehicles validateVehicles(Hashtable<String, String> hashtable, Hashtable<String, Long> vehicleTypeNameToId, Hashtable<String, Long> locationNameToId) {
		
		User user = getLoggedInUser();
		
		String plate = (String) hashtable.get("plate");
	    String vin = (String) hashtable.get("vin");
		String manufacturedYear = (String) hashtable.get("manufacturedYear");
	    String vehicleTypeMakeModel = (String) hashtable.get("vehicleTypeMakeModel");
	    String locationName = (String) hashtable.get("locationName");

		
		
		if (!(plate.length() <= 12 && plate.length() > 0) || !(plate.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle but the Plate was not between 1 and 12 alphanumeric characters long.",user.getUsername());
			return null;	
		}
		
		if (!(vin.length() <= 17 && vin.length() > 0) || !(vin.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle but the Plate was not between 1 and 17 alphanumeric characters long.",user.getUsername());
			return null;	
		}
	
		if (!(manufacturedYear.length() <= 4 && manufacturedYear.length() > 0) || !(manufacturedYear.matches("^[0-9]+$"))) {
			Logger.error("{} attempted to upload a Vehicle but the Year was not between 1 and 4 numeric characters long.",user.getUsername());
			return null;	
		}
		
		if(!(vehicleTypeMakeModel.contains(" "))) {
			Logger.error("{} attempted to upload a Vehicle but the Make and Model did not contain a space.",user.getUsername());
			return null;
		}
		
		if (!(locationName.length() <= 32 && locationName.length() > 0) || !(locationName.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} attempted to upload a Vehicle but the Location Name was not between 1 and 32 alphanumeric characters long.",user.getUsername());
			return null;
		}
		
		Vehicles vehicle = new Vehicles();
		
	try {
		
	    vehicle.setPlateNumber(plate);
	    vehicle.setVinNumber(vin);
	    vehicle.setManufacturedYear(manufacturedYear);
	    vehicle.setVehicleType(vehicleTypesRepository.findById(vehicleTypeNameToId.get(vehicleTypeMakeModel)).orElseThrow(() -> new IllegalArgumentException("Invalid vehicleType Id")));
	    vehicle.setLocation(locationsRepository.findById(locationNameToId.get(locationName)).orElseThrow(() -> new IllegalArgumentException("Invalid location Id")));
		vehicle.setCarrier(user.getCarrier());
		
	}
	catch(Exception e) {
		e.printStackTrace();
		System.out.println("Failed to set Vehicle");
		return null;
	}

		return vehicle;
	}
	
	
	public List<Driver> validateDriverSheet(XSSFSheet worksheet){
		List <Driver> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
	}
	public List<MaintenanceOrders> validateMaintenanceOrdersSheet(XSSFSheet worksheet){
		List <MaintenanceOrders> result = new ArrayList<>();
		try {
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				Boolean isNull = false;
				 
				MaintenanceOrders maintenanceOrder = new MaintenanceOrders();
		        XSSFRow row = worksheet.getRow(i);
		        
		        for (int j = 0; j<8; j++) {
		        	if (row.getCell(j)== null || row.getCell(j).toString().equals("")) {
		        		isNull = true;
		        	}
		        }
		        if (isNull == true) {
		        	break;
		        }
		        
		        String date = row.getCell(0).toString();
			    String details = row.getCell(1).toString().strip();
	    		String serviceType = row.getCell(2).toString().strip();
			    String cost = row.getCell(3).toString().strip();
			    String status = row.getCell(4).toString().strip();
			    String type = row.getCell(5).toString().strip();
			    String vechiclePlateAndVin = row.getCell(6).toString().strip();
			    String techniciansContactFullName = row.getCell(7).toString().strip();

			    System.out.println("THIS:" + date);
	    		
	    		cost = cost.substring(0, cost.length() - 2);
	    	
	    		
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("date", date);
	    		hashtable.put("details", details);
	    		hashtable.put("serviceType", serviceType);
	    		hashtable.put("cost", cost);
	    		hashtable.put("status", status);
	    		hashtable.put("type", type);
	    		hashtable.put("vechiclePlateAndVin", vechiclePlateAndVin);
	    		hashtable.put("techniciansContactFullName", techniciansContactFullName);


	    		
	    		//maintenanceOrder = validateMaintenanceOrders(hashtable, vehiclePlateAndVinToID, techniciansContactFullNameToID );
	    		//if (maintenanceOrder == null) {
	    		//	return null;								
	    		//}
	    		
		        result.add(maintenanceOrder);
			 		
			 	}
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/*
	public List<Locations> validateLocationsSheet(XSSFSheet worksheet){
		List <Locations> result = new ArrayList<>();
		try {
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				Boolean isNull = false;
				 
				Locations location = new Locations();
		        XSSFRow row = worksheet.getRow(i);
		        
		        for (int j = 0; j<9; j++) {
		        	if (row.getCell(j)== null || row.getCell(j).toString().equals("")) {
		        		isNull = true;
		        	}
		        }
		        if (isNull == true) {
		        	break;
		        }
		        
		        String name = row.getCell(0).toString().strip();
			    String addressOne = row.getCell(1).toString().strip();
	    		String addressTwo = row.getCell(2).toString().strip();
			    String city = row.getCell(3).toString().strip();
			    String state = row.getCell(4).toString();
	    		String zip = row.getCell(5).toString().strip();
	    		String latitude = row.getCell(6).toString().strip();
	    		String longitude = row.getCell(7).toString().strip();
	    		String type = row.getCell(8).toString().strip();

	    		
	    		latitude = latitude.substring(0, latitude.length() - 2);
	    		longitude = longitude.substring(0, longitude.length() - 2);
	    		zip = zip.substring(0, zip.length() - 2);

	    		
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("name", name);
	    		hashtable.put("addressOne", addressOne);
	    		hashtable.put("addressTwo", addressTwo);
	    		hashtable.put("city", city);
	    		hashtable.put("state", state);
	    		hashtable.put("zip", zip);
	    		hashtable.put("latitude", latitude);
	    		hashtable.put("longitude", longitude);
	    		hashtable.put("type", type);
	    	
	    		location = validateLocation(hashtable);
	    		if (location == null) {
	    			return null;								
	    		}
	    		
		        result.add(location);
			 		
			 	}
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
		public Locations validateLocation(Hashtable<String, String> hashtable) {

			User user = getLoggedInUser();
			
			String name = (String) hashtable.get("name");
		    String addressOne = (String) hashtable.get("addressOne");
			String addressTwo = (String) hashtable.get("addressTwo");
		    String city = (String) hashtable.get("city");
		    String model = (String) hashtable.get("state");
			String zip = (String) hashtable.get("zip");
			String latitude = (String) hashtable.get("latitude");
			String longitude = (String) hashtable.get("longitude");
			String type = (String) hashtable.get("type");


			List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
			List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
			
			User user = getLoggedInUser();
			
			
			
			if(!(name.length() <= 32 && name.length() > 0) || !(name.matches("^[a-zA-Z0-9.]+$"))) {
				Logger.error("{} attempted to upload a Location but the Name must be between 0 and 32 alphanumeric characters long.",user.getUsername());
				return null;
			}
			
			if(!(freightBillNumber.length() <= 32 && freightBillNumber.length() > 0) || !(freightBillNumber.matches("^[0-9]*\\.?[0-9]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Freight Bill Number must be between 0 and 32 numbers long.",user.getUsername());
				return null;
			}
			
			if (!(clientName.length() <= 64 && clientName.length() > 0) || !(clientName.matches("^[a-zA-Z0-9.]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Client Name must be between 0 and 64 characters and alphanumeric.",user.getUsername());
				return null;
			}
			
			if(!(clientMode.equals("LTL") || clientMode.equals("FTL"))) {
				Logger.error("{} attempted to upload a shipment but the Client Mode must be LTL or FTL.",user.getUsername());
				return null;
			}
			
			
			if(!(date.length() <= 12 && date.length() > 0 && date.matches("^\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}$"))) { 
				Logger.error("{} attempted to upload a shipment but the Date must be between 0 and 12 characters and formated MM/DD/YYYY.",user.getUsername());
				return null;
			}
			
			
			if(!(commodityClass.length() <= 12 && commodityClass.length() > 0) || !(commodityClass.matches("^[a-zA-Z0-9.]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Commodity Class must be between 0 and 12 characters and alphanumeric.",user.getUsername());
				return null;
			}
			
			if(!(commodityPieces.length() <= 64 && commodityPieces.length() > 0) || !(commodityPieces.matches("^[0-9.]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Commodity Pieces must be between 0 and 64 characters long and numeric.",user.getUsername());
				return null;
			}
			
			if(!(commodityPaidWeight.length() <= 16 && commodityPaidWeight.length() > 0) || !(commodityPaidWeight.matches("^[0-9.]*\\.?[0-9.]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Commodity Paid Weight must be between 0 and 16 characters long and numeric.",user.getUsername());
				return null;
			}
			
			if(!(shipperCity.length() <= 64 && shipperCity.length() > 0) || !(shipperCity.matches("^[a-zA-Z]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Shipper City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
				return null;
			}
			
			if(!(states.contains(shipperState) || stateAbbreviations.contains(shipperState))) {
				Logger.error("{} attempted to upload a shipment but the Shipper State must be a state or state abbreviation.",user.getUsername());
				return null;
			}
			
			if(!(shipperZip.length() <= 12 && shipperZip.length() > 0) || !(shipperZip.matches("^[0-9.]+$"))){
				Logger.error("{} attempted to upload a shipment but the Shipper Zip must be between 0 and 12 characters and is numeric.",user.getUsername());
				return null;
			}
			
			if(!(shipperLatitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))) {
				Logger.error("{} attempted to upload a shipment but the Shipper Latitude must be between 90 and -90 up to 7 decimal places.",user.getUsername());
				return null;
			}
			
			if(!(shipperLongitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))) {
				Logger.error("{} attempted to upload a shipment but the Shipper Longitude must be between 0 and 12 characters.",user.getUsername());
				return null;
			}
			
			if(!(consigneeCity.length() <= 64 && consigneeCity.length() > 0) || !( consigneeCity.matches("^[a-zA-Z]+$"))) {
				Logger.error("{} attempted to upload a shipment but the Consignee City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
				return null;
			}
			
			if(!(states.contains(consigneeState) || stateAbbreviations.contains(consigneeState))) {
				Logger.error("{} attempted to upload a shipment but the Consignee State must be a state or state abbreviation.",user.getUsername());
				return null;
			}
			
			if(!(consigneeZip.length() <= 12 && consigneeZip.length() > 0) || !(consigneeZip.matches("^[0-9.]+$"))){
				Logger.error("{} attempted to upload a shipment but the Consignee Zip must be between 0 and 12 characters and is alphabetic.",user.getUsername());
				return null;
			}
			
			if(!(consigneeLatitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))) {
				Logger.error("{} attempted to upload a shipment but the Consignee Latitude must be between 90 and -90 up to 7 decimal places.",user.getUsername());
				return null;
			}
			
			if(!(consigneeLongitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))) {
				Logger.error("{} attempted to upload a shipment but the Consignee Longitude must be between 180 and -180 up to 7 decimal places.",user.getUsername());
				return null;
			}

			Locations location = new Locations();
			
		try {
			vehicleType.setType(type);
			vehicleType.setSubType(subType);
			vehicleType.setDescription(description);
			vehicleType.setMake(make);
			vehicleType.setModel(model);
			vehicleType.setMinimumWeight(Integer.parseInt(minimumWeight));
			vehicleType.setMaximumWeight(Integer.parseInt(maximumWeight));
			vehicleType.setCapacity(capacity);
			vehicleType.setMaximumRange(Integer.parseInt(maximumRange));
			vehicleType.setRestrictions(restrictions);
			vehicleType.setHeight(Integer.parseInt(height));
			vehicleType.setEmptyWeight(Integer.parseInt(emptyWeight));
			vehicleType.setLength(Integer.parseInt(length));
			vehicleType.setMinimumCubicWeight(Integer.parseInt(minimumCubicWeight));
			vehicleType.setMaximumCubicWeight(Integer.parseInt(maximumCubicWeight));
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to parseInt on Vehicle Types");
			return null;
		}

			return vehicleType;
		}
			
			
				return location
	}	
			
		}
		*/
	
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