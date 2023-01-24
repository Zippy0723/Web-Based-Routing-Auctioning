package edu.sru.thangiah.webrouting.services;


/**
 * Sets up the isAuthenticated and autoLogin methods
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

public interface SecurityService {
	
	boolean isAuthenticated();
	


	/**
	 * Automatically logs the user in with the username and password string
	 * @param username Username of the user
	 * @param password Password of the user
	 */
	void autoLogin(String username, String password);





}
