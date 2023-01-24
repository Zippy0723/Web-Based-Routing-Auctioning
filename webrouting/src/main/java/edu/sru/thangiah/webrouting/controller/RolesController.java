package edu.sru.thangiah.webrouting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.sru.thangiah.webrouting.repository.RoleRepository;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with Roles.
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/6/2022
 */

@Controller
public class RolesController {
	
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
        return "roles";
    }
}
