package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.ContactsRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;
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
    private SecurityService securityService;
	
	private ContactsRepository contactsRepository;
	
	@Autowired
    private UserValidator userValidator;
	
	private static final Logger Logger = LoggerFactory.getLogger(ContactsController.class);
	
	
	/**
	 * Constructor for ContactsController. <br>
	 * Instantiates the contactsRepository
	 * @param contactsRepository Used to interact with Contacts in the database
	 */
	public ContactsController(ContactsRepository contactsRepository) {
		this.contactsRepository = contactsRepository;
	}
	
	/**
	 * Adds all of the contacts to the "contacts" model and redirects user to
	 * the contacts page.
	 * @param model Used to add data to the model
	 * @return "contacts"
	 */
	@RequestMapping({"/contacts"})
    public String showContactList(Model model) {
        model.addAttribute("contacts", getLoggedInUser().getCarrier().getContacts());
        return "contacts";
    }
    

	/**
	 * Redirects user to the /add/add-contact page
	 * @param model Used to add data to the model
	 * @param contact Stores information on the contact to be added
	 * @param result Ensures inputs from the user are valid
	 * @return "/add/add-contact"
	 */
  	@RequestMapping({"/signupcontact"})
      public String showContactSignUpForm(Model model, Contacts contact, BindingResult result) {
          return "/add/add-contact";
    }
      
  	/**
  	 * Adds a contact to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the contact is saved in the contactsRepository. and the user is redirect to /contacts <br>
  	 * If there are errors, the user is redirected to the /add/add-contact page.
  	 * @param contacts Stores information on the contact to be added
  	 * @param result Checks user inputs to ensure they are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/contacts" or "/add/add-contact"
  	 */
  	@RequestMapping({"/addcontact"})
  	public String addContact(@Validated Contacts contacts, BindingResult result, Model model) {
  		userValidator.addition(contacts, result);
  		contacts.setCarrier(getLoggedInUser().getCarrier());
  		User user = getLoggedInUser();
  		
  		if (result.hasErrors()) {
  			return "/add/add-contact";
		}
  		
  		Boolean deny = false;
  		List<Contacts> checkContacts = new ArrayList<>();
  		checkContacts = (List<Contacts>) contactsRepository.findAll();
  		
  		for(Contacts check: checkContacts) {
  			if(contacts.getEmailAddress().toString().equals(check.getEmailAddress().toString())) {
  				deny = true;
  				break;
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Contact. Contact Email already in use");
  			Logger.error("{} attempted to add contact and it failed because the email address {} is already in use.", user.getUsername(), contacts.getEmailAddress().toString());
  			model.addAttribute("contacts", getLoggedInUser().getCarrier().getContacts());
  			return "contacts";
			 
  		}
  		
  		contactsRepository.save(contacts);
  		Logger.info("{} successfully added a new contact with ID {}.", user.getUsername(), contacts.getId());
  		
  		return "redirect:/contacts";
  	}
  	
  	/**
  	 * Finds a contact using the id parameter and if found, redirects user to delete confirmation page
  	 * Checks if dependencies are empty before deleting it.
  	 * @param id Stores the ID of the contact to be deleted
  	 * @param model Used to add data to the model
  	 * @return "contacts" or "/delete/deletecontactconfirm"
  	 */
  	@GetMapping("/deletecontact/{id}")
    public String deleteContact(@PathVariable("id") long id, Model model) {
        Contacts contacts = contactsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        
        User user = getLoggedInUser();
        if(!contacts.getDrivers().isEmpty() || !contacts.getTechnicians().isEmpty()) {
        	model.addAttribute("error", "Unable to delete due to dependency conflict."); 
        	Logger.error("{} attmpted to delete contact. Deletion failed due to dependency conflict.", user.getUsername());
        	model.addAttribute("contacts", getLoggedInUser().getCarrier().getContacts());
        	return "contacts";
        }
        model.addAttribute("contacts", contacts);
    	return "/delete/deletecontactconfirm";
    }
  	
  	/**
  	 * Finds a contact using the id parameter and if found, deletes the contact and redirects to contacts page
  	 * @param id ID of the contact being deleted
  	 * @param model Used to add data to the model
  	 * @return "redirect:/contacts"
  	 */
  	@GetMapping("/deletecontactconfirmation/{id}")
    public String deleteContactConfirmation(@PathVariable("id") long id, Model model) {
  		Contacts contacts = contactsRepository.findById(id)
  	          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
  		
  		User user = getLoggedInUser();
  		Logger.info("{} successfully deleted the contact with ID {}.", user.getUsername(), contacts.getId());
        contactsRepository.delete(contacts);
        return "redirect:/contacts";
    }
  	
  	/**
  	 * Finds a contact using the id parameter and if found, adds the details of that contact
  	 * to the view contact page
  	 * @param id Stores the ID of the contact to be viewed
  	 * @param model Used to add data to the model
  	 * @return "/view/view-contact"
  	 */
  	@GetMapping("/viewcontact/{id}")
    public String viewContact(@PathVariable("id") long id, Model model) {
        Contacts contacts = contactsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        
        model.addAttribute("contacts", contacts);
        return "contacts";
    }
  	
  	/**
  	 * Finds a contact using the id parameter and if found, adds the details of that contact
  	 * to a form and redirects the user to that update form.
  	 * @param id Stores the ID of the contact to be edited
  	 * @param model Used to add data to the model
  	 * @return "update/update-contact"
  	 */
  	@GetMapping("/editcontact/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Contacts contacts = contactsRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
        
        model.addAttribute("contacts", contacts);
        return "/update/update-contact";
    }
  	
  	/**
  	 * Updates a contact to the database. Checks if there are errors in the form. <br>
  	 * If there are no errors, the contact is updated in the contactsRepository. and the user is redirected to /contacts <br>
  	 * If there are errors, the user is redirected to the /update/update-contact page.
  	 * @param id Stores the ID of the contact to be updated
  	 * @param contact Stores information on the contact that will be updated
  	 * @param result Checks user inputs to ensure they are valid
  	 * @param model Used to add data to the model
  	 * @return "redirect:/contacts"
  	 */
  	@PostMapping("/updatecontact/{id}")
    public String updateContact(@PathVariable("id") long id, @Validated Contacts contact, 
      BindingResult result, Model model) {
  		userValidator.addition(contact, result);
  		contact.setCarrier(getLoggedInUser().getCarrier());
  		User user = getLoggedInUser();
  		
  		if (result.hasErrors()) {
            contact.setId(id);
            return "/update/update-contact";
        }
        
        Boolean deny = false;
  		List<Contacts> checkContacts = new ArrayList<>();
  		checkContacts = (List<Contacts>) contactsRepository.findAll();
  		
  		for(Contacts check: checkContacts) {
  			if(contact.getEmailAddress().toString().equals(check.getEmailAddress().toString()) && contact.getId() != check.getId()) {
  				deny = true;
  				break;
  	  		}
  		}
  		
  		if(deny == true) {
  			model.addAttribute("error", "Unable to add Contact. Contact Email already in use");
  			Logger.error("{} failed to add contact with email {} because the email is already in use.",user.getUsername(), contact.getEmailAddress());
  			model.addAttribute("contacts", getLoggedInUser().getCarrier().getContacts());
  			return "contacts";
			 
  		}
            
        contactsRepository.save(contact);
        Logger.info("{} successfully updated the contact with ID {}.", user.getUsername(), contact.getId());
        return "redirect:/contacts";
    }
  	
  	/**
  	 * Uploads an excel file containing new contact info
  	 * @param model Used to add data to the model 
  	 * @return "/uploadcontacts" 
  	 */
	@GetMapping("/uploadcontacts")
	public String ListFromExcelData(Model model){
		return "/uploads/uploadcontacts";	
	}
	
	/**
  	 * Reads an excel file containing contact information and adds it to the contacts repository. <br>
  	 * After the file is uploaded and added to the database, user is redirected to the contacts page
  	 * @param excelData Excel file that is being added to the database
  	 * @return "redirect:contacts/"
	 * @throws AccessException 
  	 */
	@SuppressWarnings("unused")
	@PostMapping("/upload-contact")
	public String LoadFromExcelData(@RequestParam("file") MultipartFile excelData) throws AccessException{
		XSSFWorkbook workbook;
		try {
			User user = getLoggedInUser();
			workbook = new XSSFWorkbook(excelData.getInputStream());
	
		
			XSSFSheet worksheet = workbook.getSheetAt(0);
			List<Contacts> contactsList;
			contactsList = (List<Contacts>) contactsRepository.findAll();
			
			for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
				 
				Contacts contact = new Contacts();
		        XSSFRow row = worksheet.getRow(i);
		        
		        if(row.getCell(0).getStringCellValue().isEmpty() || row.getCell(0)== null ) {
		        	break;
		        }
		        
		        
		        List<String> states = Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
				List<String> stateAbbreviations = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");
		        
	    		
	    		String firstName = row.getCell(0).toString();
			    String lastName = row.getCell(1).toString();
			    String middleInitial = row.getCell(2).toString();
	    		String emailAddress = row.getCell(3).toString();
	    		String streetAddress1 = row.getCell(4).toString();
	    		String streetAddress2 = row.getCell(5).toString();
	    		String city = row.getCell(6).toString();
	    		String state = row.getCell(7).toString();
	    		String zip = row.getCell(8).toString();
	    		String primaryPhone = row.getCell(9).toString();
	    		String workPhone = row.getCell(10).toString();
	   
	    		
		    	
	    		
	    		if (!(firstName.length() < 32 && firstName.length() > 0) || (firstName.matches("^[A-Z][a-z]*$"))) {
	    			workbook.close();
	    			Logger.info("Contact first name field must be between 0 and 32 characters and alphabetic.");
	    			continue;
	    		}
	    		
	    		if(!(lastName.length() < 32 && firstName.length() > 0) || (lastName.matches("^[A-Za-z][A-Za-z]$"))) {
	    			workbook.close();
	    			Logger.info("Contact last name field must be between 0 and 32 characters and alphbetic");
	    			continue;
	    		}
	    		
	    		if(!(middleInitial.length() < 16 && middleInitial.length() > 0) || (middleInitial.matches("^[A-Z]\\.$"))) {
	    			workbook.close();
	    			Logger.info("Contact Middle initial must be 1 character and alphabetic.");
	    			continue;
	    		}
	    		
	    		if(!(emailAddress.length() < 64 && emailAddress.length() > 0) || (emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){
	    			workbook.close();
	    			Logger.info("Contact email address must be between 0 and 64 characters that are alpahnumeric.");
	    			continue;
	    		}
	    		
	    		if(!(streetAddress1.length() < 64 && streetAddress1.length() > 0) || !(streetAddress1.matches("\\d+\\s+([a-zA-Z]+\\s?)+"))) {
	    			workbook.close();
	    			Logger.info("Contact street address must be between 0 and 128 characters that are alphanumeric.");
	    			continue;
	    		}
	    		
	    		if(!(streetAddress2.length() < 64 && streetAddress2.length() > 0) || (streetAddress2.matches("\\d+\\s+([a-zA-Z]+\\s?)+"))) {
	    			workbook.close();
	    			Logger.info("Contact street address 2 must be between 0 and 64 characters that are alphanumeric.");
	    			continue;
	    		}
	    		
	    		if(!(city.length() < 64 && city.length() > 0) || (city.matches("^[a-zA-Z]+$"))) {
	    			workbook.close();
	    			Logger.info("Shipper City must be between 0 and 64 characters and is alphabetic.");
	    			continue;
	    		}
	    		
	    		if(!(states.contains(state) || stateAbbreviations.contains(state))) {
	    			workbook.close();
	    			Logger.info("Contact state must be a state or state abbreviation.");
	    			continue;
	    		}
	    		
	    		if(!(zip.length() < 12 && zip.length() > 0) || !(zip.matches("^[0-9.]+$"))){
	    			workbook.close();
	    			Logger.info("Contact Zip must be between 0 and 12 characters and is numeric.");
	    			continue;
	    		}
	    		
	    		if(!(primaryPhone.length() < 13 && primaryPhone.length() > 0) || !(primaryPhone.matches("\\d{3}-\\d{3}-\\d{4}"))){
	    			workbook.close();
	    			Logger.info("Contact primary phone must be between 0 and 12 characters and is numeric.");
	    			continue;
	    		}
	    		
	    		if(!(workPhone.length() < 13 && workPhone.length() > 0) || !(workPhone.matches("\\d{3}-\\d{3}-\\d{4}"))){
	    			workbook.close();
	    			Logger.info("Contact work phone must be between 0 and 12 characters and is numeric.");
	    			continue;
	    		}
	    	 
	    
	    		contact.setFirstName(firstName);
	    		contact.setLastName(lastName);
	    		contact.setMiddleInitial(middleInitial);
	    		contact.setEmailAddress(emailAddress);
	    		contact.setStreetAddress1(streetAddress1);
	    		contact.setStreetAddress2(streetAddress2);
	    		contact.setCity(city);
	    		contact.setState(state);
	    		contact.setZip(zip);
	    		contact.setPrimaryPhone(primaryPhone);
	    		contact.setWorkPhone(workPhone);
		    	
	    	
		        contactsRepository.save(contact);
		        Logger.info("{} successfully saved contact with ID {}.", user.getUsername(), contact.getId());
			 		
			 }
			 
			 workbook.close();
		 
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/contacts";
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
