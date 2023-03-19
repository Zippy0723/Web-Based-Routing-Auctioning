package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.NotificationService;
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
    
    @Autowired
    private NotificationService notificationService;


	private CarriersRepository carriersRepository;
	
	private ShipmentsRepository shipmentsRepository;
	
	private VehiclesRepository vehiclesRepository;
	
	private BidsRepository bidsRepository;
	
	@Autowired
	private UserValidator userValidator;
	
	private static final Logger Logger = LoggerFactory.getLogger(ShipmentsController.class);

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
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "shipmentshomeshipper";
	}
	
	/**
	 * Redirects user to the shipper home page for carriers
	 * @param model Used to add data to the model
	 * @return "shipmentshomecarrier"
	 */
	@GetMapping("/shipmentshomecarrier")
	public String shipmentsHomeCarrier(Model model) {
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "shipmentshomecarrier";
	}
	
	/**
	 * Redirects user to the shipper home page for master lists
	 * @param model Used to add data to the model
	 * @return "shipmentshomemaster"
	 */
	@GetMapping("/shipmentshomemaster")
	public String shipmentsHomeMaster(Model model) {
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
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
		
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
        return "shipments";
    }
	
	/**
	 * Adds the created shipments to the model depending on what role the user has 
	 * and redirects user to /createdshipments. 
	 * @param model Used to add data to the model
	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
	 * @return "createdshipments"
	 */
	@RequestMapping({"/createdshipments"})
	public String showCreatedShipmentsList(Model model, HttpSession session) {
		
		List<Shipments> shipmentsWOCarrier = new ArrayList<>();
		User user = getLoggedInUser();
		model.addAttribute("user",user);
		session.setAttribute("redirectLocation", "/createdshipments");
		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("message");
		
		if (user.getRole().toString().equals("SHIPPER")) {
			List<Shipments> shipments = user.getShipments();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getFullFreightTerms().equals("AVAILABLE SHIPMENT")) {
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
					if (shipments.get(i).getFullFreightTerms().equals("AVAILABLE SHIPMENT")) {
						shipmentsWOCarrier.add(shipments.get(i));
					}
				}
			}
			if (shipmentsWOCarrier.size() != 0 && shipmentsWOCarrier != null) {
				model.addAttribute("shipments", shipmentsWOCarrier);     
			}
		}
		
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
        return "createdshipments";
    }
	
	/**
	 * Adds the accepted shipments to the model depending on what role the user has 
	 * and redirects user to /acceptedshipments
	 *
	 * @param model Used to add data to the model
	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
	 * @return "acceptedshipments"
	 */
	@RequestMapping({"/acceptedshipments"})
	public String showAcceptedShipmentsList(Model model, HttpSession session) {
		List<Shipments> shipmentsWCarrier = new ArrayList<>();
		User user = getLoggedInUser();
		session.setAttribute("redirectLocation", "/acceptedshipments");
		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("message");
		
		if (user.getRole().toString().equals("SHIPPER")) {
			List<Shipments> shipments = user.getShipments();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getFullFreightTerms().equals("BID ACCEPTED")) {
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
			List<Shipments> acceptedShipments = new ArrayList<Shipments>();
			
			if (shipments.size() != 0 && shipments != null) {
				for (Shipments s : shipments) {
					if (s.getCarrier() != null && s.getFullFreightTerms().toString().equals("BID ACCEPTED")) {
						acceptedShipments.add(s);
					}
				}
			}
			if (acceptedShipments.size() != 0 && acceptedShipments != null) {
				model.addAttribute("shipments", acceptedShipments);
				
			}
		} else if (user.getRole().toString().equals("MASTERLIST")) {
			List<Shipments> shipments = (List<Shipments>) shipmentsRepository.findAll();
			if (shipments.size() != 0 && shipments != null) {
				for (int i = 0; i < shipments.size(); i++) {
					if (shipments.get(i).getFullFreightTerms().equals("BID ACCEPTED")) {
						shipmentsWCarrier.add(shipments.get(i));
						
					}
				}
			}
			if (shipmentsWCarrier.size() != 0 && shipmentsWCarrier != null) {
				
				model.addAttribute("shipments", shipmentsWCarrier);   
			}
		}
		
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
        return "acceptedshipments";
    }
	
	/**
	 * Adds Frozen Shipments to the Shipment model, 
	 * or, if the user attempts to access the frozen shipments page and is not MASTERSEVER or SHIPPER, redirects them to index.
	 * @param model Used to add data to the model
	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
	 * @return "frozenshipments" or "/index" if user is not MASTERLIST or SHIPPER
	 
	 */
	@RequestMapping({"/frozenshipments"})
	public String showFrozenShipmentsList(Model model, HttpSession session) {
		List<Shipments> shipmentsFrozen = new ArrayList<>();
		User user = getLoggedInUser();
		session.setAttribute("redirectLocation", "/frozenshipments");
		List<Shipments> shipments;
		
		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("message");
		
		if (user.getRole().toString().equals("SHIPPER")) {  
			shipments = user.getShipments();
		}
		else if (user.getRole().toString().equals("MASTERLIST")) {
			shipments = (List<Shipments>) shipmentsRepository.findAll();
		}
		else { //Carriers shouldnt be able to see this page, this else will throw them back to index if they try
			session.setAttribute("redirectLocation", "/index");
			return "/index"; 
		}
		
		if (shipments.size() != 0 && shipments != null) {
			for (int i = 0; i < shipments.size(); i++) {
				if (shipments.get(i).getFullFreightTerms().equals("FROZEN")) {
					shipmentsFrozen.add(shipments.get(i));
						
				}
			}
		}
		
		if (shipmentsFrozen.size() != 0 && shipmentsFrozen != null) {
			model.addAttribute("shipments", shipmentsFrozen);   
		}
			
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "frozenshipments";
	}
	
	/**
	 * Adds Pending Shipments to the Shipment model, then returns to the pending shipments page
	 * @param model Used to add data to the model
	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
	 * @return "pendingshipments" or "/index" if user is not MASTERLIST or SHIPPER
	 */
	@RequestMapping({"/pendingshipments"})
	public String showPendingShipmentsList(Model model, HttpSession session) {
		List<Shipments> shipmentsPending = new ArrayList<>();
		User user = getLoggedInUser();
		model.addAttribute("user",user);
        model = NotificationController.loadNotificationsIntoModel(user, model);
		session.setAttribute("redirectLocation", "/pendingshipments");
		List<Shipments> shipments;
		
		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("message");
		
		if (user.getRole().toString().equals("SHIPPER")) {  
			shipments = user.getShipments();
		}
		else if (user.getRole().toString().equals("MASTERLIST")) {
			shipments = (List<Shipments>) shipmentsRepository.findAll();
		}
		else { //Carriers shouldnt be able to see this page
			session.setAttribute("redirectLocation", "/index");
			return "/index"; 
		}
		
		if (shipments.size() != 0 && shipments != null) {
			for (int i = 0; i < shipments.size(); i++) {
				if (shipments.get(i).getFullFreightTerms().equals("PENDING")) {
					shipmentsPending.add(shipments.get(i));
						
				}
			}
		}
		
		if (shipmentsPending.size() != 0 && shipmentsPending != null) {
			model.addAttribute("shipments", shipmentsPending);   
		}
		
		
		
		return "pendingshipments";
	}
	
	@GetMapping("/allshipments")
	public String allShipments(Model model, HttpSession session) {
		session.setAttribute("redirectLocation", "/allshipments");
		List<Shipments> shipmentsPending = new ArrayList<>();
		List<Shipments> shipmentsAccepted = new ArrayList<>();
		List<Shipments> shipmentsFrozen = new ArrayList<>();
		List<Shipments> shipmentsAvailable = new ArrayList<>();
		List<Shipments> shipmentsAwaiting = new ArrayList<>();
		List<Shipments> allShipments = new ArrayList<>();
		List<Shipments> ownShipments = new ArrayList<>();
		User user = getLoggedInUser();
		model.addAttribute("user",user);
		String status = "";	
		model = NotificationController.loadNotificationsIntoModel(user, model);
		
		if (user.getRole().toString().equals("SHIPPER")) {  
			ownShipments = user.getShipments();
				for (int i = 0; i < ownShipments.size(); i++) {
					if (ownShipments.get(i).getFullFreightTerms().equals("FROZEN")) {
						shipmentsFrozen.add(ownShipments.get(i));
							
					}
					if (ownShipments.get(i).getFullFreightTerms().equals("PENDING")) {
						shipmentsPending.add(ownShipments.get(i));
							
					}
					if (ownShipments.get(i).getFullFreightTerms().equals("BID ACCEPTED")) {
						shipmentsAccepted.add(ownShipments.get(i));
						
					}
					if (ownShipments.get(i).getFullFreightTerms().equals("AVAILABLE SHIPMENT")) {
						shipmentsAvailable.add(ownShipments.get(i));
						
					}
					if (ownShipments.get(i).getFullFreightTerms().equals("AWAITING ACCEPTANCE")) {
						shipmentsAwaiting.add(ownShipments.get(i));
						
					}
				}
				
			}
		else if (user.getRole().toString().equals("MASTERLIST")) {
			allShipments = (List<Shipments>) shipmentsRepository.findAll();
			for (int i = 0; i < allShipments.size(); i++) {
				if (allShipments.get(i).getFullFreightTerms().equals("FROZEN")) {
					shipmentsFrozen.add(allShipments.get(i));
						
				}
				if (allShipments.get(i).getFullFreightTerms().equals("PENDING")) {
					shipmentsPending.add(allShipments.get(i));
						
				}
				if (allShipments.get(i).getFullFreightTerms().equals("BID ACCEPTED")) {
					shipmentsAccepted.add(allShipments.get(i));
					
				}
				if (allShipments.get(i).getFullFreightTerms().equals("AVAILABLE SHIPMENT")) {
					shipmentsAvailable.add(allShipments.get(i));
					
				}
				if (allShipments.get(i).getFullFreightTerms().equals("AWAITING ACCEPTANCE")) {
					shipmentsAwaiting.add(allShipments.get(i));
					
				}
			}
		}
		else if (user.getRole().toString().equals("CARRIER")) {
			status = "CARRIER";
			ownShipments = user.getCarrier().getShipments();
			allShipments = (List<Shipments>) shipmentsRepository.findAll();
			
			for (int i = 0; i < allShipments.size(); i++) {
				if (allShipments.get(i).getFullFreightTerms().equals("AVAILABLE SHIPMENT")) {
					shipmentsAvailable.add(allShipments.get(i));
				}
			}
			
			for (int i = 0; i < ownShipments.size(); i++) {
				if (ownShipments.get(i).getFullFreightTerms().equals("BID ACCEPTED")) {
					shipmentsAccepted.add(ownShipments.get(i));
				}
				if (ownShipments.get(i).getFullFreightTerms().equals("AWAITING ACCEPTANCE")) {
					shipmentsAwaiting.add(ownShipments.get(i));
				}
			}
			
		}
			model.addAttribute("shipmentsAvailable", shipmentsAvailable);
			model.addAttribute("shipmentsFrozen", shipmentsFrozen);   
			model.addAttribute("shipmentsPending", shipmentsPending);
			model.addAttribute("shipmentsAccepted", shipmentsAccepted);
			model.addAttribute("shipmentsAwaiting", shipmentsAwaiting);
			model.addAttribute("status", status);
			return "/allshipments";
			
	}
		
	@RequestMapping({"/awaitingshipments"})
	public String showAwaitingShipmentsList(Model model, HttpSession session) {
		List<Shipments> shipmentsAwaitingAcceptance = new ArrayList<>();
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		session.setAttribute("redirectLocation", "/awaitingshipments");
		List<Shipments> shipments;
		
		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("message");
		
		if(user.getRole().toString().equals("CARRIER")) {
			shipments = user.getCarrier().getShipments();
		}
		else if (user.getRole().toString().equals("SHIPPER")) {
			shipments = user.getShipments();
		} 
		else if (user.getRole().toString().equals("MASTERLIST")){
			shipments = (List<Shipments>) shipmentsRepository.findAll();
		}
		else {
			session.setAttribute("redirectLocation", "/index");
			return "/index"; 	
		}
		
		if (shipments.size() != 0 && shipments != null) {
			for (int i = 0; i < shipments.size(); i++) {
				if (shipments.get(i).getFullFreightTerms().equals("AWAITING ACCEPTANCE")) {
					shipmentsAwaitingAcceptance.add(shipments.get(i));
						
				}
			}
		}
		
		if (shipmentsAwaitingAcceptance.size() != 0 && shipmentsAwaitingAcceptance != null) {
			model.addAttribute("shipments", shipmentsAwaitingAcceptance);   
		}
		
		return "awaitingshipments";
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
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
        return "/add/add-shipments";
    }

	/**
  	 * Adds a shipment to the database. Checks if there are errors in the form. <br>
  	 * Sets carrier, vehicle to null, paid amount and scac are empty strings and full freight terms is set to AVAILABLE SHIPMENT. <br>
  	 * Currently logged in user is also associated with that shipment. <br>
  	 * If there are no errors, the shipment is saved in the shipmentsRepository. and the user is redirect to /pendingshipments <br>
  	 * If there are errors, the user is redirected to the /add/add-shipments page.
  	 * @param shipment Stores the information on the shipment being added
  	 * @param result Ensures that the values entered by the user are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/pendingshipments" or "/add/add-shipments"
  	 */
	@RequestMapping({"/addshipments"})
  	public String addShipment(@Validated Shipments shipment, BindingResult result, Model model) {
		userValidator.addition(shipment, result);
  		if (result.hasErrors()) {
  			
  			User users = getLoggedInUser();
            List<Notification> notifications = new ArrayList<>();
            
            if(!(users == null)) {
                notifications = NotificationController.fetchUnreadNotifications(users);
            }
            
            model.addAttribute("notifications",notifications);
  			
  			return "/add/add-shipments";
		}
  		
  		User user = getLoggedInUser();
  		
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
  			Logger.error("{} attempted to add a shipment that already exists.", user.getUsername());
  			List<Shipments> shipmentsWOCarrier = new ArrayList<>();
  			if (user.getRole().toString().equals("SHIPPER")) {
  				List<Shipments> shipments = user.getShipments();
  				if (shipments.size() != 0 && shipments != null) {
  					for (int i = 0; i < shipments.size(); i++) {
  						if (shipments.get(i).getFullFreightTerms().equals("AVAILABLE SHIPMENT")) {
  							shipmentsWOCarrier.add(shipments.get(i));
  						}
  					}
  				}
  				if (shipmentsWOCarrier.size() != 0 && shipmentsWOCarrier != null) {
  					model.addAttribute("shipments", shipmentsWOCarrier);   
  				}
  			     
  			}
  			
  	        model = NotificationController.loadNotificationsIntoModel(user, model);
  			
  			return "pendingshipments";
  		}

  		shipment.setCarrier(null);
  		shipment.setVehicle(null);
  		shipment.setPaidAmount("");
  		shipment.setScac("");
  		shipment.setFreightbillNumber("");
  		shipment.setFullFreightTerms("PENDING");
  		shipment.setUser(getLoggedInUser());
  		shipmentsRepository.save(shipment);
  		Logger.info("{} has successfully added a new shipment with ID {}.",user.getUsername(), shipment.getId());       
  		
        model = NotificationController.loadNotificationsIntoModel(user, model);
  		
  		return "redirect:/pendingshipments";
  	}

	/**
  	 * Finds a frozen shipment that Master wants to delete. Using the id parameter and if found, redirects to delete confirmation page
  	 * @param id ID of the shipment being deleted
  	 * @param model Used to add data to the model
  	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
  	 * @return "redirect:"/delete/deleteshipmentconfirm"  
  	 */
	
	@GetMapping("/deleteshipment/{id}")
    public String deleteShipment(@PathVariable("id") long id, Model model, HttpSession session) {
        Shipments shipment = shipmentsRepository.findById(id)
        		.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
        User user = getLoggedInUser();
        String redirectLocation = (String) session.getAttribute("redirectLocation");
        
        if (shipment.getFullFreightTerms().toString().equals("FROZEN") && !user.getRole().toString().equals("MASTERLIST")) {
        	System.out.println("Non-Master user attempted to delete a frozen shipment!");
        	Logger.error("Non-Master user, {}, attempted to delete a frozen shipment with ID {}.", user.getUsername(), shipment.getId());//TODO: Replace this with a proper error message(what user would see this error?)
        	return redirectLocation; 
        }
        
        model.addAttribute("shipments", shipment);
        model.addAttribute("redirectLocation",redirectLocation); //Needed so confirmation html page can redirect to the right place if the user clicks no
        
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "/delete/deleteshipmentconfirm";
    }
	
	/**
  	 * Finds a shipment using the id parameter and if found, deletes the shipment and redirects to previous page.
  	 * @param id ID of the shipment being deleted
  	 * @param model Used to add data to the model
  	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
  	 * @return "redirect: To whatever the previous page was."" 
  	 */
  	@GetMapping("/deleteshipmentconfirmation/{id}")
    public String deleteShipmentConfirmation(@PathVariable("id") long id, Model model, HttpSession session) {
  		Shipments shipment = shipmentsRepository.findById(id)
  		        .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));

  		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
      
        if(!shipment.getBids().isEmpty()) {
        	List<Bids> bids = (List<Bids>) shipment.getBids();
        	User bidUser;
        	for (Bids bid : bids) 
        	{ 
        		bidUser = CarriersController.getUserFromCarrier(bid.getCarrier());
        		notificationService.addNotification(bidUser, "ALERT: Your bid with ID " + bid.getId() + " placed on shipment with ID " + bid.getShipment().getId() + " was deleted because the shipment was deleted", false);
        		bidsRepository.delete(bid); 
        	}
        	Logger.info("{} successfully deleted bids.", user.getUsername());
        	
        }

        Logger.info("{} successfully deleted a shipment with ID {}.", user.getUsername(), shipment.getId());
        if (user.getId() != shipment.getId()) {
        	notificationService.addNotification(shipment.getUser(), 
        			"ALERT: Your shipment with ID " + shipment.getId() + " and client " + shipment.getClient() + " was deleted by " + user.getUsername(), false);
        }

        shipmentsRepository.delete(shipment);
        return "redirect:" + session.getAttribute("redirectLocation"); 
    }
	
	/**
  	 * Finds a carrier using the id parameter and if found, adds all of the shipments of that carrier
  	 * to the shipments page
  	 * @param id ID of the shipment being viewed
  	 * @param model Used to add data to the model
  	 * @return "shipments"
  	 */
  	@GetMapping("/viewshipment/{id}")
    public String viewCarrierShipments(@PathVariable("id") long id, Model model, HttpSession session) {
        Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
        String redirectLocation = (String) session.getAttribute("redirectLocation");
        model.addAttribute("redirectLocation", redirectLocation);
        model.addAttribute("shipments", shipment);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "viewfullshipment";
    }
  	
  	/**
  	 * Finds a shipment using the id parameter and if found, adds all of the bids of that shipment
  	 * to the bids page
  	 * @param id ID of the shipment being used to get the bids
  	 * @param model Used to add data to the model
  	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
  	 * @return "bids"
  	 */
  	@GetMapping("/viewshipmentbids/{id}")
    public String viewShipmentBids(@PathVariable("id") long id, Model model, HttpSession session) {
        Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + id));
        
        model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
        model.addAttribute("bids", shipment.getBids());
        
        if (shipment.getCarrier() != null) {
        	
        	User users = getLoggedInUser();
            List<Notification> notifications = new ArrayList<>();
            
            if(!(users == null)) {
                notifications = NotificationController.fetchUnreadNotifications(users);
            }
            
            model.addAttribute("notifications",notifications);
        	
        	return "viewbidscomplete"; //TODO: rework this system, i dont like there being two separate bids.html pages, it makes things confusing. 
        }
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "bids";
    }
	
	/**
  	 * Finds a shipment using the id parameter and if found, adds the details of that shipment
  	 * to a form and redirects the user to that update form.
  	 * @param id ID of the shipment being edited
  	 * @param model Used to add data to the model
  	 * @return for Master: "/update/update-shipments" for Shipper: "/update/update-shipments-shipper"
  	 */
	@GetMapping("/editshipment/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
        
		model.addAttribute("vehicles", vehiclesRepository.findAll());
	    model.addAttribute("shipments", shipment);
	    model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
	    
	    User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
	    
        if (shipment.getFullFreightTerms().toString().equals("FROZEN") && !user.getRole().toString().equals("MASTERLIST")) {
        	System.out.println("Non-Master user attempted to edit a frozen shipment!");
        	Logger.error("Non-Master, {}, attempted to edit a frozen shipment with ID {}.", user.getUsername(), shipment.getId());
        	return "/index"; //TODO: Replace this with a proper message and redirect.
        }					//TODO: Add notification to this after master editing is implimented properly
	    
	    if (user.getRole().toString().equals("SHIPPER")) {
	    	return "/update/update-shipments-shipper";
	    }
	    else {
	    	return "/update/update-shipments";
	    }
        
    }
	
	/**
	 * Finds a shipment by ID, then Redirects to the Freeze Shipment confirmation page
	 * @param id ID of the shipment being frozen
  	 * @param model Used to add data to the model
  	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
  	 * @return "/freeze/freezeshipmentconfirm"
	 */
	@GetMapping("/freezeshipment/{id}")
	public String freezeShipment(@PathVariable("id") long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
		.orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		
		model.addAttribute("redirectLocation",redirectLocation);
		model.addAttribute("shipments", shipment);
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "/freeze/freezeshipmentconfirm";
	}
	
	/**
	 * Finds a shipment by ID, then sets that shipments freight terms to FROZEN, disabling interaction with it for all users except master. 
	 * @param id ID of the shipment being frozen
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments" or "redirect:/acceptedshipments" or "redirect:/pendingshipments"
	 */
	@GetMapping("/freezeshipmentconfirmation/{id}")
	public String freezeShipmentConfirmation(@PathVariable("id") long id, Model model, HttpSession session) {
		String redirectLocation = "redirect:" + (String) session.getAttribute("redirectLocation");
		Shipments shipment = shipmentsRepository.findById(id)
	     .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
        notificationService.addNotification(shipment.getUser(), 
        		"ALERT: Your shipment with ID " + shipment.getId() + " and Client " + shipment.getClient() + " was frozen by " + user.getUsername(), false);
        
		shipment.setFullFreightTerms("FROZEN");
		shipmentsRepository.save(shipment);
		Logger.info("{} successfully froze shipment with ID {}.", user.getUsername(), shipment.getId());
		
		return redirectLocation;
	}
	
	/**
	 * Finds a shipment by ID, then Redirects to the Unfreeze Shipment confirmation page
	 * @param id ID of the shipment being frozen
  	 * @param model Used to add data to the model
  	 * @return "/freeze/unfreezeshipmentconfirm"
	 */
	@GetMapping("/unfreezeshipment/{id}")
	public String unfreezeShipment(@PathVariable("id") long id, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", redirectLocation);
		Shipments shipment = shipmentsRepository.findById(id)
		.orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		
		model.addAttribute("shipments", shipment);
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "/freeze/unfreezeshipmentconfirm";
	}
	
	/**
	 * Finds a shipment by ID, then sets that shipments freight terms to AVAILABLE SHIPMENT, effectively unfreezing it. 
	 * @param id ID of the shipment being frozen
  	 * @param model Used to add data to the model
  	 * @return "redirect:/frozenshipments"
	 */
	@GetMapping("/unfreezeshipmentconfirmation/{id}")
	public String unfreezeShipmentConfirmation(@PathVariable("id") long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
	     .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		if (shipment.getBids().isEmpty()) {
			shipment.setFullFreightTerms("PENDING");
		} else {
			shipment.setFullFreightTerms("AVAILABLE SHIPMENT");
		}
		
		notificationService.addNotification(shipment.getUser(), 
        		"ALERT: Your shipment with ID " + shipment.getId() + " and Client " + shipment.getClient() + " was unfrozen by " + user.getUsername(), false);
		
		shipmentsRepository.save(shipment);
		Logger.info("{} successsfully unfroze shipment with ID {}.", user.getUsername(), shipment.getId());
		
		return "redirect:"+(String) session.getAttribute("redirectLocation");
	}
	
	/**
	 * 
	 */
	@GetMapping("/directassignshipment/{id}")
	public String directAssignShipment(@PathVariable("id") long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
			     .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		ArrayList<Carriers> carriers = (ArrayList<Carriers>) carriersRepository.findAll();
		
		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e){
			//do nothing if there is no error mssage
		}
		session.removeAttribute("message");
		
		model.addAttribute("shipment",shipment);
		model.addAttribute("shipmentId",shipment.getId());
		model.addAttribute("carriers",carriers);
		model.addAttribute("selectedCarrierId", 1);
		model.addAttribute("redirectLocation",(String)session.getAttribute("redirectLocation"));
		
		
		return "directassignshipment";
	}
	
	@PostMapping("/selectcarrier")
	public String selectCarrier(@RequestParam("selectedCarrierId") Long selectedCarrierId, 
			@RequestParam("inputPrice") String inputPrice,
			@RequestParam("shipmentId") Long shipmentId ,Model model, HttpSession session) {
		Carriers carrier = carriersRepository.findById(selectedCarrierId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Carrier Id:" + selectedCarrierId));
		Shipments shipment = shipmentsRepository.findById(shipmentId)
			     .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + shipmentId));
		
		Double paidAmount;
		
		model.addAttribute("selectedCarrierId",selectedCarrierId);
		
		try {
			paidAmount = Double.parseDouble(inputPrice);
		} catch (NumberFormatException e) {
			session.setAttribute("message","Error: Please input a valid price for this shipment");
			return "redirect:/directassignshipment/" + shipment.getId();
		}
		
		assignShipment(shipment, paidAmount, carrier);
		
		return "redirect:" + session.getAttribute("redirectLocation");
	}
	
	public void assignShipment(Shipments shipment, Double paidAmount, Carriers carrier) {
		
		User user = CarriersController.getUserFromCarrier(carrier);
		notificationService.addNotification(user, "Shipper " + shipment.getUser().getUsername() + " has requested that you pick up a shipment with a value of " + paidAmount +
				". You may accept from the 'AWAITING ACCEPTANCE' menu under the shipments.", true);
		
		shipment.setCarrier(carrier);
		shipment.setPaidAmount(paidAmount.toString());
		shipment.setScac(carrier.getScac());
		shipment.setFullFreightTerms("AWAITING ACCEPTANCE");
		shipmentsRepository.save(shipment);
	}
	
	/**
	 * 
	 * @param id
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/acceptawaitingshipment/{id}")
	public String acceptAwaitingShipment(@PathVariable("id") Long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
			     .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		User shipmentUser = shipment.getUser();
		
		shipment.setFullFreightTerms("BID ACCEPTED");
		shipmentsRepository.save(shipment);
		
		notificationService.addNotification(shipmentUser, "Your request to carrier " + shipment.getCarrier().getCarrierName() + " to take shipment with ID " + shipment.getId() + " was accpeted!", true);
		
		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}
	
	/**
	 * 
	 * @param id
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/denyawaitingshipment/{id}")
	public String denyAwaitingShipment(@PathVariable("id") Long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
			     .orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		User shipmentUser = shipment.getUser();
		
		notificationService.addNotification(shipmentUser, "Your request to carrier " + shipment.getCarrier().getCarrierName() + " to take shipment with ID " + shipment.getId() + " was denied!", true);
		
		shipment.setCarrier(null);
		shipment.setPaidAmount("");
		shipment.setScac("");
		shipment.setFullFreightTerms("PENDING");
		shipmentsRepository.save(shipment);
		
		return "redirect:" + (String) session.getAttribute("redirectLocation");
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
      BindingResult result, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		userValidator.addition(shipment, result);
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
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
      			Logger.error("{} failed to update shipment with ID {} because it already exists.", user.getUsername(), shipment.getId());
      			List<Shipments> shipmentsWOCarrier = new ArrayList<>();
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
      			return redirectLocation;
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
      		Logger.info("{} successfully saved shipment with ID {}", user.getUsername(), shipment.getId());
      		
      		return "redirect:" + redirectLocation;
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
      			Logger.error("{} failed to update shipment with ID {} because it already exists.", user.getUsername(), shipment.getId());
    			
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
        	Logger.info("{} successfully saved shipment with ID {}.", user.getUsername(), shipment.getId());
        	return "redirect:" + redirectLocation;
        }
        else {
        	shipmentsRepository.save(shipment);
        	Logger.info("{} successfully saved shipment with ID {}.", user.getUsername(), shipment.getId());
        	return "redirect:" + redirectLocation;
        }
    }
	
	/**
  	 * Uploads an excel file containing shipments
  	 * @param model Used to add data to the model 
  	 * @return "/uploadshipments" 
  	 */
	@GetMapping("/uploadshipments")
	public String ListFromExcelData(Model model, HttpSession session){
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "/uploadshipments";	
	}
	
	
	/**
  	 * Reads an excel file containing shipments and adds it to the shipments repository. <br>
  	 * After the file is uploaded and added to the database, user is redirected to the created shipments page
  	 * @param excelData Excel file that is being added to the database
  	 * @return "redirect:/createdshipments"
	 * @throws AccessException 
  	 */
	@SuppressWarnings("unused")
	@PostMapping("/break")
	public String LoadFromExcelData(@RequestParam("file") MultipartFile excelData, HttpSession session){
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		XSSFWorkbook workbook;
		try {
			User user = getLoggedInUser();
			workbook = new XSSFWorkbook(excelData.getInputStream());
	
		
			XSSFSheet worksheet = workbook.getSheetAt(0);
			
			List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
			List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				
				 
				Shipments shipment = new Shipments();
		        XSSFRow row = worksheet.getRow(i);
		        
		        if(row.getCell(0).getStringCellValue().isEmpty() || row.getCell(0)== null ) {
		        	break;
		        }
		        
		        
		       
		        String scac = "";										//Not Set By Upload
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

	    		if (!(clientName.length() < 64 && clientName.length() > 0) || !(clientName.matches("^[a-zA-Z0-9.]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Client field must be between 0 and 64 characters and alphanumeric.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(clientMode.equals("LTL") || clientMode.equals("FTL"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Client Mode must be between 0 and 3 characters and alphanumeric.",user.getUsername());
	    			continue;
	    		}
	    		
	    		
	    		if(!(date.length() < 12 && date.length() > 0 && date.matches("^\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}$"))) { 
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Date must be between 0 and 12 characters and formated MM/DD/YYYY.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(freightBillNumber.length() < 32 && freightBillNumber.length() > 0) || !(freightBillNumber.matches("^[0-9]*\\.?[0-9]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Freight Bill Number must be between 0 and 32 numbers long.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(paidAmount.length() < 16 && paidAmount.length() > 0) || !(paidAmount.matches("^[0-9]*\\.?[0-9]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Paid Amount must be between 0 and 16 numbers long.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(fullFreightTerms.length() < 24 && fullFreightTerms.length() > 0)) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Full Freight Terms has to be between 0 and 24 characters.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(commodityClass.length() < 12 && commodityClass.length() > 0) || !(commodityClass.matches("^[a-zA-Z0-9.]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Commodity Class must be between 0 and 12 characters and alphanumeric.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(commodityPieces.length() < 64 && commodityPieces.length() > 0) || !(commodityPieces.matches("^[0-9.]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Commodity Pieces must be between 0 and 64 characters long and numeric.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(commodityPaidWeight.length() < 16 && commodityPaidWeight.length() > 0) || !(commodityPaidWeight.matches("^[0-9.]*\\.?[0-9.]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Commodity Paid Weight must be between 0 and 16 characters long and numeric.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(shipperCity.length() < 64 && shipperCity.length() > 0) || !(shipperCity.matches("^[a-zA-Z]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Shipper City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(states.contains(shipperState) || stateAbbreviations.contains(shipperState))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Shipper State must be a state or state abbreviation.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(shipperZip.length() < 12 && shipperZip.length() > 0) || !(shipperZip.matches("^[0-9.]+$"))){
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Shipper Zip must be between 0 and 12 characters and is numeric.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(shipperLatitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Shipper Latitude must be between 90 and -90 up to 7 decimal places.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(shipperLongitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Shipper Longitude must be between 0 and 12 characters.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(consigneeCity.length() < 64 && consigneeCity.length() > 0) || !( consigneeCity.matches("^[a-zA-Z]+$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Consignee City must be between 0 and 64 characters and is alphabetic.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(states.contains(consigneeState) || stateAbbreviations.contains(consigneeState))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Consignee State must be a state or state abbreviation.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(consigneeZip.length() < 12 && consigneeZip.length() > 0) || !(consigneeZip.matches("^[0-9.]+$"))){
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Consignee Zip must be between 0 and 12 characters and is alphabetic.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(consigneeLatitude.matches("^(-?[0-8]?\\d(\\.\\d{1,7})?|90(\\.0{1,7})?)$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Consignee Latitude must be between 90 and -90 up to 7 decimal places.",user.getUsername());
	    			continue;
	    		}
	    		
	    		if(!(consigneeLongitude.matches("^-?(180(\\.0{1,7})?|\\d{1,2}(\\.\\d{1,7})?|1[0-7]\\d(\\.\\d{1,7})?|-180(\\.0{1,7})?|-?\\d{1,2}(\\.\\d{1,7})?)$"))) {
	    			workbook.close();
	    			Logger.error("{} attempted to upload a shipment but the Consignee Longitude must be between 180 and -180 up to 7 decimal places.",user.getUsername());
	    			continue;
	    		}
	    		
	    		
	    		shipment.setCarrier(null);			//THIS IS DEFAULT
	    		shipment.setVehicle(null);			//THIS IS DEFAULT
	    		
	    		
	    		shipment.setClient(clientName);
	    		shipment.setClientMode(clientMode);
	    		shipment.setScac(scac);
	    		shipment.setShipDate(date);
	    		shipment.setFreightbillNumber(freightBillNumber);
	    		shipment.setPaidAmount(paidAmount);
	    		shipment.setFullFreightTerms(fullFreightTerms);
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

		        
		        shipment.setUser(user);
		        shipmentsRepository.save(shipment);
		        Logger.info("{} successfully saved shipment with ID {}.", user.getUsername(), shipment.getId());
			 		
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
