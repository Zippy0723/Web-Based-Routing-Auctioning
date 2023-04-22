package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.FreightRateTable;
import edu.sru.thangiah.webrouting.domain.Notification;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.FreightRateTableRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.services.ApiServiceImpl;
import edu.sru.thangiah.webrouting.services.NotificationService;
import edu.sru.thangiah.webrouting.services.UserService;

/**
 * Controller for freight rate table functions
 * @author Thomas Haley
 *
 */
@Controller
public class FreightRateController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ApiServiceImpl apiService;
	
	@Autowired
	NotificationService notificationService;
	
	private FreightRateTableRepository freightRateTableRepository;
	
	private CarriersRepository carrierRepository;
	
	private ShipmentsRepository shipmentsRepository;
	
	private static final Logger Logger = LoggerFactory.getLogger(FreightRateController.class);

	public FreightRateController(FreightRateTableRepository freightRateTableRepository, CarriersRepository carrierRepository, ShipmentsRepository shipmentsRepository) {
		this.freightRateTableRepository = freightRateTableRepository;
		this.carrierRepository = carrierRepository;
		this.shipmentsRepository = shipmentsRepository;
	}
	
	/**
	 * Loads the Freight rate table homepage
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/freightratehome")
	public String freightRateHome(Model model,HttpSession session) {
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage","/freightratehome");
		ArrayList<Carriers> carriers = (ArrayList<Carriers>) carrierRepository.findAll();
		model.addAttribute("carriers",carriers);
		
		try {
			model.addAttribute("message",session.getAttribute("message"));
			session.removeAttribute("message");
		}
		catch(Exception e){
			//do nothing
		}
		try {
			Long selectedCarrierId = (Long) session.getAttribute("selectedCarrierID");
			Carriers selectedCarrier = carrierRepository.findById(selectedCarrierId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + selectedCarrierId));
			session.removeAttribute("selectedCarrier");
			List<FreightRateTable> tables = user.getFreightRateTables();
			
			for(FreightRateTable table : tables) {
				if(table.getCarrier() == selectedCarrier) {
					model.addAttribute("distancebreakpoints",table.getDistanceBreakPoints().replace("]", "").replace("[", ""));
					model.addAttribute("priceppermiles",table.getPricePerMileArray().replace("]", "").replace("[", ""));
				}
			}

		}
		catch(Exception e){
			//do nothing
		}

		return "freightratehome";
	}
	
	/**
	 * Uploads a freight rate table from excel into the SQL database 
	 * 
	 * @param excelData the file the user uploaded
	 * @param model pointer to the thymeleaf model
	 * @param session pointer to the users HTTP session
	 * @return
	 */
	@RequestMapping("upload-freightratetable")
	public String loadFreightRateTable(@RequestParam("file") MultipartFile excelData, Model model, HttpSession session) {
		XSSFWorkbook workbook;
		User user = userService.getLoggedInUser();

		try {
			
			if (!excelData.getOriginalFilename().endsWith(".xls") && !excelData.getOriginalFilename().endsWith(".xlsx")) {
				Logger.error("{} || attempted to upload Freight Rate Table but failed.",user.getUsername());
				session.setAttribute("message", "Incorrect file type.");
				return "redirect:/freightratehome"; 
			}
			
			if(excelData.getSize() > 10000000) {
				Logger.error("{} || attempted to upload Freight Rate Table but failed.",user.getUsername());
				session.setAttribute("message", "File is too large, must be under 10 MB.");
				return "redirect:/freightratehome";
			}
			
			workbook = new XSSFWorkbook(excelData.getInputStream());
			int numSheets = workbook.getNumberOfSheets();
			ArrayList<Carriers> carriers = (ArrayList<Carriers>) carrierRepository.findAll();
			
			List<FreightRateTable> currentTables = user.getFreightRateTables();
			freightRateTableRepository.deleteAll(currentTables);
			
			for (int sheetIndex = 0; sheetIndex < numSheets; sheetIndex++) {
				XSSFSheet worksheet = workbook.getSheetAt(sheetIndex); 
				String carrierSCAC = worksheet.getSheetName();
				Carriers carrier = null;
				
				for (Carriers c : carriers) {
					if(c.getScac().equals(carrierSCAC)) {
						carrier = c;
					}
				}
				if(carrier == null) {
					continue;
				}
				ArrayList<String> distanceBreakPoints = new ArrayList<String>();
				ArrayList<String> pricesPerMile = new ArrayList<String>();
				
				for(int rowIndex=1; rowIndex<worksheet.getPhysicalNumberOfRows(); rowIndex++) {
					XSSFRow row = worksheet.getRow(rowIndex);
					distanceBreakPoints.add(row.getCell(0).toString().replace(">", ""));
					pricesPerMile.add(row.getCell(1).toString());
					
				}
				
				if(distanceBreakPoints.size() != pricesPerMile.size()) {
					session.setAttribute("message", "One of your freight rate table was not formatted correctly. Please check the template");
					workbook.close();
					return "redirect:/freightratehome";
				}
				
				FreightRateTable freightRateTable = new FreightRateTable();
				freightRateTable.setCarrier(carrier);
				freightRateTable.setUser(user);
				freightRateTable.setDistanceBreakPoints(distanceBreakPoints.toString());
				freightRateTable.setPricePerMileArray(pricesPerMile.toString());
				freightRateTableRepository.save(freightRateTable);
				
			}
			
			
			session.setAttribute("message", "Freight Rate Table Uploaded Succesfully");
			workbook.close();
			return "redirect:/freightratehome";
		}
		catch(Exception e ) {
			e.printStackTrace();
			Logger.error("{} || attempted to upload Freight Rate Table but failed.",user.getUsername());
			model.addAttribute("message", "Incorrect file.");
			return "redirect:/freightratehome";
		}
		
	}
	
	/**
	 * Method intended to be called through AJAX, fetches the price of a shipment given the
	 * users freight rate table. Returns null if no freight rate table exists for the carrier
	 * 
	 * @param shipmentid Id of the Shipment being Priced
	 * @param carrierid Id of the selected carrier
	 * @param session pointer to the current users HTTP sessions
	 * @return result The price of the shipment dictated by the freight rate table
	 * @throws IOException
	 */
	@RequestMapping(value="/getfreightrateprice/{shipmentid}-{carrierid}",method = RequestMethod.GET)
	@ResponseBody
	public String getShipmentPriceFromFreightRateTable(@PathVariable String shipmentid, @PathVariable String carrierid, HttpSession session) throws IOException {
		Shipments shipment = shipmentsRepository.findById(Long.parseLong(shipmentid))
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + shipmentid));
		Carriers carrier = carrierRepository.findById(Long.parseLong(carrierid))
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + carrierid));
		User user = userService.getLoggedInUser();
		
		Long distance = Long.parseLong(apiService.fetchDistanceBetweenCoordinates(shipment.getShipperLatitude(), shipment.getShipperLongitude(), shipment.getConsigneeLatitude(), shipment.getConsigneeLongitude()).replace(",", ""));
		FreightRateTable freightRateTable = null;
		List<FreightRateTable> freightRateTables= user.getFreightRateTables();
		for (FreightRateTable f : freightRateTables) {
			if(f.getCarrier() == carrier) {
				freightRateTable = f;
			}
		}
		if(freightRateTable == null) {
			session.setAttribute("message", "No Freight Rate table avalible for this carrier. Please check your Freight Rate Tables");
			return null;
		}
		ArrayList<String> distanceBreakPointTokens = new ArrayList<>(Arrays.asList(freightRateTable.getDistanceBreakPoints().replace("[", "").replace("]", "").replace(" ", "").split(",")));
		ArrayList<Long> distanceBreakPoints = distanceBreakPointTokens.stream()
                  .map(Long::parseLong)
                  .collect(Collectors.toCollection(ArrayList::new));
		
		String price = null;
		for(int priceIndex = 0; priceIndex < distanceBreakPoints.size(); priceIndex++) {
			if (distance <= distanceBreakPoints.get(priceIndex)) {
				price = freightRateTable.getPricePerMileArray().replace("[", "").replace("]", "").replace(" ", "").split(",")[priceIndex];
				break;
			}
		}
		
		if (price == null) {
			session.setAttribute("message", "There is a formatting error in your freight rate table. Unable to price shipment");
			return null;
		}
		
		Double result = null;
		try {
			result = Double.parseDouble(price) * Double.parseDouble(distance.toString()); // why
		}
		catch(Exception e) {
			session.setAttribute("message", "There is a formatting error in your prices. Please check your freight rate table");
			return null;
		}
		
		return result.toString();
	}
	
	/**
	 * Shows the freight rate home page with a specific carriers freight rate table selected
	 * 
	 * @param selectedCarrier the carrier the user selected in the dropdown
	 * @param session pointer to the users http session
	 * @return
	 */
	@PostMapping("displayCarrierFreightRateTable")
	public String displayCarrierFreightRateTable(@RequestParam("selectedCarrierId") Long selectedCarrierId, HttpSession session) {
		session.setAttribute("selectedCarrierID", selectedCarrierId);
		
		return "redirect:/freightratehome";
	}
}
