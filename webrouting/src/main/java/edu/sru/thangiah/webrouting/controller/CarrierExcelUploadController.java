package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

@Controller
public class CarrierExcelUploadController {

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
	
	private static final Logger Logger = LoggerFactory.getLogger(BidsController.class);
	
	
	public CarrierExcelUploadController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
		this.vehiclesRepository = vehiclesRepository;
		this.bidsRepository = bidsRepository;
	}
	
	
	@PostMapping("/upload-carrier-excel")
	public String UploadCarrierInformationByExcel(@RequestParam("file") MultipartFile excelData, HttpSession session){
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		XSSFWorkbook workbook;
		
		List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
		List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
		Boolean fieldIsNull = false;
		User user = getLoggedInUser();
			
		try {
			
			workbook = new XSSFWorkbook(excelData.getInputStream());
			
			
			
			XSSFSheet vehicleTypesWorksheet = workbook.getSheetAt(0);
			XSSFSheet locationsWorksheet = workbook.getSheetAt(1);
			XSSFSheet contactsWorksheet = workbook.getSheetAt(2);
			XSSFSheet vehiclesWorksheet = workbook.getSheetAt(3);
			XSSFSheet techniciansWorksheet = workbook.getSheetAt(4);
			XSSFSheet driversWorksheet = workbook.getSheetAt(5);
			XSSFSheet maintenanceOrdersWorksheet = workbook.getSheetAt(6);
			
			for(int i=1; i<vehicleTypesWorksheet.getPhysicalNumberOfRows(); i++) {
				
		        XSSFRow row = vehicleTypesWorksheet.getRow(i);
		        
		        for(int j=0; j<=13; j++ ) { //Change for each number of columns CHECKING FOR NULL ENTRYS
                    if (row.getCell(j) == null) {
                        Logger.error("{} attempted to upload a contact but a required field was left null",user.getUsername());
                        fieldIsNull = true;
                        break;
                    }
		        }
		        
		        if (fieldIsNull = true) {
                    break;
                }
		        
		        VehicleTypes vehicleTypes = new VehicleTypes();
		        
		        
		       
		       	//ASSIGN HARDCODED VARIABLES

	    		
	    		
	    	 	//VARIABLE ASSIGNMENT
			  
			
	    		

	    		
	    		 // cut off decimals with 			substring(0, commodityClass.length() - 2)
	    
	    		
	    		

	    		//if (!(clientName.length() < 64 && clientName.length() > 0) || !(clientName.matches("^[a-zA-Z0-9.]+$"))) {
	    			//workbook.close();
	    			//Logger.error("{} attempted to upload a shipment but the Client field must be between 0 and 64 characters and alphanumeric.",user.getUsername());
	    			//continue;
	    		//}
	    		
	    	
	    		
	    		
	    		//shipment.setVehicle(null);			//ASSIGN HARDCODED VALUES
	    		

	    	
	    		//ASSIGN VARIABLES

		        
		       //SAVE TO REPO
		      //LOG IT	
			 		
			 }
			 
			 workbook.close();
		 
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

