package edu.sru.thangiah.webrouting.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
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

	@Autowired
	private ValidationServiceImp validationServiceImp;

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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");
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
		model.addAttribute("currentPage","/shipments");
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
		model.addAttribute("currentPage","/shipments");
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
		model.addAttribute("currentPage","/shipments");
		session.setAttribute("redirectLocation", "/pendingshipments");
		List<Shipments> shipments;

		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("message");

		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

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
		model.addAttribute("currentPage","/shipments");

		session.removeAttribute("message");

		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e) {
			//do nothing
		}
		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

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
		model.addAttribute("currentPage","/shipments");
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
		model.addAttribute("currentPage","/shipments");

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
			Logger.error("{} || attempted to add a shipment that already exists.", user.getUsername());
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
		Logger.info("{} || has successfully added a new shipment with ID {}.",user.getUsername(), shipment.getId());       

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
			Logger.error("{} ||, (Non-Master) attempted to delete a frozen shipment with ID {}.", user.getUsername(), shipment.getId());//TODO: Replace this with a proper error message(what user would see this error?)
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
			Logger.info("{} || successfully deleted bids.", user.getUsername());

		}

		Logger.info("{} || successfully deleted a shipment with ID {}.", user.getUsername(), shipment.getId());
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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");

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
		model.addAttribute("currentPage","/shipments");

		notificationService.addNotification(shipment.getUser(), 
				"ALERT: Your shipment with ID " + shipment.getId() + " and Client " + shipment.getClient() + " was frozen by " + user.getUsername(), false);

		shipment.setFullFreightTerms("FROZEN");
		shipmentsRepository.save(shipment);
		Logger.info("{} || successfully froze shipment with ID {}.", user.getUsername(), shipment.getId());

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
		model.addAttribute("currentPage","/shipments");

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
		Logger.info("{} || successsfully unfroze shipment with ID {}.", user.getUsername(), shipment.getId());

		return "redirect:"+(String) session.getAttribute("redirectLocation");
	}

	/**
	 * @throws IOException 
	 * 
	 */
	@GetMapping("/directassignshipment/{id}")
	public String directAssignShipment(@PathVariable("id") long id, Model model, HttpSession session) throws IOException {
		Shipments shipment = shipmentsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));
		ArrayList<Carriers> carriers = (ArrayList<Carriers>) carriersRepository.findAll();

		User user = getLoggedInUser();
		model.addAttribute("user",user);
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/shipments");

		try {
			model.addAttribute("message",session.getAttribute("message"));
		} catch (Exception e){
			//do nothing if there is no error mssage
		}
		session.removeAttribute("message");

		model.addAttribute("shipment",shipment);
		model.addAttribute("shipmentId",shipment.getId());
		model.addAttribute("carriers",carriers);
		model.addAttribute("selectedCarrierId", 1); //TODO: this will probably break if there is no carrier with ID 1 in the database
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

	/*
	 * This function is very complicated and a bit of a mess, needs proper documentation
	 */
	@RequestMapping(value="/getfreightrateprice/{shipmentid}-{carrierid}",method = RequestMethod.GET)
	@ResponseBody
	public String getShipmentPriceFromFreightRateTable(@PathVariable String shipmentid, @PathVariable String carrierid, HttpSession session) throws IOException {
		Shipments shipment = shipmentsRepository.findById(Long.parseLong(shipmentid))
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + shipmentid));
		Carriers carrier = carriersRepository.findById(Long.parseLong(carrierid))
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + carrierid));
		User user = getLoggedInUser();

		long Weight;
		String commodityClass;
		try {
			Weight = Long.parseLong(shipment.getCommodityPaidWeight());
			commodityClass = shipment.getCommodityClass();
		} catch (NumberFormatException e) {
			session.setAttribute("message", "There is an error in shipment data. Please contact an administrator.");
			return null;
		}

		byte[] freightRateTableRaw = user.getFreightRateTables();
		InputStream stream = new ByteArrayInputStream(freightRateTableRaw);
		XSSFWorkbook workbook = new XSSFWorkbook(stream);
		stream.close();
		int rowIndex = 1;
		int cellIndex = 1;
		Integer targetCellIndex = null;
		Integer targetRowIndex = null;
		XSSFSheet activeSheet = null;

		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			if(workbook.getSheetName(i).equals(carrier.getScac())) {
				activeSheet = workbook.getSheetAt(i);
			}
		}
		if (activeSheet == null) {
			workbook.close();
			session.setAttribute("message", "Error, your freight rate table does not have a table for this carrier.");
			return null;
		}

		XSSFRow weightRow = activeSheet.getRow(rowIndex);
		ArrayList<Long> weightRowData = new ArrayList<Long>();
		XSSFCell activeCell = weightRow.getCell(cellIndex);
		while(activeCell != null && !activeCell.toString().equals("")) {
			try {
				weightRowData.add(Long.parseLong(activeCell.toString().replaceAll("[^0-9]", "")));
			} catch (NumberFormatException e) {
				workbook.close();
				session.setAttribute("message", "Error reading your weight specifications, please check your freight rate table");
				return null;
			}
			activeCell = weightRow.getCell(++cellIndex);
		}

		for(int i = 0; i < weightRowData.size(); i++) {
			if (Weight < weightRowData.get(i)) {
				targetCellIndex = i + 1;
				break;
			}
		}

		rowIndex++;
		XSSFRow activeRow = activeSheet.getRow(rowIndex);
		activeCell = activeRow.getCell(0);
		while(activeCell != null && !activeCell.toString().equals("")) {
			String cellVal = activeCell.toString().substring(0,activeCell.toString().length() - 2);
			if(commodityClass.equals(cellVal)){
				targetRowIndex = rowIndex;
				break;
			}
			activeRow = activeSheet.getRow(++rowIndex);
			if(activeRow == null) {
				break;
			}
			activeCell = activeRow.getCell(0);
		}

		if(targetCellIndex == null || targetRowIndex == null) {
			workbook.close();
			session.setAttribute("message", "Error, there is no price specified for this shipment class and weight. Please check your freight rate table.");
			return null;
		}

		activeRow = activeSheet.getRow(targetRowIndex);
		activeCell = activeRow.getCell(targetCellIndex);
		Long result;

		try {
			result = Long.parseLong(activeCell.toString().substring(0,activeCell.toString().length()-2));
		} catch (NumberFormatException e) {
			workbook.close();
			session.setAttribute("message", "Error, the prices in your freight rate table are in an impoper format");
			return null;
		}

		workbook.close();
		return result.toString();
	}


	@GetMapping("/editshipment/{id}")
	public String showShipmentsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Shipments shipment = shipmentsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));

		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		model.addAttribute("shipments", shipment);
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/shipments");

		session.removeAttribute("message");

		return "/edit/edit-shipments";

	}



	@PostMapping("/updateshipment/{id}")
	public String updateShipment(@PathVariable("id") long id, Shipments shipment, 
			Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));

		Shipments temp = shipmentsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Shipment Id:" + id));


		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);


		Hashtable<String, String> hashtable = new Hashtable<>();

		hashtable.put("clientName", shipment.getClient().strip());
		hashtable.put("clientMode", shipment.getClientMode().strip());
		hashtable.put("date", shipment.getShipDate().strip());
		hashtable.put("commodityClass", shipment.getCommodityClass().strip());
		hashtable.put("commodityPieces", shipment.getCommodityPieces().strip());
		hashtable.put("commodityPaidWeight", shipment.getCommodityPaidWeight().strip());
		hashtable.put("shipperCity", shipment.getShipperCity().strip());
		hashtable.put("shipperState", shipment.getShipperState().strip());
		hashtable.put("shipperZip", shipment.getShipperZip().strip());
		hashtable.put("shipperLatitude", shipment.getShipperLatitude().strip());
		hashtable.put("shipperLongitude", shipment.getShipperLongitude().strip());
		hashtable.put("consigneeCity", shipment.getConsigneeCity().strip());
		hashtable.put("consigneeState", shipment.getConsigneeState().strip());
		hashtable.put("consigneeZip", shipment.getConsigneeZip().strip());
		hashtable.put("consigneeLatitude", shipment.getConsigneeLatitude().strip());
		hashtable.put("consigneeLongitude", shipment.getConsigneeLongitude().strip());

		Shipments result;

		result = validationServiceImp.validateShipmentForm(hashtable, session);

		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-shipments";
		}

		result.setScac(temp.getScac());
		result.setFullFreightTerms(temp.getFullFreightTerms());
		result.setPaidAmount(temp.getPaidAmount());
		result.setFreightbillNumber(temp.getFreightbillNumber());
		result.setId(id);
		result.setCarrier(temp.getCarrier());
		result.setUser(temp.getUser());


		shipmentsRepository.save(result);
		Logger.info("{} || successfully updated the shipment with ID {}",user.getUsername(), result.getId());
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
