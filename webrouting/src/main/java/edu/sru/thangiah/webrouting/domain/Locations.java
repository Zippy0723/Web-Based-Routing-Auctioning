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
import org.springframework.lang.NonNull;

/**
 * Sets up the Locations database
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */

@Entity
@Table(name="locations")
public class Locations {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@NonNull
	@Column(name="name",  columnDefinition="varchar(32)")
	private String name;
	
	@NonNull
	@Column(name="street_address1",  columnDefinition="varchar(128)")
	private String streetAddress1;
	
	@Column(name="street_address2", nullable= true, columnDefinition="varchar(64) default NULL")
	private String streetAddress2;
	
	@NonNull
	@Column(name="city",  columnDefinition="varchar(64)")
	private String city;
	
	@NonNull
	@Column(name="state",  columnDefinition="varchar(64)")
	private String state;
	
	@NonNull
	@Column(name="zip",  columnDefinition="varchar(12)")
	private String zip;
	
	@Column(name="latitude", nullable= true, columnDefinition="varchar(12) default NULL")
	private String latitude;
	
	@Column(name="longitude", nullable= true, columnDefinition="varchar(12) default NULL")
	private String longitude;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;
	
	@NonNull
	@Column(name="location_type", columnDefinition="varchar(64)")
	private String locationType;
	
	@OneToMany(mappedBy = "location")
	private List<Vehicles> vehicles = new ArrayList<>();

	/**
	 * Gets the Locations ID
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Locations ID
	 * @param id ID of the location
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the Location Name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the Location Name
	 * @param name Name of the location
	 */
	public void setName(String name) {
		this.name = name.trim();
	}

	/**
	 * Gets the Location Street Adress 1
	 * @return streetAddress1
	 */
	public String getStreetAddress1() {
		return streetAddress1;
	}

	/**
	 * Sets the Location Street Address 1
	 * @param streetAddress1 Street address 1 of the location
	 */
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1.trim();
	}

	/**
	 * Gets the Location Street Address 2
	 * @return streetAddress2
	 */
	public String getStreetAddress2() {
		return streetAddress2;
	}

	/**
	 * Sets the Location Street Address 2
	 * @param streetAddress2 Street address 2 of the location
	 */
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2.trim();
	}

	/**
	 * Gets the Location City
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the Location City
	 * @param city City of the location
	 */
	public void setCity(String city) {
		this.city = city.trim();
	}

	/**
	 * Gets the Location State
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the Location State
	 * @param state State of the location
	 */ 
	public void setState(String state) {
		this.state = state.trim();
	}

	/**
	 * Gets the Location Zip
	 * @return zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Sets the Location Zip
	 * @param zip Zip code of the location
	 */
	public void setZip(String zip) {
		this.zip = zip.trim();
	}

	/**
	 * Gets the Location Latitude
	 * @return latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Sets the Location Latitude 
	 * @param latitude Latitude coordinates of the location
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude.trim();
	}

	/**
	 * Gets the Location Longitude
	 * @return longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Sets the Location Longitude
	 * @param longitude Longitude coordinates of the location
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude.trim();
	}
	
	/**
	 * Gets the Location Carrier
	 * @return carrier
	 */
	public Carriers getCarrier() {
		return carrier;
	}

	/**
	 * Sets the Location Carrier
	 * @param carrier Carrier of the location
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}

	/**
	 * Gets the Location Type
	 * @return locationType
	 */
	public String getLocationType() {
		return locationType;
	}
	
	/**
	 * Sets the Location Type
	 * @param locationType Location type of the location
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType.trim();
	}
	
	/**
	 * Gets the Vehicles List
	 * @return vehicles
	 */
	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	/**
	 * Sets the Vehicles List
	 * @param vehicles Vehicles of the location
	 */
	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * Prints out the name of the instance of the location
	 * @return location name
	 */
	public String toString() {
		return this.getName();
	}
}
