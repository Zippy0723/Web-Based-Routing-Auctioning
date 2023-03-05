package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
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
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    private static final Logger Logger = LoggerFactory.getLogger(MaintenanceOrderController.class);
    
	/**
	 * Constructor for MaintenanceOrderController. <br>
	 * Instantiates the maintenanceOrderRepository <br>
	 * Instantiates the techniciansRepository
	 * @param maintenanceOrderRepository Used to interact with maintenance orders in the database
	 * @param techniciansRepository Used to interact with technicians in the database
	 */
	public MaintenanceOrderController(MaintenanceOrdersRepository maintenanceOrderRepository, TechniciansRepository techniciansRepository) {
		this.maintenanceOrderRepository = maintenanceOrderRepository;
		this.techniciansRepository = techniciansRepository;
	}
	
	/**
	 * Adds all of the maintenance orders to the "maintenanceOrder" model and redirects user to
	 * the maintenanceorders page.
	 * @param model Used to add data to the model
	 * @return "maintenanceorders"
	 */
	@RequestMapping({"/maintenanceorders"})
    public String showMaintenanceOrdersList(Model model, HttpSession session) {
		User user = getLoggedInUser();
		
		String redirectLocation = "/maintenanceorders";
		session.setAttribute("redirectLocation",  redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
        model.addAttribute("maintenanceOrder", user.getCarrier().getOrders());
        
        User users = getLoggedInUser();
        List<Notification> notifications = new ArrayList<>();
        
        if(!(users == null)) {
            notifications = NotificationController.fetchUnreadNotifications(users);
        }
        
        model.addAttribute("notifications",notifications);
        
        return "maintenanceorders";
    }
	
	/**
	 * Redirects user to the /add/add-maintenance page
	 * @param model Used to add data to the model
	 * @param maintenanceOrder Used to store information on the maintenance order
	 * @param result Ensures the user inputs are valid
	 * @return "/add/add-maintenance"
	 */
	@GetMapping({"/add-maintenance"})
    public String showOrderList(Model model, MaintenanceOrders maintenanceOrder, BindingResult result, HttpSession session) {
		User user = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("technicians", techniciansRepository.findAll());
		model.addAttribute("drivers", user.getCarrier().getDrivers());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        
        
        return "/add/add-maintenance";
    }
	
	/**
  	 * Adds a maintenance order to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the order is saved in the maintenanceOrderRepository. and the user is redirect to /maintenanceorders <br>
  	 * If there are errors, the user is redirected to the /add/add-maintenance page.
  	 * @param maintenanceOrder Stores information on the maintenance order that is added
  	 * @param result Ensures the user 
  	 * @param model Used to add data to the model
  	 * @return "redirect:/maintenanceorders" or "/add/add-maintenance"
  	 */
	@RequestMapping({"/addmaintenance"})
  	public String addMaintenanceOrder(@Validated MaintenanceOrders maintenanceOrder, BindingResult result, Model model, HttpSession session) {
		maintenanceOrder.setCarrier(getLoggedInUser().getCarrier());
		
		User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		
		if (result.hasErrors()) {
  			return "/add/add-maintenance";
		}
  		
  		Boolean deny = false;
  		List<MaintenanceOrders> orders = new ArrayList<>();
  		orders = (List<MaintenanceOrders>) maintenanceOrderRepository.findAll();
  		
  		for(MaintenanceOrders check: orders) {
  			if(maintenanceOrder.getVehicle().getId() == check.getVehicle().getId() && maintenanceOrder.getMaintenance_type().toString().equalsIgnoreCase(check.getMaintenance_type().toString()) ) {
  				if(maintenanceOrder.getStatus_key().toString().equals("Pending")) {
  					deny = true;
  	  				break;
  				}
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Maintenance Request. Same Request is currently pending.");
  			Logger.error("{} was unable to add Maintenance Request. Same Request is currently pending.", loggedInUser.getUsername());
  			model.addAttribute("maintenanceOrder", getLoggedInUser().getCarrier().getOrders());
  			return "/maintenanceorders";
			 
  		}
  		maintenanceOrderRepository.save(maintenanceOrder);
  		Logger.info("{} successfully saved the maintenance order with ID {}", loggedInUser.getUsername(), maintenanceOrder.getId());
  		return "redirect:" + redirectLocation;
  	}
	
	/**
     * Redirects user to the /uploadmaintenance page when clicking "Upload an excel file" button in the Maintenance section of Carrier login
     * @param model used to add data to the model
     * @return "/uploadmaintenance"
     */
    
    @RequestMapping({"/uploadmaintenance"})
    public String showAddMaintenanceExcel(Model model) {
 	   return "/uploadmaintenance";
    }
	
	/**
  	 * Finds a maintenance order using the id parameter and if found, redirects to confirmation page
  	 * @param id Stores the ID of the maintenance order to be deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/maintenanceorders"
  	 */
	@GetMapping("/deleteorder/{id}")
    public String deleteMaintenance(@PathVariable("id") long id, Model model) {
		MaintenanceOrders maintenanceOrder = maintenanceOrderRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));

		model.addAttribute("maintenanceorders", maintenanceOrder);
		
		User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
		
		return "/delete/deleteorderconfirm";
    }
    
    /**
  	 * Finds an order using the id parameter and if found, deletes the order and redirects to orders page
  	 * @param id ID of the order being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/maintenanceorders"
  	 */
  	@GetMapping("/deleteorderconfirmation/{id}")
    public String deleteOrderConfirmation(@PathVariable("id") long id, Model model) {
  		MaintenanceOrders maintenanceOrder = maintenanceOrderRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
  		
  		User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		Logger.info("{} successfully deleted the maintenace order with ID {}", loggedInUser.getUsername(), maintenanceOrder.getId());
  		maintenanceOrderRepository.delete(maintenanceOrder);
  		
  	    return "redirect:/maintenanceorders";
    }
	
	/**
  	 * Finds a maintenance order using the id parameter and if found, adds the details of that maintenance order
  	 * to a form and redirects the user to that update form. <br>
  	 * Also adds the technicians, drivers, and vehicles to the form.
  	 * @param id Stores the ID of the maintenance order to be edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-maintenance"
  	 */
	@GetMapping("/editorder/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		MaintenanceOrders maintenanceOrder = maintenanceOrderRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
		User user = getLoggedInUser();
		
		
		model.addAttribute("technicians", techniciansRepository.findAll());
		model.addAttribute("vehicles", user.getCarrier().getVehicles());
	    model.addAttribute("maintenanceOrders", maintenanceOrder);
	    model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
	    
        model = NotificationController.loadNotificationsIntoModel(user, model);
	    
        return "/update/update-maintenance";
    }
	
	/**
  	 * Updates a maintenance order to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the maintenance order is updated in the maintenanceOrderRepository and the user is redirect to /maintenanceorders <br>
  	 * If there are errors, the user is redirected to the /update/update-maintenance page.
  	 * @param id Stores the ID of the maintenance order that is being updated
  	 * @param maintenanceOrder Stores the information on the maintenance order being updated
  	 * @param result Ensures the inputs from the user are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/locations" or "/update/update-location"
  	 */
	@PostMapping("/updateorder/{id}")
    public String updateOrder(@PathVariable("id") long id, @Validated MaintenanceOrders maintenanceOrder, 
      BindingResult result, Model model, HttpSession session) {
		
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		
        maintenanceOrder.setCarrier(getLoggedInUser().getCarrier());
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		
		if (result.hasErrors()) {
        	maintenanceOrder.setId(id);
            return "/update/update-maintenance";
        }
        Boolean deny = false;
  		List<MaintenanceOrders> orders = new ArrayList<>();
  		orders = (List<MaintenanceOrders>) maintenanceOrderRepository.findAll();
  		
  		for(MaintenanceOrders check: orders) {
  			if(maintenanceOrder.getVehicle().getId() == check.getVehicle().getId() && maintenanceOrder.getMaintenance_type().toString().equalsIgnoreCase(check.getMaintenance_type().toString()) ) {
  				if(maintenanceOrder.getStatus_key().toString().equals("Pending") && maintenanceOrder.getId() != check.getId()) {
  					deny = true;
  	  				break;
  				}
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to update Maintenance Request. Same Request is currently pending.");
  			Logger.error("{} was unable to update Maintenance Request with ID {}. Same Request is currently pending.",loggedInUser.getUsername(), maintenanceOrder.getId());
  			model.addAttribute("maintenanceOrder", getLoggedInUser().getCarrier().getOrders());
  			return "/maintenanceorders";
			 
  		}
  		
        maintenanceOrderRepository.save(maintenanceOrder);
        Logger.info("{} successfully updated the maintenance order with ID {}",loggedInUser.getUsername(), maintenanceOrder.getId());
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
