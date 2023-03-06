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
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	private static final Logger Logger = LoggerFactory.getLogger(ExcelController.class);
	
	
	public ExcelController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository,ValidationServiceImp validationServiceImp) {
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
		this.vehiclesRepository = vehiclesRepository;
		this.bidsRepository = bidsRepository;
		this.validationServiceImp = validationServiceImp;
	}
	
	
	/**
  	 * Reads an excel file containing shipments and adds it to the shipments repository. <br>
  	 * After the file is uploaded and added to the database, user is redirected to the created shipments page
  	 * @param excelData Excel file that is being added to the database
  	 * @return "redirect: ${redirectLocation}"
  	 */
	@PostMapping("/upload-shipment")
	public String LoadFromExcelData(@RequestParam("file") MultipartFile excelData, HttpSession session, Model model){
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

