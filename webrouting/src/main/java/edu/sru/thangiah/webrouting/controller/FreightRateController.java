package edu.sru.thangiah.webrouting.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.sru.thangiah.webrouting.domain.Carriers;
import edu.sru.thangiah.webrouting.domain.Shipments;
import edu.sru.thangiah.webrouting.domain.User;
import edu.sru.thangiah.webrouting.repository.CarriersRepository;
import edu.sru.thangiah.webrouting.repository.ShipmentsRepository;
import edu.sru.thangiah.webrouting.services.UserService;

@Controller
public class FreightRateController {
	
	@Autowired
	UserService userService;
	
	private ShipmentsRepository shipmentsRepository;
	
	private CarriersRepository carriersRepository;
	
	public FreightRateController(ShipmentsRepository shipmentsRepository, CarriersRepository carriersRepository) {
		this.shipmentsRepository = shipmentsRepository;
		this.carriersRepository = carriersRepository;
	}
	
	@GetMapping("/freightratetable")
	public String showFreightRateTable(Model model) {
		User user = userService.getLoggedInUser();
		model = NotificationController.loadNotificationsIntoModel(user, model);
		model.addAttribute("currentPage", "/freightratetable");
		
		return "/freightratetable";
	}
	
	/*
	 * This function is very complicated and a bit of a mess, needs proper documentation
	 */
	@RequestMapping(value="/getfreightrateprice/{shipmentid}-{carrierid}",method = RequestMethod.GET)
	@ResponseBody
	public String getShipmentPriceFromFreightRateTable(@PathVariable String shipmentid, @PathVariable String carrierid, HttpSession session) throws IOException {
		Shipments shipment = shipmentsRepository.findById(Long.parseLong(shipmentid))
				.orElseThrow(() -> new IllegalArgumentException("Invalid shipment Id:" + shipmentid));
		Carriers carrier = carriersRepository.findById(Long.parseLong(carrierid))
				.orElseThrow(() -> new IllegalArgumentException("Invalid carrier Id:" + carrierid));
		User user = userService.getLoggedInUser();

		long Weight;
		String commodityClass;
		try {
			Weight = Long.parseLong(shipment.getCommodityPaidWeight());
			commodityClass = shipment.getCommodityClass();
		} catch (NumberFormatException e) {
			session.setAttribute("message", "There is an error in shipment data. Please contact an administrator.");
			return null;
		}

		byte[] freightRateTableRaw = user.getFreightRateTables();
		InputStream stream = new ByteArrayInputStream(freightRateTableRaw);
		XSSFWorkbook workbook = new XSSFWorkbook(stream);
		stream.close();
		int rowIndex = 1;
		int cellIndex = 1;
		Integer targetCellIndex = null;
		Integer targetRowIndex = null;
		XSSFSheet activeSheet = null;

		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			if(workbook.getSheetName(i).equals(carrier.getScac())) {
				activeSheet = workbook.getSheetAt(i);
			}
		}
		if (activeSheet == null) {
			workbook.close();
			session.setAttribute("message", "Error, your freight rate table does not have a table for this carrier.");
			return null;
		}

		XSSFRow weightRow = activeSheet.getRow(rowIndex);
		ArrayList<Long> weightRowData = new ArrayList<Long>();
		XSSFCell activeCell = weightRow.getCell(cellIndex);
		while(activeCell != null && !activeCell.toString().equals("")) {
			try {
				weightRowData.add(Long.parseLong(activeCell.toString().replaceAll("[^0-9]", "")));
			} catch (NumberFormatException e) {
				workbook.close();
				session.setAttribute("message", "Error reading your weight specifications, please check your freight rate table");
				return null;
			}
			activeCell = weightRow.getCell(++cellIndex);
		}

		for(int i = 0; i < weightRowData.size(); i++) {
			if (Weight < weightRowData.get(i)) {
				targetCellIndex = i + 1;
				break;
			}
		}

		rowIndex++;
		XSSFRow activeRow = activeSheet.getRow(rowIndex);
		activeCell = activeRow.getCell(0);
		while(activeCell != null && !activeCell.toString().equals("")) {
			String cellVal = activeCell.toString().substring(0,activeCell.toString().length() - 2);
			if(commodityClass.equals(cellVal)){
				targetRowIndex = rowIndex;
				break;
			}
			activeRow = activeSheet.getRow(++rowIndex);
			if(activeRow == null) {
				break;
			}
			activeCell = activeRow.getCell(0);
		}

		if(targetCellIndex == null || targetRowIndex == null) {
			workbook.close();
			session.setAttribute("message", "Error, there is no price specified for this shipment class and weight. Please check your freight rate table.");
			return null;
		}

		activeRow = activeSheet.getRow(targetRowIndex);
		activeCell = activeRow.getCell(targetCellIndex);
		Long result;

		try {
			result = Long.parseLong(activeCell.toString().substring(0,activeCell.toString().length()-2));
		} catch (NumberFormatException e) {
			workbook.close();
			session.setAttribute("message", "Error, the prices in your freight rate table are in an impoper format");
			return null;
		}

		workbook.close();
		return result.toString();
	}
	
}
