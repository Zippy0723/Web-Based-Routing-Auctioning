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
	private SecurityService securityService;
	
	@Autowired
	private UserService userService;
	
	private RoleRepository roleRepository;
    
	/**
	 * Constructor for roles. <br>
	 * Instantiates the roleRepository
	 * @param roleRepository Used to interact with the roles in the database
	 */
    public RolesController(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
    
    /**
	 * Adds all of the roles to the "role" model and redirects user to
	 * the roles page.
	 * @param model Used to add data to the model
	 * @return "roles"
	 */
    @RequestMapping({"/roles"})
    public String showRoleList(Model model) {
        model.addAttribute("role", roleRepository.findAll());
        model = NotificationController.loadNotificationsIntoModel(getLoggedInUser(), model);
        return "roles";
    }
    
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
