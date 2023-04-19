package edu.sru.thangiah.webrouting.utilities;

import java.io.IOException;

import org.springframework.ui.Model;

/*
 * Some ideas for this code were taken from:
 * https://waynestalk.com/en/spring-boot-backup-restore-mysql-en/
 * TODO: Check and add proper citation if needed
 */

public class BackupUtil {

	/**
	 * Backs up the current database, exporting it as a file
	 * @param dbUsername holds the database username
	 * @param dbPassword holds the database password
	 * @param dbName holds the database name
	 * @param outputFile holds the backup file
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public static void backupDatabase(String dbUsername, String dbPassword, String dbName, String outputFile)
			throws IOException, InterruptedException {
		try {

			String command = String.format("src/main/resources/static/binaries/mysqldump -u%s -p%s --add-drop-table --databases %s -r %s",
					dbUsername, dbPassword, dbName, outputFile);

			System.out.println("Staring database backup....");
			

			Process process = Runtime.getRuntime().exec(command);
			int status = process.waitFor();

			if(status == 0) {
				System.out.println("Database backup complete!");
				
			} else {
				System.out.println("Error backing up database! Backup exited with error code " + status);
				
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restores the database from a specified file
	 * @param dbUsername holds the database username
	 * @param dbPassword holds the database password
	 * @param dbName holds the database name
	 * @param sourceFile holds the sql backup file
	 * @param model used to load attributes into the Thymeleaf model
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public static void restoreDatabase(String dbUsername, String dbPassword, String dbName, String sourceFile, Model model)
			throws IOException, InterruptedException {

		try {
			String[] command = new String[]{
					"src/main/resources/static/binaries/mysql",
					"-u" + dbUsername,
					"-p" + dbPassword,
					"-e",
					" source " + sourceFile,
					dbName
			};

			System.out.println("Loading Database from Backup");

			Process runtimeProcess = Runtime.getRuntime().exec(command);
			int status = runtimeProcess.waitFor();

			if(status == 0) {
				System.out.println("Database restore complete!");
				model.addAttribute("message", "The database was restored from the backup successfully!");
				
			} else {
				System.out.println("Error restoring database! Restore exited with error code " + status);
				model.addAttribute("message", "There was a problem loading the database! Please check your input file. Error code was " + status);
				
			}


		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
