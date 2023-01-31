package edu.sru.thangiah.webrouting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuctionController {
	//TODO: Find out if this Controller needs a constructor 
	
	/**
	 * Handles loading auctioning homepage
	 * @param model : used to add data to the page model
	 * @return : returns "auctioninghome"
	 */
	@RequestMapping("/auctioninghome")
	public String auctioningHome(Model model) {
		return "auctioninghome";
	}
	
	/**
	 * Finds a given shipment by ID, then assigns that shipment to the carrier that has the lowest active bid on it, then moves that shipment from AVAILABLE SHIPMENT to complete.
	 * If the shipment has no bids on it, or is not AVAILABLE SHIPMENT, returns the user to their page and alerts the master that the operation has failed
	 * @
	 */
	@RequestMapping("/forcecompleteshipment/{id}")
	public String forceCompleteShipment(Model model) {
		
		return "null";
	}
}
