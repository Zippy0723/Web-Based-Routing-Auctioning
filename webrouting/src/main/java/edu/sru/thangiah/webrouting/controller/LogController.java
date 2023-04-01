package edu.sru.thangiah.webrouting.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import edu.sru.thangiah.webrouting.domain.Filter;
import edu.sru.thangiah.webrouting.domain.Log;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;



@Controller
public class LogController {
	/**
	 * This creates logs from the log file and uploads them to the model.
	 * It also allows the user to filter the logs
	 * @param model is used to add data to the model
	 * @return "loghome"
	 */


	private UserRepository userRepository;


	public LogController(UserRepository userRepository) {

		this.userRepository = userRepository;

	}


	@GetMapping("/loghome")
	public String logHome(Model model, Filter filter, BindingResult result, HttpSession session) {
		session.removeAttribute("message");
		session.setAttribute("redirectLocation", "/loghome");
		model.addAttribute("redirectLocation", "/loghome");
		model.addAttribute("currentPage","/loghome");
		ArrayList<Log> logs = getLogs();
		ArrayList<User> users = getAllUsers();
		model.addAttribute("logs",logs);
		model.addAttribute("users",users);
		return "loghome";
	}

	@PostMapping("/applyFilter")
	public String applyFilter(Filter filter, Model model, HttpSession session) {
		session.removeAttribute("message");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/loghome");
		Boolean dateOrderCheck = false;
		ArrayList<Log> logs = new ArrayList<Log>();
		logs = getLogs();
		ArrayList<User> users = getAllUsers();
		model.addAttribute("users",users);

		if(!(filter.getStartDate().equals("")) && (!(filter.getEndDate().equals("")))) {
			dateOrderCheck = checkDateOrder(filter, session);
		}

		if(dateOrderCheck == true) {
			model.addAttribute("message", session.getAttribute("message"));
			model.addAttribute("logs",logs);
			return "loghome";
		}

		if(!(filter.getUser().equals("-1"))) {
			logs = userFilter(filter, logs);
		}

		if(!(filter.getStartDate().equals(""))) {
			logs = startDateFilter(filter, logs);
		}

		if(!(filter.getEndDate().equals(""))) {
			logs = endDateFilter(filter, logs);
		}

		if(!(filter.getLevel().equals(""))) {
			logs = levelFilter(filter, logs);
		}

		model.addAttribute("logs",logs);

		return "loghome";
	}

	public ArrayList<Log> getLogs(){
		try {
			ArrayList<Log> logs = new ArrayList<Log>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
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
					LocalDate date = LocalDate.parse(dateStr,formatter);
					Log log = new Log(date, time, where, level, who, person, msg);
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

	public ArrayList <User> getAllUsers(){
		ArrayList<User> allUsers = (ArrayList) userRepository.findAll();	 
		return allUsers;
	}

	public ArrayList<Log> startDateFilter(Filter filter, ArrayList<Log> logs) {
		LocalDate startDate = LocalDate.parse(filter.getStartDate());

		for (int i = 0; i < logs.size(); i++) {

			if(logs.get(i).getDateAsLocalDate().isBefore(startDate)){
				logs.remove(i);
				i--;
			}
		}
		return logs;
	}

	public ArrayList<Log> endDateFilter(Filter filter, ArrayList<Log> logs) {
		LocalDate endDate = LocalDate.parse(filter.getEndDate());

		for (int i = 0; i < logs.size(); i++) {

			if(logs.get(i).getDateAsLocalDate().isAfter(endDate)){
				logs.remove(i);
				i--;
			}
		}
		return logs;
	}

	public boolean checkDateOrder(Filter filter, HttpSession session) {
		LocalDate startDate = LocalDate.parse(filter.getStartDate());
		LocalDate endDate = LocalDate.parse(filter.getEndDate());

		if(startDate.isAfter(endDate)) {
			session.setAttribute("message", "Ensure that start date is before end date.");
			return true;
		}

		return false;
	}


	public ArrayList<Log> levelFilter(Filter filter, ArrayList<Log> logs){

		for (int i = 0; i < logs.size(); i++) {

			if(!(logs.get(i).getLevel().strip().equals(filter.getLevel().strip()))) {
				logs.remove(i);
				i--;
			}
		}
		return logs;
	}

	public ArrayList<Log> userFilter(Filter filter, ArrayList<Log> logs){

		for (int i = 0; i < logs.size(); i++) {

			if(!(logs.get(i).getUser().strip().equals(filter.getUser().strip()))) {
				logs.remove(i);
				i--;
			}
		}
		return logs;
	}


	public void  populateLogs() {
		try {
			FileOutputStream fos = new FileOutputStream("WebroutingApplication.log", true);
			PrintWriter writer = new PrintWriter(fos);


			LocalDate today = LocalDate.now();
			LocalDate startOfYear = LocalDate.of(today.getYear(), 1, 1);

			int numDays = today.getDayOfYear() - startOfYear.getDayOfYear() + 1;
			String[] dateArray = new String[numDays];
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

			for (int i = 0; i < numDays; i++) {
				LocalDate date = startOfYear.plusDays(i);
				dateArray[i] = date.format(formatter);
			}

			LocalTime time = LocalTime.of(0, 0, 0);
			int numMinutes = 24 * 60;
			String[] timeArray = new String[numMinutes];
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");

			for (int i = 0; i < numMinutes; i++) {
				timeArray[i] = time.format(formatter2);
				time = time.plusMinutes(1);
			}

			String[] levelArray = {"ERROR","INFO"};

			String where = "http-nio-8080-exec-5";

			String[] numArray = new String[50];
			for (int i = 1; i <= 50; i++) {
				numArray[i-1] = Integer.toString(i);
			}

			String [] people = {"Carrier", "ShadowAdmin", "AdminTry", "Ship4U", "Shipper", "WillyWonka","Master" };

			String [] entity = {"Bid", "Contact", "Driver", "Location", "Shipment", "Technician", "Vehicle", "VehicleType", "Maintenance Order"};

			String [] action = {"updated", "saved","deleted"};



			for (int i = 0; i<dateArray.length; i++){
				Random rand = new Random();
				String completeMessage;
				int randomNumber = rand.nextInt(1440);

				completeMessage = "||" + dateArray[i] + "||"; //date included

				for (int j = 0; j<3; j++) { //3x per date

					completeMessage = "||" + dateArray[i] + "||";
					completeMessage += timeArray[randomNumber]+ "||"; //time included
					completeMessage += where + "||"; //where included

					randomNumber = rand.nextInt(2);

					completeMessage += levelArray[randomNumber] + "||"; //level included

					completeMessage += "ExampleController||";

					randomNumber = rand.nextInt(7);
					completeMessage += people[randomNumber]+"||";


					randomNumber = rand.nextInt(3);
					completeMessage += "Successfully "+ action[randomNumber] + " a ";

					randomNumber = rand.nextInt(9);
					completeMessage += entity[randomNumber] + " ";
					
					randomNumber = rand.nextInt(50)+ 1;
					completeMessage += "with ID "+ randomNumber + ".";

					writer.println(completeMessage);
					completeMessage = "";

				}

			}

			writer.close();
			fos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}


}

