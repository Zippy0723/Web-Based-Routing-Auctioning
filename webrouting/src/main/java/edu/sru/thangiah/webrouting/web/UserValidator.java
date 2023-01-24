package edu.sru.thangiah.webrouting.web;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.sru.thangiah.webrouting.domain.Bids;
import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Locations;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.domain.VehicleTypes;
import edu.sru.thangiah.webrouting.domain.Vehicles;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Used to validate a user by checking their username and password.
 * @author Josh Gearhart	jjg1018@sru.edu
 * @since 9/6/2022
 */

public class UserValidator implements Validator {
	
	@Autowired
    private UserService userService;
	
	private boolean equals;
	
	private Pattern pattern;
	
	private Matcher matcher;
	
	

	/**
	 * Constructor for the UserValidator
	 * @param userService - used to set the userService for testing
	 * @author Josh Gearhart 	jjg1018@sru.edu
	 */
	public UserValidator(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Checks if the user class is a valid class.
	 */
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }
    
    /**
     * Checks the email to see if the email is a duplicate
     * @param Object o - Object being validated
     * @param errors - Used to show the errors if they are found
     * @author Josh Gearhart	jjg1018@sru.edu
     */
    public void validateEmail(Object o, Errors errors) {
    	User user = (User) o;
    	
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty", "No email entered!");
    	
		equals = user.getUpdateEmail().equals(user.getEmail());
    	if(equals != true) {
    		if(userService.findByEmail(user.getEmail()) != null) {
    			errors.rejectValue("email", "Duplicate.userForm.email", "This email is already in use");
    			}
        	}
    	if(user.getEmail().length() < 3||user.getEmail().length() > 64) {
        	errors.rejectValue("email", "email.length", "This email is not valid");
        }
    		if(!user.getEmail().contains("@")) {
    			errors.rejectValue("email", "invalid.email", "This email is not a valid email");
    			}
        	
        
    }
    /**
     * Checks the password and confirmPassword to see if they have a valid length
     * Checks to see if the passwords match
     * @param o - Object being validated
     * @param errors - Used to show if the errors if found
     * @author Josh Gearhart	jjg1018@sru.edu
     */
    public void validatePassword(Object o, Errors errors) {
    	User user = (User) o;
    	
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "You Must Enter A Password");
    	if(user.getPassword().length()< 8 || user.getPassword().length() > 32) {
    		errors.rejectValue("password", "Size.userForm.password", "Your password must be between 8 and 32 characters");
    	}
    	
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword","Not Empty", "You Must Re-type Your Password");
    	if(user.getConfirmPassword().length()< 8 || user.getConfirmPassword().length() > 32) {
    		errors.rejectValue("confirmPassword", "Size.userForm.password", "Your password must be between 8 and 32 characters");
    	}
    
