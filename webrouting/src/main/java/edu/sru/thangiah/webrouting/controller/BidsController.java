package edu.sru.thangiah.webrouting.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with carriers.
 * @author Ian Black	imb1007@sru.edu
 * @since 4/8/2022
 */

@Controller
public class BidsController {

	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
	private BidsRepository bidsRepository;
	
	private ShipmentsRepository shipmentsRepository;
	
	private CarriersRepository carriersRepository;
	
	@Autowired
	private UserValidator userValidator;
	
	
	/**
	 * Constructor for BidsController. <br>
	 * Instantiates the bidsRepository <br>
	 * Instantiates the carriersRepository <br>
	 * Instantiates the shipmentsRepository
	 * @param bidsRepository Used to interact with bids in the database
	 * @param shipmentsRepository Used to interact with shipments in the database
	 * @param carriersRepository Used to interact with carriers in the database
	 */
	public BidsController(BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository) {
		this.bidsRepository = bidsRepository;
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
	}
	
	/**
	 * Redirects user to the /add/add-bid page <br>
	 * Adds shipments and carriers to the model
	 * @param id ID of the shipment being found
	 * @param model Used to add data the model
	 * @param bid Holds information for the new bid
	 * @param result Checks entered data to ensure it is valid
	 * @return "/add/add-bid"
	 */
	@GetMapping({"/add-bid/{id}"})
    public String showBidList(@PathVariable("id") long id, Model model, Bids bid, BindingResult result) {
		Shipments shipment = shipmentsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id: " + id));
        model.addAttribute("shipments", shipment);
        model.addAttribute("carriers", carriersRepository.findAll());
        return "/add/add-bid";
    }
	
	/**
  	 * Adds a bid to the database. Checks if there are errors in the form. <br>
  	 * Adds the date, time, and logged in user to the bid. <br>
  	 * If there are no errors, the bid is saved in the bidsRepository. and the user is redirect to /bids <br>
  	 * If there are errors, the user is redirected to the /add/add-technician page.
  	 * @param bid Holds information for the new bid
  	 * @param result Checks entered data to ensure it is valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments" or "/add/add-bid"
  	 */
	@RequestMapping({"/addbid"})
  	public String addBid(@Validated Bids bid, BindingResult result, Model model) {
		userValidator.addition(bid, result);
  		if (result.hasErrors()) {
  			return "/add/add-bid";
		}
  		
  		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
  		
				
  		LocalDateTime now = LocalDateTime.now();
  		
  		User user = getLoggedInUser();
  		bid.setCarrier(user.getCarrier());
  		
  		bid.setDate(date.format(now));
  		bid.setTime(time.format(now));
  		
  		boolean deny = false;
  		
  		List<Bids> bids = (List<Bids>) bidsRepository.findAll();
		
		for (int i = 0; i < bids.size(); i++) {

			Bids currentBid = bids.get(i);
			
			if (currentBid.getCarrier().getCarrierName().equals(bid.getCarrier().getCarrierName())
					&& currentBid.getPrice().equals(bid.getPrice())) {
				deny = true;
			}
		}
  		
  		if (deny == true) {
  			model.addAttribute("error", "Error: Bid with the same carrier and price has already been placed.");
  			model.addAttribute("shipments", bid.getShipment());
  	        model.addAttribute("carriers", carriersRepository.findAll());
  	        return "/add/add-bid";
  		}
  		  		
  		bidsRepository.save(bid);
  		return "redirect:/createdshipments";
  	}
	
	/**
  	 * Finds a bid using the id parameter and if found, redirects user to delete confirmation page
  	 * @param id Holds the ID of the bid to be deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments" or "/delete/deletebidconfirm"
  	 */
	@GetMapping("/deletebid/{id}")
    public String deleteBid(@PathVariable("id") long id, Model model) {
        Bids bid = bidsRepository.findById(id)
        		 .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        User user = getLoggedInUser();
        
        if (bid.getCarrier().equals(user.getCarrier())) {
        	model.addAttribute("bids", bid);
        	return "/delete/deletebidconfirm";
        }
        
        return "redirect:/createdshipments";
    }
	
