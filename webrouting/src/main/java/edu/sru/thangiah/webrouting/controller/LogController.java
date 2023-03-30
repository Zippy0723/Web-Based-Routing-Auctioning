package edu.sru.thangiah.webrouting.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.sru.thangiah.webrouting.domain.Log;



@Controller
public class LogController {
	/**
	 * This creates logs from the log file and uploads them to the model.
	 * @param model is used to add data to the model
	 * @return "loghome"
	 */
	
	
	public ArrayList<Log> getLogs(){
		try {
			ArrayList<Log> logs = new ArrayList<Log>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
	        File file = new File("WebroutingApplication.log");
	        Scanner scanner = new Scanner(file);
	        scanner.useDelimiter("\\|\\|");
	        while (scanner.hasNext()) {
	            String dateStr = scanner.next().strip();
	            String time = scanner.next().strip();
	            String where = scanner.next().strip();
	            String level = scanner.next().strip();
	            String who = scanner.next().strip();
	            String person = scanner.next().strip();
	            String msg = scanner.next().strip();
	            
	            if (who.endsWith("Controller") || who.endsWith("Handler")) {
	            	Log log = new Log(LocalDate.parse(dateStr,formatter), time, where, level, who, person, msg);
	            	logs.add(log);
	            	}
	        	}
	        	scanner.close();
	        	Collections.reverse(logs);
	        	return logs;
	      
			} catch (IOException e) {
				System.out.println("An error occurred while reading the file: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
	}
	
	public List<Log> filterByDate(List<Log> logs, LocalDate startDate, LocalDate endDate) {
	       return logs.stream()
	                  .filter(log -> log.getDateAsLocalDate().isAfter(startDate.minusDays(1)) && log.getDateAsLocalDate().isBefore(endDate.plusDays(1)))
	                  .collect(Collectors.toList());
	   }

	@GetMapping("/loghome")
	public String logHome(Model model) {
		
		ArrayList<Log> logs = new ArrayList<Log>();
		logs = getLogs();
		model.addAttribute("logs",logs);
		return "loghome";
	}
	
	
	@GetMapping("/reverseDateOrder")
	public String reverseDateOrder(Model model){
		
		ArrayList<Log> logs = new ArrayList<Log>();
		logs = getLogs();
		Collections.reverse(logs);
		
		model.addAttribute("logs",logs);
		return "loghome";
		
	}
	
	
	@GetMapping("/levelOrder")
	public String levelOrder(Model model){
		
		ArrayList<Log> logs = new ArrayList<Log>();
		ArrayList<Log> errorLogs = new ArrayList<Log>();
		logs = getLogs();
		
		for (Log l : logs) {
			if (l.getLevel().equals("ERROR")){
				errorLogs.add(l);
		}
		}
		
		model.addAttribute("logs",errorLogs);
		return "loghome";
		
	}
	
	@PostMapping("/applyFilter")
	public String applyFilter(Model model) {
		
		ArrayList<Log> logs = new ArrayList<Log>();
		logs = getLogs();
		
		
		
		
		
		
		
		
		
		model.addAttribute("logs",logs);
		
		return "loghome";
	}
	
}

