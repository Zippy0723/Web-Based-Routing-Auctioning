package edu.sru.thangiah.webrouting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.thangiah.webrouting.mailsending.Emailing;

/**
 * Handles the url sent to a user when registering for an account
 * @author Josh Gearhart	jjg1018@sru.edu	
 * @since 9/6/2022
 */

@Controller
public class VerifyEmailController {
	
	@Autowired
    private Emailing emailImpl;


/**
* Gets the mapping sent from the email link
* Requests the parameter from the URL 
* uses the requested parameter to verify the account
* @param verificationCode holds the verificationcode of the user sent in the URL
* @return "verified" if the account is successfully verified or "verificationfail" if the account is already verified or the code is wrong
*/
	
@GetMapping("/verify")
public String verifyAccount(@RequestParam(required = true, value="code") String verificationCode) {
	if(emailImpl.verifyAccount(verificationCode)) {
		return "verified";
		
	} else
	return "verificationfail";
}

@GetMapping("/verified")
public void verifiedppage() {
	
}

@GetMapping("/verificationfail")
public void verifiedFailed() {
	
}


}
