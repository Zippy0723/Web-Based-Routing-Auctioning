package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with shipments.
 * @author Ian Black		imb1007@sru.edu
 * @author Fady Aziz		faa1002@sru.edu
 * @since 2/8/2022
 */

@Controller
public class ShipmentsController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;


	private CarriersRepository carriersRepository;
	
	private ShipmentsRepository shipmentsRepository;
	
	private VehiclesRepository vehiclesRepository;
	
	private BidsRepository bidsRepository;
	
	@Autowired
	private UserValidator userValidator;

	/**
	 * Constructor for ShipmentsController. <br>
	 * Instantiates the shipmentsRepository <br>
	 * Instantiates the carriersRepository <br>
	 * Instantiates the vehiclesRepository <br>
	 * Instantiates the bidsRepository
	 * @param shipmentsRepository Used to interact with the shipments in the database
	 * @param carriersRepository Used to interact with the carriers in the database
	 * @param vehiclesRepository Used to interact with the vehicles in the database
	 * @param bidsRepository Used to interact with the bids in the database
	 */
	public ShipmentsController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
		this.vehiclesRepository = vehiclesRepository;
		this.bidsRepository = bidsRepository;
	}
	
	/**
	 * Redirects user to the shipper home page for shippers
	 * @param model Used to add data to the model
	 * @return "shipmentshomeshipper"
	 */
	@GetMapping("/shipmentshomeshipper")
	public String shipmentsHomeShipper(Model model) {
		return "shipmentshomeshipper";
	}
	
	/**
	 * Redirects user to the shipper home page for carriers
	 * @param model Used to add data to the model
	 * @return "shipmentshomecarrier"
	 */
	@GetMapping("/shipmentshomecarrier")
	public String shipmentsHomeCarrier(Model model) {
		return "shipmentshomecarrier";
	}
	
	/**
	 * Redirects user to the shipper home page for master lists
	 * @param model Used to add data to the model
	 * @return "shipmentshomemaster"
	 */
	@GetMapping("/shipmentshomemaster")
	public String shipmentsHomeMaster(Model model) {
		return "shipmentshomemaster";
	}

	/**
	 * Adds all of the shipments to the "shipments" model and redirects user to
	 * the shipments page.
	 * @param model Used to add data to the model
	 * @return "shipments"
	 */
	@RequestMapping({"/shipments"})
	public String showShipmentList(Model model) {
		
		User user = getLoggedInUser();
		if (user.getRole().toString().equals("SHIPPER")) {
			
			 model.addAttribute("shipments", user.getShipments());
		     
		} else {
			model.addAttribute("shipments", shipmentsRepository.findAll());
		}
       
        return "shipments";
    }
	
	/**
	 * Adds the created shipments to the model depending on what role the user has 
	 * and redirects user to /createdshipments
	 * @param model Used to add data to the model
	 * @return "createdshipments"
	 */
	@RequestMapping({"/createdshipments"})
	public String showCreatedShipmentsList(Model model) {
		
		List<Shipments> shipmentsWOCarrier = new ArrayList<>();
		User user = getLoggedInUser();
		if (user.getRole().toString().equals("SHIPPER")) {
			List<Shipments> shipments = user.getShipments();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getCarrier() == null) {
						shipmentsWOCarrier.add(shipments.get(i));
					}
				}
			}
			if (shipmentsWOCarrier.size() != 0 && shipmentsWOCarrier != null) {
				model.addAttribute("shipments", shipmentsWOCarrier);   
			}
		     
		}
		else if (user.getRole().toString().equals("CARRIER") || user.getRole().toString().equals("MASTERLIST")) {
			List<Shipments> shipments = (List<Shipments>) shipmentsRepository.findAll();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getCarrier() == null) {
						shipmentsWOCarrier.add(shipments.get(i));
					}
				}
			}
			if (shipmentsWOCarrier.size() != 0 && shipmentsWOCarrier != null) {
				model.addAttribute("shipments", shipmentsWOCarrier);     
			}
		}
       
        return "createdshipments";
    }
	
	/**
	 * Adds the accepted shipments to the model depending on what role the user has 
	 * and redirects user to /acceptedshipments
	 * @param model Used to add data to the model
	 * @return "acceptedshipments"
	 */
	@RequestMapping({"/acceptedshipments"})
	public String showAcceptedShipmentsList(Model model) {
		List<Shipments> shipmentsWCarrier = new ArrayList<>();
		User user = getLoggedInUser();
		if (user.getRole().toString().equals("SHIPPER")) {
			List<Shipments> shipments = user.getShipments();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getCarrier() != null) {
						shipmentsWCarrier.add(shipments.get(i));
						
					}
				}
			}
			if (shipmentsWCarrier.size() != 0 && shipmentsWCarrier != null) {
				
				model.addAttribute("shipments", shipmentsWCarrier);   
			}
		}
		else if (user.getRole().toString().equals("CARRIER")) {

			if (user.getCarrier() == null) {
				return "acceptedshipments";
			}
			List<Shipments> shipments = user.getCarrier().getShipments();
			
			
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getCarrier() == null) {
						shipments.remove(i);
					}
				}
			}
			if (shipments.size() != 0 && shipments != null) {
				model.addAttribute("shipments", shipments);
				
			}
		} else if (user.getRole().toString().equals("MASTERLIST")) {
			List<Shipments> shipments = (List<Shipments>) shipmentsRepository.findAll();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getCarrier() != null) {
						shipmentsWCarrier.add(shipments.get(i));
						
					}
				}
			}
			if (shipmentsWCarrier.size() != 0 && shipmentsWCarrier != null) {
				
				model.addAttribute("shipments", shipmentsWCarrier);   
			}
		}
		
        return "acceptedshipments";
    }

	/**
	 * Redirects user to the /add/add-shipments page <br>
	 * Adds carriers and vehicles to the model.
	 * @param model Used to add data to the model
	 * @param shipment Information on the shipment being added
	 * @param result Ensures that the values entered by the user are valid
	 * @return "/add/add-shipments"
	 */
	@GetMapping({"/add-shipments"})
	public String showList(Model model, Shipments shipment, BindingResult result) { 
		model.addAttribute("carriers", carriersRepository.findAll());
		model.addAttribute("vehicles", vehiclesRepository.findAll());
        return "/add/add-shipments";
    }

	/**
  	 * Adds a shipment to the database. Checks if there are errors in the form. <br>
  	 * Sets carrier, vehicle to null, paid amount and scac are empty strings and full freight terms is set to AVAILABLE SHIPMENT. <br>
  	 * Currently logged in user is also associated with that shipment. <br>
  	 * If there are no errors, the shipment is saved in the shipmentsRepository. and the user is redirect to /createdshipments <br>
  	 * If there are errors, the user is redirected to the /add/add-shipments page.
  	 * @param shipment Stores the information on the shipment being added
  	 * @param result Ensures that the values entered by the user are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments" or "/add/add-shipments"
  	 */
	@RequestMapping({"/addshipments"})
  	public String addShipment(@Validated Shipments shipment, BindingResult result, Model model) {
		userValidator.addition(shipment, result);
  		if (result.hasErrors()) {
  			return "/add/add-shipments";
		}
  		
  		boolean deny = false;
  		List<Shipments> shipmentsList = (List<Shipments>) shipmentsRepository.findAll();
  		
  		for (Shipments s : shipmentsList) {
  			if (s.getCommodityClass().equals(shipment.getCommodityClass()) 
  					&& s.getCommodityPaidWeight().equals(shipment.getCommodityPaidWeight())
  					&& s.getCommodityPieces().equals(shipment.getCommodityPieces())
  					&& s.getClient().equals(shipment.getClient())
  					&& s.getConsigneeLatitude().equals(shipment.getConsigneeLatitude())
  					&& s.getConsigneeLongitude().equals(shipment.getConsigneeLongitude())
  					&& s.getShipDate().equals(shipment.getShipDate())) {
  				deny = true;
  			}
  		}
  		
  		if (deny == true) {
  			model.addAttribute("error", "Error adding a shipment: Shipment already exists!");
  			List<Shipments> shipmentsWOCarrier = new ArrayList<>();
  			User user = getLoggedInUser();
  			if (user.getRole().toString().equals("SHIPPER")) {
  				List<Shipments> shipments = user.getShipments();
  				if (shipments.size() != 0 && shipments != null) {
  					for (int i = 0; i < shipments.size(); i++) {
  						if (shipments.get(i).getCarrier() == null) {
  							shipmentsWOCarrier.add(shipments.get(i));
  						}
  					}
  				}
  				if (shipmentsWOCarrier.size() != 0 && shipmentsWOCarrier != null) {
  					model.addAttribute("shipments", shipmentsWOCarrier);   
  				}
  			     
  			}
  			return "createdshipments";
  		}

  		shipment.setCarrier(null);
  		shipment.setVehicle(null);
  		shipment.setPaidAmount("");
  		shipment.setScac("");
  		shipment.setFreightbillNumber("");
  		shipment.setFullFreightTerms("AVAILABLE SHIPMENT");
  		shipment.setUser(getLoggedInUser());
  		shipmentsRepository.save(shipment);
  		return "redirect:/createdshipments";
  	}

	/**
  	 * Finds a shipment using the id parameter and if found, redirects to confirmation page
  	 * @param id ID of the shipment being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/shipments"
  	 */
	@GetMapping("/deleteshipment/{id}")
    	public String deleteShipment(@PathVariable("id") long id, Model model) {
        Shipments shipment = shipmentsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
        
        model.addAttribute("shipments", shipment);
        return "/delete/deleteshipmentconfirm";
    }
	
	/**
  	 * Finds a shipment using the id parameter and if found, deletes the shipment and redirects to shipments page
  	 * @param id ID of the shipment being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments"
  	 */
  	@GetMapping("/deleteshipmentconfirmation/{id}")
    public String deleteShipmentConfirmation(@PathVariable("id") long id, Model model) {
  		Shipments shipment = shipmentsRepository.findById(id)
  		        .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
  		        
        if(!shipment.getBids().isEmpty()) {
        	List<Bids> bids = (List<Bids>) shipment.getBids();
        	for (Bids bid : bids) 
        	{ 
        		bidsRepository.delete(bid); 
        	}
        	
        	
        }
        shipmentsRepository.delete(shipment);
        return "redirect:/createdshipments";
    }
	
	/**
  	 * Finds a carrier using the id parameter and if found, adds all of the shipments of that carrier
  	 * to the shipments page
  	 * @param id ID of the shipment being viewed
  	 * @param model Used to add data to the model
  	 * @return "shipments"
  	 */
  	@GetMapping("/viewshipment/{id}")
    public String viewCarrierShipments(@PathVariable("id") long id, Model model) {
        Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
        
        model.addAttribute("shipments", shipment);
        return "viewfullshipment";
    }
  	
  	/**
  	 * Finds a shipment using the id parameter and if found, adds all of the bids of that shipment
  	 * to the bids page
  	 * @param id ID of the shipment being used to get the bids
  	 * @param model Used to add data to the model
  	 * @return "shipments"
  	 */
  	@GetMapping("/viewshipmentbids/{id}")
    public String viewShipmentBids(@PathVariable("id") long id, Model model) {
        Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
        
        model.addAttribute("bids", shipment.getBids());
        
        if (shipment.getCarrier() != null) {
        	return "viewbidscomplete";
        }
        return "bids";
    }
	
	/**
  	 * Finds a shipment using the id parameter and if found, adds the details of that shipment
  	 * to a form and redirects the user to that update form.
  	 * @param id ID of the shipment being edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-location"
  	 */
	@GetMapping("/editshipment/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
		Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
        
		model.addAttribute("vehicles", vehiclesRepository.findAll());
	    model.addAttribute("shipments", shipment);
	    
	    User user = getLoggedInUser();
	    
	    if (user.getRole().toString().equals("SHIPPER")) {
	    	return "/update/update-shipments-shipper";
	    }
	    else {
	    	return "/update/update-shipments";
	    }
        
    }
	
	/**
  	 * Updates a shipment to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the shipment is updated in the shipmentsRepository. and the user is redirect to /shipments <br>
  	 * If there are errors, the user is redirected to the /update/update-shipments page.
  	 * @param id ID of the shipment being updated
  	 * @param shipment Information on the shipment being updated
  	 * @param result Ensures that the values entered by the user are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/shipments" or "/update/update-shipments"
  	 */
	@PostMapping("/updateshipment/{id}")
    public String updateShipment(@PathVariable("id") long id, @Validated Shipments shipment, 
      BindingResult result, Model model) {
		userValidator.addition(shipment, result);
        if (result.hasErrors()) {
        	if (getLoggedInUser().getRole().toString().equals("SHIPPER")) {
        		shipment.setId(id);
                return "/update/update-shipments-shipper";
        	}
        	else {
        		shipment.setId(id);
                return "/update/update-shipments";
        	}
        }
        
        if (getLoggedInUser().getRole().toString().equals("SHIPPER")) {
        	
        	boolean deny = false;
      		List<Shipments> shipmentsList = (List<Shipments>) shipmentsRepository.findAll();
      		
      		for (Shipments s : shipmentsList) {
      			if (s.getCommodityClass().equals(shipment.getCommodityClass()) 
      					&& s.getCommodityPaidWeight().equals(shipment.getCommodityPaidWeight())
      					&& s.getCommodityPieces().equals(shipment.getCommodityPieces())
      					&& s.getClient().equals(shipment.getClient())
      					&& s.getConsigneeLatitude().equals(shipment.getConsigneeLatitude())
      					&& s.getConsigneeLongitude().equals(shipment.getConsigneeLongitude())
      					&& s.getShipDate().equals(shipment.getShipDate())) {
      				if (s.getId() != shipment.getId()) {
      					deny = true;
      	  				break;
      				}
      			}
      		}
      		
      		if (deny == true) {
      			model.addAttribute("error", "Error adding a shipment: Shipment already exists!");
      			List<Shipments> shipmentsWOCarrier = new ArrayList<>();
      			User user = getLoggedInUser();
      			if (user.getRole().toString().equals("SHIPPER")) {
      				List<Shipments> shipments = user.getShipments();
      				if (shipments.size() != 0 && shipments != null) {
      					for (int i = 0; i < shipments.size(); i++) {
      						if (shipments.get(i).getCarrier() == null) {
      							shipmentsWOCarrier.add(shipments.get(i));
      						}
      					}
      				}
      				if (shipmentsWOCarrier.size() != 0 && shipmentsWOCarrier != null) {
      					model.addAttribute("shipments", shipmentsWOCarrier);   
      				}
      			     
      			}
      			return "createdshipments";
      		}
        	
        	shipment.setUser(getLoggedInUser());
        	shipment.setCarrier(null);
      		shipment.setVehicle(null);
      		shipment.setPaidAmount("");
      		shipment.setScac("");
      		shipment.setFreightbillNumber("");
      		shipment.setFullFreightTerms("AVAILABLE SHIPMENT");
      		shipment.setUser(getLoggedInUser());
      		shipmentsRepository.save(shipment);
      		
      		return "redirect:/createdshipments";
        }
        else if (getLoggedInUser().getRole().toString().equals("CARRIER")) {
        	Shipments s = shipmentsRepository.findById(id)
        	          .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
        	shipment.setUser(s.getUser());
        	shipment.setCarrier(s.getCarrier());
        	
        	boolean deny = false;
      		List<Shipments> shipmentsList = (List<Shipments>) shipmentsRepository.findAll();
      		
      		for (Shipments s1 : shipmentsList) {
      			if (s1.getCommodityClass().equals(shipment.getCommodityClass()) 
      					&& s1.getCommodityPaidWeight().equals(shipment.getCommodityPaidWeight())
      					&& s1.getCommodityPieces().equals(shipment.getCommodityPieces())
      					&& s1.getClient().equals(shipment.getClient())
      					&& s1.getConsigneeLatitude().equals(shipment.getConsigneeLatitude())
      					&& s1.getConsigneeLongitude().equals(shipment.getConsigneeLongitude())
      					&& s1.getShipDate().equals(shipment.getShipDate())) {
      				if (s1.getId() != shipment.getId()) {
      					deny = true;
      	  				break;
      				}
      			}
      		}
      		
      		if (deny == true) {
      			model.addAttribute("error", "Error adding a shipment: Shipment already exists!");
    			
      			List<Shipments> shipments = getLoggedInUser().getCarrier().getShipments();
    			
    			
    			if (shipments.size() != 0 && shipments != null) {
    				for (int i = 0; i < shipments.size(); i++) {
    					if (shipments.get(i).getCarrier() == null) {
    						shipments.remove(i);
    					}
    				}
    			}
    			if (shipments.size() != 0 && shipments != null) {
    				model.addAttribute("shipments", shipments);
    				
    			}
      			return "acceptedshipments";
      		}
        	
        	shipmentsRepository.save(shipment);
        	return "redirect:/acceptedshipments";
        }
        else {
        	shipmentsRepository.save(shipment);
        	return "redirect:/shipments";
        }
    }
	
	/**
  	 * Uploads an excel file containing shipments
  	 * @param model Used to add data to the model 
  	 * @return "/uploadshipments" 
  	 */
	@GetMapping("/uploadshipments")
	public String ListFromExcelData(Model model){
		return "/uploadshipments";	
	}
	
	/**
  	 * Reads an excel file containing shipments and adds it to the shipments repository. <br>
  	 * After the file is uploaded and added to the database, user is redirected to the created shipments page
  	 * @param excelData Excel file that is being added to the database
  	 * @return "redirect:/createdshipments"
  	 */
	@SuppressWarnings("unused")
	@PostMapping("/upload-shipment")
	public String LoadFromExcelData(@RequestParam("file") MultipartFile excelData){
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(excelData.getInputStream());
	
		
			XSSFSheet worksheet = workbook.getSheetAt(0);
			List<Carriers> carriersList;
			carriersList = (List<Carriers>) carriersRepository.findAll();
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				 
				Shipments shipment = new Shipments();
		        XSSFRow row = worksheet.getRow(i);
		        
		        if(row.getCell(0).getStringCellValue().isEmpty() || row.getCell(0)== null ) {
		        	break;
		        }
	
	    		shipment.setClient(row.getCell(0).toString());
	    		shipment.setCarrier(null);
	    		shipment.setVehicle(null);
	    		shipment.setClientMode(row.getCell(3).toString());
	    		shipment.setScac("");
	    		
	    		Date date = row.getCell(4).getDateCellValue();
	    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    		String shipDate = dateFormat.format(date);
		        shipment.setShipDate(shipDate);
		        
		        shipment.setFreightbillNumber("");
		        shipment.setPaidAmount("");
		        shipment.setFullFreightTerms("AVAILABLE SHIPMENT");
		        shipment.setCommodityClass(row.getCell(8).getRawValue());
		        shipment.setCommodityPieces(row.getCell(9).getRawValue());
		        shipment.setCommodityPaidWeight(row.getCell(10).getRawValue());
		        shipment.setShipperCity(row.getCell(11).toString());
		        shipment.setShipperState(row.getCell(12).toString());
		        shipment.setShipperZip(row.getCell(13).getRawValue());
		        shipment.setShipperLatitude(row.getCell(14).getRawValue());
		        shipment.setShipperLongitude(row.getCell(15).getRawValue());
		        shipment.setConsigneeCity(row.getCell(16).toString());
		        shipment.setConsigneeState(row.getCell(17).toString());
		        shipment.setConsigneeZip(row.getCell(18).getRawValue());
		        shipment.setConsigneeLatitude(row.getCell(19).getRawValue());
		        shipment.setConsigneeLongitude(row.getCell(20).getRawValue());
		        
		        shipment.setUser(getLoggedInUser());
		        shipmentsRepository.save(shipment);
			 		
			 }
			 
			 workbook.close();
		 
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/createdshipments";
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

