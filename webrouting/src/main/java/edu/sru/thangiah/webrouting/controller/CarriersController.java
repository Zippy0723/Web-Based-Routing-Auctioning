package edu.sru.thangiah.webrouting.controller;


import java.util.ArrayList;
import java.util.List;

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


import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with carriers.
 * @author Logan Kirkwood		llk1005@sru.edu
 * @since 3/22/2022
 */

@Controller
public class CarriersController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

	private CarriersRepository carriersRepository;

	/**
	 * Constructor for CarriersController. <br>
	 * Instantiates the carriersRepository
	 * @param carriersRepository Used to interact with carriers in the database
	 */
	public CarriersController (CarriersRepository carriersRepository) {
		this.carriersRepository = carriersRepository;
	}

	/**
	 * Adds all of the carriers to the "carriers" model and redirects user to
	 * the carriers page. <br>
	 * If a user is logged in as a carrier, the detials of their carrier is added to the page.
	 * @param model Used to add data to the model
	 * @return "carriers"
	 */
	@RequestMapping({"/carriers"})
	public String showCarriersList(Model model) {
		
		User user = getLoggedInUser();
		if (user.getRole().toString().equals("CARRIER")) {
			
			 model.addAttribute("carriers", user.getCarrier());
		     
		} else {
			model.addAttribute("carriers", carriersRepository.findAll());
		}
	
	
        return "carriers";
    }
	
	/**
  	 * Finds a carrier using the id parameter and if found, adds the details of that carrier
  	 * to a form and redirects the user to that update form.
  	 * @param id Stores the ID of the carrier to be edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-carrier"
  	 */
	@GetMapping("/editcarrier/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
		Carriers carrier = carriersRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));
		User user = getLoggedInUser();
        if (carrier.equals(user.getCarrier())) {
        	 model.addAttribute("carriers", carrier);
             return "/update/update-carriers";
        } else {
        	
        	
        	return "redirect:/carriers";
        }
    }
	
	/**
  	 * Finds a carrier using the id parameter and if found, adds all of the shipments of that carrier
  	 * to the shipments page
  	 * @param id Stores the ID of the carrier for the shipments to be added to the model
  	 * @param model Used to add data to the model
  	 * @return "shipments"
  	 */
  	@GetMapping("/viewcarriershipments/{id}")
    public String viewCarrierShipments(@PathVariable("id") long id, Model model) {
        Carriers carrier = carriersRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));
        
        model.addAttribute("shipments", carrier.getShipments());
        return "shipments";
    }
  	
  	/**
  	 * Finds a carrier using the id parameter and if found, adds all of the bids of that carrier
  	 * to the bids page
  	 * @param id Stores the ID of the carrier for the bids to be added to the model
  	 * @param model Used to add data to the model
  	 * @return "bids"
  	 */
  	@GetMapping("/viewcarrierbids/{id}")
    public String viewCarrierBids(@PathVariable("id") long id, Model model) {
        Carriers carrier = carriersRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));
        
        User user = getLoggedInUser();
        if (user.getRole().toString().equals("SHIPPER")) {
        	if (carrier.equals(user.getCarrier())) {
        		model.addAttribute("bids", carrier.getBids());
        		return "bids";
        	} 
    		else {
        	
       			model.addAttribute("bids", user.getCarrier().getBids());
       			return "bids";
       		}
        } 
        else {
        	model.addAttribute("bids", carrier.getBids() );
   			return "bids";
        }
        
    }
  	
  	/**
  	 * Finds a carrier using the id parameter and if found shows the carrier on the carrier page
  	 * @param id Stores the ID of the carrier to be viewed
  	 * @param model Used to add data to the model
  	 * @return "carriers"
  	 */
  	@GetMapping("/viewcarrier/{id}")
    public String viewCarrier(@PathVariable("id") long id, Model model) {
        Carriers carrier = carriersRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));
        
        model.addAttribute("carriers", carrier);
        return "carriers";
    }
	
	/**
  	 * Updates a carrier to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the carrier is updated in the carriersRepository. and the user is redirected to /carriers <br>
  	 * If there are errors, the user is redirected to the /update/update-carriers page.
  	 * @param id Stores the ID of the carrier to be updated
  	 * @param carrier Stores information on the carrier that is being updated
  	 * @param result Checks user inputs to ensure they are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/carriers" or "/update/update-carriers"
  	 */
	@PostMapping("/updatecarrier/{id}")
    public String updateCarrier(@PathVariable("id") long id, @Validated Carriers carrier, 
      BindingResult result, Model model) {
        if (result.hasErrors()) {
        	carrier.setId(id);
            return "/update/update-carriers";
        }
        User user = getLoggedInUser();
        Boolean deny = false;
  		List<Carriers> checkCarriers = new ArrayList<>();
  		checkCarriers = (List<Carriers>) carriersRepository.findAll();
  		for(Carriers check: checkCarriers) {
  			if(carrier.getCarrierName().toString().equals(check.getCarrierName().toString()) || carrier.getScac().toString().equals(check.getScac().toString())) {
  				if(carrier.getId() != check.getId()) {
  					deny = true;
  					break;
  				}
  			
  			}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to update Carrier. Carrier name or SCAC code already exists");
  			model.addAttribute("carriers", user.getCarrier());
  			return "carriers";	 
  		}
            
        carriersRepository.save(carrier);
        return "redirect:/carriers";
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