	/**
  	 * Finds a bid using the id parameter and if found, deletes the bid and redirects to created shipments page
  	 * @param id ID of the bid being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments"
  	 */
  	@GetMapping("/deletebidconfirmation/{id}")
    public String deleteUserConfirm(@PathVariable("id") long id, Model model) {
  		Bids bid = bidsRepository.findById(id)
       		 .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        
        bidsRepository.delete(bid);
        return "redirect:/createdshipments";
    }
	
	/**
	 * Accepts a bid from the carrier. <br>
	 * Assigns a carrier, price, scac, and changes the full freight terms of a shipment. <br>
	 * Updates the shipment using the shipmentsRepository.
	 * @param id Holds the ID of the bid to be accepted
	 * @param model Used to add data to the model
	 * @return "redirect:/shipmentshomeshipper"
	 */
	@GetMapping("/acceptbid/{id}")
	public String acceptBid(@PathVariable("id") long id, Model model) {
		Bids bid = bidsRepository.findById(id)
		.orElseThrow(() -> new IllegalArgumentException("Invalid bid Id: " + id));
		
		Carriers carrier = bid.getCarrier();
		Shipments shipment = bid.getShipment();
		
		shipment.setCarrier(carrier);
		shipment.setPaidAmount(bid.getPrice());
		shipment.setScac(carrier.getScac());
		shipment.setFullFreightTerms("BID ACCEPTED");
		
		shipmentsRepository.save(shipment);
		
		return "redirect:/shipmentshomeshipper";
	}
	
	/**
  	 * Finds a bid using the id parameter and if found, adds the details of that bid
  	 * to a form and redirects the user to that update form. Also adds the shipments to the form. <br>
  	 * Adds shipments and carriers to the model.
  	 * @param id Holds the ID of the bid to be edited
  	 * @param model Used to add data to the model
  	 * @return "/update/update-bid"
  	 */
	@GetMapping("/editbid/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
		Bids bid = bidsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id:" + id));
		 User user = getLoggedInUser();
		 
		 if (!bid.getCarrier().equals(user.getCarrier())) {
	        	return "redirect:/createdshipments";
	        }
        
		 model.addAttribute("bids", bid);
		 model.addAttribute("shipments", shipmentsRepository.findAll());  
		 model.addAttribute("carriers", carriersRepository.findAll());
	     
        return "/update/update-bid";
    }
	
	/**
  	 * Updates a bid to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the bid is updated in the bidsRepository. and the user is redirected to /createdshipments <br>
  	 * If there are errors, the user is redirected to the /update/update-bid page.
  	 * @param id Holds the ID of the bid to be edited
  	 * @param bid Holds the information on the bid that is being updated
  	 * @param result Checks if the inputs for the bid are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/createdshipments" or "/update/update-bid"
  	 */
	@PostMapping("/updatebid/{id}")
    public String updateBid(@PathVariable("id") long id, @Validated Bids bid, 
      BindingResult result, Model model) {
		userValidator.addition(bid, result);
        if (result.hasErrors()) {
        	bid.setId(id);
            return "/update/update-bid";
        }
        
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
  		
  		LocalDateTime now = LocalDateTime.now();
  		
  		User user = getLoggedInUser();
  		bid.setCarrier(user.getCarrier());
  		
  		bid.setDate(date.format(now));
  		bid.setTime(time.format(now));
  		
  		boolean deny = false;
  		
  		List<Bids> bids = (List<Bids>) bidsRepository.findAll();
		
		for (int i = 0; i < bids.size(); i++) {

			Bids currentBid = bids.get(i);
			
			if (currentBid.getCarrier().getCarrierName().equals(bid.getCarrier().getCarrierName())
					&& currentBid.getPrice().equals(bid.getPrice())
					&& currentBid.getShipment().getId().equals(bid.getShipment().getId())) {
				deny = true;
			}
		}
  		
  		if (deny == true) {
  			model.addAttribute("error", "Error: Bid with the same carrier and price has already been placed.");
  			model.addAttribute("shipments", bid.getShipment());
  	        model.addAttribute("carriers", carriersRepository.findAll());
  	        return "/update/update-bid";
  		}
            
        bidsRepository.save(bid);
        return "redirect:/createdshipments";
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
