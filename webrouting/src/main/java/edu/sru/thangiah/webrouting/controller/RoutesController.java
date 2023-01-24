package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.Vehicles;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with routes.
 * @author Ian Black		imb1007@sru.edu
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/8/2022
 */

@Controller
public class RoutesController {
	
	private VehiclesRepository vehiclesRepository;
	
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

	/**
	 * Constructor for RoutesController. <br>
	 * Instantiates the vehiclesRepository <br>
	 * @param vehiclesRepository Used to interact with the vehicles in the database
	 */
	public RoutesController (VehiclesRepository vehiclesRepository) {
		this.vehiclesRepository = vehiclesRepository;
	}
	
	/**
	 * User selects a vehicle from a dropdown list and the shipments associated with that vehicle are then
	 * listed in that on the routes page
	 * @param model Used to add data to the model
	 * @param id ID of the vehicle being used to get the shipments
	 * @param date Date of the shipments being searched for
	 * @param anydate Stores whether or a specific date is being searched for
	 * @return /viewvehicleroute/id
	 */
	@RequestMapping(path = {"/search"})
	public String showVehicleRoute(Model model, String id, String date, boolean anydate) {
		Long vehicleId = Long.parseLong(id);
		
		Vehicles vehicle = vehiclesRepository.findById(vehicleId)
		          .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
		
		if (anydate) {
			model.addAttribute("shipments", vehicle.getShipments());
			model.addAttribute("vehicle", vehiclesRepository.findAll());
			return "routes";
		}
		
		else {
			List<Shipments> shipments = vehicle.getShipments();
			List<Shipments> shownShipments = new ArrayList<>();
			
			for (Shipments shipment : shipments) {
				System.out.println("\n\n" + shipment.getShipDate() + "\n" + date);
				String shipDate = shipment.getShipDate();
				if (shipDate.equalsIgnoreCase(date)) {
					System.out.println("Here!\n");
					shownShipments.add(shipment);
				}
			}
			
			System.out.println(shownShipments);
			model.addAttribute("shipments", shownShipments);
			model.addAttribute("vehicle", getVehiclesWithShipments());
			return "routes";
		}
	}

	/**
	 * Adds all of the routes to the "routes" model and redirects user to
	 * the locations page.
	 * @param model Used to add data to the model
	 * @return "routes"
	 */
	@RequestMapping({"/routes"})
    public String showVehiclesList(Model model) {
		if (getLoggedInUser().getRole().getName().equals("CARRIER")) {
			model.addAttribute("vehicle", getVehiclesWithShipmentsFromList(getLoggedInUser().getCarrier().getVehicles()));
		}
		else {
			model.addAttribute("vehicle", getVehiclesWithShipments());
		}
        return "routes";
    }
	
	/**
	 * Looks through the list of vehicles and determines if that vehicle has shipments associated with it. <br>
	 * If there are shipments, it is added to a list called displayedList which is returned to the user.
	 * @return displayedList
	 */
	public List<Vehicles> getVehiclesWithShipments() {
		Iterable<Vehicles> vehicles = vehiclesRepository.findAll();
		List<Vehicles> displayedList = new ArrayList<>();
		for (Vehicles vehicle : vehicles) {
			if (!vehicle.getShipments().isEmpty()) {
				displayedList.add(vehicle);
			}
		}
		return displayedList;
	}
	
	public List<Vehicles> getVehiclesWithShipmentsFromList(List<Vehicles> vehicles) {
		List<Vehicles> displayedList = new ArrayList<>();
		for (Vehicles vehicle : vehicles) {
			if (!vehicle.getShipments().isEmpty()) {
				displayedList.add(vehicle);
			}
		}
		return displayedList;
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
	

