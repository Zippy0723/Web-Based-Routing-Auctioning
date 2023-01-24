package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.thangiah.webrouting.captcha.Recaptcha;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.mailsending.Emailing;
import edu.sru.thangiah.webrouting.mailsending.MailSending;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for pages
 * dealing with the login function
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/6/2022
 */

@Controller
public class LoginController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private Emailing emailImpl;

    @Autowired
    private UserValidator userValidator;
    
    private CarriersRepository carriersRepository;
    
    @Autowired
    private Recaptcha captcha;
    
    private String googleResponse;
    
    @Autowired
    private HttpServletRequest mailRequest; 
    
    @Autowired
    private HttpServletRequest request;
    /**
     * Constructor for LoginController <br>
     * Instantiates carriersRepository
     * @param carriersRepository Used to interact with the carriers in the database
     */
    public LoginController(CarriersRepository carriersRepository) {
    	this.carriersRepository = carriersRepository;
    }
    
    /**
     * Redirects user to the registration home page
     * @param model Used to add data to the model
     * @return "registrationhome"
     */
    @GetMapping("/registrationhome")
    public String registrationHome(Model model) {
    	return "registrationhome";
    }
    
    /**
     * Redirects user to the registration shipper page and adds a new instance of a User to the userForm model.
     * @param model Used to add data to the model
     * @return "registration"
     */
    @GetMapping("/registrationshipper")
    public String registrationShipperInitial(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new User());

        return "registrationshipper";
    }

    /**
  	 * Adds a user to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, and the Recaptcha is valid the user is saved in the userService and sent a verification email. and the user is redirect to /registrationshipper <br>
  	 * If there are errors, the user is redirected to the registrationlogin page.
  	 * @param userForm Stores information on the user
  	 * @param bindingResult Ensures the user inputs are valid
  	 * @param redirectAttr - Used to display the Message to the user
  	 * @return "registrationshipper" or "registrationlogin"
     * @throws IOException 
     * @throws MessagingException 
  	 */

    @PostMapping("/registrationshipper")
    public String registrationShipper(@ModelAttribute("userForm") User userForm,BindingResult bindingResult, RedirectAttributes redirectAttr) throws IOException, MessagingException {
        userValidator.validate(userForm, bindingResult);
        Role role = new Role();
        String websiteUrl;
        role.setName("SHIPPER");
        role.setId(2);
        userForm.setRole(role);
        
        userForm.setCarrier(null);
        googleResponse= request.getParameter("g-recaptcha-response");
        if(!captcha.VerifyRecaptcha(googleResponse)) {
        	return "registrationshipper";
        }
        
        if (bindingResult.hasErrors()) {
            return "registrationshipper";
        }
        websiteUrl = MailSending.getUrl(mailRequest);
        userService.save(userForm);
        emailImpl.saveVerificationCode(userForm);
        emailImpl.sendVerification(userForm, websiteUrl);
        redirectAttr.addFlashAttribute("emailMessage","Your Account Has been Created Please Check Your Inbox or Spam  to Verify Your Account");
        return "redirect:login";
    }

    
    /**
     * Redirects user to the registration carrier page and adds a new instance of a User to the userForm model.
     * @param model Used to add data to the model
     * @return "registrationcarrier"
     */
    @GetMapping("/registrationcarrier")
    public String registrationCarrierInitial(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new User());

        return "registrationcarrier";
    }
    
    /**
     * Adds a user to the database. Checks if there are errors in the form. <br>
  	 * Creates a new instance of a carrier. 
  	 * Associates that carrier with the user. 
  	 * If there are no errors, and the Recaptcha is verified the user is saved in the userService. and the user is redirect to /registrationcarrier 
  	 * Sends the user an email to verify their accountZ
  	 * If there are errors, the user is redirected to the registrationlogin page.
  	 * @param userForm Stores information on the user
  	 * @param bindingResult Ensures the user inputs are valid
  	 * @param model Used to add data to the model
     * @param carrierName Name of the carrier
     * @param scac SCAC of the carrier
     * @param ltl If the carrier offers LTL
     * @param ftl If the carrier offers FTL
     * @param pallets Number of pallets offered by the carrier
     * @param weight Weight carrier can hold
     * @param redirectAttr - Used to display the Message to the user
     * @return "registrationcarrier" or "registrationlogin"
     * @throws MessagingException 
     * @throws UnsupportedEncodingException 
     */
    @PostMapping("/registrationcarrier")
    public String registrationCarrier(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model,
    		String carrierName, String scac, String ltl, String ftl, String pallets, String weight,RedirectAttributes redirectAttr) throws UnsupportedEncodingException, MessagingException {

    	
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
        
        googleResponse= request.getParameter("g-recaptcha-response");
        if(!captcha.VerifyRecaptcha(googleResponse)) {
        	return "registrationcarrier";
        }
        

        if (bindingResult.hasErrors()) {
            return "registrationcarrier";
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
  			return "registrationcarrier";	 
  		}
  		String websiteUrl;

        carriersRepository.save(carrier);
        websiteUrl = MailSending.getUrl(mailRequest);
        userService.save(userForm);
        emailImpl.saveVerificationCode(userForm);
        emailImpl.sendVerification(userForm, websiteUrl);
        redirectAttr.addFlashAttribute("emailMessage","Your Account Has been Created Please Check Your Email to Verify Your Account");
        return "redirect:login";
    }
    
    /**
     * Tries to login the user. If successful, user is redirected to the index page. <br>
     * If there are errors, the error is added to the error model and the user is redirected to "registrationlogin".
     * @param model Used to add data to the model
     * @param error Returns an error if username or password are invalid
     * @return "redirect:/" or "registrationlogin"
     */
    @GetMapping("/registrationlogin")
    public String registrationLogin(Model model, String error) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        return "registrationlogin";
    }
    
    /**
     * Tries to login the user. If successful, the user is redirected to the index page. <br>
     * If there are errors, the error is added to the error model and the user is redirected to "login". <br>
     * If the user logs out, the logout message is added to the message model.
     * @param model Used to add data to the model
     * @param error Returns an error if there is one during login
     * @param logout Returns a string if the user logs out
     * @return "redirect:/" or "login"
     */
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("error", "Your username or password is invalid.");
       
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        
        return "login";
    }
    
    /**
     * Redirects the user to the index page from /
     * @param model Used to add data to the model
     * @return "/index"
     */
    @GetMapping({"/"})
    public String welcome(Model model) {
        return "index";
    }

    /**
     * Displays the 403 access denied page
     * @return "/403"
     */
    @GetMapping("/403")
    public String error403() {
        return "/403";
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
