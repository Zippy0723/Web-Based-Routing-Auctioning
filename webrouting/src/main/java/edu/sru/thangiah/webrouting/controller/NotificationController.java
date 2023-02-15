package edu.sru.thangiah.webrouting.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.NotificationRepository;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;

@Controller
public class NotificationController {
	
	@Autowired
	UserService userService;
	
    @Autowired
    private SecurityService securityService;
	
    /**
     * Constructor for NotificationController.
     */
	private UserRepository userRepository;
	private static NotificationRepository notificationRepository;
	
	public NotificationController(UserRepository ur, NotificationRepository nr) {
		this.userRepository = ur;
		NotificationController.notificationRepository = nr;
	}
	
	/**
	 * Handles loading the notifications page
	 */
	@RequestMapping("/notifications")
	public String notifications(Model model) {
		return "notifications";
	}
	
	/**
	 * Creates a new notification with the specified parameters
	 */
	public static void addNotification(User user, String time, String message) {
		Notification notification = new Notification(user, time, message);
		notificationRepository.save(notification);
	}
	
	/**
	 * Returns the user that is currently logged into the system. <br>
	 * If there is no user logged in, null is returned.
	 * @return user2 or null
	 */
	public User getLoggedInUser() {
    	if (securityService.isAuthenticated()) {
    		org.springframework.security.core.userdetails.User user = 
    				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		
    		User user2 = userService.findByUsername(user.getUsername());
    		
    		return user2;
    	}
    	else {
    		return null;
    	}
    }
	

}
