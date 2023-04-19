package edu.sru.thangiah.webrouting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.RoleRepository;
import edu.sru.thangiah.webrouting.services.SecurityService;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Roles.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/6/2022
 */

@Controller
public class RolesController {

	@Autowired
	private UserService userService;

	private RoleRepository roleRepository;

	/**
	 * Constructor for RolesController
	 * @param roleRepository Instantiates the roles Repository
	 */
	
	public RolesController(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	/**
	 * Adds all of the required attributes to render the roles page
	 * @param model used to load attributes into the Thymeleaf model
	 * @return /roles
	 */
	
	@RequestMapping({"/roles"})
	public String showRoleList(Model model) {
		model.addAttribute("role", roleRepository.findAll());
		model = NotificationController.loadNotificationsIntoModel(userService.getLoggedInUser(), model);
		return "roles";
	}

}
