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
		        
		        
		        Boolean NULLFIELD = false;
		        for(int j = 0; j <= 15; j++) {
		        	
		       
		        if (row.getCell(j) == null || row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK) {
		        	NULLFIELD = true;
		        	break;
		        }}
		        if (NULLFIELD == true) {
		        	Logger.error("{} attempted to upload a shipment but left a required cell empty", user.getUsername());
		        	break;
		        }
		        //Still saves shipments that worked before the broken one
		        
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
	    			System.out.println("THIS FIRED");
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
	public List<Locations> validateLocationsSheet(XSSFSheet worksheet){
		List <Locations> result = new ArrayList<>();
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
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
		/* Breaks the worksheet up into strings. Adds the strings to a hashtable 
		sends the hashtable to validate<>
		If the <> is valid then adds it to a list which it returns. If at any point validate<> returns null then this method should return null and not the list
		*/
		return result;
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
