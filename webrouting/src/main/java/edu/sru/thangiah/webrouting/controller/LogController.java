package edu.sru.thangiah.webrouting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {
	
	
	@GetMapping("/loghome")
	public String logHome(Model model) {
    	return "loghome";
    }

}
