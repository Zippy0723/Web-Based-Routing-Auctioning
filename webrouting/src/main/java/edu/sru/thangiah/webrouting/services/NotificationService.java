package edu.sru.thangiah.webrouting.services;

import edu.sru.thangiah.webrouting.domain.User;

public interface NotificationService {
	public void addNotification(User user, String message, Boolean toEmail);
}
