package edu.sru.thangiah.webrouting.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Sets up the Carriers database
 * @author Logan Kirkwood	llk1005@sru.edu
 * @author Ian Black		imb1007@sru.edu
 * @author Fady Aziz		faa1002@sru.edu
 * @since 3/21/2022
 */

@Entity
@Table(name="carriers")
public class Carriers {

	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
	private Long id;
	
	@Column(name="carrier_name", nullable= true, columnDefinition="varchar(128) default NULL")
	private String carrierName;
	
	@Column(name="scac", nullable= true, columnDefinition="varchar(4) default NULL")
	private String scac;
	
	@Column(name="ltl", nullable= true, columnDefinition="varchar(3) default NULL")
	private String ltl;
	
	@Column(name="ftl", nullable= true, columnDefinition="varchar(3) default NULL")
	private String ftl;
	
	@Column(name="pallets", nullable= true, columnDefinition="varchar(32) default NULL")
	private String pallets;
	
	@Column(name="weight", nullable= true, columnDefinition="varchar(32) default NULL")
	private String weight;
	
	@OneToMany(mappedBy = "carrier")
	private List<Contacts> contacts = new ArrayList<>();
	
	@OneToMany(mappedBy = "carrier")
	private List<Shipments> shipments = new ArrayList<>();
	
	@OneToMany(mappedBy = "carrier")
	private List<Bids> bids = new ArrayList<>();
	
	@OneToMany(mappedBy = "carrier")
	private List<Locations> locations = new ArrayList<>();
	
	@OneToMany(mappedBy = "carrier")
	private List<Vehicles> vehicles = new ArrayList<>();
	
	@OneToMany(mappedBy = "carrier")
	private List<Driver> drivers = new ArrayList<>();
	
	@OneToMany(mappedBy = "carrier")
	private List<MaintenanceOrders> orders = new ArrayList<>();

	/**
	 * Gets the Carrier ID
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Carrier ID
	 * @param id ID of the carrier
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the carrier name
	 * @return carrierName
	 */
	public String getCarrierName() {
		return carrierName;
	}

	/**
	 * Sets the carrier name
	 * @param carrierName Name of the carrier
	 */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName.trim();
	}

	/**
	 * Gets the SCAC
	 * @return scac
	 */
	public String getScac() {
		return scac;
	}

	/**
	 * Sets the SCAC
	 * @param scac SCAC of the carrier
	 */
	public void setScac(String scac) {
		this.scac = scac.trim();
	}

	/**
	 * Gets the LTL
	 * @return ltl
	 */
	public String getLtl() {
		return ltl;
	}

	/**
	 * Sets the LTL
	 * @param ltl Whether or not the carrier offers LTL
	 */
	public void setLtl(String ltl) {
		this.ltl = ltl.trim();
	}

	/**
	 * Gets the FTL
	 * @return ftl 
	 */
	public String getFtl() {
		return ftl;
	}

	/**
	 * Sets the FTL
	 * @param ftl Whether or not the carrier offers FTL
	 */
	public void setFtl(String ftl) {
		this.ftl = ftl.trim();
	}

	/**
	 * Gets the pallets
	 * @return pallets
	 */
	public String getPallets() {
		return pallets;
	}

	/**
	 * Sets the pallets
	 * @param pallets How many pallets the carrier can handle
	 */
	public void setPallets(String pallets) {
		this.pallets = pallets.trim();
	}

	/**
	 * Gets the weight
	 * @return weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * Sets the weight
	 * @param weight Weight that the carrier can handle
	 */
	public void setWeight(String weight) {
		this.weight = weight.trim();
	}

	/**
	 * Gets the contacts
	 * @return contacts
	 */
	public List<Contacts> getContacts() {
		return contacts;
	}

	/**
	 * Sets the contacts
	 * @param contacts Contacts of the carrier
	 */
	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	/**
	 * Gets the shipments
	 * @return shipments
	 */
	public List<Shipments> getShipments() {
		return shipments;
	}

	/**
	 * Sets the shipments
	 * @param shipments Shipments of the carrier
	 */
	public void setShipments(List<Shipments> shipments) {
		this.shipments = shipments;
	}

	/**
	 * Gets the bids
	 * @return bids
	 */
	public List<Bids> getBids() {
		return bids;
	}

	/**
	 * Sets the bids
	 * @param bids Bids of the carrier
	 */
	public void setBids(List<Bids> bids) {
		this.bids = bids;
	}

	/**
	 * Gets the locations
	 * @return locations
	 */
	public List<Locations> getLocations() {
		return locations;
	}

	/**
	 * Sets the locations
	 * @param locations Locations of the carrier
	 */
	public void setLocations(List<Locations> locations) {
		this.locations = locations;
	}

	/**
	 * Gets the vehicles
	 * @return vehicles
	 */
	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	/**
	 * Sets the vehicles
	 * @param vehicles Vehicles of the carrier
	 */
	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * Gets the drivers
	 * @return drivers
	 */
	public List<Driver> getDrivers() {
		return drivers;
	}

	/**
	 * Sets the drivers
	 * @param drivers Drivers of the carrier
	 */
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	/**
	 * Gets the orders
	 * @return orders
	 */
	public List<MaintenanceOrders> getOrders() {
		return orders;
	}

	/**
	 * Sets the orders
	 * @param orders Orders of the carrier
	 */
	public void setOrders(List<MaintenanceOrders> orders) {
		this.orders = orders;
	}

	/**
	 * toString method returns all of the values inside of the carrier.
	 * @return carrier information
	 */
	@Override
	public String toString() {
		return "Carrier: " + "Carrier Name = " + carrierName + ", SCAC = " + scac + ", LTL = " + ltl
				+ ", FTL = " + ftl + ", Pallets = " + pallets + ", Weight = " + weight;
	}
}


