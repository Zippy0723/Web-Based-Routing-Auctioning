package edu.sru.thangiah.webrouting.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import edu.sru.thangiah.webrouting.utilities.BackupUtil;

@Controller
public class BackupController {
	
	private String dbUsername;
	private String dbPassword;
	private String dbName;
	private String outputFile;
	
	public BackupController() {
		this.dbUsername = "root";
		this.dbPassword = "password"; //TODO: Make constructor grab these from applications.properties
		this.dbName = "webrouting";
		this.outputFile = "backups/backup.sql";
	}
	
	@Scheduled(fixedRate = 1200000) //Every twenty minutes
	public void executeBackup() {
		try {
			BackupUtil.backupDatabase(dbUsername, dbPassword, dbName, outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Redirects users to the Database restore page
	 * @param model: stores the springboot model
	 * @return /database
	 */
	@GetMapping("/database")
	public String database(Model model) {
		return "database";
	}
	
	@PostMapping("/restore-database")
	public String restoreDatabase(Model model, @RequestParam("file") MultipartFile backupFile) throws AccessException, IOException {
		File tmpFile = convertMultipartFileToFile(backupFile);
		
		System.out.println(tmpFile.getAbsolutePath());
		
		try {
			BackupUtil.restoreDatabase(dbUsername, dbPassword, dbName,tmpFile.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "database";
	}
	
	//Borrowed from https://stackoverflow.com/questions/29923682/how-does-one-specify-a-temp-directory-for-file-uploads-in-spring-boot
	//TODO: citation of code
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private File convertMultipartFileToFile(MultipartFile file) throws IOException
    {    
        File convFile = File.createTempFile("temp", ".xlsx"); // choose your own extension I guess? Filename accessible with convFile.getAbsolutePath()
        FileOutputStream fos = new FileOutputStream(convFile); 
        fos.write(file.getBytes());
        fos.close(); 
        return convFile;
    }
}
