package edu.sru.thangiah.webrouting.controller;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sru.thangiah.webrouting.utilities.BackupUtil;

@RestController
public class BackupController {
	
	private String dbUsername;
	private String dbPassword;
	private String dbName;
	private String outputFile;
	
	public BackupController() {
		this.dbUsername = "root";
		this.dbPassword = "password";
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
	 * Redirects users to the Database restore confirmation page
	 * @param model: stores the springboot model
	 * @return /restoredatabaseconfirm
	 */
	@RequestMapping("/restoredatabase")
	public String restoreDatabase(Model model) {
		return "/reset/restoredatabaseconfirm";
	}
	
}
