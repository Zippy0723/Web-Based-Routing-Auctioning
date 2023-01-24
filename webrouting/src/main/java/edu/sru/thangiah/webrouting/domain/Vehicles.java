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
 * Sets up the Vehicles database
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */


@Entity
@Table(name="vehicles")
public class Vehicles {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@NonNull
	@Column(name="plate_number", columnDefinition="varchar(12) default NULL")
	private String plateNumber;
	
	@NonNull
	@Column(name="vin_number", columnDefinition="varchar(17) default NULL")
	private String vinNumber;
	
	@NonNull
	@Column(name="manufactured_year", columnDefinition="varchar(4) default NULL")
	private String manufacturedYear;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_type_id")
	private VehicleTypes vehicleType;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Locations location;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;
	
	@OneToMany(mappedBy = "vehicle")
	private List<MaintenanceOrders> orders = new ArrayList<>();
	
	@OneToMany(mappedBy = "vehicle")
	private List<Shipments> shipments = new ArrayList<>();
	
	@OneToMany(mappedBy = "vehicle")
	private List<Driver> drivers = new ArrayList<>();

	/**
	 * Gets Vehicle ID
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Vehicle ID
	 * @param id ID of the vehicle
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the Vehicle Lisence Plate Number
	 * @return plateNumber
	 */
	public String getPlateNumber() {
		return plateNumber;
	}

	/**
	 * Sets the Vehicle Plate Number
	 * @param plateNumber Plate number of the vehicle 
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber.trim();
	}

	/**
	 * Gets the Vehicle VIN
	 * @return vinNumber
	 */
	public String getVinNumber() {
		return vinNumber;
	}

	/**
	 * Sets the Vehicle VIN
	 * @param vinNumber VIN of the vehicle
	 */
	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber.trim();
	}

	/**
	 * Gets the Vehicle Manufactured Year
	 * @return manufacturedYear
	 */
	public String getManufacturedYear() {
		return manufacturedYear;
	}

	/**
	 * Sets the Vehicle Manufactured Year
	 * @param manufacturedYear Manufactured year of the vehicle
	 */
	public void setManufacturedYear(String manufacturedYear) {
		this.manufacturedYear = manufacturedYear.trim();
	}

	/**
	 * Gets the Vehicle Type
	 * @return vehicleType
	 */
	public VehicleTypes getVehicleType() {
		return vehicleType;
	}

	/**
	 * Sets the Vehicle Type
	 * @param vehicleType Vehicle type of the vehicle
	 */
	public void setVehicleType(VehicleTypes vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * Gets the Vehicle Location
	 * @return location
	 */
	public Locations getLocation() {
		return location;
	}

	/**
	 * Sets the Vehicle Location
	 * @param location Location of the vehicle
	 */
	public void setLocation(Locations location) {
		this.location = location;
	}

	/**
	 * Gets the Vehicle Carrier
	 * @return carrier
	 */
	public Carriers getCarrier() {
		return carrier;
	}

	/**
	 * Sets the Vehicle Carrier
	 * @param carrier Carrier of the vehicle
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}
	
	/**
	 * Gets the Vehicle Orders List
	 * @return orders
	 */
	public List<MaintenanceOrders> getOrders() {
		return orders;
	}

	/**
	 * Sets the Vehicle Orders List
	 * @param orders Maintenance orders of the vehicle
	 */
	public void setOrders(List<MaintenanceOrders> orders) {
		this.orders = orders;
	}

	/**
	 * Gets the Vehicle Shipments List
	 * @return shipments
	 */
	public List<Shipments> getShipments() {
		return shipments;
	}

	/**
	 * Sets the Vehicle Shipments List
	 * @param shipments Shipments of the vehicle
	 */
	public void setShipments(List<Shipments> shipments) {
		this.shipments = shipments;
	}

	/**
	 * Gets the Vehicle Drivers List
	 * @return drivers
	 */
	public List<Driver> getDrivers() {
		return drivers;
	}

	/**
	 * Sets the Vehicle Drivers List
	 * @param drivers Drivers of the vehicle
	 */
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	/**
	 * Prints out the vehicle type and the plate number of the instance of the vehicles
	 * @return vehicle type and plate number
	 */
	public String toString() {
		return this.getVehicleType().toString() + " Plate: " + this.getPlateNumber();
	}
}
