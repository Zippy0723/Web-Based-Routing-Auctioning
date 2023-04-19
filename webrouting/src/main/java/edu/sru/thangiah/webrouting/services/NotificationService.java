package edu.sru.thangiah.webrouting.services;

import edu.sru.thangiah.webrouting.domain.User;

/**
 * Interface class for the save and nofiticationService methods
 * @author Thomas Haley tjh1019@sru.edu
 * @since 1/01/2023
 */

public interface NotificationService {
	public void addNotification(User user, String message, Boolean toEmail);
}
