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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.Role;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
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

	private CarriersRepository carriersRepository;

	private static final Logger Logger = LoggerFactory.getLogger(CarriersController.class);
	private static UserRepository userRepository;


	/**
	 * Constructor for CarriersController. 
	 */
	public CarriersController (CarriersRepository carriersRepository, UserRepository userRepository) {
		this.carriersRepository = carriersRepository;
		CarriersController.userRepository = userRepository;
	}

	/**
	 * Adds all of the carriers to the "carriers" model and redirects user to
	 * the carriers page. 
	 * If a user is logged in as a carrier, the detials of their carrier is added to the page.
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /carriers
	 */
	@RequestMapping({"/carriers"})
	public String showCarriersList(Model model, HttpSession session) {

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/carriers");
		String redirectLocation = "/carriers";

		session.removeAttribute("message");

		if (user.getRole().toString().equals("CARRIER")) {

			model.addAttribute("carriers", user.getCarrier());

		} else {
			model.addAttribute("carriers", carriersRepository.findAll());
		}

		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);

		return "carriers";
	}

	/**
	 * Redirects user to the /uploadcarrier page when clicking "Upload an excel file" button in the carriers section of AdminTry
	 * @param model used to add data to the model
	 * @return /uploadcarrier
	 */

	@RequestMapping({"/uploadcarrier"})
	public String showAddCarrierExcel(Model model) {
		model.addAttribute("currentPage","/carriers");
		return "/uploadcarrier";
	}

	/**
	 * Finds a carrier using the id parameter and if found, adds the details of that carrier
	 * to a form and redirects the user to that update form.
	 * @param id Stores the ID of the carrier to be edited
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /update/update-carrier
	 */
	@GetMapping("/editcarrier/{id}")
	public String showEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Carriers carrier = carriersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/carriers");
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
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /shipments
	 */
	@GetMapping("/viewcarriershipments/{id}")
	public String viewCarrierShipments(@PathVariable("id") long id, Model model, HttpSession session) {
		Carriers carrier = carriersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("shipments", carrier.getShipments());
		model.addAttribute("currentPage","/shipments");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "shipments";
	}

	/**
	 * Finds a carrier using the id parameter and if found, adds all of the bids of that carrier
	 * to the bids page
	 * @param id Stores the ID of the carrier for the bids to be added to the model
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /bids
	 */
	@GetMapping("/viewcarrierbids/{id}")
	public String viewCarrierBids(@PathVariable("id") long id, Model model, HttpSession session) {
		Carriers carrier = carriersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation",session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/bids");
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
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /carriers
	 */
	@GetMapping("/viewcarrier/{id}")
	public String viewCarrier(@PathVariable("id") long id, Model model, HttpSession session) {

		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("currentPage","/carriers");
		Carriers carrier = carriersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + id));

		model.addAttribute("carriers", carrier);

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "carriers";
	}

	/**
	 * Updates a carrier to the database. Checks if there are errors in the form.
	 * If there are no errors, the carrier is updated in the carriersRepository. and the user is redirected to /carriers 
	 * If there are errors, the user is redirected to the /update/update-carriers page.
	 * @param id Stores the ID of the carrier to be updated
	 * @param carrier Stores information on the carrier that is being updated
	 * @param result Checks user inputs to ensure they are valid
	 * @param model Used to add data to the model
	 * @return redirect:/carriers or /update/update-carriers
	 */
	@PostMapping("/updatecarrier/{id}")
	public String updateCarrier(@PathVariable("id") long id, @Validated Carriers carrier, 
			BindingResult result, Model model, HttpSession session) {
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/carriers");

		if (result.hasErrors()) {
			carrier.setId(id);
			return "/update/update-carriers";
		}

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
			Logger.error("{} || attempted to update {}, carrier. Update failed because Carrier name or SCAC code already exists.", user.getUsername(), carrier.getCarrierName());
			return "redirect:" + redirectLocation;
		}

		carriersRepository.save(carrier);
		Logger.info("{} || successfully updated the carrier with ID {}.", user.getUsername() , carrier.getId());
		return "redirect:" + redirectLocation;
	}

	/**
	 * Receives and carrier object and finds the corresponding user object associated
	 * @param carrier holds the carrier object
	 * @param result holds the user found
	 * @return result
	 */
	public static User getUserFromCarrier(Carriers carrier) { //This helper function is to fix a lack of relationship between carriers and users in the database. it should not be permenant.
		List<User> carrierUsers = new ArrayList<>();
		List<User> users = userRepository.findAll();
		User result = null;

		for (User user : users) {
			if(user.getCarrier() != null) {
				carrierUsers.add(user);
			}	
		}

		for (User user : carrierUsers) {
			if(user.getCarrier().getId() == carrier.getId()) {
				result = user;
			}
		}

		return result;
	}
	
	/**
	 * Adds the required attributes to the model to render the edit carriers page
	 * @param id of the carrier being edited
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /index or /edit/edit-carrier 
	 */

	@GetMapping("/editcarriers/{id}")
	public String showCarrierEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		User userForm = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
		User user = userService.getLoggedInUser();
		Carriers carrierForm = userForm.getCarrier();
		if(carrierForm == null) {
			System.out.println("Something has gone horribly horribly wrong, abort");
			return "redirect:/";
		}

		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/users");

		model.addAttribute("user", userForm);
		model.addAttribute("carrier",carrierForm);

		session.removeAttribute("message");

		return "/edit/edit-carriers";

	}


	/**
	 * Receives a carrier and user object that gets validated and saved to the repository through the edit carriers page
	 * @param id of the user calling the method
	 * @param user holds the user object being created
	 * @param carrier holds the new carrier object passed by the user
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /edit/edit-carriers or redirect: + redirectLocation
	 */

	@PostMapping("edit-carrier/{id}")
	public String carrierUpdateForm(@PathVariable("id") long id, User user, Carriers carrier, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User loggedInUser = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);
		User result = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
		Carriers carrierResult = result.getCarrier();

		List <User> repoUsers =  userRepository.findAll();
		List<Carriers> repoCarriers = (List<Carriers>) carriersRepository.findAll();


		String username = user.getUsername().strip();
		String emailAddress = user.getEmail().strip();
		String scac = carrier.getScac();
		String carrierName = carrier.getCarrierName();
		String pallets = carrier.getPallets();
		String weight = carrier.getWeight();

		model.addAttribute("carrier",carrier);

		if(!(emailAddress.length() <= 64 && emailAddress.length() > 0) || !(emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){
			Logger.error("{} || attempted to edit a carrier but the email address must be between 1 and 64 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Email must be between 1 and 64 alphanumeric characters.");
			return "/edit/edit-carriers";	
		}

		if (!(username.length() <= 32 && username.length() > 0) || !(username.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} || attempted to edit a carrier but the username was not between 1 and 32 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Username must be between 1 and 32 alphanumeric characters.");
			return "/edit/edit-carriers";		
		}	

		for(User check: repoUsers) {
			String repoUsername = check.getUsername().strip();
			if(username.equals(repoUsername) && id != check.getId()) {
				Logger.error("{} || attempted to save a carrier with the same username as another user.",loggedInUser.getUsername());
				model.addAttribute("message", "Another user already exists with that username.");
				return "/edit/edit-carriers";	
			}
		}

		for(User check: repoUsers) {
			String repoEmailAddress = check.getEmail().strip();
			if(emailAddress.equals(repoEmailAddress) && id != check.getId()) {
				Logger.error("{} || attempted to carrier a shipper with the same email as another user.",loggedInUser.getUsername());
				model.addAttribute("message", "Another user already exists with that email.");
				return "/edit/edit-carriers";	
			}
		}

		for(Carriers check: repoCarriers) {
			String repoCarrierName = check.getCarrierName().strip();
			if(carrierName.equals(repoCarrierName) && result.getCarrier().getId() != check.getId()) {
				Logger.error("{} || attempted to save a carrier with the same carrier name as another carrier.",loggedInUser.getUsername());
				model.addAttribute("message", "Another carrier already exists with that carrier name.");
				return "/edit/edit-carriers";	
			}
		}

		for(Carriers check: repoCarriers) {
			String repoScac = check.getScac().strip();
			if(scac.equals(repoScac) && result.getCarrier().getId() != check.getId()) {
				Logger.error("{} || attempted to save a carrier with the same scac as another carrier.",loggedInUser.getUsername());
				model.addAttribute("message", "Another carrier already exists with that scac.");
				return "/edit/edit-carriers";	
			}
		}

		if(!(scac.length() <= 4 && scac.length() >= 2) || !(scac.matches("^[a-zA-Z0-9]+$"))) {
			Logger.error("{} || attempted to edit a carrier but the scac was not between 2 and 4 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Scac was not between 2 and 4 alphanumeric characters.");
			return "/edit/edit-carriers";	
		}


		if(!(pallets.length() <= 32 && pallets.length() > 0) || !(pallets.matches("^[0-9]+$"))) {
			Logger.error("{} || attempted to edit a carrier but the pallets must be between 1 and 32 numeric chracters.",loggedInUser.getUsername());
			model.addAttribute("message", "Pallets must be between 1 and 32 numeric characters.");
			return "/edit/edit-carriers";	
		}

		if(!(weight.length() <= 32 && weight.length() > 0) || !(weight.matches("^[0-9]+$"))) {
			Logger.error("{} || attempted to edit a carrier but the weight must be between 1 and 32 numeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Weight must be be between 1 and 32 numeric characters.");
			return "/edit/edit-carriers";	
		}

		result.setEmail(emailAddress);
		result.setUsername(username);
		result.setAuctioningAllowed(user.getAuctioningAllowed());
		result.setEnabled(user.isEnabled());

		carrierResult.setCarrierName(carrierName);
		carrierResult.setScac(scac);
		carrierResult.setWeight(weight);
		carrierResult.setPallets(pallets);

		userRepository.save(result);
		carriersRepository.save(carrierResult);
		Logger.error("{} || successfully updated a carrier with ID {}.", loggedInUser.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
	}

	/**
	 * Adds all of the required attributes to render the add carrier page to the model
	 * @param user user object being passed to the model
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /add/add-carrier
	 */

	@GetMapping("/addcarrier")
	public String showCarrierAddForm(User user, Model model, HttpSession session) {
		model.addAttribute("userForm", new User());
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model = NotificationController.loadNotificationsIntoModel(userService.getLoggedInUser(), model);

		session.removeAttribute("message");

		return "/add/add-carrier";
	}


	/**
	 * Receives a carrier and user object from the add carrier page.
	 * Validates and saves the new user and carrier
	 * @param userForm holds the user object
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @param carrierName holds the carrier name of the new carrier
	 * @param scac holds the scac of the new carrier
	 * @param ltl holds the ltl of the new carrier
	 * @param ftl holds the ftl of the new carrier
	 * @param pallets holds the pallets of the new carrier
	 * @param weight holds the weight of the new carrier
	 * @return /add/add-carrier or redirect: + redirectLocation
	 */
	@PostMapping("addcarrierform")
	public String carrierAddForm(@ModelAttribute("userForm") User userForm, Model model,
			String carrierName, String scac, boolean ltl, boolean ftl, String pallets, String weight, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User loggedInUser = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(loggedInUser, model);

		List <User> repoUsers =  userRepository.findAll();
		List <Carriers> repoCarriers =  (List<Carriers>) carriersRepository.findAll();

		String username = userForm.getUsername().strip();
		String emailAddress = userForm.getEmail().strip();
		String password = userForm.getPassword().strip();


		if(!(emailAddress.length() <= 64 && emailAddress.length() > 0) || !(emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){
			Logger.error("{} || attempted to add a carrier but the email address must be between 1 and 64 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Email must be between 1 and 64 alphanumeric characters.");
			return "/add/add-carrier";	
		}

		if (!(username.length() <= 32 && username.length() > 0) || !(username.matches("^[a-zA-Z0-9.]+$"))) {
			Logger.error("{} || attempted to add a carrier but the username was not between 1 and 32 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Username must be between 1 and 32 alphanumeric characters.");
			return "/add/add-carrier";	
		}
		if (!(password.length() <= 32 && password.length() > 7)) {
			Logger.error("{} || attempted to add a carrier but the password was not between 1 and 32 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "password must be between 8 and 32 alphanumeric characters.");
			return "/add/add-carrier";	
		}

		for(User check: repoUsers) {
			String repoUsername = check.getUsername().strip();
			if(username.equals(repoUsername)) {
				Logger.error("{} || attempted to add a carrier with the same username as another user.",loggedInUser.getUsername());
				model.addAttribute("message", "Another user already exists with that username.");
				return "/add/add-carrier";
			}
		}

		for(User check: repoUsers) {
			String repoEmailAddress = check.getEmail().strip();
			if(emailAddress.equals(repoEmailAddress)) {
				Logger.error("{} || attempted to add a carrier with the same email as another user.",loggedInUser.getUsername());
				model.addAttribute("message", "Another user already exists with that email.");
				return "/add/add-carrier";
			}
		}

		for(Carriers check: repoCarriers) {
			String repoCarrierName = check.getCarrierName().strip();
			if(carrierName.equals(repoCarrierName)) {
				Logger.error("{} || attempted to save a carrier with the same carrier name as another carrier.",loggedInUser.getUsername());
				model.addAttribute("message", "Another carrier already exists with that carrier name.");
				return "/add/add-carrier";
			}
		}

		for(Carriers check: repoCarriers) {
			String repoScac = check.getScac().strip();
			if(scac.equals(repoScac)) {
				Logger.error("{} || attempted to save a carrier with the same scac as another carrier.",loggedInUser.getUsername());
				model.addAttribute("message", "Another carrier already exists with that scac.");
				return "/add/add-carrier";
			}
		}

		if(!(scac.length() <= 4 && scac.length() >= 2) || !(scac.matches("^[a-zA-Z0-9]+$"))) {
			Logger.error("{} || attempted to add a carrier but the scac was not between 2 and 4 alphanumeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Scac was not between 2 and 4 alphanumeric characters.");
			return "/add/add-carrier";
		}


		if(!(pallets.length() <= 32 && pallets.length() > 0) || !(pallets.matches("^[0-9]+$"))) {
			Logger.error("{} || attempted to add a carrier but the pallets must be between 1 and 32 numeric chracters.",loggedInUser.getUsername());
			model.addAttribute("message", "Pallets must be between 1 and 32 numeric characters.");
			return "/add/add-carrier";
		}

		if(!(weight.length() <= 32 && weight.length() > 0) || !(weight.matches("^[0-9]+$"))) {
			Logger.error("{} || attempted to add a carrier but the weight must be between 1 and 32 numeric characters.",loggedInUser.getUsername());
			model.addAttribute("message", "Weight must be be between 1 and 32 numeric characters.");
			return "/add/add-carrier";
		}


		User result = new User();
		Carriers carrierResult = new Carriers();

		Role role = new Role();

		role.setId(3);


		result.setEmail(emailAddress);
		result.setUsername(username);
		result.setPassword(password);
		result.setAuctioningAllowed(userForm.getAuctioningAllowed());
		result.setEnabled(userForm.isEnabled());
		result.setRole(role);


		carrierResult.setLtl(ltl);
		carrierResult.setFtl(ftl);
		carrierResult.setCarrierName(carrierName);
		carrierResult.setPallets(pallets);
		carrierResult.setScac(scac);
		carrierResult.setWeight(weight);



		carriersRepository.save(carrierResult);
		result.setCarrier(carrierResult);
		userService.save(result);
		Logger.info("{} || successfully saved a carrier with ID {}.", loggedInUser.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
	}

}
