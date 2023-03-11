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

import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Driver;
import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.MaintenanceOrders;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.Technicians;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.domain.Vehicles;
import edu.sru.thangiah.webrouting.repository.UserRepository;



@Service
public class ValidationServiceImp {
	
	private static final Logger Logger = LoggerFactory.getLogger(ValidationServiceImp.class);

	private SecurityService securityService;
	
	private UserService userService;
	
	private UserRepository userRepository;
	
	public ValidationServiceImp(SecurityService securityService,UserRepository userRepository, UserService userService ) {
		this.securityService = securityService;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	
	
	
	
	
	public List<Shipments> validateShipmentSheet(XSSFSheet worksheet){
		
		List <Shipments> result = new ArrayList<>();
		
		try {
			User user = getLoggedInUser();
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				
				 
				Shipments shipment = new Shipments();
		        XSSFRow row = worksheet.getRow(i);
		        
		        
		        if(row.getCell(0)== null || row.getCell(0).toString().equals("")) {
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
	    		
	    		/*
	    		commodityClass = commodityClass.substring(0, commodityClass.length() - 2);
	    		commodityPieces = commodityPieces.substring(0, commodityPieces.length() - 2);
	    		commodityPaidWeight = commodityPaidWeight.substring(0, commodityPaidWeight.length() - 2);
	    		shipperZip = shipperZip.substring(0, shipperZip.length() - 2);
	    		shipperLatitude = shipperLatitude.substring(0, shipperLatitude.length() - 2);
	    		shipperLongitude = shipperLongitude.substring(0, shipperLongitude.length() - 2);
	    		consigneeZip = consigneeZip.substring(0, consigneeZip.length() - 2);
	    		consigneeLatitude = consigneeLatitude.substring(0, consigneeLatitude.length() - 2);
	    		consigneeLongitude = consigneeLongitude.substring(0, consigneeLongitude.length() - 2);
	    		*/
	    		
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
	    			return null;
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
			Logger.error("{} attempted to upload a shipment but the Client Mode must be between 0 and 3 characters and alphanumeric.",user.getUsername());
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
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
		
	}
	
	/* Breaks the locations worksheet up into strings. Adds the strings to a hashtable 
	sends the hashtable to validateLocations<>
	If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
	*/
	public List<Locations> validateLocationsSheet(XSSFSheet worksheet){
		List <Locations> result = new ArrayList<>();
		
		try {
			User user = getLoggedInUser();
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				
				 
				Locations location = new Locations();
		        XSSFRow row = worksheet.getRow(i);
		        
		        
		        if(row.getCell(0)== null || row.getCell(0).toString().equals("")) {
		        	break;
		        }
		        
		        String locationName = row.getCell(0).toString().strip();
	    		String streetAddress1 = row.getCell(4).toString().strip();
	    		String streetAddress2 = row.getCell(5).toString().strip();
	    		String locationCity = row.getCell(6).toString().strip();
	    		String locationState = row.getCell(7).toString().strip();
	    		String locationZip = row.getCell(8).toString().strip();
	    		String locationLatitude = row.getCell(9).toString().strip();
	    		String locationLongitude = row.getCell(10).toString().strip();
	    		String carrier = row.getCell(9).toString().strip();
	    		String locationType = row.getCell(10).toString().strip();
	    			
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("locationName", locationName);
	    		hashtable.put("streetAddress1", streetAddress1);
	    		hashtable.put("streetAddress2", streetAddress2);
	    		hashtable.put("locationCity", locationCity); 
	    		hashtable.put("locationState", locationState);
	    		hashtable.put("locationZip", locationZip);
	    		hashtable.put("locationLatitude", locationLatitude);
	    		hashtable.put("locationLongitude", locationLongitude);
	    		hashtable.put("carrier", carrier);
	    		hashtable.put("locationType", locationType);
	    		
	 
	    		location = validateLocations(hashtable);
	    		if (location == null) {
	    			return null;
	    		}

		        location.setCarrier(user.getCarrier());						//THIS USER
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
		public Locations validateLocations(Hashtable<String, String> hashtable) {
			
			List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
			List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
			
			User user = getLoggedInUser();
			
			String locationName = (String) hashtable.get("locationName");
			String streetAddress1 = (String)hashtable.get("streetAddress1");
			String streetAddress2 = (String)hashtable.get("streetAddress2");
			String locationCity = (String)hashtable.get("locationCity");
			String locationState = (String)hashtable.get("locationState");
			String locationZip = (String)hashtable.get("locationZip");
			String latitude = (String)hashtable.get("latitude");
			String longitude = (String)hashtable.get("longitude");
			//String carrier = (String)hashtable.get("carrier");
			String locationType = (String)hashtable.get("locationType");
			
			if (!(locationName.length() < 32 && locationName.length() > 0) || !(locationName.matches("^[a-zA-Z]+$"))) { 
    			Logger.info("{} attempted to upload a location name field must be between 0 and 32 characters and alphabetic.",user.getUsername());
    			return null;
    		}
    		
			
    		if(!(streetAddress1.length() < 64 && streetAddress1.length() > 0) || !(streetAddress1.matches("\\d+\\s+([a-zA-Z.]+\\s?)+"))) { //validation working
    			Logger.info("{} attempted to upload a location but location address must be between 0 and 64 characters that are alphanumeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(streetAddress2.length() < 64 && streetAddress2.length() > 0) || !(streetAddress2.matches("^[A-Za-z0-9./-]+(?:[\\s-][A-Za-z0-9.-]+)*$"))) { //Validation working
    			Logger.info("{} attempted to upload a location street address but it must be 2 must be between 0 and 64 characters that are alphanumeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(locationCity.length() < 64 && locationCity.length() > 0) || !(locationCity.matches("^[A-Za-z]+(?:[\\s-][A-Za-z]+)*$"))) { //Validation for a city made up of one or two words.
    			Logger.info("{} attempted to upload a location city but location city must be between 0 and 64 characters and is alphabetic.",user.getUsername());
    			return null;
    		}
    		
    		if(!(states.contains(locationState) || stateAbbreviations.contains(locationState))) {  
    			Logger.info("{} attempted to upload a location state but location state must be a state or state abbreviation.",user.getUsername());
    			return null;
    		}
    		
    		if(!(locationZip.length() < 12 && locationZip.length() > 0) || !(locationZip.matches("^[0-9.]+$"))){ //validation working
    			Logger.info("{} attempted to upload a location zip but location zip must be between 0 and 12 characters and is numeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(latitude.length() < 13 && latitude.length() > 0) || !(latitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))){ 
    			Logger.info("{} attempted to upload a locations Latitude - must be between 90 and -90 up to 7 decimal places." ,user.getUsername());
    			return null;
    		}
    		
    		if(!(longitude.length() < 13 && longitude.length() > 0) || !(longitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))){ 
    			Logger.info("{} attempted to upload a locations latitude - locations longitude must be between 0 and 12 characters.",user.getUsername());
    			return null;
    		}
    		if(!(locationType.length() < 64 && locationType.length() > 0) || !(locationType.matches("^[a-zA-Z]+$"))){ 
    			Logger.info("{} attempted to upload a location type must be 0 to 32 alphabetic characters.",user.getUsername());
    			return null;
    		}
    	 
    		Locations location = new Locations();
    		

    		location.setName(locationName);
    		location.setStreetAddress1(streetAddress1);
    		location.setStreetAddress2(streetAddress2);
    		location.setCity(locationCity);
    		location.setState(locationState);
    		location.setZip(locationZip);
    		location.setLatitude(latitude);
    		location.setLongitude(longitude);
    		location.setCarrier(user.getCarrier());
    		location.setLocationType(locationType);
	    	

    		
    		return location;
	}

	/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
	sends the hashtable to validate<>
	If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
	*/
	public List<Contacts> validateContactsSheet(XSSFSheet worksheet){
		List <Contacts> result = new ArrayList<>();
	
		try {
			User user = getLoggedInUser();
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				
				 
				Contacts contact = new Contacts();
		        XSSFRow row = worksheet.getRow(i);
		        
		        
		        if(row.getCell(0)== null || row.getCell(0).toString().equals("")) {
		        	break;
		        }
		        
		        String firstName = row.getCell(0).toString().strip();
			    String lastName = row.getCell(1).toString().strip();
			    String middleInitial = row.getCell(2).toString().strip();
	    		String emailAddress = row.getCell(3).toString().strip();
	    		String streetAddress1 = row.getCell(4).toString().strip();
	    		String streetAddress2 = row.getCell(5).toString().strip();
	    		String contactCity = row.getCell(6).toString().strip();
	    		String contactState = row.getCell(7).toString().strip();
	    		String contactZip = row.getCell(8).toString().strip();
	    		String primaryPhone = row.getCell(9).toString().strip();
	    		String workPhone = row.getCell(10).toString().strip();
	    			
	    		Hashtable<String, String> hashtable = new Hashtable<>();
	    		
	    		hashtable.put("firstName", firstName);
	    		hashtable.put("lastName", lastName);
	    		hashtable.put("middleInitial", middleInitial);
	    		hashtable.put("emailAddress", emailAddress);
	    		hashtable.put("streetAddress1", streetAddress1);
	    		hashtable.put("streetAddress2", streetAddress2);
	    		hashtable.put("contactCity", contactCity); 
	    		hashtable.put("contactState", contactState);
	    		hashtable.put("contactZip", contactZip);
	    		hashtable.put("primaryPhone", primaryPhone);
	    		hashtable.put("workPhone", workPhone);
	 
	    		contact = validateContact(hashtable);
	    		if (contact == null) {
	    			return null;
	    		}

		        contact.setCarrier(user.getCarrier());						//THIS USER
		        result.add(contact);	
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	    		
		return result;
	}
		public Contacts validateContact(Hashtable<String, String> hashtable) {
			
			List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
			List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
			
			User user = getLoggedInUser();
			
			String firstName = (String) hashtable.get("firstName");
			String lastName = (String)hashtable.get("lastName");
			String middleInitial = (String)hashtable.get("middleInitial");
			String emailAddress = (String)hashtable.get("emailAddress");
			String streetAddress1 = (String)hashtable.get("streetAddress1");
			String streetAddress2 = (String)hashtable.get("streetAddress2");
			String contactCity = (String)hashtable.get("contactCity");
			String contactState = (String)hashtable.get("contactState");
			String contactZip = (String)hashtable.get("contactZip");
			String primaryPhone = (String)hashtable.get("primaryPhone");
			String workPhone = (String)hashtable.get("workPhone");
			
			if (!(firstName.length() < 32 && firstName.length() > 0) || !(firstName.matches("^[a-zA-Z]+$"))) { 
    			Logger.info("{} attempted to upload a contact but Contact first name field must be between 0 and 32 characters and alphabetic.",user.getUsername());
    			return null;
    		}
    		
    		if(!(lastName.length() < 32 && lastName.length() > 0) || !(lastName.matches("^[a-zA-Z]+$"))) {//Validation working
    			Logger.info("{} attempted to upload a contact but Contact last name field must be between 0 and 32 characters and alphbetic",user.getUsername());
    			return null;
    		}
    		
    		if(!(middleInitial.length() < 16 && middleInitial.length() > 0) || !(middleInitial.matches("^[A-Za-z]{1}$"))) { //validation working
    			Logger.info("{} attempted to upload a contact but Contact Middle initial must be 1 character and alphabetic.",user.getUsername());
    			return null;
    		}
    		
    		if(!(emailAddress.length() < 64 && emailAddress.length() > 0) || !(emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){//Validation working
    			Logger.info("{} attempted to upload a contact but Contact email address must be between 0 and 64 characters that are alpahnumeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(streetAddress1.length() < 64 && streetAddress1.length() > 0) || !(streetAddress1.matches("\\d+\\s+([a-zA-Z.]+\\s?)+"))) { //validation working
    			Logger.info("{} attempted to upload a contact but Contact street address must be between 0 and 128 characters that are alphanumeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(streetAddress2.length() < 64 && streetAddress2.length() > 0) || !(streetAddress2.matches("^[A-Za-z0-9./-]+(?:[\\s-][A-Za-z0-9.-]+)*$"))) { //Validation working
    			Logger.info("{} attempted to upload a contact but Contact street address 2 must be between 0 and 64 characters that are alphanumeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(contactCity.length() < 64 && contactCity.length() > 0) || !(contactCity.matches("^[A-Za-z]+(?:[\\s-][A-Za-z]+)*$"))) { //Validation for a city made up of one or two words.
    			Logger.info("{} attempted to upload a contact but Contact City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
    			return null;
    		}
    		
    		if(!(states.contains(contactState) || stateAbbreviations.contains(contactState))) {  
    			Logger.info("{} attempted to upload a contact but Contact state must be a state or state abbreviation.",user.getUsername());
    			return null;
    		}
    		
    		if(!(contactZip.length() < 12 && contactZip.length() > 0) || !(contactZip.matches("^[0-9.]+$"))){ //validation working
    			Logger.info("{} attempted to upload a contact but Contact Zip must be between 0 and 12 characters and is numeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(primaryPhone.length() < 13 && primaryPhone.length() > 0) || !(primaryPhone.matches("\\d{3}-\\d{3}-\\d{4}"))){ //validation working
    			Logger.info("{} attempted to upload a contact but Contact primary phone must be between 0 and 12 characters and is numeric.",user.getUsername());
    			return null;
    		}
    		
    		if(!(workPhone.length() < 13 && workPhone.length() > 0) || !(workPhone.matches("\\d{3}-\\d{3}-\\d{4}"))){ //validation working
    			Logger.info("{} attempted to upload a contact but Contact work phone must be between 0 and 12 characters and is numeric.",user.getUsername());
    			return null;
    		}
    	 
    		Contacts contact = new Contacts();
    		

    		contact.setFirstName(firstName);
    		contact.setLastName(lastName);
    		contact.setMiddleInitial(middleInitial);
    		contact.setEmailAddress(emailAddress);
    		contact.setStreetAddress1(streetAddress1);
    		contact.setStreetAddress2(streetAddress2);
    		contact.setCity(contactCity);
    		contact.setState(contactState);
    		contact.setZip(contactZip);
    		contact.setPrimaryPhone(primaryPhone);
    		contact.setWorkPhone(workPhone);
	    	
    		contact.setCarrier(user.getCarrier());
    		
    		return contact;
	}
		
		
	public List<Technicians> validateTechniciansSheet(XSSFSheet worksheet){
		List <Technicians> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
	}
	public List<Vehicles> validateVehiclesSheet(XSSFSheet worksheet){
		List <Vehicles> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
	}
	public List<Driver> validateDriverSheet(XSSFSheet worksheet){
		List <Driver> result = new ArrayList<>();
		 {
			 try {	
			 User user = getLoggedInUser();
				
				for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
					 
					Driver driver = new Driver();
			        XSSFRow row = worksheet.getRow(i);
			        
			        if(row.getCell(0)== null || row.getCell(0).toString().equals("")) {
			        	break;
			        }
			        
			        String contact = row.getCell(0).toString().strip();
				    String carrier = row.getCell(1).toString().strip();
				    String vehicle = row.getCell(2).toString().strip();
		    		String licenseNumber = row.getCell(3).toString().strip();
		    		String licenseExpiration = row.getCell(4).toString().strip();
		    		String licenseClass = row.getCell(5).toString().strip();
		    			
		    		Hashtable<String, String> hashtable = new Hashtable<>();
		    		
		    		hashtable.put("contact", contact);
		    		hashtable.put("carrier", carrier);
		    		hashtable.put("vehicle", vehicle);
		    		hashtable.put("licenseNumber", licenseNumber);
		    		hashtable.put("licenseExpiration", licenseExpiration);
		    		hashtable.put("licenseClass", licenseClass);
		 
		    		driver = validateDriver(hashtable);
		    		if (contact == null) {
		    			return null;
		    		}
		    		
					driver.setCarrier(user.getCarrier());
			        result.add(driver);	
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		 }
			return result;
		}
			public Driver validateDriver(Hashtable<String, String> hashtable) {
				
				User user = getLoggedInUser();
				
				//String contact = hashtable.get("contact");
				//String carrier = (String)hashtable.get("carrier");
				//String vehicle = (String)hashtable.get("vehicle");
				String licenseNumber = (String)hashtable.get("licenseNumber");
				String licenseExpiration = (String)hashtable.get("licenseExpiration");
				String licenseClass = (String)hashtable.get("licenseClass");

	    		if(!(licenseNumber.length() < 32 && licenseNumber.length() > 0) || !(licenseNumber.matches("^[A-Za-z0-9]{6,12}$"))){
	    			Logger.info("{} attempted to upload a license number but the license number must be between 0 and 12 characters that are alpahnumeric.",user.getUsername());
	    			return null;
	    		}
	    		
	    		if(!(licenseExpiration.length() < 12 && licenseExpiration.length() > 0) || !(licenseExpiration.matches("^\\\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\\\d{4}$"))){ 
	    			Logger.info("{} attempted to upload a Date but the Date must be between 0 and 12 characters and formated MM/DD/YYYY.", user.getUsername());
	    			return null;
	    		}
	    		
	    		if(!(licenseClass.length() < 12 && licenseClass.length() > 0) || !(licenseClass.matches("^[A-Za-z]{1}$"))) { //Validation working
	    			Logger.info("{} attempted to upload a license class but the license class must be 1 character and alphabetic.",user.getUsername());
	    			return null;
	    		}
	    		
	    		Driver driver = new Driver();
	    	
	    		driver.getContact();
	    		driver.getCarrier();
	    		driver.getVehicle();
	    		driver.setLisence_number(licenseNumber);
	    		driver.setLisence_expiration(licenseExpiration);
	    		driver.setLisence_class(licenseClass);
	    
	    		driver.setCarrier(user.getCarrier());
	    		
	    		return driver;
		}
	
			

	public List<MaintenanceOrders> validateMaintenanceOrdersSheet(XSSFSheet worksheet){
		List <MaintenanceOrders> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
	}
	
	public VehicleTypes validateVehicleTypes(Hashtable<String, String> hashtable) {
		//Takes data from hashtable breaks it down into Strings
		//Validates the strings and returns the correct datatype
		//returns null if any field is not valid.
		//Use proper logging EXAMPLE ' Logger.error("{} attempted to upload a shipment but the Consignee State must be a state or state abbreviation.",user.getUsername()); ' "
		VehicleTypes vehicleType = new VehicleTypes();
		return vehicleType;
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