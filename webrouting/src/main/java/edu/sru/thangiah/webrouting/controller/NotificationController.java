package edu.sru.thangiah.webrouting.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.mailsending.Emailing;
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
	private static UserRepository userRepository;
	private static NotificationRepository notificationRepository;
	
	public NotificationController(UserRepository ur, NotificationRepository nr) {
		NotificationController.userRepository = ur;
		NotificationController.notificationRepository = nr;
	}
	
	/**
	 * Handles loading the notifications page
	 */
	@RequestMapping("/unreadnotifications")
	public String unreadNotifications(Model model) {
		User user = getLoggedInUser();
		List<Notification> notifications = new ArrayList<>();
		
		notifications = fetchUnreadNotifications(user);
		   
		model.addAttribute("notifications",notifications);
			
		return "unreadnotifications";
	}
	
	/**
	 * 
	 */
	@RequestMapping("/readnotifications")
	public String readNotifications(Model model) {
		User user = getLoggedInUser();
		List<Notification> notifications = new ArrayList<>();
		List<Notification> unreadNotifications = new ArrayList<>();
		
		notifications = fetchReadNotifications(user);
		unreadNotifications = fetchUnreadNotifications(user);
		
		model.addAttribute("unreadcount",unreadNotifications.size());
		model.addAttribute("notifications",notifications);
			
		return "readnotifications";
		
	}
	
	/**
	 * 
	 */
	@RequestMapping("/markasread/{id}")
	public String markAsRead(Model model, @PathVariable("id") long id) {
		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid notification Id:" + id));
		
		notification.setIsread(true);
		notificationRepository.save(notification);
		
		return "redirect:/unreadnotifications";
		
	}
	
	/**
	 * 
	 */
	@RequestMapping("/markasunread/{id}")
	public String markAsUnread(Model model, @PathVariable("id") long id) {
		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid notification Id:" + id));
		
		notification.setIsread(false);
		notificationRepository.save(notification);
		
		return "redirect:/readnotifications";
		
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/markallasread")
	public String markAllAsRead(Model model) {
		User user = getLoggedInUser();
		List<Notification> noitifications = NotificationController.fetchUnreadNotifications(user);
		
		for (Notification n : noitifications) {
			n.setIsread(true);
			notificationRepository.save(n);
		}
		
		return "redirect:/unreadnotifications";
		
	}
	
	//TODO: these static methods should be refactored to be a part of NotificationService, that way they wont have to be static and dont have potential to cause weird issues
	
	/**
	 * 
	 */
	public static List<Notification> fetchUnreadNotifications(User user){
		List<Notification> notifications = user.getNotifications();
		List<Notification> result = new ArrayList<>();
		
		for (Notification n: notifications) {
			if (!n.getIsread()) {
				result.add(n);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	public static List<Notification> fetchReadNotifications(User user){
		List<Notification> notifications = user.getNotifications();
		List<Notification> result = new ArrayList<>();
		
		for (Notification n: notifications) {
			if (n.getIsread()) {
				result.add(n);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	public static Model loadNotificationsIntoModel(User user, Model model) {
        List<Notification> notifications = new ArrayList<>();
        
        if(!(user == null)) {
            notifications = NotificationController.fetchUnreadNotifications(user);
        }
        
        model.addAttribute("notifications",notifications);
        
        return model;
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
