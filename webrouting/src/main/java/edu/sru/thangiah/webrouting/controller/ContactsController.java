package edu.sru.thangiah.webrouting.controller;

import java.util.ArrayList;
import java.util.Hashtable;
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

import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.ContactsRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
import edu.sru.thangiah.webrouting.services.ValidationServiceImp;
import edu.sru.thangiah.webrouting.web.UserValidator;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with contacts.
 * @author Ian Black		img1007@sru.edu
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/1/2022
 */

@Controller
public class ContactsController {

	@Autowired
	private UserService userService;

	@Autowired
	private ValidationServiceImp validationServiceImp;

	private ContactsRepository contactsRepository;

	@Autowired
	private UserValidator userValidator;

	private static final Logger Logger = LoggerFactory.getLogger(ContactsController.class);


	/**
	 * Constructor for ContactsController
	 * @param contactsRepository Instantiates the contacts Repository
	 */
	
	public ContactsController(ContactsRepository contactsRepository) {
		this.contactsRepository = contactsRepository;
	}

	/**
	 * Adds all of the contacts to the "contacts" model and redirects user to the contacts page
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /contacts
	 */
	@RequestMapping({"/contacts"})
	public String showContactList(Model model, HttpSession session) {


		try {
			model.addAttribute("error",session.getAttribute("error"));
		} catch(Exception e){
			//do nothing
		}
		session.removeAttribute("error");
		try {
			model.addAttribute("successMessage",session.getAttribute("successMessage"));
		} catch (Exception e) {
			//do nothing
		}
		session.removeAttribute("successMessage");

		String redirectLocation = "/contacts";
		session.setAttribute("redirectLocation", redirectLocation);
		model.addAttribute("redirectLocation", redirectLocation);
		model.addAttribute("currentPage","/contacts");

		model.addAttribute("contacts", userService.getLoggedInUser().getCarrier().getContacts());

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "contacts";
	}


	/**
	 * Redirects user to the /uploadcontacts page when clicking "Upload an excel file" button in the contacts section of Carrier login
	 * @param model used to add data to the model
	 * @return /uploadcontacts
	 */

	@RequestMapping({"/uploadcontacts"})
	public String showAddContactsExcel(Model model) {
		model.addAttribute("currentPage","/contacts");
		return "/uploadcontacts";
	}

	/**
	 * Finds a contact using the id parameter and if found, redirects user to delete confirmation page
	 * Checks if dependencies are empty before deleting it.
	 * @param id  of the contact to be deleted
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /contacts or /delete/deletecontactconfirm
	 */
	@GetMapping("/deletecontact/{id}")
	public String deleteContact(@PathVariable("id") long id, Model model, HttpSession session) {
		Contacts contacts = contactsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/contacts");

		if(!contacts.getDrivers().isEmpty() || !contacts.getTechnicians().isEmpty()) {
			session.setAttribute("error", "Unable to delete due to dependency conflict."); 
			Logger.error("{} || attmpted to delete contact. Deletion failed due to dependency conflict.", user.getUsername());
			model.addAttribute("contacts", userService.getLoggedInUser().getCarrier().getContacts());

			return "redirect:" + (String) session.getAttribute("redirectLocation");
		}

		model.addAttribute("contacts", contacts);

		return "/delete/deletecontactconfirm";
	}

	/**
	 * Finds a contact using the id parameter and if found, deletes the contact and redirects to contacts page
	 * @param id of the contact being deleted
	 * @param model used to load attributes into the Thymeleaf model
	 * @return redirects to /contacts
	 */
	@GetMapping("/deletecontactconfirmation/{id}")
	public String deleteContactConfirmation(@PathVariable("id") long id, Model model) {
		Contacts contacts = contactsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/contacts");
		Logger.info("{} || successfully deleted the contact with ID {}.", user.getUsername(), contacts.getId());

		contactsRepository.delete(contacts);
		return "redirect:/contacts";
	}

	/**
	 * Adds the required attributes to the model to render the contacts page
	 * @param id Stores the ID of the contact to be viewed
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /contacts
	 */
	@GetMapping("/viewcontact/{id}")
	public String viewContact(@PathVariable("id") long id, Model model, HttpSession session) {

		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		Contacts contacts = contactsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage","/contacts");

		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		return "contacts";
	}

	/**
	 * Adds the required attributes to the model to render the edit contact page
	 * @param id of the contact being edited
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /edit/edit-contacts
	 */
	