    	if(!(user.getPassword().equals(user.getConfirmPassword()))) {
    	errors.rejectValue("password", "MissMatch", "Your Passwords Do Not Match!");
    }
   }
    /**
     * Checks the username and the password to see if the length is valid. <br>
     * Username must be between 6 and 32 characters in length. <br>
     * Username has not been taken. <br>
     * Password must be between 8 and 32 characters in length.
     * @param o Object being validated
     * @param errors Used to store if there are errors
     */
    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "No username entered!");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username", "Username must be between 6 and 32 characters!");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username", "Username already taken!");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "No password entered!");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "Password must be between 8 and 32 characters!");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty", "No email entered!");
        if(userService.findByEmail(user.getEmail()) != null )
        errors.rejectValue("email", "Duplicate.userForm.email", "This email is already in use");
        
        ValidationUtils.rejectIfEmpty(errors, "password", "NotEmpty", "Size.userForm.password");
        if(user.getEmail().length() < 3||user.getEmail().length() > 64) {
        	errors.rejectValue("email", "email.length", "This email is too long");
        }
        if(!user.getEmail().contains("@")) {
        	errors.rejectValue("email", "invalid.email", "This email is not a valid email");
        }
        
    }
    
    /**
     * Checks the username and the password to see if the length is valid. <br>
     * Username must be between 6 and 32 characters in length. <br>
     * Password must be between 8 and 32 characters in length.
     * @param o Object being validated
     * @param errors Used to store if there are errors
     * @author Josh Gearhart 	jjg1018@sru.edu
     */
    public void validateUpdate(Object o, Errors errors) {
    	User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "No password entered!");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "Password must be between 8 and 32 characters!");
        }
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword","Not Empty", "You Must Re-type Your Password");
    	if(user.getConfirmPassword().length()< 8 || user.getConfirmPassword().length() > 32) {
    		errors.rejectValue("confirmPassword", "missmatch.confirmPassword", "Your password does not match");
    	}
        
        equals = user.getUpdateEmail().equals(user.getEmail());
    	if(equals != true) {
    		if(userService.findByEmail(user.getEmail()) != null) {
    			errors.rejectValue("email", "Duplicate.userForm.email", "This email is already in use");
    			}
        	}
    	
    	if(user.getEmail().length() < 3||user.getEmail().length() > 64) {
        	errors.rejectValue("email", "email.length", "This email is not valid");
        }
    	if(!user.getEmail().contains("@")) {
    		errors.rejectValue("email", "invalid.email", "This email is not a valid email");
    	}
    }
      /**
       * Validation function used to check form values before saving to system.
       * @param Object o -  Object being validated
       * @param errors - Errors object used to store if there are errors
       * @author Josh Gearhart	jjg1018@sru.edu
       */
      public void addition(Object o, Errors errors) {
    	  
    	if(o instanceof Shipments) {
    		 pattern = Pattern.compile("(-?[0-9]*[.]{1}[0-9]{7})");
    		Shipments shipment = (Shipments) o;
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "client", "NotEmpty","No client name entered");
    		if(shipment.getClient().length() < 6 || shipment.getClient().length() > 64) {
    			errors.rejectValue("client", "invalid.client", "The length of this client is not valid");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "commodityClass", "NotEmpty","No commodity Class name entered");
    		if(shipment.getCommodityClass().length() > 12 || shipment.getCommodityClass().length() < 1) {
    			errors.rejectValue("commodityClass", "invalid.commodityClass", "This input is invalid");
    		}
    		if(shipment.getCommodityPieces().length() < 1 || shipment.getCommodityPieces().length() > 64) {
    			errors.rejectValue("commodityPieces", "invalid.commodityPieces", "Invalid commodity Pieces");
    		}
    		if(shipment.getCommodityPaidWeight().length() < 1 || shipment.getCommodityPaidWeight().length() > 16) {
    			errors.rejectValue("commodityPaidWeight", "invalid.commodityPaidWeight", "Invalid commodity Paid weight");
    		}
    		
    		if(shipment.getShipperCity().length() < 1 || shipment.getShipperCity().length() > 64) {
    			errors.rejectValue("shipperCity", "invalid.shipperCity", "Invalid City");
    		}
    		
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shipperState", "NotEmpty","No state entered");
    		if(shipment.getShipperState().length() != 2) {
    			errors.rejectValue("shipperState","invalid.shipperState", "Invalid State");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shipperZip", "NotEmpty","No zip  entered");
    		if(shipment.getShipperZip().length() < 5 || shipment.getShipperZip().length() > 10) {
    			errors.rejectValue("shipperZip", "invalid.shipperZip", "This is not a valid zip code");
    		}
    		if(shipment.getShipperLatitude().length() < 1 || shipment.getShipperLatitude().length() > 12 ) {
    			errors.rejectValue("shipperLatitude", "invalid.shipperLatitude", "This is not a valid latitude");
    		}
    		matcher = pattern.matcher(shipment.getShipperLatitude());
    		if(matcher.find() == false) {
    			errors.rejectValue("shipperLatitude", "invalidpattern.shipperLatitude", "Does not match pattern!");
    		}
    		if(shipment.getShipperLongitude().length() < 1 || shipment.getShipperLongitude().length() > 12) {
    			errors.rejectValue("shipperLongitude", "invalid.shipperLongitude", "This is not a valid longitude");
    		}
    		matcher = pattern.matcher(shipment.getShipperLongitude());
    		if(matcher.find()== false) {
    			errors.rejectValue("shipperLongitude","invalidpattern.shipperLongitude", "Does not match pattern!");
    		}
    		if(shipment.getConsigneeCity().length() < 1 || shipment.getConsigneeCity().length() > 64) {
    			errors.rejectValue("consigneeCity", "invald.consigneeCity", "This is not a valid consignee City");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "consigneeState", "NotEmpty","No state  entered");
    		if(shipment.getConsigneeState().length() > 2) {
    			errors.rejectValue("consigneeState", "invalid.consigneeState", "This is not a valid Consignee State");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "consigneeZip", "NotEmpty","No zip  entered");
    		if(shipment.getConsigneeZip().length()< 5 || shipment.getConsigneeZip().length() > 10) {
    			errors.rejectValue("consigneeZip", "invalid.consigneeZip", "This is not a valid zip code");
    		}
    		if(shipment.getConsigneeLatitude().length() < 1 || shipment.getConsigneeLatitude().length() > 12) {
    			errors.rejectValue("consigneeLatitude", "invalid.consigneeLatitude", "Not a valid latitude");
    		} 
    		matcher = pattern.matcher(shipment.getConsigneeLatitude());
    		if(matcher.find()== false) {
    			errors.rejectValue("consigneeLatitude","invalidpattern.consigneeLatitude", "Does not match pattern!");
    		}
    		if(shipment.getConsigneeLongitude().length() < 1 || shipment.getConsigneeLongitude().length() > 12) {
    			errors.rejectValue("consigneeLongitude", "invalid.consigneeLongitude", "Not a valid longitude");
    		}
    		matcher = pattern.matcher(shipment.getConsigneeLongitude());
    		if(matcher.find()== false) {
    			errors.rejectValue("consigneeLongitude","invalidpattern.consigneeLongitude", "Does not match pattern!");
    		}
    	}
    	else if(o instanceof Contacts) {
    		Contacts contact = (Contacts) o;
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty","No first name");
    		if(contact.getFirstName().length() < 3 || contact.getFirstName().length() > 32) {
    			errors.rejectValue("firstName", "invald.firstName", "This is not a valid first Name");
    		}
    		
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty","No last name");
    		if(contact.getLastName().length() < 3 || contact.getLastName().length() > 32) {
    			errors.rejectValue("lastName", "invald.lastName", "This is not a valid last Name");
    		}
    		
    		if(contact.getMiddleInitial().length() > 1) {
    			errors.rejectValue("middleInitial","invalid.middleinit", "please only use an initial");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "NotEmpty", "No Email Entered");
    		if(contact.getEmailAddress().length() < 3 || contact.getEmailAddress().length() > 64) {
    			errors.rejectValue("emailAddress", "invalid.email", "The length of this email is invalid");
    		}
    		if(!contact.getEmailAddress().contains("@")) {
    			errors.rejectValue("emailAddress", "invalid.email", "This email is not valid");
    		}
    	
    		if(contact.getStreetAddress1().length() > 128) {
    			errors.rejectValue("streetAddress1", "invalid.streetAddress", "This is not a valid address");

    		}
    		
    		if(contact.getStreetAddress2().length() > 64) {
    			errors.rejectValue("streetAddress2", "invalid.streetAddress", "Not a valid address");
    		}
    		
    		if(contact.getCity().length() > 64) {
    			errors.rejectValue("city", "invalid.city", "Not a valid city name");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "NotEmpty","No state entered");
    		if(contact.getState().length() != 2 ) {
    		errors.rejectValue("state", "invalid state", "Input your state");
    		}
    		if(contact.getZip().length() > 10) {
    		errors.rejectValue("zip", "invalid.zip", "Not a valid zip code");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "primaryPhone", "NotEmpty", "No primary phone Entered");
    		if(contact.getPrimaryPhone().length() < 10 || contact.getPrimaryPhone().length() > 11 ) {
    			errors.rejectValue("primaryPhone", "invalid.phoneNumber", "This is not a valid phone number");
    		}
    	
    		if(contact.getWorkPhone().length() > 11) {
    			errors.rejectValue("workPhone", "invalid.phoneNumber", "This is not a valid Phone Number");
    		}
    	}
    
    	else if(o instanceof Bids) {
    		Bids bid = (Bids) o;
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty", "You cannot enter an empty bid");
    		if(bid.getPrice().length() > 16) {
    			errors.rejectValue("price", "amount.price", "Thats a lot of money!");
    		}
    		if(bid.getPrice().length()== 1) {
    			errors.rejectValue("price", "amount.price", "You are trying to win the bid.");
    		}
    	}
    	else if(o instanceof Locations) {
    		Locations location = (Locations) o;
    		 pattern = Pattern.compile("(-?[0-9]*[.]{1}[0-9]{7})");
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty", "You must name the location");
    		if(location.getName().length() > 32) {
    			errors.rejectValue("name", "invalid.name", "invalid input");
    		}
    		if(location.getStreetAddress1().length() > 128) {
    			errors.rejectValue("streetAddress1", "invalid.StreetAddress", "Invalid Street Address");
    		}
    		
    		if(location.getStreetAddress2().length() > 64) {
    			errors.rejectValue("streetAddress2", "invalid.StreetAddress2", "This input is too long");
    		}
    		if(location.getCity().length() > 64) {
    			errors.rejectValue("city", "invalid.city", "This city name is not valid");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "NotEmpty","No state entered");
    		if(location.getState().length() != 2) {
    			errors.rejectValue("state", "invalid.state", "Input your state");
    		}
    		if(location.getZip().length() < 5 || location.getZip().length() > 10) {
    			errors.rejectValue("zip", "invalid.zip", "Invalid zip code");
    		}
    		if(location.getLocationType().length() > 64) {
    			errors.rejectValue("locationType", "invalidLocationType", "Not a valid location type");
    		}
    		if(location.getLongitude().length() < 1 || location.getLongitude().length() > 12) {
    			errors.rejectValue("longitude", "invalidlongitude", "This is not a valid longitude");
    		}
    		if(location.getLatitude().length() < 1 || location.getLatitude().length() > 12) {
    			errors.rejectValue("latitude", "invalidLatitude", "This is not a valid latitude");
    		}
    		matcher = pattern.matcher(location.getLatitude());
    		if(matcher.find()== false) {
    			errors.rejectValue("latitude","invalidpattern.latitude", "Does not match pattern!");
    		}
    		matcher = pattern.matcher(location.getLongitude());
    		if(matcher.find()== false) {
    			errors.rejectValue("longitude","invalidpattern.longitude", "Does not match pattern!");
    		}
    	}
    	else if(o instanceof Vehicles) {
    		Vehicles vehicle = (Vehicles) o;
    		int checkVin;
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plateNumber", "NotEmpty", "You must Enter the license Plate Number");
    		if(vehicle.getPlateNumber().length() < 6 || vehicle.getPlateNumber().length() > 7) {
    			errors.rejectValue("plateNumber","invalid.plateNumber","plate numbers are between 6 and 7 characters");
    		}
    		checkVin = Integer.parseInt(vehicle.getManufacturedYear());
    		if(checkVin <= 1981) {
    			if(vehicle.getVinNumber().length() < 5 || vehicle.getVinNumber().length() > 17) {
    				errors.rejectValue("vinNumber", "invalid.vinNumber", "Invalid Vin Number");
    			}
    		}
    		else if(vehicle.getVinNumber().length() != 17) {
    			errors.rejectValue("vinNumber", "invalid.vinNumber", "Invalid Vin Number");
    		}
    		if(vehicle.getManufacturedYear().length() != 4){
    			errors.rejectValue("manufacturedYear","invalid.Year", "Please enter the whole year: yyyy");
    		}
    		
    	}
    	else if(o instanceof VehicleTypes) {
    		pattern = Pattern.compile("\\w\\s?");
    		VehicleTypes vehicleType = (VehicleTypes) o;
    		
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors,"type", "NotEmpty","You must enter the vehicle type");
    		if(vehicleType.getType().length() > 32) {
    			errors.rejectValue("type","invalid.type" ,"Not Valid Input");
    		}
    		matcher = pattern.matcher(vehicleType.getType());
    		if(matcher.find() == false) {
    			errors.rejectValue("type", "characters.type", "Please only use numbers and letters");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subType", "NotEmpty", "You must enter the vehicle subType");
    		if(vehicleType.getSubType().length() > 32) {
    			errors.rejectValue("subType","invalid.subType", "Not valid Input");
    		}
    		
    		if(vehicleType.getSubType().length() > 64) {
    			errors.rejectValue("description", "invalid.description", "This is too much describing");
    		}
    		
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "make", "NotEmpty", "This field cannot be empty");
    		if(vehicleType.getMake().length() < 3 || vehicleType.getMake().length() > 32) {
    			errors.rejectValue("make", "invalid.make", "Invalid make");
    		}
    		matcher = pattern.matcher(vehicleType.getMake());
    		if(matcher.find()== false) {
    			errors.rejectValue("make","character.make","Please only use numbers and letters");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "model", "NotEmpty", "This field cannot be empty");
    		if(vehicleType.getModel().length() < 3 || vehicleType.getModel().length() > 32) {
    			errors.rejectValue("model", "invalid.model", "Invalid mode;");
    		}
    		matcher = pattern.matcher(vehicleType.getMake());
    		if(matcher.find()== false) {
    			errors.rejectValue("model","character.model","Please only use numbers and letters");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "minimumWeight", "NotEmpty", "This field cannot be empty");
    		if(vehicleType.getMinimumWeight() > 500000) {
    			errors.rejectValue("minimumWeight", "invalid.minimumWeight", "This is minimum weight");
    		}
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maximumWeight", "NotEmpty", "This field cannot be empty");
    		if(vehicleType.getMaximumWeight() < 250 || vehicleType.getMaximumWeight() > 500000) {
    			errors.rejectValue("maximumWeight", "invalidMaxWeight", "Invalid Maximum weight");
    		}
    		
    		if(vehicleType.getCapacity().length() > 16) {
    			errors.rejectValue("capacity", "invalid.capacity", "This capacity is invalid");
    		}
    	
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maximumRange", "NotEmpty", "This field cannot be empty");
    		if(vehicleType.getMaximumRange() > 900000) {
    			errors.rejectValue("maximumRange", "invalid.maxRange", "This field should not be this large");
    		}
    		
    	}
      }
         
}
