package edu.sru.thangiah.webrouting.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.BidsRepository;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ContactsRepository;
import edu.sru.thangiah.webrouting.repository.DriverRepository;
import edu.sru.thangiah.webrouting.repository.LocationsRepository;
import edu.sru.thangiah.webrouting.repository.MaintenanceOrdersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.repository.TechniciansRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.repository.VehicleTypesRepository;
import edu.sru.thangiah.webrouting.repository.VehiclesRepository;
import edu.sru.thangiah.webrouting.services.NotificationService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with the simulation
 * @author Dakota Myers drm1022@sru.edu
 * @since 1/01/2023
 */

@Controller
public class SimulationController {

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	private ShipmentsRepository shipmentsRepository;

	private BidsRepository bidsRepository;

	private UserRepository userRepository;

	private static final Logger Logger = LoggerFactory.getLogger(SimulationController.class);

	/**
	 * Constructor for the SimulationController
	 * @param bidsRepository Instantiates the bids Repository
	 * @param shipmentsRepository Instantiates the shipments Repository
	 * @param userRepository Instantiates the user Repository
	 */
	
	public SimulationController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, UserRepository userRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.bidsRepository = bidsRepository;
		this.userRepository = userRepository;

	}

	/**
	 * Adds all of the required attributes to the model to render the simulations page
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /simulations/selection
	 */
	@GetMapping("/simulations")
	public String simulationsPage(Model model, HttpSession session){
		session.setAttribute("redirectLocation", "/simulations");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/simulations");

		session.removeAttribute("message");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "/simulations/selection";	
	}


	/**
	 * Mapping for bid simulation button, calls the doBidsSimulation method
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirectLocation
	 */
	@PostMapping("/bidsimulation")
	public String bidsSimulation(Model model, HttpSession session) {
		User user = userService.getLoggedInUser();
		doBidsSimulation();
		Logger.info("{} || ran the bidding simulation.",user.getUsername());
		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}

	/**
	 * Mapping for direct assignment simulation button, calls the doDirectAssignSimulation method
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return redirectLocation
	 */
	
	@PostMapping("/directassignsimulation")
	public String auctionSimulation(Model model, HttpSession session) {
		User user = userService.getLoggedInUser();
		doDirectAssignSimulation();
		Logger.info("{} || ran the direct assignment simulation.",user.getUsername());
		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}


	/**
	 * Driver function for the bids simulation. Constructs the simulation over 20 seconds
	 */
	
	private void doBidsSimulation() {
		try {
			System.out.println("Shipper is creating a shipment.");
			Shipments shipment = makeShipment();

			Thread.sleep(5000); //5 seconds

			System.out.println("Pushing shipment to auction.");
			pushToAuction(shipment);

			Thread.sleep(5000);

			System.out.println("Carrier is placing a bid on the shipment.");
			Bids bid = addBids(shipment);

			Thread.sleep(5000); 

			System.out.println("Shipper is accepting the bid.");
			acceptBid(bid);

			Thread.sleep(5000); 

			System.out.println("Ending simulation.");
			bidsRepository.delete(bid);
			shipmentsRepository.delete(shipment);
		}
		catch(Exception e) {
		}
	}

	/**
	 * Driver function for the direct assign simulation. Constructs the simulation over 20 seconds
	 */
	
	private void doDirectAssignSimulation() {
		try {
			System.out.println("Shipper is creating a shipment.");
			Shipments shipment = makeShipment();

			Thread.sleep(5000); //5 seconds

			System.out.println("Shipper is directly assigning the shipment to a carrier.");
			directAssignShipment(shipment);

			Thread.sleep(5000); 

			System.out.println("Carrier is accepting the shipment.");
			acceptAwaitingShipment(shipment);

			Thread.sleep(5000); 

			System.out.println("Ending simulation.");
			shipmentsRepository.delete(shipment);
		}
		catch(Exception e) {
		}
	}

	/**
	 * Creates a shipment and gives it to 'SHIPPER'
	 * @return shipment
	 */
	
	private Shipments makeShipment(){

		User shipper = userRepository.findByEmail("shipper@gmail.com");

		Shipments shipment = new Shipments();

		shipment.setClient("Simulation");
		shipment.setClientMode("FTL");
		shipment.setShipDate("05-JAN-2023");
		shipment.setCommodityClass("72");
		shipment.setCommodityPieces("400");
		shipment.setCommodityPaidWeight("3000");
		shipment.setShipperCity("Las Vegas");
		shipment.setShipperState("NV");
		shipment.setShipperZip("88901");
		shipment.setShipperLatitude("36.1716");
		shipment.setShipperLongitude("115.1391");
		shipment.setConsigneeCity("Boston");
		shipment.setConsigneeState("MA");
		shipment.setConsigneeZip("02124");
		shipment.setConsigneeLatitude("42.3601");
		shipment.setConsigneeLongitude("71.0589");

		shipment.setFreightbillNumber("439");
		shipment.setPaidAmount("0.00");
		shipment.setFullFreightTerms("PENDING");
		shipment.setScac("");

		shipment.setCarrier(null);					//THIS IS DEFAULT
		shipment.setVehicle(null);					//THIS IS DEFAULT
		shipment.setUser(shipper);						//THIS USER

		shipmentsRepository.save(shipment);

		return shipment;
	}

	/**
	 * Takes in a shipment and pushes it to auction
	 * @param shipment holds the shipment that is being pushed to auction
	 */
	
	private void pushToAuction(Shipments shipment) {

		shipment.setFullFreightTerms("AVAILABLE SHIPMENT");
		shipmentsRepository.save(shipment);
	}

	/**
	 * Takes in a shipment and adds a bid to it by 'CARRIER'
	 * @param shipment holds the shipment that the bid is referring to
	 * @return bid
	 */
	private Bids addBids(Shipments shipment) {

		Carriers carrier = userRepository.findByEmail("carrier@gmail.com").getCarrier();
		Bids bid = new Bids();
		bid.setCarrier(carrier);
		bid.setDate("11-DEC-2023");
		bid.setPrice("3600");
		bid.setShipment(shipment);
		bid.setTime("17:56:29");
		bidsRepository.save(bid);

		notificationService.addNotification(bid.getShipment().getUser(), 
				"ALERT: A new bid as been added on your shipment with ID " + bid.getShipment().getId() + " and Client " + bid.getShipment().getClient(), false);

		return bid;
	}

	/**
	 * Takes in a bid and accepts it
	 * @param shipment holds the shipment that the bid is referring to
	 */
	
	private void acceptBid(Bids bid) {

		Carriers carrier = bid.getCarrier();
		Shipments shipment = bid.getShipment();
		shipment.setCarrier(carrier);
		shipment.setPaidAmount(bid.getPrice());
		shipment.setScac(carrier.getScac());
		shipment.setFullFreightTerms("BID ACCEPTED");
		shipmentsRepository.save(shipment);

		User bidUser = CarriersController.getUserFromCarrier(carrier);
		notificationService.addNotification(bidUser, 
				"ALERT: You have won the auction on shipment with ID " + shipment.getId() + " with a final bid value of " + bid.getPrice(), false);
	}

	/**
	 * Takes in a shipment directly assigns it to 'CARRIER'
	 * @param shipment holds the shipment that is being directly assigned
	 */
	
	private void directAssignShipment(Shipments shipment) {

		Carriers carrier = userRepository.findByEmail("carrier@gmail.com").getCarrier();
		shipment.setCarrier(carrier);
		shipment.setPaidAmount("7000.00");
		shipment.setScac(carrier.getScac());
		shipment.setFullFreightTerms("AWAITING ACCEPTANCE");
		shipmentsRepository.save(shipment);

		User user = CarriersController.getUserFromCarrier(carrier);
		notificationService.addNotification(user, "Shipper " + shipment.getUser().getUsername() + " has requested that you pick up a shipment with a value of " + 7000.00 +
				". You may accept from the 'AWAITING ACCEPTANCE' menu under the shipments.", true);

	}

	/**
	 * Accepts a bid for the shipment being passed
	 * @param shipment
	 */
	
	private void acceptAwaitingShipment(Shipments shipment) {

		shipment.setFullFreightTerms("BID ACCEPTED");
		shipmentsRepository.save(shipment);

		notificationService.addNotification(shipment.getUser(), "Your request to carrier " + shipment.getCarrier().getCarrierName() + " to take shipment with ID " + shipment.getId() + " was accpeted!", true);
	}


}
