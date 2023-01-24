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
import org.springframework.lang.NonNull;

/**
 * Sets up the Vehicles Types database
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 1/30/2022
 */

@Entity
@Table(name="vehicle_types")
public class VehicleTypes {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@NonNull
	@Column(name="type", nullable = false,columnDefinition="varchar(32)")
	private String type;
	
	@NonNull
	@Column(name="sub_type", nullable = false,columnDefinition="varchar(32)")
	private String subType;
	
	@Column(name="description", nullable= true, columnDefinition="varchar(64) default NULL")
	private String description;
	
	@NonNull
	@Column(name="make",nullable = false, columnDefinition="varchar(32)")
	private String make;
	
	@NonNull
	@Column(name="model",nullable = false, columnDefinition="varchar(32)")
	private String model;
	
	@NonNull
	@Column(name="minimum_weight",nullable = false, columnDefinition="varchar(16)")
	private int minimumWeight;
	
	@NonNull
	@Column(name="maximum_weight", nullable = false,columnDefinition="varchar(16)")
	private int maximumWeight;
	
	@Column(name="capacity", nullable= true, columnDefinition="varchar(16) default NULL")
	private String capacity;
	
	@NonNull
	@Column(name="maximum_range", nullable = false, columnDefinition="varchar(16)")
	private int maximumRange;
	
	@Column(name="restrictions", nullable= true, columnDefinition="varchar(128) default NULL")
	private String restrictions;
	
	@NonNull
	@Column(name="height", nullable = false, columnDefinition="varchar(16)")
	private int height;
	
	@NonNull
	@Column(name="empty_weight", nullable = false, columnDefinition="varchar(16)")
	private int emptyWeight;
	
	@NonNull
	@Column(name="length", columnDefinition="varchar(16)")
	private int length;
	
	@Column(name="minimum_cubic_weight", nullable= false, columnDefinition="varchar(16)")
	private int minimumCubicWeight;
	
	@Column(name="maximum_cubic_weight", nullable= false, columnDefinition="varchar(16)")
	private int maximumCubicWeight;
	
	@OneToMany(mappedBy = "vehicleType")
	private List<Vehicles> vehicles = new ArrayList<>();

	/**
	 * Gets the Vehicle Type ID
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Vehicle Type ID
	 * @param id ID of the vehicle type
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the Vehicle Type
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the Vehicle Type
	 * @param type Type of vehicle type
	 */ 
	public void setType(String type) {
		this.type = type.trim();
	}

	/**
	 * Gets the Vehicle Sub Type
	 * @return subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * Sets the Vehicle Sub Type
	 * @param subType Sub type of vehicle type
	 */
	public void setSubType(String subType) {
		this.subType = subType.trim();
	}

	/**
	 * Gets the Vehicle Type Description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Vehicle Type Description
	 * @param description Description of vehicle type
	 */
	public void setDescription(String description) {
		this.description = description.trim();
	}

	/**
	 * Gets the Vehicle Type Make
	 * @return make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the Vehicle Type Make
	 * @param make Make of vehicle type
	 */
	public void setMake(String make) {
		this.make = make.trim();
	}

	/**
	 * Gets the Vehicle Type Model
	 * @return model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the Vehicle Type Model
	 * @param model Model of vehicle type
	 */
	public void setModel(String model) {
		this.model = model.trim();
	}

	/**
	 * Gets the Vehicle Type Minimum Weight
	 * @return minimumWeight
	 */
	public int getMinimumWeight() {
		return minimumWeight;
	}

	/**
	 * Sets the Vehicle Type Minimum Weight
	 * @param minimumWeight Minimum weight of vehicle type
	 */
	public void setMinimumWeight(int minimumWeight) {
		this.minimumWeight = minimumWeight;
	}

	/**
	 * Gets the Vehicle Type Maximum Weight
	 * @return maximumWeight
	 */
	public int getMaximumWeight() {
		return maximumWeight;
	}

	/**
	 * Sets the Vehicle Type Maximum Weight
	 * @param maximumWeight Maximum weight of vehicle type
	 */
	public void setMaximumWeight(int maximumWeight) {
		this.maximumWeight = maximumWeight;
	}

	/**
	 * Gets the Vehicle Type Capacity 
	 * @return capacity
	 */
	public String getCapacity() {
		return capacity;
	}

	/**
	 * Sets the Vehicle Type Capcity
	 * @param capacity Capacity of vehicle type
	 */
	public void setCapacity(String capacity) {
		this.capacity = capacity.trim();
	}

	/**
	 * Gets the Vehicle Type Maximum Range
	 * @return maximumRange
	 */
	public int getMaximumRange() {
		return maximumRange;
	}

	/**
	 * Sets the Vehicle Type Maximum Range
	 * @param maximumRange Maximum range of vehicle type
	 */
	public void setMaximumRange(int maximumRange) {
		this.maximumRange = maximumRange;
	}

	/**
	 * Gets the Vehicle Type Restrictions
	 * @return restrictions
	 */
	public String getRestrictions() {
		return restrictions;
	}

	/**
	 * Sets the Vehicle Type Restrictions
	 * @param restrictions Restrictions of vehicle type
	 */
	public void setRestrictions(String restrictions) {
		this.restrictions = restrictions.trim();
	}

	/**
	 * Gets the Vehicle Type Height
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the Vehicle Type Height
	 * @param height Height of vehicle type
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the Vehicle Type Empty Weight
	 * @return emptyWeight
	 */
	public int getEmptyWeight() {
		return emptyWeight;
	}

	/**
	 * Sets the Vehicle Type Empty Weight
	 * @param emptyWeight Empty weight of vehicle type
	 */
	public void setEmptyWeight(int emptyWeight) {
		this.emptyWeight = emptyWeight;
	}

	/**
	 * Get the Vehicle Type Length
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set the Vehicle Type Length
	 * @param length Length of vehicle type
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Gets the Vehicle Type Minimum Cubic Weight
	 * @return minimumCubicWeight
	 */
	public int getMinimumCubicWeight() {
		return minimumCubicWeight;
	}

	/**
	 * Sets the Vehicle Type Minimum Cubic Weight
	 * @param minimumCubicWeight Minimum cubic weight of vehicle type
	 */
	public void setMinimumCubicWeight(int minimumCubicWeight) {
		this.minimumCubicWeight = minimumCubicWeight;
	}

	/**
	 * Gets the Vehicle Type Maximum Cubic Weight
	 * @return maximumCubicWeight
	 */
	public int getMaximumCubicWeight() {
		return maximumCubicWeight;
	}

	/**
	 * Sets the Vehicle Type Maximum Cubic Weight
	 * @param maximumCubicWeight Maximum weight of vehicle type
	 */
	public void setMaximumCubicWeight(int maximumCubicWeight) {
		this.maximumCubicWeight = maximumCubicWeight;
	}
	
	/**
	 * Gets the Vehicle Type Vehicles
	 * @return vehicles
	 */
	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	/**
	 * Sets the Vehicle Type Vehicles
	 * @param vehicles Vehicles of the vehicle type
	 */
	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * Prints out the make, model, and type of the instance of the vehicle type
	 * @return make, model, and type
	 */
	public String toString() {
		return this.getMake() + " " + this.getModel() + " - " + this.getType();
	}
	
}
