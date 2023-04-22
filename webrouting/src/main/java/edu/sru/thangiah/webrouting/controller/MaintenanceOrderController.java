package edu.sru.thangiah.webrouting.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import edu.sru.thangiah.webrouting.domain.MaintenanceOrders;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.MaintenanceOrdersRepository;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Maintenance Orders.
 * @author Ian Black		imb1007@sru.edu
 * @since 2/8/2022
 */

@Controller
public class MaintenanceOrderController {

	private MaintenanceOrdersRepository maintenanceOrderRepository;

	private TechniciansRepository techniciansRepository;

	@Autowired
	private ValidationServiceImp validationServiceImp;

	@Autowired
	private UserService userService;

	private static final Logger Logger = LoggerFactory.getLogger(MaintenanceOrderController.class);

	/**
	 * Constructor for MaintenanceOrderController
	 * @param maintenanceOrderRepository Instantiates the maintenanceOrder Repository
	 * @param techniciansRepository Instantiates the technicians Repository
	 */
	
	public MaintenanceOrderController(MaintenanceOrdersRepository maintenanceOrderRepository, TechniciansRepository techniciansRepository) {
		this.maintenanceOrderRepository = maintenanceOrderRepository;
		this.techniciansRepository = techniciansRepository;
	}

	/**
	 * Adds all of the maintenance orders to the /maintenanceOrder model and redirects user to
	 * the maintenanceorders page
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /maintenanceorders
	 */
	@RequestMapping({"/maintenanceorders"})
	public String showMaintenanceOrdersList(Model model, HttpSession session) {


		try {
			model.addAttribute("error",session.getAttribute("error"));
		} catch(Exception e){
			//do nothing
		}
		session.removeAttribute("error");

		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

		String redirectLocation = "/maintenanceorders";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("currentPage","/maintenanceorders");
		User user = userService.getLoggedInUser();

		model.addAttribute("maintenanceOrder", user.getCarrier().getOrders());

		User users = userService.getLoggedInUser();
		List<Notification> notifications = new ArrayList<>();

		if(!(users == null)) {
			notifications = NotificationController.fetchUnreadNotifications(users);
		}

		model.addAttribute("notifications",notifications);

		return "maintenanceorders";
	}

	/**
	 * Redirects user to the /uploadmaintenance page when clicking "Upload an excel file" button in the Maintenance section of Carrier login
	 * @param model used to add data to the model
	 * @return "/uploadmaintenance"
	 */

	@RequestMapping({"/uploadmaintenance"})
	public String showAddMaintenanceExcel(Model model) {
		model.addAttribute("currentPage","/maintenanceorders");
		return "/uploadmaintenance";
	}

