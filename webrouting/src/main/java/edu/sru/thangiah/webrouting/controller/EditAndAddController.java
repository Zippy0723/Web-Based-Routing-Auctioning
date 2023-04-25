package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.controller.ExcelController;
import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Driver;
import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.MaintenanceOrders;
import edu.sru.thangiah.webrouting.domain.Role;
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
import edu.sru.thangiah.webrouting.repository.RoleRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.NotificationService;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with edit and add screens
 * @author Dakota Myers drm1022@sru.edu
 * @since 1/01/2023
 */

@Controller
public class EditAndAddController {


	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private NotificationService notificationService;

	private CarriersRepository carriersRepository;

	private ShipmentsRepository shipmentsRepository;

	private VehiclesRepository vehiclesRepository;

	private BidsRepository bidsRepository;

	private VehicleTypesRepository vehicleTypesRepository;

	private LocationsRepository	locationsRepository;

	private ContactsRepository contactsRepository;

	private UserRepository userRepository;

	private TechniciansRepository techniciansRepository;

	private DriverRepository driverRepository;

	private RoleRepository roleRepository;

	private MaintenanceOrdersRepository maintenanceOrdersRepository;

	private ValidationServiceImp validationServiceImp;

	private static final Logger Logger = LoggerFactory.getLogger(EditAndAddController.class);


	/**
	 * Constructor for the EditAndAddController
	 * @param bidsRepository Instantiates the bids Repository
	 * @param shipmentsRepository Instantiates the shipments Repository
	 * @param carriersRepository Instantiates the carriers Repository
	 * @param vehiclesRepository Instantiates the vehicles Repository
	 * @param vehicleTypesRepository Instantiates the vehicleTypes Repository
	 * @param locationsRepository Instantiates the locations Repository
	 * @param contactsRepository Instantiates the contacts Repository
	 * @param techniciansRepository Instantiates the technicians Repository
	 * @param driverRepository Instantiates the driver Repository
	 * @param maintenanceOrdersRepository Instantiates the maintenanceOrders Repository
	 * @param validationServiceImp Instantiates the validation Service Implementation
	 * @param userRepository Instantiates the user Repository
	 * @param roleRepository Instantiates the roles Repository
	 */
	
	public EditAndAddController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository, VehiclesRepository vehiclesRepository, 
			VehicleTypesRepository vehicleTypesRepository,LocationsRepository	locationsRepository, ContactsRepository contactsRepository, TechniciansRepository techniciansRepository,
			DriverRepository driverRepository, MaintenanceOrdersRepository maintenanceOrdersRepository, ValidationServiceImp validationServiceImp, UserRepository userRepository, RoleRepository roleRepository) {
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
		this.validationServiceImp = validationServiceImp;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;

	}
	
	/**
	 * Sets up the add bid page with required model attributes
	 * @param id ID of the shipment being found
	 * @param model Used to add data the model
	 * @param bid Holds information for the new bid
	 * @param result Checks entered data to ensure it is valid
	 * @param session stores the current logged in users HTTP session. Attribute "redirectLocation" can store a string containing the last page the user visited.
	 * @return /add/add-bid
	 */
	@GetMapping({"/add-bid/{id}"})
	public String showBidList(@PathVariable("id") long id, Model model, Bids bids, BindingResult result, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", redirectLocation);
		Shipments shipment = shipmentsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id: " + id));
		model.addAttribute("shipments", shipment);
		model.addAttribute("carriers", carriersRepository.findAll());
		User loggedInUser = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);

		if (!shipment.getFullFreightTerms().toString().equals("AVAILABLE SHIPMENT")) {
			System.out.println("Error: User attempeted to place a bid on a shipment that was not in auction");
			Logger.error("{} || attempted to place a bid on a shipment that was not in auction", loggedInUser.getUsername());
			return (String) session.getAttribute("redirectLocation");
		}
		
		try {
			model.addAttribute("message",session.getAttribute("message"));
		}
		catch(Exception e){

		}
		session.removeAttribute("message");
		
		model.addAttribute("bids", bids);
		


		return "/add/add-bid";
	}

	/**
	 * Adds a bid to the database. Checks if there are errors in the form
	 * Adds the date, time, and logged in user to the bid
	 * If there are no errors, the bid is saved in the bidsRepository. and the user is redirect to /bids 
	 * If there are errors, the user is redirected to the /add/add-technician page.
	 * @param bid Holds information for the new bid
	 * @param result Checks entered data to ensure it is valid
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirects to /createdshipments or /add/add-bid
	 */
	@RequestMapping({"/addbid"})
	public String addBid(@ModelAttribute("bids") Bids bid, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		Shipments shipment = bid.getShipment();
		List<Bids> bidsInShipment = shipment.getBids();
		
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		bid.setCarrier(user.getCarrier());
		bid.setDate(date.format(now));
		bid.setTime(time.format(now));
		bid.setShipment(shipment);
		
		String price = bid.getPrice();
		
		if (price.contains(".")) {
		    // Trim off any trailing "0"
		    price = price.replaceAll("0*$", "");
		}

		for (Bids b: bidsInShipment) {
			if (b.getCarrier().getCarrierName().equals(bid.getCarrier().getCarrierName())
					&& b.getPrice().equals(price)) {
				Logger.error("{} || attempted to add a but but they already placed a bid on that shipment for that price.",user.getUsername());
				session.setAttribute("message", "You already placed a bid on this shipment for this price.");
				return "redirect:/add-bid/"+shipment.getId();
			}
		}
		
		if (!(price.length() <= 16 && price.length() > 0) || !(price.matches("^[0-9.]+$"))) {
			Logger.error("{} || attempted to add a bid but the price was not between 1 and 16 numeric characters long.",user.getUsername());
			session.setAttribute("message", "Price was not between 1 and 16 numeric characters.");
			return "redirect:/add-bid/"+shipment.getId();	
		}
		
		bid.setPrice(price);

		System.out.print("THIS:"+bid.getTime());
		bidsRepository.save(bid);
		Logger.info("{} || successfully created a new bid with ID {}", user.getUsername(), bid.getId());
		notificationService.addNotification(bid.getShipment().getUser(), 
				"ALERT: A new bid as been added on your shipment with ID " + bid.getShipment().getId() + " and Client " + bid.getShipment().getClient(), false);

		return "redirect:" + redirectLocation;
	}

}