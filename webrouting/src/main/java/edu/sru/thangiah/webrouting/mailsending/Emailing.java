package edu.sru.thangiah.webrouting.mailsending;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;

import edu.sru.thangiah.webrouting.domain.User;


/**
 * Interface class for the verifyAccount, sendVerification,
 * findByVerificationCode and saveVerificationCode methods
 * @author Josh Gearhart	jjg1018@sru.edu
 * @since 9/6/2022
 */

public interface Emailing{
	
	
	 boolean verifyAccount(String verifyCode);
	
	
	 void sendVerification(User user, String webUrl) throws UnsupportedEncodingException, MessagingException;
		

	 User findByVerificationCode(String code);

	 void saveVerificationCode(User user);

	void forgotPassword(String email, String webUrl) throws MessagingException, UnsupportedEncodingException;

	void forgotPasswordAdminFunction(String username, String webUrl);


	MimeMessageHelper setMimeMessage(MimeMessage email,String sender ,String recieverEmail, String heading, String emailBody);

	void alertPreviousEmail(String previousEmail, String webUrl);
	
	void updateUsersEmail(String userEmail, String webUrl, String previousEmail);
	 

}
