package edu.sru.thangiah.webrouting.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.sru.thangiah.webrouting.domain.Log;



@Controller
public class LogController {
	
	
	@GetMapping("/loghome")
	public String logHome(Model model) {
		try {
			ArrayList<Log> logs = new ArrayList<Log>();
	        File file = new File("WebroutingApplication.log");

	        Scanner scanner = new Scanner(file);
	        scanner.useDelimiter("\\|"); 
	        while (scanner.hasNext()) {
	            String date = scanner.next().strip();
	            String time = scanner.next().strip();
	            String where = scanner.next().strip();
	            String level = scanner.next().strip();
	            String who = scanner.next().strip();
	            String msg = scanner.next().strip();
	            
	            if (who.endsWith("Controller") || who.endsWith("Handler")) {
	            	Log log = new Log(date, time, where, level, who, msg);
	            	logs.add(log);
	            }
	        }
	        scanner.close();
	        
	        Collections.reverse(logs);
	        model.addAttribute("logs",logs);
	      
	    } catch (IOException e) {
	        System.out.println("An error occurred while reading the file: " + e.getMessage());
	        e.printStackTrace();
	        return "loghome";
			
		}
    	return "loghome";
	}
}

