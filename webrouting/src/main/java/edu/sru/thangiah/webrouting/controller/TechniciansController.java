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

import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.Technicians;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with technicians.
 * @author Ian Black		imb1007@sru.edu
 * @since 2/8/2022
 */

@Controller
public class TechniciansController {

	private TechniciansRepository techniciansRepository;
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    private static final Logger Logger = LoggerFactory.getLogger(TechniciansController.class);
	
	/**
	 * Constructor for TechniciansController. <br>
	 * Instantiates the techniciansRepository
	 * @param techniciansRepository Used to interact with technicians in the database
	 */
	public TechniciansController(TechniciansRepository techniciansRepository) {
		this.techniciansRepository = techniciansRepository;
	}
	
	/**
	 * Adds all of the technicians to the "technicians" model and redirects user to
	 * the technicians page.
	 * @param model Used to add data to the model
	 * @return "technicians"
	 */
	@RequestMapping({"/technicians"})
    public String showTechList(Model model, HttpSession session) {
		
		User user = getLoggedInUser();
	    try {
	       model.addAttribute("error",session.getAttribute("error"));
	    } catch(Exception e){
	          //do nothing
	    }
	    session.removeAttribute("error");
	        
	    String redirectLocation = "/technicians";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
        model.addAttribute("technicians", user.getCarrier().getTechnicians());
        model.addAttribute("currentPage","/technicians");
        
        
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "technicians";
    }
	
	/**
	 * Redirects user to the /add/add-technician page <br>
	 * Adds contacts to the model
	 * @param model Used to add data to the model
	 * @param technician Information on the technician being added
	 * @param result Ensures the information entered by the user is valid
	 * @return "/add/add-technician"
	 */
	@GetMapping({"/add-technician"})
    public String showContactList(Model model, Technicians technician, BindingResult result, HttpSession session) {
        model.addAttribute("contacts", getLoggedInUser().getCarrier().getContacts()); 
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        model.addAttribute("currentPage","/technicians");
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "/add/add-technician";
    }
	
	/**
  	 * Adds a technician to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the technician is saved in the techniciansRepository. and the user is redirect to /technicians <br>
  	 * If there are errors, the user is redirected to the /add/add-technician page.
  	 * @param technician Information on the technician being added
  	 * @param result Ensures information entered by the user is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/technicians" or "/add/add-technician"
  	 */
	@RequestMapping({"/addtechnician"})
  	public String addtechnician(@Validated Technicians technician, BindingResult result, Model model, HttpSession session) {
  		if (result.hasErrors()) {
  			return "/add/add-technician";
		}
  		Boolean deny = false;
  		User loggedInUser = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		String redirectLocation = (String) session.getAttribute("redirectLocation");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		model.addAttribute("currentPage","/technicians");
  		
  		List<Technicians> checkTech = new ArrayList<>();
  		checkTech = (List<Technicians>) loggedInUser.getCarrier().getTechnicians();
  		for(Technicians tech: checkTech) {
  			if(technician.getContact().getId() == tech.getContact().getId()) {
  				deny = true;
  				break;
  			}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Technician. Contact already in use");
  			model.addAttribute("technicians", techniciansRepository.findAll());
  			return "technicians";
			 
  		}
  		
  		technician.setCarrier(loggedInUser.getCarrier());
  		techniciansRepository.save(technician);
  		Logger.info("{} || successfully saved Technician with ID {}.",loggedInUser.getUsername(), technician.getId());
  		return "redirect:" + redirectLocation;
  	}
	
	/**
     * Redirects user to the /uploadtechnicians page when clicking "Upload an excel file" button in the technicians section of Carrier login
     * @param model used to add data to the model
     * @return "/uploadtechnicians"
     */
    
    @RequestMapping({"/uploadtechnicians"})
    public String showAddTechniciansExcel(Model model) {
       model.addAttribute("currentPage","/technicians");
 	   return "/uploadtechnicians";
    }
	
	/**
  	 * Finds a technician using the id parameter and if found, redirects to confirmation page 
  	 * If there are dependency issues, the technician is not deleted and an error is displayed to the user.
  	 * @param id ID of the technician being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/technicians"
  	 */
	@GetMapping("/deletetechnician/{id}")
    public String deletetechnician(@PathVariable("id") long id, Model model, HttpSession session) {
        Technicians technician = techniciansRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid technicians Id:" + id));
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        model.addAttribute("currentPage","/technicians");
        
        if(!technician.getOrders().isEmpty()) {
        	session.setAttribute("error", "Unable to delete due to dependency conflict."); 
        	Logger.error("{} || was unable to delete Technician with ID {} due to a dependecy conflict.", loggedInUser.getUsername(), technician.getId());
        	model.addAttribute("technicians", techniciansRepository.findAll());
        	
        	return "redirect:" + (String) session.getAttribute("redirectLocation");
        }
        model.addAttribute("technicians", technician);
        
        return "/delete/deletetechnicianconfirm";
    }
	
	/**
  	 * Finds a technician using the id parameter and if found, deletes the technician and redirects to technicians page
  	 * @param id ID of the technician being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/technicians"
  	 */
  	@GetMapping("/deletetechnicianconfirmation/{id}")
    public String deleteTechnicianConfirmation(@PathVariable("id") long id, Model model) {
  		Technicians technician = techniciansRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid technicians Id:" + id));
  		
  		User user = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(user, model);
  		model.addAttribute("currentPage","/technicians");
  		Logger.info("{} || successfully deleted Technician with ID {}.", user.getUsername(), technician.getId());

  		techniciansRepository.delete(technician);
        return "redirect:/technicians";
    }
	
	/**
  	 * Finds a technician using the id parameter and if found, adds the details of that technician
  	 * to the technicians page
  	 * @param id ID of the technician being viewed
  	 * @param model Used to add data to the model
  	 * @return "technicians"
  	 */
  	@GetMapping("/viewtechnician/{id}")
    public String viewTechnician(@PathVariable("id") long id, Model model, HttpSession session) {
        Technicians technician = techniciansRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid technician Id:" + id));
        
        model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        
        model.addAttribute("technicians", technician);
        model.addAttribute("currentPage","/technicians");
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "technicians";
    }
  	
  	/**
  	 * Gets all of the maintenance orders associated with the technician and adds those to the maintenance order page
  	 * @param id ID of the technician used to get the maintenance orders
  	 * @param model Used to add data to the model
  	 * @return "maintenanceorders"
  	 */
  	@GetMapping("/viewmaintenanceorders/{id}")
    public String viewMaintenanceOrders(@PathVariable("id") long id, Model model, HttpSession session) {
  	  Technicians technician = techniciansRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid technician Id:" + id));
  	  	
  	  	model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
        model.addAttribute("maintenanceOrder", technician.getOrders());
        model.addAttribute("currentPage","/maintenanceorders");
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "maintenanceorders";
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
