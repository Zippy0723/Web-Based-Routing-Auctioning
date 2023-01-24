package edu.sru.thangiah.webrouting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.sru.thangiah.webrouting.domain.User;

/**
 * Sets the Users Repository using the JpaRepository
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */

public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * Sets the findByUsername method
	 * @param username Username being found
	 */
	User findByUsername(String username);
	
	/**
	 *creates the findByVerificationCode method
	 *Query the database
	 * @param verification code for finding the user
	 * @return the User found
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);
	
	/**
	 *creates the findByEmail method
	 *Query the database
	 * @param email for finding the user
	 * @return the User found
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public User findByEmail(String email);
	
	/**
	 *creates the findByOtpCode method
	 *Query the database
	 * @param OTP code for finding the user
	 * @return the User found
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Query("SELECT c FROM User c WHERE c.otpCode = ?1")
	public User findByOtpCode(String otp);
}
