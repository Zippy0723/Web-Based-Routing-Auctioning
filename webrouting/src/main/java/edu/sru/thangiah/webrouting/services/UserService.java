package edu.sru.thangiah.webrouting.services;

import edu.sru.thangiah.webrouting.domain.User;

/**
 * Interface class for the save and findByUsername methods
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

public interface UserService {

    User findByUsername(String username);

	void save(User user);

	User findByOtp(String otpCode);

	User findByEmail(String userEmail);

	void assignOtpCode(String email);

	void resetPassword(User user, String password);
	
	String createOtpCode();


	


}
