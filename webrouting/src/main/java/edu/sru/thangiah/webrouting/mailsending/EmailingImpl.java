package edu.sru.thangiah.webrouting.mailsending;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.services.UserService;
import net.bytebuddy.utility.RandomString;


/**
 * Implements Emailing class 
 * @author Josh Gearhart	jjg1018@sru.edu
 * @since 9/6/2022
 */
@Service
public class EmailingImpl implements Emailing {
	
	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private JavaMailSender sendMail;
    
    @Autowired
    private UserService userService;
    
    private String verificationCode;
    
    private String heading;
    
	private String sender;
	
	private String emailBody;
	
	private String verifyingUrl; 
	
	private MimeMessage email;
	
	private MimeMessageHelper helping;
	
	private User user;
	

    
  

	public EmailingImpl(UserRepository userRepository, JavaMailSender sendMail) {
		this.userRepository = userRepository;
		this.sendMail = sendMail;
	}

	/**
     * Method for setting the verification code for the user 
     * As well as sets their status to disabled
     * @param User user is getting verification code made and disabled
     */
	@Override
    public void saveVerificationCode(User user) {
		user.setEnabled(false);
		verificationCode= RandomString.make(64);
		user.setVerificationCode(verificationCode);
	    this.userRepository.save(user);
	}
	
	/**
	 * Method for sending an email to the user, also formats email
	 * @param user User who is receiving the email
	 * @param webUrl for the verification code to take back to
	 * @throws UnsupportedEncodingException 
	 * @throws MessagingException 
	 */
	@Override
	@Async
	public void sendVerification(User user, String webUrl) {
		heading = "Verify your Account!";
		sender = "Enterprise Routing Admin";
		verifyingUrl = webUrl + "/verify?code=" + user.getVerificationCode();
		emailBody = "<p> Please verify your account </p>" +
				 "<p>Click the link to verify your account and gain access</p>" +
				"<a href=\""+ verifyingUrl +"\">Verify Your Account</a>" +
				 "<p><b>Thank you for registering to our service</b><p>";
		email = sendMail.createMimeMessage();
		setMimeMessage(email,sender,user.getEmail(),heading,emailBody);
		sendMail.send(email);
	}
	/**
	 * Security function for handling account verification, if account has been verified then it returns false
	 * else it will verify the account
	 * @param verifycode to find the user.
	 * @return true or false
	 */
	
	@Override
	public boolean verifyAccount(String verifyCode) {
		user = new User();
		user = userRepository.findByVerificationCode(verifyCode);
		if(user == null || user.isEnabled()) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepository.save(user);
			return true;
		}
	}
	
	/**
	 * Finds the verification code and returns the user for specified code
	 * @param verification for finding the user
	 * @return User
	 */
	@Override
	public User findByVerificationCode(String verificationCode) {
		return userRepository.findByVerificationCode(verificationCode);
	}
	
	/**
	 * Helper function for sending emails
	 * @param email - MimeMessage used to send the email
	 * @param sender - Person sending the email
	 * @param recieverEmail - Person getting the email
	 * @param heading - Heading of the email
	 * @param emailBody - The content of the email
	 * @return helping - Message helper that will set the mime message properties
	 */
	public MimeMessageHelper setMimeMessage(MimeMessage email, String sender, String recieverEmail, String heading, String emailBody) {
		helping = new MimeMessageHelper(email);
		try {
			helping.setFrom("joshuajgearhart@gmail.com", sender);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			helping.setTo(recieverEmail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			helping.setSubject(heading);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			helping.setText(emailBody, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return helping;
	}
	/**
	 * Method for sending the forgotpassword email to a user, sends the link with context path, and the users OTP code
	 * @param userEmail - used to find the user by their email 
	 * @param webUrl - used to create the context path of the application
	 */
	@Override
	@Async
	public void forgotPassword(String userEmail, String webUrl)  {
	user = userService.findByEmail(userEmail);
	heading = "Password Reset";
	sender = "Enterprise Routing Admin";
	verifyingUrl = webUrl + "/resetpassword?code=" + user.getOtpCode();
	emailBody = "<p> A request to reset your password has been made </p>" +
			 "<p>Click the link to reset your password </p>" +
			"<a href=\""+ verifyingUrl +"\">Reset Password</a>" +
			 "<p><b>If you have not made this request or remembered your password you may ignore this email</b><p>";
	email = sendMail.createMimeMessage();
	setMimeMessage(email,sender,user.getEmail(),heading,emailBody);
	sendMail.send(email);
	}
	
	/**
	 * Method for an admin sending the forgot password email to a user.
	 * @param String username - the username of the user who is having their password reset
	 * @param String webUrl - used to create the context path of the application
	 */
	@Async
	public void forgotPasswordAdminFunction(String username, String webUrl)  {
		user = userService.findByUsername(username);
		heading = "Password Reset";
		sender = "Enterprise Routing Admin";
		verifyingUrl = webUrl + "/resetpassword?code=" + user.getOtpCode();
		emailBody = "<p> An Admin has requested to reset your password </p>" +
				 "<p>Click the link to reset your password </p>" +
				"<a href=\""+ verifyingUrl +"\">Reset Password</a>" +
				 "<p><b>If you have not made this request or remembered your password you may ignore this email</b><p>";
		email = sendMail.createMimeMessage();
		setMimeMessage(email,sender,user.getEmail(),heading,emailBody);
		sendMail.send(email);
	}
	/**
	 * Sends a message to the users old email address to alert of the change
	 * @param previousEmail - Users past email 
	 * @param webUrl - the url of the host
	 */
	@Async
	public void alertPreviousEmail(String previousEmail, String webUrl) {
		heading = "Email Changed!";
		sender = "Enterprise Routing Admin";
		emailBody = "<p>Changes have been made to your email!</p>" +
				 "<p><b>If you have not made this change please contact an admin!</b><p>";
		email = sendMail.createMimeMessage();
		setMimeMessage(email,sender,previousEmail,heading,emailBody);
		sendMail.send(email);
	}
	
	/**
	 * Function for when a user updates their email address
	 * @param userEmail - users new email
	 * @param webUrl - the url of the host
	 * @param previousEmail - users old email
	 */
	@Async 
	public void updateUsersEmail(String userEmail, String webUrl, String previousEmail) {
		user = userService.findByEmail(userEmail);
		heading = "Email Changed!";
		sender = "Enterprise Routing Admin";
		verifyingUrl = webUrl + "/verify?code=" + user.getVerificationCode();
		emailBody = "<p> You have changed your email !</p>" +
				 "<p>Click the link to re-verify your account </p>" +
				"<a href=\""+ verifyingUrl +"\">Verify Account</a>" ;
		email = sendMail.createMimeMessage();
		setMimeMessage(email,sender,user.getEmail(),heading,emailBody);
		sendMail.send(email);
		alertPreviousEmail(previousEmail, webUrl);
	}

	
}