	@GetMapping("/editcontact/{id}")
	public String showContactsEditForm(@PathVariable("id") long id, Model model, HttpSession session) {
		Contacts contacts = contactsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));


		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/contacts");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		session.removeAttribute("message");

		model.addAttribute("contacts", contacts);

		return "/edit/edit-contacts";
	}

	/**
	 * Recives a new contact object from the user
	 * Validates the contact and then saves it to the contacts repository
	 * @param id of the contact being edited
	 * @param contacts holds the new contact information
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /edit/edit-contacts or redirects to /contacts
	 */

	@PostMapping("edit-contact/{id}")
	public String contactsUpdateForm(@PathVariable("id") long id, Contacts contacts, Model model, HttpSession session) {
		String redirectLocation = (String) session.getAttribute("redirectLocation");
		model.addAttribute("redirectLocation", session.getAttribute("redirectLocation"));
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		List<Contacts> carrierContacts = user.getCarrier().getContacts();

		model.addAttribute("contacts", contacts);


		Hashtable<String, String> hashtable = new Hashtable<>();

		hashtable.put("firstName", contacts.getFirstName().strip());
		hashtable.put("lastName", contacts.getLastName().strip());
		hashtable.put("middleInitial", contacts.getMiddleInitial().strip());
		hashtable.put("emailAddress", contacts.getEmailAddress().strip());
		hashtable.put("streetAddress1", contacts.getStreetAddress1().strip());
		hashtable.put("streetAddress2", contacts.getStreetAddress2().strip());
		hashtable.put("contactCity", contacts.getCity().strip()); 
		hashtable.put("contactState", contacts.getState().strip());
		hashtable.put("contactZip", contacts.getZip().strip());
		hashtable.put("primaryPhone", contacts.getPrimaryPhone().strip());
		hashtable.put("workPhone", contacts.getWorkPhone().strip());

		Contacts result;

		result = validationServiceImp.validateContactForm(hashtable, session);


		if (result == null) {
			model.addAttribute("message", session.getAttribute("message"));
			return "/edit/edit-contacts";
		}

		String fullName = result.getFirstName() + " " + result.getLastName();


		result.setId(id);

		for(Contacts check: carrierContacts) {
			String carrierContactsFullName = check.getFirstName() + " " + check.getLastName();
			if(fullName.equals(carrierContactsFullName) && (result.getId() != check.getId())) {
				Logger.info("{} || attempted to save a contact with the same name as an existing contact.",user.getUsername());
				model.addAttribute("message", "Another contact exists with that name.");
				return "/edit/edit-contacts";
			}
		}

		contactsRepository.save(result);
		Logger.info("{} || successfully updated contact with ID {}.", user.getUsername(), result.getId());

		return "redirect:" + redirectLocation;
	}
	
	/**
	 * Adds all of the required attributes to the model to render the add contact page
	 * @param contact holds the new contact being added to the model
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /add/add-contact
	 */
	@GetMapping("/add-contact")
	public String showContactAddForm(Contacts contact,Model model, HttpSession session) {
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/contacts");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		session.removeAttribute("message");
		model.addAttribute("contactForm", new Contacts());

		return "/add/add-contact";
	}

	/**
	 * Receives a contact object by the user and passes it off for validation
	 * Once valid it is saved to the contact repository
	 * @param contact holds the new contact being created by the user
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /add/add-contact
	 */

	@PostMapping("submit-add-contact")
	public String contactAddForm(@ModelAttribute("contactForm") Contacts contact, Model model, HttpSession session) {
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/contacts");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

		Hashtable<String, String> hashtable = new Hashtable<>();

		hashtable.put("firstName", contact.getFirstName().strip());
		hashtable.put("lastName", contact.getLastName().strip());
		hashtable.put("middleInitial", contact.getMiddleInitial().strip());
		hashtable.put("emailAddress", contact.getEmailAddress().strip());
		hashtable.put("streetAddress1", contact.getStreetAddress1().strip());
		hashtable.put("streetAddress2", contact.getStreetAddress2().strip());
		hashtable.put("contactCity", contact.getCity().strip()); 
		hashtable.put("contactState", contact.getState().strip());
		hashtable.put("contactZip", contact.getZip().strip());
		hashtable.put("primaryPhone", contact.getPrimaryPhone().strip());
		hashtable.put("workPhone", contact.getWorkPhone().strip());

		Contacts result;

		result = validationServiceImp.validateContact(hashtable, session);
		
		
		if (result == null) {
			Logger.error("{} || attempted to add a new Contact but "+ session.getAttribute("message") ,user.getUsername());
			model.addAttribute("message", session.getAttribute("message"));
			return "/add/add-contact";
		}

		contactsRepository.save(result);
		Logger.info("{} || successfully added a new Contact with ID {}.", user.getUsername(), result.getId());

		return "redirect:" + (String) session.getAttribute("redirectLocation");
	}


}
