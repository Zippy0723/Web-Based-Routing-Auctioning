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

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with notification
 * @author Thomas Haley tjh1019@sru.edu
 * @since 1/01/2023
 */

@Controller
public class NotificationController {

	@Autowired
	UserService userService;
	
	private static UserRepository userRepository;
	
	private static NotificationRepository notificationRepository;
	
	/**
	 * Constructor for NotificationController
	 * @param ur Instantiates the user Repository
	 * @param nr Instantiates the notification Repository
	 */

	public NotificationController(UserRepository ur, NotificationRepository nr) {
		NotificationController.userRepository = ur;
		NotificationController.notificationRepository = nr;
	}


	/**
	 * Adds all of the required attributes to the model to render the unread notifications page
	 * @param model used to load attributes into the Thymeleaf model
	 * @return /unreadnotifications
	 */
	
	@RequestMapping("/unreadnotifications")
	public String unreadNotifications(Model model) {
		User user = userService.getLoggedInUser();
		List<Notification> notifications = new ArrayList<>();

		notifications = fetchUnreadNotifications(user);

		model.addAttribute("notifications",notifications);
		model.addAttribute("currentPage","/unreadnotifications");

		return "unreadnotifications";
	}

	/**
	 * Adds all of the required attributes to the model to render the read notifications page
	 * @param model used to load attributes into the Thymeleaf model
	 * @return /readnotifications
	 */
	
	@RequestMapping("/readnotifications")
	public String readNotifications(Model model) {
		User user = userService.getLoggedInUser();
		List<Notification> notifications = new ArrayList<>();
		List<Notification> unreadNotifications = new ArrayList<>();

		notifications = fetchReadNotifications(user);
		unreadNotifications = fetchUnreadNotifications(user);

		model.addAttribute("unreadcount",unreadNotifications.size());
		model.addAttribute("notifications",notifications);
		model.addAttribute("currentPage","/unreadnotifications"); //just for the purpose of selecting the right button on the sidebar

		return "readnotifications";

	}

	/**
	 * Marks the selected notification as read
	 * @param model used to load attributes into the Thymeleaf model
	 * @param id of the notification being marked as read
	 * @return redirects to /unreadnotifications
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
	 * Marks the selected notification as unread
	 * @param model used to load attributes into the Thymeleaf model
	 * @param id of the notification being marked as read
	 * @return redirects to /readnotifications
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
	 * Marks all notifications as read
	 * @param model used to load attributes into the Thymeleaf model
	 * @return redirects to /unreadnotifications
	 */
	
	@RequestMapping("/markallasread")
	public String markAllAsRead(Model model) {
		User user = userService.getLoggedInUser();
		List<Notification> noitifications = NotificationController.fetchUnreadNotifications(user);

		for (Notification n : noitifications) {
			n.setIsread(true);
			notificationRepository.save(n);
		}

		return "redirect:/unreadnotifications";

	}

	/**
	 * Takes in a user and finds all of the associated notifications that are unread
	 * @param user holds the user that has notifications
	 * @return result
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
	 * Takes in a user and finds all of the associated notifications that are read
	 * @param user holds the user that has notifications
	 * @return result
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
	 * Loads the notifications into the model
	 * @param user holds the currently logged in user
	 * @param model used to load attributes into the Thymeleaf model
	 * @return model
	 */
	
	public static Model loadNotificationsIntoModel(User user, Model model) {
		List<Notification> notifications = new ArrayList<>();

		if(!(user == null)) {
			notifications = NotificationController.fetchUnreadNotifications(user);
		}

		model.addAttribute("notifications",notifications);

		return model;
	}

}
