package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.mailsending.Emailing;
import edu.sru.thangiah.webrouting.mailsending.MailSending;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.NotificationRepository;
import edu.sru.thangiah.webrouting.repository.RoleRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.services.NotificationService;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;
import edu.sru.thangiah.webrouting.controller.CarriersController;


/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Users.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */

@Controller
public class UserController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
    
    @Autowired  
    NotificationService notificationService;
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private CarriersRepository carriersRepository;
	
	private ShipmentsRepository shipmentsRepository;
	
	private BidsRepository bidsRepository;
	
	private NotificationRepository notficationRepository;
	
	@Autowired
    private Emailing emailImpl;
	
	@Autowired
	private HttpServletRequest mailRequest;
	
	private User dtoUser;
    
	private String websiteUrl;
	
	private static final Logger Logger = LoggerFactory.getLogger(UserController.class);
	/**
	 * Constructor for UserController. <br>
	 * Instantiates the userRepository <br>
	 * Instantiates the roleRepository
	 * @param userRepository Used to interact with the users in the database
	 * @param roleRepository Used to interact with the roles in the database
	 * @param carriersRepository Used to interact with the carriers in the database
	 */
    public UserController(UserRepository userRepository, RoleRepository roleRepository, CarriersRepository carriersRepository, ShipmentsRepository shipmentsRepository, BidsRepository bidsRepository, NotificationRepository notificationRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.carriersRepository = carriersRepository;
		this.shipmentsRepository = shipmentsRepository;
		this.bidsRepository = bidsRepository;
		this.notficationRepository = notificationRepository;
	}
    
    @GetMapping("/userhome")
    public String showUserHome(Model model, HttpSession session) {
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        session.setAttribute("redirectLocation", "/userhome");
        
        return "userhome";
    }
    
    /**
	 * Adds all of the users to the "userstable" model and redirects user to
	 * the users page.
	 * @param model Used to add data to the model
	 * @return "users"
	 */
    @GetMapping({"/users"})
    public String showUserList(Model model, HttpSession session) {
    	List<User> userList = userRepository.findAll();
    	List<User> userShipperList = new ArrayList<User>();
    	List<User> userCarrierList = new ArrayList<User>();
    	List<User> userOtherList = new ArrayList<User>();
    	
    	session.setAttribute("redirectLocation", "/users");
    	
    	
    	for (User user : userList) {
    		if (user.getRole().toString().equals("SHIPPER"))  {
    			userShipperList.add(user);
    		}
    	}
    	
    	for (User user : userList) {
    		if (user.getRole().toString().equals("CARRIER"))  {
    			userCarrierList.add(user);
    		}
    	}
    	
    	for (User user : userList) {
    		if (!user.getRole().toString().equals("CARRIER") && !user.getRole().toString().equals("SHIPPER") && !user.getRole().toString().equals("SHADOWADMIN")) {
    			userOtherList.add(user);
    		}
    	}
    	
    	model.addAttribute("shippers", userShipperList);
    	model.addAttribute("carriers", userCarrierList);
    	model.addAttribute("others", userOtherList);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "users";
    }
    
    @GetMapping("/CarrierAdministrationPage")
    public String carrierAdministrationPage(Model model, HttpSession session) {
    	List<User> userList = userRepository.findAll();
    	List<User> userCarrierList = new ArrayList<User>();
    	
    	for (User user : userList) {
    		if (user.getRole().toString().equals("CARRIER")) {
    			userCarrierList.add(user);
    		}
    	}
    	
        model.addAttribute("carriers", userCarrierList);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        session.setAttribute("redirectLocation", "/CarrierAdministrationPage");
        
        return "CarrierAdministrationPage";
    }
    
    @RequestMapping("/ShipperAdministrationPage")
    public String shipperAdministrationPage(Model model, HttpSession session) {
    	List<User> userList = userRepository.findAll();
    	List<User> finalUserList = new ArrayList<User>();
    	session.setAttribute("redirectLocation", "/ShipperAdministrationPage");
    	
    	for (User user : userList) {
    		if (user.getRole().toString().equals("SHIPPER")) {
    			finalUserList.add(user);
    		}
    	}
    	
    	
        model.addAttribute("userstable", finalUserList);
        
        User user = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(user, model);
        
        return "ShipperAdministrationPage";
    }
    
    /**
     * Redirects user to the /add/add-user-home page
     * @param model Used to add data to the model
     * @return "/add/add-user-home"
     */
    
   @RequestMapping({"/signup"})
   public String shownAddHomePage(Model model) {
	   model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
	   return "/add/add-user-home";
   }
   /**
    * Redirects user to the /add/add-user-carrier page. <br>
    * Adds instance of user to the userForm. <br>
    * @param user Stores information on the user being added
    * @param model Used to add data to the model
    * @return "/add/add-user-carrier"
    */
   
   @RequestMapping({"/addcarrieruser"})
   public String showCarrierPage(User user, Model model) {
	   model.addAttribute("userForm", new User());
	   model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
	   
       return "/add/add-user-carrier";
   }
   
   /**
    * Redirects user to the /upload users page when clicking "Upload an excel file" button in the users section of AdminTry
    * @param model used to add data to the model
    * @return "/uploadusers"
    */
   
   @RequestMapping({"/uploadusers"})
   public String showAddUserExcel(Model model) {
	   return "/uploadusers";
   }
    
    /**
	 * Redirects user to the /add/add-user page and displays all of the roles 
	 * except for the CARRIER role as that is created on a separate page.
	 * @param model Used to add data to the model
	 * @param user Used to the store the information on the user being added
	 * @return "/add/add-user"
	 */
  	@RequestMapping({"/addotheruser"})
      public String showOtherPage(User user, Model model) {
  		List<Role> roles = (List<Role>) roleRepository.findAll();
  		List<Role> result = new ArrayList<Role>();
  		for(Role r : roles){
  		  if(!r.getName().equals("SHIPPER") && !r.getName().equals("CARRIER")){
  		    result.add(r);
  		  }
  		}
  		model.addAttribute("roles", result);
  		model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
  		
  		return "/add/add-user";
    }
  	
  	@RequestMapping({"/addshipperuser"})
    public String showShipperPage(User user, Model model) {
		List<Role> roles = (List<Role>) roleRepository.findAll();
		List<Role> result = new ArrayList<Role>();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		for(Role r : roles){
		  if(r.getName().equals("SHIPPER")){
		    result.add(r);
		  }
		}

		model.addAttribute("roles",result);
		
		return "/add/add-user-shipper";
  }
  	
  	/**
  	 * Adds a user with the CARRIER role to the database. <br> 
  	 * Creates an instance of a carrier and associates it with the new user.
  	 * @param userForm Information on the user being added
  	 * @param bindingResult Ensures the inputs from the user are valid
  	 * @param model Used to add data to the model
  	 * @param carrierName Name of the carrier
  	 * @param scac SCAC code of the carrier
  	 * @param ltl Whether or not LTL is offered by the carrier
  	 * @param ftl Whether or not FTL is offered by the carrier
  	 * @param pallets Number of pallets a carrier can handle
  	 * @param weight Weight a carrier can handle
  	 * @return "redirect:/users" or "/add/add-user-carrier"
  	 */
  	@RequestMapping({"/addusercarrier"})
  	public String addUserCarrier(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model,
    		String carrierName, String scac, boolean ltl, boolean ftl, String pallets, String weight) {
  		
		model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
  		
  		List<Carriers> carrierList = (List<Carriers>) carriersRepository.findAll();
    	
    	Carriers carrier = new Carriers();
    	
    	User loggedInUser = getLoggedInUser();
    	
    	Long carrierId;

    	if (carrierList.size() != 0) {
    		carrierId = carrierList.get(carrierList.size() - 1).getId() + 1;
    	}
    	else {
    		carrierId = (long) 1;
    	}
    	
    	carrier.setId(carrierId);
    	carrier.setCarrierName(carrierName);
    	carrier.setScac(scac);
    	carrier.setLtl(ltl);
    	carrier.setFtl(ftl);
    	carrier.setPallets(pallets);
    	carrier.setWeight(weight);
    	
        userValidator.validate(userForm, bindingResult);
        
        Role role = new Role();
        role.setName("CARRIER");
        role.setId(3);
        
        userForm.setRole(role);
        
        userForm.setCarrier(carrier);

        if (bindingResult.hasErrors()) {
            return "/add/add-user-carrier";
        }
        
        Boolean deny = false;
  		for(Carriers check: carrierList) {
  			if(carrier.getCarrierName().toString().equals(check.getCarrierName().toString()) || carrier.getScac().toString().equals(check.getScac().toString())) {
  				deny = true;
  				break;
  			}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Carrier. Carrier name or SCAC code already exists");
  			Logger.error("{} || was unable to add {} as a carrier because that Carrier name or SCAC code already exists.",loggedInUser.getUsername(), userForm.getUsername());
  			return "/add/add-user-carrier";	 
  		}
        
  		carriersRepository.save(carrier);
        userService.save(userForm);
        Logger.info("{} || successfully added the carrier {}." ,loggedInUser.getUsername(), userForm.getUsername());

        return "redirect:/CarrierAdministrationPage";
  	}
      
  	/**
  	 * Adds a user to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the user is saved in the userService. and the user is redirect to /users <br>
  	 * If there are errors, the user is redirected to the /add/add-user page.
  	 * @param user Information on the uesr being added
  	 * @param result Ensures the information given by the user is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/users" or "/add/add-user"
  	 */
  	@RequestMapping({"/adduser"})
  	public String addUser(@Validated User user, BindingResult result, Model model) {
  		userValidator.validate(user, result);
  		User loggedInUser = getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		if (result.hasErrors()) {
  			return "/add/add-user";
		}
  		userService.save(user);
  		Logger.info("{} || successfully saved the user {}." ,loggedInUser.getUsername(), user.getUsername());
  		return "redirect:/users";
  	}
  	
  	@RequestMapping({"/addusershipper"})
  	public String addUserShipper(@Validated User user, BindingResult result, Model model) {
  		userValidator.validate(user, result);
  		if (result.hasErrors()) {
  			return "/add/add-user-shipper";
		}
  		
  		userService.save(user);
  		return "redirect:/ShipperAdministrationPage";
  	} 
  	
  	/**
  	 * Finds a user using the id parameter and if found, redirects user to confirmation page
  	 * Checks if there are dependencies and if so, user is not deleted and an error message is displayed to the user
  	 * @param id ID of the user being deleted
  	 * @param model Used to add data to the model
  	 * @return "/delete/deleteuserconfirm" or "users"
  	 */
  	@GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
          
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
         
        model.addAttribute("users", user);
        return "/delete/deleteuserconfirm";
    }
  	
  	/**
  	 * Finds a user using the id parameter and if found, deletes the user and redirects to users page
  	 * @param id ID of the user being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/users"
  	 */
  	@GetMapping("/deleteuserconfirmation/{id}")
    public String deleteUserConfirm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        
      if (!user.getShipments().isEmpty()) {
        	
        	for(Shipments shipment: user.getShipments()) {
        		
        		List<Bids> bids = (List<Bids>) shipment.getBids();
            	User bidUser;
            	for (Bids bid : bids) 
            	{ 
            		bidUser = CarriersController.getUserFromCarrier(bid.getCarrier());
            		notificationService.addNotification(bidUser, "ALERT: Your bid with ID " + bid.getId() + " placed on shipment with ID " + bid.getShipment().getId() + " was deleted because the shipment was deleted", false);
            		bidsRepository.delete(bid); 
            	}
        		
        		Logger.info("{} || successfully deleted a shipment with ID {}.", user.getUsername(), shipment.getId());
        		shipmentsRepository.delete(shipment);
        	}
        	
        	model.addAttribute("userstable", userRepository.findAll());
        	
        }
      
      	List<Notification> notifications = user.getNotifications();
  		for(Notification n: notifications)
  		{
  			notficationRepository.delete(n);
  		}
        
       
        Logger.info("{} || successfully deleted the user {}.", loggedInUser.getUsername(), user.getUsername());
        userRepository.delete(user);
         
        return "redirect:/users";
    }
  	
  	/**
  	 * Finds a user using the id parameter and if found, adds the details of that user
  	 * to a form and redirects the user to that update form. Also adds the roles, and carriers to the form.
  	 * @param id ID of the user being edited 
  	 * @param model Used to add data to the model
  	 * @return "update/update-user"
  	 */
  	@GetMapping("/edituser/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
  		User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setUpdateEmail(user.getEmail());
  		model.addAttribute("roles", roleRepository.findAll());
  		model.addAttribute("carriers", carriersRepository.findAll());
        model.addAttribute("user", user);
        
       if(user.getRole().toString().equals("SHIPPER")) {
        List<Role> roles = (List<Role>) roleRepository.findAll();
		List<Role> results = new ArrayList<Role>();

		for(Role rs : roles){
		  if(rs.getName().equals("SHIPPER")){
		    results.add(rs);
		  }
		}

		model.addAttribute("roles",results);
		User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		return "/update/update-shipper-user";
       }
       
       if(user.getRole().toString().equals("CARRIER")) {
	        List<Role> roles = (List<Role>) roleRepository.findAll();
			List<Role> results = new ArrayList<Role>();

			for(Role rsc : roles){
			  if(rsc.getName().equals("CARRIER")){
			    results.add(rsc);
			  }
			}
			
			model.addAttribute("roles",results);
			User loggedInUser = getLoggedInUser();
	        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
			return "/update/update-carrier-user";
	    }
       else {
    	   List<Role> roles = (List<Role>) roleRepository.findAll();
			List<Role> results = new ArrayList<Role>();

			for(Role rso : roles){
			  if(!rso.getName().equals("CARRIER") && !rso.getName().equals("SHIPPER")){
			    results.add(rso);
			  }
			}
			
		model.addAttribute("roles",results);
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        
        return "/update/update-user";
       }
    }
  	
  	@GetMapping("/editshipperuser/{id}")
    public String showShipperEditForm(@PathVariable("id") long id, Model model) {
  		User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setUpdateEmail(user.getEmail());
  		model.addAttribute("roles", roleRepository.findAll());
  		model.addAttribute("carriers", carriersRepository.findAll());
        model.addAttribute("user", user);
        
       if(user.getRole().toString().equals("SHIPPER")) {
        List<Role> roles = (List<Role>) roleRepository.findAll();
		List<Role> results = new ArrayList<Role>();

		for(Role rs : roles){
		  if(rs.getName().equals("SHIPPER")){
		    results.add(rs);
		  }
		}

		model.addAttribute("roles",results);
       }
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        
        return "/update/update-shipper-user";
    }
  	
  	
  	@GetMapping("/editcarrieruser/{id}")
    public String showCarrierEditForm(@PathVariable("id") long id, Model model) {
  		User user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setUpdateEmail(user.getEmail());
  		model.addAttribute("roles", roleRepository.findAll());
  		model.addAttribute("carriers", carriersRepository.findAll());
        model.addAttribute("user", user);
        
       if(user.getRole().toString().equals("CARRIER")) {
        List<Role> roles = (List<Role>) roleRepository.findAll();
		List<Role> results = new ArrayList<Role>();

		for(Role rsc : roles){
		  if(rsc.getName().equals("CARRIER")){
		    results.add(rsc);
		  }
		}

		model.addAttribute("roles",results);
       }
        User loggedInUser = getLoggedInUser();
        model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
        
        return "/update/update-carrier-user";
    }
  	/**
  	 * Admin functionality to update a certain users information
  	 * @param id - ID of the user being updated
  	 * @param user - Information on the user being updated
  	 * @param result - Ensures inputs from the user are valid
  	 * @param model - Used to add data to the model
  	 * @param nocarrier - Signifies if user is supposed to have a carrier associated with them
  	 * @param resetPassword - when checked resets the users password
  	 * @param redirectAttr - Used to display messages to the user after being redirected
  	 * @param updateEmail - stores the users previous email to allow them to keep the same email
  	 * @author Josh Gearhart 	jjg1018@sru.edu
  	 * @return "redirect:/users" or "/update/update-user"
  	 */
  
  	@PostMapping("/updateuser/{id}")
    public String updateUser(@PathVariable("id") long id, @Validated User user, 
      BindingResult result, Model model, boolean nocarrier, boolean resetPassword, RedirectAttributes redirectAttr, String updateEmail) {
  		updateEmail = user.getUpdateEmail();
  		User loggedInUser = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		userValidator.validateEmail(user, result);
        if (result.hasErrors()) {
        	user.setId(id);
            return "/update/update-user";
        }
        if(resetPassword) {
        	user.setEnabled(true);
        	user.setOtpCode(userService.createOtpCode());
        	userService.save(user);
        	redirectAttr.addFlashAttribute("resetMsg", user.getUsername() + "'s Email has Recieved a Password Reset");
        	 websiteUrl = MailSending.getUrl(mailRequest);
        	 emailImpl.forgotPasswordAdminFunction(user.getUsername(), websiteUrl);
        }
        
        user.setEnabled(true);
        userService.save(user);

        if(user.getRole().toString().equals("SHIPPER")){
      	  return "redirect:/ShipperAdministrationPage";
        }
        
        if(user.getRole().toString().equals("CARRIER")){
      	  return "redirect:/CarrierAdministrationPage";
        }
        
        else {

        Logger.info("{} || successfully updated the user {}.", loggedInUser.getUsername(), user.getUsername());

        return "redirect:/users";
        }
    }
  	
  	@PostMapping("/updateshipperuser/{id}")
    public String updateShipperUser(@PathVariable("id") long id, @Validated User user, 
      BindingResult result, Model model, boolean nocarrier, boolean resetPassword, RedirectAttributes redirectAttr, String updateEmail) {
  		updateEmail = user.getUpdateEmail();
  		User loggedInUser = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		userValidator.validateEmail(user, result);
        if (result.hasErrors()) {
        	user.setId(id);
            return "/update/update-shipper-user";
        }
        if(resetPassword) {
        	user.setEnabled(true);
        	user.setOtpCode(userService.createOtpCode());
        	userService.save(user);
        	redirectAttr.addFlashAttribute("resetMsg", user.getUsername() + "'s Email has Recieved a Password Reset");
        	 websiteUrl = MailSending.getUrl(mailRequest);
        	 emailImpl.forgotPasswordAdminFunction(user.getUsername(), websiteUrl);
        }
        
        user.setEnabled(true);
        userService.save(user);


        Logger.info("{} || successfully updated the user {}.", loggedInUser.getUsername(), user.getUsername());

        return "redirect:/ShipperAdministrationPage";
    }
  	
  	@PostMapping("/updatecarrieruser/{id}")
    public String updateCarrierUser(@PathVariable("id") long id, @Validated User user, 
      BindingResult result, Model model, boolean nocarrier, boolean resetPassword, RedirectAttributes redirectAttr, String updateEmail) {
  		updateEmail = user.getUpdateEmail();
  		User loggedInUser = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		userValidator.validateEmail(user, result);
        if (result.hasErrors()) {
        	user.setId(id);
            return "/update/update-carrier-user";
        }
        if(resetPassword) {
        	user.setEnabled(true);
        	user.setOtpCode(userService.createOtpCode());
        	userService.save(user);
        	redirectAttr.addFlashAttribute("resetMsg", user.getUsername() + "'s Email has Recieved a Password Reset");
        	 websiteUrl = MailSending.getUrl(mailRequest);
        	 emailImpl.forgotPasswordAdminFunction(user.getUsername(), websiteUrl);
        }
        
        user.setEnabled(true);
        userService.save(user);


        Logger.info("{} || successfully updated the user {}.", loggedInUser.getUsername(), user.getUsername());

        return "redirect:/CarrierAdministrationPage";
  	}
  	/**
  	 * Redirects user to the /update/update-user-details page.
  	 * Adds details of currently logged in user to the form.
  	 * @param model - Used to add data to the model
  	 * @return "/update/update-user-details"
  	 * @author Josh Gearhart 	jjg1018@sru.edu
  	 */
  	@GetMapping("update-user-details")
  	public String showUserDetailsForm(Model model, HttpSession session) {
  		session.setAttribute("redirectLocation", "/update-user-details");
  		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
  		
  		User user = getLoggedInUser();
  		String status = "CARRIER";
  		if(user.getRole().toString().equals("CARRIER")) {
  			model.addAttribute("status", status);
  		}
  		
  		model.addAttribute("user", getLoggedInUser());
  		dtoUser = new User();
  		dtoUser = getLoggedInUser();
  		dtoUser.setUpdateEmail(dtoUser.getEmail());
  		
  		User loggedInUser = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		
  		return "/update/update-user-details";
  	}
  	
  	/**
  	 * Updates users username, password, and email. Sets role, shipments, carrier, and id to that of <br>
  	 * the currently logged in user. <br>
  	 * If there are errors, the user is redirected to the home page with an error message displayed. <br>
  	 * If there are no errors, the user is still redirected to the home page but with a  message being displayed.
  	 * @param user - Stores information on the user being updated
  	 * @param result - Ensures information entered by the user is valid
  	 * @param model - Used to add data to the model
  	 * @param updateEmail - stores the users previous email to allow them to keep the same email
  	 * @author Josh Gearhart 	jjg1018@sru.edu
  	 * @return "/index"
  	 */
  		
  	@PostMapping("/updatedetails")
  	public String updateDetails( User user, BindingResult result, Model model,String updateEmail) {  
  		updateEmail = user.getUpdateEmail();
  		user.setRole(getLoggedInUser().getRole());
  		user.setShipments(getLoggedInUser().getShipments());
  		user.setCarrier(getLoggedInUser().getCarrier());
  		user.setId(getLoggedInUser().getId());
  		user.setEnabled(true);
  		userValidator.validateUpdate(user, result);
  		User loggedInUser = getLoggedInUser();
  		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
  		if (result.hasErrors()) {
  			model.addAttribute("error","Error: Information entered is invalid");
  			Logger.error("{} || failed to update the user {}.",loggedInUser.getUsername(), user.getUsername());
  			return "/update/update-user-details";
		}
  		if(!updateEmail.equals(user.getEmail())) {
  			websiteUrl = MailSending.getUrl(mailRequest);
  			emailImpl.saveVerificationCode(user);
  			emailImpl.updateUsersEmail(user.getEmail(), websiteUrl, updateEmail);
  		}
  		userService.save(user);
  		Logger.info("{} || sucessfully updated the user infomation for {}.", loggedInUser.getUsername(), user.getUsername());
  		model.addAttribute("message", "Information Updated! If you changed your email please re-verify your account!");
  		return "/index";
  	}
  	
  	@RequestMapping("/toggleenabled/{id}")
  	public String toggleEnabled(@PathVariable("id") long id, Model model, HttpSession session) {
  		User user = userRepository.findById(id)
  				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
  		
  		user.setEnabled(!user.isEnabled());
  		userRepository.save(user);
  		
  		return "redirect:" + (String) session.getAttribute("redirectLocation");
  	}
  	
  	
  	@RequestMapping({"/viewavailableroles"})
    public String showAvailableRoleList(Model model) {
        model.addAttribute("role", roleRepository.findAll());
        model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
        return "viewavailableroles";
    }
  	
  	@RequestMapping({"/rolesignup"})
    public String shownAddRolePage(Model model) {
  		model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
 	   return "/add/add-role";
    }
  	
  	@PostMapping({"/addrole"})
  	public String addRole(@RequestParam("roleName") String roleName, Model model) {
  		Role role = new Role(roleName);
  		roleRepository.save(role);
  		return "redirect:/viewavailableroles";
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
