package edu.sru.thangiah.webrouting.services;



import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import net.bytebuddy.utility.RandomString;


/*
 * UserServiceImpl
 * 
 * Created 2/7/2022 by Logan Kirkwood
 * 
 * Description: Implements the UserService Interface
 * Used as a service for Spring Security
 */

/**
 * Implements the UserService Interface <br>
 * Used as a service for Spring Security
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    
	private String otpCode;
	
	
	/**
	 * Constructor for the UserServiceImpl Class
	 * @param userRepository - used to instantiate the user Repository
	 * @author Josh Gearhart 	jjg1018@sru.edu
	 */
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
     * Saves the user in the userRepository and encodes the password <br>
     * The password is encrypted using the BCryptPasswordEncoder
     * @param user User being saved
     * @throws MessagingException 
     * @throws UnsupportedEncodingException 
     */
	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    userRepository.save(user);
	}
	
	  /**
     * finds the user in the database by searching the username
     * @param username for the User being found
     * @return the user found
     */
	@Override
	public User findByUsername(String username) {
			return userRepository.findByUsername(username);		
	}
	
	/**
	 * Finds the user in the database by searching the otpcode
	 * @param otpCode - for the user being found
	 * @return the user found
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Override
	public User findByOtp(String otpCode) {
			return userRepository.findByOtpCode(otpCode);
	}
	
	
	/**
	 * Finds the user in the database by searching for their email
	 * @param email - users email for finding the user
	 * @return the user found
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Override
	public User findByEmail(String email) {
			return userRepository.findByEmail(email);
	}
	
	/**
	 * Creates a One Time Password 
	 * @return otpCode - the One Time Password that is created
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public String createOtpCode() {
		otpCode= RandomString.make(6);
		
		return otpCode;
	}
	/**
	 * Takes in the users email, finds the user by their email then sets their OTP code
	 * saves user
	 * @param email - used to find the user by their email
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Transactional
	public void assignOtpCode(String email) {
		User user;
		user = new User();
		user = findByEmail(email);
		if(user.getOtpCode() != null) {
		user.setOtpCode(createOtpCode());
		}else {
			user.setOtpCode(createOtpCode());
		}
	}
	
	/**
	 * Method for resetting the users password
	 * @param user - users being reset
	 * @param password - the new password
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Transactional
	public void resetPassword(User user,String password) {
		user.setPassword(password);
		user.setOtpCode(null);
		save(user);
	}
	



}
