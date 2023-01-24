package edu.sru.thangiah.webrouting.populatedata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * PopulateDatabase Implements Application Runner
 * Runs on Application Startup and populates the Database
 * @author Josh Gearhart	jjg1018@sru.edu
 * @since 9/6/2022 
 */
@Component
public class PopulateDatabase implements ApplicationRunner{
	private Statement sqlStatement;
	
	private String url;
	
	private Connection connect;
	
	@Value("${spring.datasource.username}")
	private String user;
	
	@Value("${spring.datasource.password}")
	private String pass;

	@SuppressWarnings("unused")
	private DriverManager driverManager;

	/**
	 * Constructor used for testing
	 */
	public PopulateDatabase() {
	
		}
	
	/**
	 * Populates bids table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the bids have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateBids(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO bids " + "VALUES (1,'2022-04-20','$50.00','11:03:58',1,3),"
					+ "(2,'2022-04-20','$150.00','11:52:58',1,2),"
					+ "(3,'2022-04-20','$700.00','12:23:58',1,1)");
		}
		catch(SQLException bids) {
			System.out.println(bids + " Bids have already been added");
		}
	}
	/**
	 * Populates carriers table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the carriers have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateCarriers(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO carriers " + "VALUES (1,'THANGIAH SHIPPING','Yes','Yes','24','GZBO','40000')"
					+ ",(2,'WONKA SHIPPING','Yes','Yes','24','COCO','40000')"
					+ ",(3,'WE SHIP 4 U', 'Yes','Yes','24','SH1P','40000')");
		}
		catch(SQLException carriers) {
			System.out.println(carriers + " Carriers have already been added");
		}
	}
	/**
	 * Populates contacts table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the contacts have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateContacts(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO contacts " + "VALUES (1,'kittanning','imbwwe@yahoo.com','Ian','Black','M','7248411924','PA','143 roundtop drive','','NA','16201',1),"
					+ "(2,'Ford City','BobBuilder@yahoo.com','Bob','Builder','B','7248889900','PA','153 building drive','','N/A','16226',1),"
					+ "(3,'Kittanning','CocoFactory@yahoo.com','Willy','Wonka','C','7571234567','PA','chocolate','','N/A','15252',1),"
					+ "(4,'Butler','john@yahoo.com','John','Driver','B','5555555555','PA','724 HereILive','','N/A','345345',2),"
					+ "(5,'West Kittanning','Phil@yahoo.com','Phil','Drove','C','1234567890','PA','444 drove road','','N/A','16201',2),"
					+ "(6,'Latrobe','Rob@yahoo.com','Rob','Wheeler','M','9990009898','PA','678 peachtree','','N/A','12376',2),"
					+ "(7,'Ford City','Rich@yahoo.com','Rich','White','','1114446677','PA','555 Thisway','','','234234',3),"
					+ "(8,'Butler','Ronnie@yahoo.com','Ronnie','Brown','C','724-545-7676','PA','909 whitemoon road','','','13456',3),"
					+ "(9,'Ford City','WandaTech@yahoo.com','Wanda','Dots','C','7248597676','PA','153 Window Road','','','16226',3),"
					+ "(10,'Slippery Rock','sam.thangiah@sru.edu','Sam','Thangiah','','1234567890','PA','1 Morrow Way','Gazebo','','16057',1)");
		}
		catch(SQLException contacts) {
			System.out.println(contacts + " Contacts have already been added");
		}
	}
	/**
	 * Populates driver table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the driver have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateDriver(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO driver " + "VALUES (1,'A','2022-11-16','384-374',1,10,1),"
					+ "(2,'B','2022-02-08','434-555',2,6,2),"
					+ "(3,'A','2022-02-26','454-090',3,9,3)");
		}
		catch(SQLException drivers) {
			System.out.println(drivers + " Drivers have already been added");
		}
	}
	/**
	 * Populates Locations table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the Locations have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateLocations(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO locations "+ "VALUES (1,'Butler','','Workshop','','Factory','PA','111 Chocolate street','','12121',2),"
					+ "(2,'Kittanning','','Office','','Ians House','Pennsylvania','143 roundtop drive','','16201',1),"
					+ "(3,'Pittsburgh ','','warehouse','','LogHouse','PA','654 tree lane','','12345',3)");
		}
		catch(SQLException locations) {
			System.out.println(locations + "Locations have already been added");
		}
	}
	/**
	 * Populates MaintenceOrders table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the MaintenceOrders have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateMaintenceOrders(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO maintenance_orders " + "VALUES (1,'$5000.45','Brake Replacement','Brakes','2022-02-26','Repair','Pending',1,2,2),"
					+ "(2,'$50.00','Oil Change','Oil','2022-02-23','Maintenance','Completed ',1,1,3)");
		}
		catch(SQLException maintenanceOrders) {
			System.out.println(maintenanceOrders + "Maintenance Orders have already been created");
		}
	}
	/**
	 * Populates shipments table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the shipments have already been added
	 * @author Josh Gearhart	jjg1018@sru,edu
	 */
	public void populateShipments(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO shipments " + "VALUES (1,'STBLLC','FTL','15','1000','52','Inwood','39.3578781','78.0399994','WV','25428','','SHIPMENT AVAILABLE','','','2022-04-17','AVON','41.4517093','-82.0354225','OH','44011',NULL,4,NULL),"
					+ "(2,'STBLLC','LTL','15','1000','52','CHEYENNE','41.079185','-104.7987710','WY','82007','','SHIPMENT AVAILABLE','','','2022-12-25','PHOENIX','33.43040260','33.43040260','AZ','85043',NULL,4,NULL),"
					+ "(3,'STBLLC','FTL','15','1000','52','LANDER','42.8330140','-108.7306725','WY','82520','3587239','BID ACCEPTED','$50.00','','2022-07-26','OKLAHOMA CITY','35.5638694','-97.4705935','OK','73131',1,4,1)");
		}
		catch(SQLException shipments) {
			System.out.println(shipments + " Shipments already inserted");
		}
	}
	/**
	 * Populates technicians table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the technicians have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateTechnicians(Statement statement) {
		try {
		statement.executeUpdate("INSERT INTO technicians " + "VALUES (1,'A',3),(2,'B',5)");
		}
		catch(SQLException technicians) {
			System.out.println(technicians + " technicians already inserted");
		}
	}
	/**
	 * Populates Vehicles table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the vehicles have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateVehicle(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO vehicles " + "VALUES (1,'1980','DEC-DEC1','4B7DH3LDJNEE945D',1,2,3),"
					+ "(2,'1995','APR-APR1','9F2AA2LDJMYT11R',3,3,4),"
					+ "(3,'2010','JAN-JAN1','8O9DH3LAVBNN343T',2,1,1),"
					+ "(4,'2014','MAR-MAR2','1O1DH3LMWTRE675T',1,2,5)");
		}
		catch(SQLException vehicles) {
			System.out.println(vehicles + " vehicles already inserted");
		}
	}
	/**
	 * Populates Vehicle Types table
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the vehicle types have already been added
	 * @author Josh Gearhart & Nick Bushee	jjg1018@sru.edu	nab1017@sru.edu
	 */
	public void populateVehicleTypes(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO vehicle_types " + "VALUES (1,'3000','',10000,80,85,'FreightLiner',0,400,80000,0,20000,'FL-384DK','N/A','Open','Tanker'),"
					+ "(2,'','',7000,90,95,'Mack',0,400,40000,0,20000,'M-DJ48DC','','Closed','Flatbed'),"
					+ "(3,'','',0,70,80,'FreightLiner',0,200,30000,0,10000,'FL-20134','No Dirt Roads','Open','Flatbed'),"
					+ "(4,'','',225,20,25,'Ford',0,500,1500,0,500,'F150','N/A','Super','Truck'),"
					+ "(5,'','',100,10,13,'Ford',0,700,400,0,100,'Focus','','4 door','Car'),"
					+ "(6,'','',10000,87,90,'Mack',0,300,65000,0,20000,'M-DD44DC','N/A','Closed','Tanker')");
		}
		catch(SQLException vehicleTypes) {
			System.out.println(vehicleTypes +" Vehicle Types have already been added");
		}
	}
	/**
	 * Populates the roles table with specified roles
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the roles have already been added
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void populateRolesTable(Statement statement)  {
		try {
			statement.executeUpdate("INSERT INTO role " + "VALUES (1,'ADMIN'),"
					+ "(2,'SHIPPER'),(3,'CARRIER'),(4,'MASTERLIST')");
			}
		catch(SQLException Roles){
			System.out.println(Roles + " Roles have been already added");
		}
	}
	
	/**
	 * Populates the user table with two users
	 * @param statement - used to connect to the sql database and make updates
	 * @throws SQLException if the users have already been created
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void populateUser(Statement statement) {
		try {
			statement.executeUpdate("INSERT INTO user " + "VALUES"
					+ "(1,'AdminTry@Gmail.com',1,NULL,'$2a$10$1czLv.unEEJZMLTIS2sTxe6DL7CuopPOcCCEGmKwak3H4KFfUBVOm','AdminTry',NULL,NULL,1),"
					+ "(2,'Master@gmail.com',1,NULL,'$2a$10$kijCY6WxZXMfsJ4NcW3SkOqeB6BDiXyR3Pmk7UExr1rJAjVmECF7i','Master',NULL,NULL,4),"
					+ "(3,'carrier@gmail.com',1,NULL,'$2a$10$EVBsfb2HGqaMlI9z443kR.zM.Tn66fT.7nbvsVhDeqAw.fc4HyXOG','Carrier',NULL,1,3),"
					+ "(4,'shipper@gmail.com',1,NULL,'$2a$10$j4JWTe7EP6vPiptRf1WP1ujvRRNVPzvuQXkO7KH9Ot.YBY0znyKam','Shipper',NULL,NULL,2),"
					+ "(5,'wonka@gmail.com',1,NULL,'$2a$10$xcIr8bwuPpCA5/dRvIOmMuVpmFZ.i18oFA5qnaQ8eiNgVKp/L.T9K','WillyWonka',NULL,2,3),"
					+ "(6,'ship4u@gmail.com',1,NULL,'$2a$10$0mjUJWe24cNS.XoMLb/Ybur3EK0ps.787omMxs4y3DbGB2y6StgNC','Ship4U',NULL,3,3)");
		}
		catch(SQLException Users) {
			System.out.println(Users + " Users have already been created");
		} 
	}
	
	/**
	 * Creates the connection to the database
	 * @return connect - connection used to connect to our database
	 * @throws SQLException
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public Connection connectToDatabase() throws SQLException {
		url = "jdbc:mysql://localhost/webrouting";
		connect = DriverManager.getConnection(url, user, pass);
		return connect;
	}
	
	public Statement setStatement(Connection connect) throws SQLException {
		sqlStatement = connect.createStatement();
		return sqlStatement;
	}
	
	/**
	 * Method that calls our populating user and roles methods calls the create method to make the connection 
	 * then passes connection to populating methods
	 * @throws SQLException
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void populateDb() throws SQLException {
		System.out.println("**** Starting Database Population ****");
		sqlStatement = setStatement(connectToDatabase());
		System.out.println("Populating Carriers Table May Throw error if already added");
		populateCarriers(sqlStatement);
		
		System.out.println("Populating Contacts Table May Throw error if already added");
		populateContacts(sqlStatement);
		
		System.out.println("Populating Locations Table May Throw error if already added");
		populateLocations(sqlStatement);
		
		System.out.println("Populating Roles table May Throw error if already added");
		populateRolesTable(sqlStatement);
		
		System.out.println("Populating Technicians Table May Throw error if already added");
		populateTechnicians(sqlStatement);
		
		System.out.println("Populating User Table May Throw error if already added");
		populateUser(sqlStatement);
		
		System.out.println("Populating VehicleTypes Table May Throw error if already added");
		populateVehicleTypes(sqlStatement);
		
		System.out.println("Populating Vehicle Table May Throw error if already added");
		populateVehicle(sqlStatement);
		
		System.out.println("Populating Drivers Table May Throw error if already added");
		populateDriver(sqlStatement);
		
		System.out.println("Populating Shipments Table May Throw error if already added");
		populateShipments(sqlStatement);
		
		System.out.println("Populating Maintence Orders Table  May Throw error if already added");
		populateMaintenceOrders(sqlStatement);
		
		System.out.println("Populating Bids Table May Throw error if already added");
		populateBids(sqlStatement);
		
		System.out.println("**** Finished Populating Database, Program Ready! ***** ");
		
	}
	/**
	 * Method from Application Runner that runs the code in the method on application start up
	 * @throws Exception - Exceptions in programs
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
	populateDb();
		
	}

}
