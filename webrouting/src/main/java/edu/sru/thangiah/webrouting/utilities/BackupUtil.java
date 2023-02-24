package edu.sru.thangiah.webrouting.utilities;

import java.io.IOException;

/*
 * Some ideas for this code were taken from:
 * https://waynestalk.com/en/spring-boot-backup-restore-mysql-en/
 * TODO: Check and add proper citation if needed
 */

public class BackupUtil {

	public static void backupDatabase(String dbUsername, String dbPassword, String dbName, String outputFile)
            throws IOException, InterruptedException {
		try {
		
			String command = String.format("src/main/resources/static/binaries/mysqldump -u%s -p%s --add-drop-table --databases %s -r %s",
					dbUsername, dbPassword, dbName, outputFile);
		 
			System.out.println("Staring database backup....");
			//TODO: Dakota logger
			
			Process process = Runtime.getRuntime().exec(command);
			int status = process.waitFor();
			
			if(status == 0) {
				System.out.println("Database backup complete!");
				//TODO: Dakota logger
			} else {
				System.out.println("Error backing up database! Backup exited with error code " + status);
				//TODO: Dakota logger
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void restoreDatabase(String dbUsername, String dbPassword, String dbName, String sourceFile)
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
				//TODO: Dakota logger
			} else {
				System.out.println("Error restoring database! Restore exited with error code " + status);
				//TODO: Dakota logger
			}
	        
	        
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
