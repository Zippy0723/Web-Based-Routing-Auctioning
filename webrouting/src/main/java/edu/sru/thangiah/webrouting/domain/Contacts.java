	package edu.sru.thangiah.webrouting.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Sets up the Contacts database
 * @author Ian Black		img1007@sru.edu
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */

@Entity
@Table(name="contacts")
public class Contacts {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@Column(name="first_name", nullable= true, columnDefinition="varchar(32) default NULL")
	private String firstName;
	
	@Column(name="last_name", nullable= true, columnDefinition="varchar(32) default NULL")
	private String lastName;
	
	@Column(name="middle_initial", nullable= true, columnDefinition="varchar(1) default NULL")
	private String middleInitial;
	
	@Column(name="email_address", nullable= true, columnDefinition="varchar(64) default NULL")
	private String emailAddress;
	
	@Column(name="street_address1", nullable= true, columnDefinition="varchar(128) default NULL")
	private String streetAddress1;
	
	@Column(name="street_address2", nullable= true, columnDefinition="varchar(64) default NULL")
	private String streetAddress2;
	
	@Column(name="city", nullable= true, columnDefinition="varchar(64) default NULL")
	private String city;
	
	@Column(name="state", nullable= true, columnDefinition="varchar(64) default NULL")
	private String state;
	
	@Column(name="zip", nullable= true, columnDefinition="varchar(12) default NULL")
	private String zip;
	
	@Column(name="primary_phone", nullable= true, columnDefinition="varchar(64) default NULL")
	private String primaryPhone;
	
	@Column(name="work_phone", nullable= true, columnDefinition="varchar(64) default NULL")
	private String workPhone;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;
	
	@OneToMany(mappedBy = "contact")
	private List<Driver> drivers = new ArrayList<>();
	
	@OneToMany(mappedBy = "contact")
	private List<Technicians> technicians = new ArrayList<>();

	/**
	 * Gets the Contact ID
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Contact ID
	 * @param id ID of the contact
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the Contact First Name
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the Contact First Name
	 * @param firstName First name of the contact
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	/**
	 * Gets the Contact Last Name
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the Contact Last Name
	 * @param lastName Last name of the contact
	 */ 
	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	/**
	 * Gets the Contact Middle Initial
	 * @return middleInitial
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}

	/**
	 * Sets the Contact Middle Initial
	 * @param middleInitial Middle initial of the contact
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial.trim();
	}

	/**
	 * Gets the Contact Email Address
	 * @return emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the Contact Email Address
	 * @param emailAddress Email address of the contact
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress.trim();
	}

	/**
	 * Gets the Contact Street Address 1
	 * @return streetAddress1
	 */
	public String getStreetAddress1() {
		return streetAddress1;
	}

	/**
	 * Sets the Contact Street Address 1
	 * @param streetAddress1 Street address 1 of the contact
	 */
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1.trim();
	}

	/**
	 * Gets the Contact Street Adress 2
	 * @return streetAddress2
	 */
	public String getStreetAddress2() {
		return streetAddress2;
	}

	/**
	 * Sets the Contact Street Adress 2
	 * @param streetAddress2 Street address 2 of the contact
	 */
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2.trim();
	}

	/**
	 * Gets the Contact City
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the Contact City
	 * @param city City of the contact
	 */
	public void setCity(String city) {
		this.city = city.trim();
	}

	/**
	 * Gets the Contact State
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the Contact State
	 * @param state State of the contact
	 */
	public void setState(String state) {
		this.state = state.trim();
	}

	/**
	 * Gets the Contact Zip
	 * @return zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Sets the Contact Zip
	 * @param zip Zip code of the contact
	 */
	public void setZip(String zip) {
		this.zip = zip.trim();
	}

	/**
	 * Gets the Contact Primary Phone
	 * @return primaryPhone 
	 */
	public String getPrimaryPhone() {
		return primaryPhone;
	}

	/**
	 * Sets the Contact Primary Phone
	 * @param primaryPhone Primary phone of the contact
	 */
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone.trim();
	}

	/**
	 * Gets the Contact Work Phone
	 * @return workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * Sets the Contact Work Phone
	 * @param workPhone Work phone of the contact
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone.trim();
	}

	/**
	 * Gets the carrier
	 * @return carrier
	 */
	public Carriers getCarrier() {
		return carrier;
	}

	/**
	 * Sets the carrier
	 * @param carrier Carrier of the contact
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}

	/**
	 * Gets the Drivers List
	 * @return drivers
	 */
	public List<Driver> getDrivers() {
		return drivers;
	}

	/**
	 * Sets the Drivers List
	 * @param drivers Drivers of the contact
	 */
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	/**
	 * Gets the Technicians List
	 * @return technicians
	 */
	public List<Technicians> getTechnicians() {
		return technicians;
	}

	/**
	 * Sets the Technicians List
	 * @param technicians Technicians of the contact
	 */
	public void setTechnicians(List<Technicians> technicians) {
		this.technicians = technicians;
	}

	/**
	 * Prints out the first and last name of the instance of the contact
	 * @return contact first name and last name
	 */
	public String toString() {
		return this.getFirstName() + " " + this.getLastName();
	}
}
