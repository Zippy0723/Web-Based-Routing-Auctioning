package edu.sru.thangiah.webrouting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Sets up the Maintenance Order database
 * @author Ian Black		imb1007@sru.edu
 * @since 2/8/2022
 */

@Entity
@Table(name="maintenance_orders")
public class MaintenanceOrders {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
	private long id;

	@ManyToOne
	@JoinColumn(name = "technician_id")
	private Technicians technician;
	
	@Column(name="scheduled_date", nullable= true, columnDefinition="varchar(12) default NULL")
	private String scheduled_date;
	
	@Column(name="details", nullable= true, columnDefinition="varchar(128) default NULL")
	private String details;
	
	@Column(name="service_type_key", nullable= false, columnDefinition="varchar(12)")
	private String service_type_key;
	
	@Column(name="cost", nullable= true, columnDefinition="varchar(16) default NULL")
	private String cost;
	
	@Column(name="status_key", nullable= false, columnDefinition="varchar(64)")
	private String status_key;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicles vehicle;
	
	@Column(name="maintenance_type", nullable= false, columnDefinition="varchar(64)")
	private String maintenance_type;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;

	/**
	 * Gets the Maintenance Order ID
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Maintenance Order ID
	 * @param id ID of hte maintenance order
	 */
	public void setId(long id) {
		this.id = id;
	}


	/**
	 * Gets the Maintenance Order Technician
	 * @return technician
	 */
	public Technicians getTechnician() {
		return technician;
	}

	/**
	 * Sets the Maintenance Order Technician
	 * @param technician Technician of the maintenance order
	 */
	public void setTechnician(Technicians technician) {
		this.technician = technician;
	}

	/**
	 * Gets the Maintenance Order Scheduled Date
	 * @return scheduled_date
	 */
	public String getScheduled_date() {
		return scheduled_date;
	}

	/**
	 * Sets the Maintenance Order Scheduled Date
	 * @param scheduled_date Scheduled date of the maintenance order
	 */
	public void setScheduled_date(String scheduled_date) {
		this.scheduled_date = scheduled_date.trim();
	}

	/**
	 * Gets the Maintenance Order Details
	 * @return details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets the Maintenance Order Details
	 * @param details Details of the maintenance order
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * Gets the Maintenance Order Service Type Key
	 * @return service_type_key
	 */
	public String getService_type_key() {
		return service_type_key;
	}

	/**
	 * Sets the Maintenance Order Service Type Key
	 * @param service_type_key Service type of the maintenance order
	 */
	public void setService_type_key(String service_type_key) {
		this.service_type_key = service_type_key.trim();
	}

	/**
	 * Gets the Maintenance Order Cost
	 * @return cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * Sets the Maintenance Order Cost
	 * @param cost Cost of the maintenance order
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}

	/**
	 * Gets the Maintenance Order Status Key
	 * @return status_key
	 */
	public String getStatus_key() {
		return status_key;
	}

	/**
	 * Sets the Maintenance Order Status Key
	 * @param status_key Status of the maintenance order
	 */
	public void setStatus_key(String status_key) {
		this.status_key = status_key.trim();
	}

	/**
	 * Gets the Maintenance Order Vehicle
	 * @return vehicle
	 */
	public Vehicles getVehicle() {
		return vehicle;
	}

	/**
	 * Sets the Maintenance Order Vehicle
	 * @param vehicle Vehicle of the maintenance order
	 */
	public void setVehicle(Vehicles vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * Gets the Maintenance Order Type
	 * @return maintenance_type
	 */
	public String getMaintenance_type() {
		return maintenance_type;
	}

	/**
	 * Sets the Maintenance Order Type
	 * @param maintenance_type Type of the maintenance order
	 */
	public void setMaintenance_type(String maintenance_type) {
		this.maintenance_type = maintenance_type.trim();
	}

	/**
	 * Gets the Maintenance Order carrier
	 * @return carrier
	 */
	public Carriers getCarrier() {
		return carrier;
	}

	/**
	 * Sets the Maintenance Order carrier
	 * @param carrier Maintenance Order Carrier
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}
	
	public String toString() {
		return details + " for " + vehicle.toString();
	}
}
