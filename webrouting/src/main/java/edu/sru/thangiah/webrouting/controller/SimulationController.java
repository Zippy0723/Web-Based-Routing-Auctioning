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


	public SimulationController (BidsRepository bidsRepository, ShipmentsRepository shipmentsRepository, UserRepository userRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.bidsRepository = bidsRepository;
		this.userRepository = userRepository;

	}


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


	@PostMapping("/bidsimulation")
	public String bidsSimulation(Model model, HttpSession session) {
		User user = userService.getLoggedInUser();
		doBidsSimulation();
		Logger.info("{} || ran the bidding simulation.",user.getUsername());
		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}

	@PostMapping("/directassignsimulation")
	public String auctionSimulation(Model model, HttpSession session) {
		User user = userService.getLoggedInUser();
		doDirectAssignSimulation();
		Logger.info("{} || ran the direct assignment simulation.",user.getUsername());
		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}


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


	private void pushToAuction(Shipments shipment) {

		shipment.setFullFreightTerms("AVAILABLE SHIPMENT");
		shipmentsRepository.save(shipment);
	}


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


	private void acceptAwaitingShipment(Shipments shipment) {

		shipment.setFullFreightTerms("BID ACCEPTED");
		shipmentsRepository.save(shipment);

		notificationService.addNotification(shipment.getUser(), "Your request to carrier " + shipment.getCarrier().getCarrierName() + " to take shipment with ID " + shipment.getId() + " was accpeted!", true);
	}


}
