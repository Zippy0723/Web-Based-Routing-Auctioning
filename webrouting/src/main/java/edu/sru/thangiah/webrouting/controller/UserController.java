package edu.sru.thangiah.webrouting.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.mailsending.Emailing;
import edu.sru.thangiah.webrouting.mailsending.MailSending;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.RoleRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

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
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private CarriersRepository carriersRepository;
	
	@Autowired
    private Emailing emailImpl;
	
	@Autowired
	private HttpServletRequest mailRequest;
	
	private User dtoUser;
    
	private String websiteUrl;
	/**
	 * Constructor for UserController. <br>
	 * Instantiates the userRepository <br>
	 * Instantiates the roleRepository
	 * @param userRepository Used to interact with the users in the database
	 * @param roleRepository Used to interact with the roles in the database
	 * @param carriersRepository Used to interact with the carriers in the database
	 */
    public UserController(UserRepository userRepository, RoleRepository roleRepository, CarriersRepository carriersRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.carriersRepository = carriersRepository;
	}
    
    /**
	 * Adds all of the users to the "userstable" model and redirects user to
	 * the users page.
	 * @param model Used to add data to the model
	 * @return "users"
	 */
    @RequestMapping({"/users"})
    public String showUserList(Model model) {
        model.addAttribute("userstable", userRepository.findAll());
        return "users";
    }
    
    /**
     * Redirects user to the /add/add-user-home page
     * @param model Used to add data to the model
     * @return "/add/add-user-home"
     */
   @RequestMapping({"/signup"})
   public String shownAddHomePage(Model model) {
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

       return "/add/add-user-carrier";
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
  		roles.remove(2);
  		model.addAttribute("roles", roles);
  		return "/add/add-user";
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
    		String carrierName, String scac, String ltl, String ftl, String pallets, String weight) {
  		
  		
  		List<Carriers> carrierList = (List<Carriers>) carriersRepository.findAll();
    	
    	Carriers carrier = new Carriers();
    	
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
  			return "/add/add-user-carrier";	 
  		}
        
  		carriersRepository.save(carrier);
        userService.save(userForm);

        return "redirect:/users";
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
  		if (result.hasErrors()) {
  			return "/add/add-user";
		}
  		
  		userService.save(user);
  		return "redirect:/users";
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
        
        if (!user.getShipments().isEmpty()) {
        	model.addAttribute("error", "Unable to delete due to dependency conflict."); 
        	model.addAttribute("userstable", userRepository.findAll());
        	return "users";
        	
        }
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
        return "/update/update-user";
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
  		userValidator.validateEmail(user, result);
        if (result.hasErrors()) {
        	user.setId(id);
            return "/update/update-user";
        }
        if (nocarrier) {
        	user.setCarrier(null);
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
        return "redirect:/users";
    }
  	
  	/**
  	 * Redirects user to the /update/update-user-details page. <br>
  	 * Adds details of currently logged in user to the form.
  	 * @param model - Used to add data to the model
  	 * @return "/update/update-user-details"
  	 * @author Josh Gearhart 	jjg1018@sru.edu
  	 */
  	@GetMapping("/update-user-details")
  	public String showUserDetailsForm(Model model) {
  		model.addAttribute("user", getLoggedInUser());
  		dtoUser = new User();
  		dtoUser = getLoggedInUser();
  		dtoUser.setUpdateEmail(dtoUser.getEmail());
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
  		if (result.hasErrors()) {
  			model.addAttribute("error","Error: Information entered is invalid");
  			return "/update/update-user-details";
		}
  		if(!updateEmail.equals(user.getEmail())) {
  			websiteUrl = MailSending.getUrl(mailRequest);
  			emailImpl.saveVerificationCode(user);
  			emailImpl.updateUsersEmail(user.getEmail(), websiteUrl, updateEmail);
  		}
  		userService.save(user);
  		model.addAttribute("message", "Information Updated! If you changed your email please re-verify your account!");
  		
  		return "/index";
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
