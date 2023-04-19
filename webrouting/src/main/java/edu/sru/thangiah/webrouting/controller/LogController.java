package edu.sru.thangiah.webrouting.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sru.thangiah.webrouting.domain.Contacts;
import edu.sru.thangiah.webrouting.domain.Filter;
import edu.sru.thangiah.webrouting.domain.Log;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.UserRepository;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Handles the Thymeleaf controls for the pages
 * dealing with the logging system
 * @author Dakota Myers drm1022@sru.edu
 */

@Controller
public class LogController {

	@Autowired
	private UserService userService;
	
	private UserRepository userRepository;

	/**
	 * Constructor for the LogController
	 * @param userRepository Instantiates the user Repository
	 */

	public LogController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Adds all of the required attributes to the model to render the log home page
	 * @param model used to load attributes into the Thymeleaf model
	 * @param filter used to load the filter into the model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /loghome
	 */
	
	@GetMapping("/loghome")
	public String logHome(Model model, Filter filter, HttpSession session) {
		session.removeAttribute("message");
		session.setAttribute("redirectLocation", "/loghome");
		model.addAttribute("redirectLocation", "/loghome");
		model.addAttribute("currentPage","/loghome");
		ArrayList<Log> logs = getLogs();
		ArrayList<User> users = getAllUsers();
		model.addAttribute("logs",logs);
		model.addAttribute("users",users);
		session.removeAttribute("filter");
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		return "loghome";
	}

	/**
	 * Recieves the filter object from the user and applies it to the logs that are uploaded on the page
	 * @param filter used to load the filter into the model
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return /loghome
	 */
	@PostMapping("/applyFilter")
	public String applyFilter(Filter filter, Model model, HttpSession session) {
		session.removeAttribute("message");
		model.addAttribute("redirectLocation", (String) session.getAttribute("redirectLocation"));
		model.addAttribute("currentPage","/loghome");
		session.removeAttribute("filter");
		Boolean dateOrderCheck = false;
		ArrayList<Log> logs = new ArrayList<Log>();
		logs = getLogs();
		ArrayList<User> users = getAllUsers();
		model.addAttribute("users",users);
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);

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
		session.setAttribute("filter", filter);

		return "loghome";
	}

	/**
	 * Applies the current filter to the log list and returns an excel file containing them
	 * @param model used to load attributes into the Thymeleaf model
	 * @param session used to load attributes into the current users HTTP session
	 * @return excel file
	 */
	
	@RequestMapping("/downloadLogs")
	public ResponseEntity<Resource> downloadLogs(Model model, HttpSession session) {
		Filter filter = new Filter();
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		try {
			filter = (Filter) session.getAttribute("filter");
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

			return getDownloadableLogs(logs);

		}
		catch(Exception e) {
			ArrayList<Log> logs = getLogs();
			return getDownloadableLogs(logs);
		}
	}

	/**
	 * Parses the WebroutingApplication.log file and adds all of the logs to a list to return to the user
	 * @return logs
	 */
	
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
				String user = scanner.next().strip();
				String msg = scanner.next().strip();

				if (who.endsWith("Controller") || who.endsWith("Handler")) {
					LocalDate date = LocalDate.parse(dateStr,formatter);
					Log log = new Log(date, time, where, level, who, user, msg);
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

	/**
	 * Finds all of the users in the user Repository
	 * @return allUsers
	 */
	
	public ArrayList <User> getAllUsers(){
		ArrayList<User> allUsers = (ArrayList) userRepository.findAll();	 
		return allUsers;
	}

	/**
	 * Applies a filter to the logs based on the starting date
	 * @param filter holds the filter object
	 * @param logs holds the logs
	 * @return logs
	 */
	
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

	/**
	 * Applies a filter to the logs based on the ending date
	 * @param filter holds the filter object
	 * @param logs holds the logs
	 * @return logs
	 */
	
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

	/**
	 * Checks to see if start date is before end date
	 * @param filter holds the filter object
	 * @param session used to load attributes into the current users HTTP session
	 * @return true or false
	 */
	
	public boolean checkDateOrder(Filter filter, HttpSession session) {
		LocalDate startDate = LocalDate.parse(filter.getStartDate());
		LocalDate endDate = LocalDate.parse(filter.getEndDate());

		if(startDate.isAfter(endDate)) {
			session.setAttribute("message", "Ensure that start date is before end date.");
			return true;
		}

		return false;
	}

	/**
	 * Applies a filter to the logs based on the level
	 * @param filter holds the filter object
	 * @param logs holds the logs
	 * @return logs
	 */

	public ArrayList<Log> levelFilter(Filter filter, ArrayList<Log> logs){

		for (int i = 0; i < logs.size(); i++) {

			if(!(logs.get(i).getLevel().strip().equals(filter.getLevel().strip()))) {
				logs.remove(i);
				i--;
			}
		}
		return logs;
	}

	/**
	 * Applies a filter to the logs based on the user 
	 * @param filter holds the filter object
	 * @param logs holds the logs
	 * @return logs
	 */
	
	public ArrayList<Log> userFilter(Filter filter, ArrayList<Log> logs){

		for (int i = 0; i < logs.size(); i++) {

			if(!(logs.get(i).getUser().strip().equals(filter.getUser().strip()))) {
				logs.remove(i);
				i--;
			}
		}
		return logs;
	}
	
	/**
	 * Creates an excel file containing all of the logs that are passed to it
	 * @param logs holds the logs
	 * @return Excel file
	 */

	public ResponseEntity<Resource> getDownloadableLogs(ArrayList<Log> logs){

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet logsWorksheet = workbook.createSheet("Logs");


		XSSFRow logsHeaderRow = logsWorksheet.createRow(0);
		logsHeaderRow.createCell(0).setCellValue("Date");
		logsHeaderRow.createCell(1).setCellValue("Time");
		logsHeaderRow.createCell(2).setCellValue("Level");
		logsHeaderRow.createCell(3).setCellValue("User");
		logsHeaderRow.createCell(4).setCellValue("Message");

		int rowIndex = 1;

		for(Log l : logs) {
			XSSFRow curRow = logsWorksheet.createRow(rowIndex++);
			curRow.createCell(0).setCellValue(l.getDate());
			curRow.createCell(1).setCellValue(l.getTime());
			curRow.createCell(2).setCellValue(l.getLevel());
			curRow.createCell(3).setCellValue(l.getUser());
			curRow.createCell(4).setCellValue(l.getMsg());
		}

		for (int i = 0; i < logsHeaderRow.getLastCellNum(); i++) {
			logsWorksheet.autoSizeColumn(i);
		}

		byte[] workbookBytes;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			workbook.write(outputStream);
			workbookBytes = outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Create a Resource object from the byte array
		ByteArrayResource resource = new ByteArrayResource(workbookBytes);

		// Set the headers for the response
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filteredLogs.xlsx");
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

		// Return a ResponseEntity with the resource and headers
		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(workbookBytes.length)
				.body(resource);
	}


	/**
	 * Populates the WebroutingApplication.log file with 3 random logs for every day of the current year that has passed
	 */
	
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

			String [] people = {"Carrier", "ShadowAdmin", "AdminTry", "Ship4U", "Shipper", "WillyWonka","Auctioneer" };

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