	/**
	 * Finds a maintenance order using the id parameter and if found, redirects to confirmation page
	 * @param id Stores the ID of the maintenance order to be deleted
	 * @param model used to load attributes into the Thymeleaf model
	 * @return redirects to /maintenanceorders
	 */
	@GetMapping("/deleteorder/{id}")
	public String deleteMaintenance(@PathVariable("id") long id, Model model) {
		MaintenanceOrders maintenanceOrder = maintenanceOrderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));

		model.addAttribute("maintenanceorders", maintenanceOrder);
		model.addAttribute("currentPage","/maintenanceorders");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "/delete/deleteorderconfirm";
	}

	/**
	 * Finds an order using the id parameter and if found, deletes the order and redirects to orders page
	 * @param id of the order being deleted
	 * @param model Used to add data to the model
	 * @return redirects to /maintenanceorders
	 */
	@GetMapping("/deleteorderconfirmation/{id}")
	public String deleteOrderConfirmation(@PathVariable("id") long id, Model model) {
		MaintenanceOrders maintenanceOrder = maintenanceOrderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));

		User loggedInUser = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		model.addAttribute("currentPage","/maintenanceorders");
		Logger.info("{} || successfully deleted the maintenace order with ID {}", loggedInUser.getUsername(), maintenanceOrder.getId());

		maintenanceOrderRepository.delete(maintenanceOrder);

		return "redirect:/maintenanceorders";
	}
	
	/**
	 * Adds all of the required attributes to the model to render the edit orders page
	 * @param id of the order being edited 
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /edit/edit-orders 
	 */

	@GetMapping("/editorder/{id}")
	public String showOrdersEditForm(@PathVariable("id") long id, Model model, HttpSession session ) {
		MaintenanceOrders maintenanceOrder = maintenanceOrderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
		User user = userService.getLoggedInUser();

		model.addAttribute("currentPage","/maintenanceorders");
		model.addAttribute("technicians", user.getCarrier().getTechnicians());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model = NotificationController.loadNotificationsIntoModel(user, model);

		session.removeAttribute("message");
		
		if(!maintenanceOrder.getScheduled_date().equals("")) {
		//This converts the date to a format that the page is expecting to load it into the date object form
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
	        Date date;
			date = inputFormat.parse(maintenanceOrder.getScheduled_date());
	        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
	        String formattedDate = outputFormat.format(date);
	        maintenanceOrder.setScheduled_date(formattedDate);
	        
		} catch (ParseException e) {
			
			System.out.println("Failed to convert date for the forms expected date");
		}
		}
		
		model.addAttribute("maintenanceOrders", maintenanceOrder);

		return "/edit/edit-orders";
	}

	/**
	 * Receives and passes the new order object off to be validated
	 * Once validated the object is saved to the maintenance order repository
	 * @param id of the order being edited
	 * @param maintenanceOrder holds new order object submitted by the user
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirects to /maintenanceorders or /edit/edit-orders
	 */
	
	@PostMapping("/editorders/{id}")
	public String updateOrder(@PathVariable("id") long id, MaintenanceOrders maintenanceOrder, 
			Model model, HttpSession session) {


		User loggedInUser = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));

		model.addAttribute("technicians", loggedInUser.getCarrier().getTechnicians());
		model.addAttribute("vehicles", loggedInUser.getCarrier().getVehicles());
		model.addAttribute("maintenanceOrders", maintenanceOrder);

		Hashtable<String, String> hashtable = new Hashtable<>();

		MaintenanceOrders result;

		if (!maintenanceOrder.getScheduled_date().equals(""))
		{
			maintenanceOrder.setScheduled_date(dateConverter(maintenanceOrder.getScheduled_date()));	
		}

		hashtable.put("date", maintenanceOrder.getScheduled_date().strip());
		hashtable.put("details", maintenanceOrder.getDetails().strip());
		hashtable.put("serviceType", maintenanceOrder.getService_type_key().strip());
		hashtable.put("cost", maintenanceOrder.getCost().strip());
		hashtable.put("status", maintenanceOrder.getStatus_key().strip());
		hashtable.put("type", maintenanceOrder.getMaintenance_type().strip());

		result = validationServiceImp.validateMaintenanceOrderForm(hashtable, session);


		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-orders";
		}

		result.setVehicle(maintenanceOrder.getVehicle());
		result.setTechnician(maintenanceOrder.getTechnician());
		result.setId(id);


		maintenanceOrderRepository.save(result);
		Logger.info("{} || successfully updated the Maintenance Order with ID {}",loggedInUser.getUsername(), maintenanceOrder.getId());
		return "redirect:" + redirectLocation;
	}
	

	/**
	 * Adds all of the required attributes to the model to render the add order page
	 * @param maintenanceOrder holds the new maintenance order being added to the model
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /add/add-order
	 */
	
	@GetMapping("/add-order")
	public String showOrderAddForm(MaintenanceOrders maintenanceOrder, Model model, HttpSession session) {
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/maintenanceorders");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("technicians", user.getCarrier().getTechnicians());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());

		session.removeAttribute("message");
		model.addAttribute("orderForm", new MaintenanceOrders());

		return "/add/add-order";
	}

	/**
	 * Receives a maintenance order object by the user and passes it off for validation
	 * Once valid it is saved to the maintenance order repository
	 * @param maintenanceOrder holds the new maintenance order created by the user
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /add/add-order
	 */

	@PostMapping("submit-add-order")
	public String maintenanceOrderForm(@ModelAttribute("orderForm") MaintenanceOrders maintenanceOrder, Model model, HttpSession session) {
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/maintenanceorders");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("technicians", user.getCarrier().getTechnicians());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());

		Hashtable<String, String> hashtable = new Hashtable<>();

		if (!maintenanceOrder.getScheduled_date().equals(""))
		{
			maintenanceOrder.setScheduled_date(dateConverter(maintenanceOrder.getScheduled_date()));	
		}
		
		hashtable.put("date", maintenanceOrder.getScheduled_date().strip());
		hashtable.put("details", maintenanceOrder.getDetails().strip());
		hashtable.put("serviceType", maintenanceOrder.getService_type_key().strip());
		hashtable.put("cost", maintenanceOrder.getCost().strip());
		hashtable.put("status", maintenanceOrder.getStatus_key().strip());
		hashtable.put("type", maintenanceOrder.getMaintenance_type().strip());
		hashtable.put("vehiclePlateAndVin", maintenanceOrder.getVehicle().getPlateNumber() + " " + maintenanceOrder.getVehicle().getVinNumber());
		hashtable.put("techniciansContactFullName", maintenanceOrder.getTechnician().getContact().getFirstName().strip() + " " + maintenanceOrder.getTechnician().getContact().getLastName().strip());
	
		MaintenanceOrders result;

		result = validationServiceImp.validateMaintenanceOrder(hashtable, session);


		if (result == null) {
			Logger.error("{} || attempted to add a new Maintenance Order but "+ session.getAttribute("message") ,user.getUsername());
			model.addAttribute("message", session.getAttribute("message"));
			return "/add/add-order";
		}


		maintenanceOrderRepository.save(result);
		Logger.info("{} || successfully added a new Maintenance Order with ID {}",user.getUsername(), result.getId());

		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}
	
	/**
	 * Converts date from date picker into the expect format for saving to the repositories
	 * @param originalDateString holds the original date
	 * @return newDateString
	 */
	
	String dateConverter(String originalDateString) {
		
		DateTimeFormatter originalDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		LocalDate originalDate = LocalDate.parse(originalDateString, originalDateFormatter);

		DateTimeFormatter newDateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		String newDateString = originalDate.format(newDateFormatter);
		
		return newDateString;
	}

}
