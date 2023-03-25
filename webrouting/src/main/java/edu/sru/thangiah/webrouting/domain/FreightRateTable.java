package edu.sru.thangiah.webrouting.domain;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="freightratetable")
public class FreightRateTable {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;
	
	@Column(name="weight_table", nullable=false, columnDefinition="varbinary(max)")
	private ArrayList<Long> weightTable;
	
	//@Column(name="rate_table", nullable=false, columnDefinition="varbinary(max)")
//	private HashMap<String, ArrayList<Long>> rateTable;
	
	public FreightRateTable() {};

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the weightTable
	 */
	public ArrayList<Long> getWeightTable() {
		return weightTable;
	}

	/**
	 * @param weightTable the weightTable to set
	 */
	public void setWeightTable(ArrayList<Long> weightTable) {
		this.weightTable = weightTable;
	}

	/**
	 * @return the rateTable
	 */
//	public HashMap<String, ArrayList<Long>> getRateTable() {
//		return rateTable;
//	}

	/**
	 * @param rateTable the rateTable to set
	 */
//	public void setRateTable(HashMap<String, ArrayList<Long>> rateTable) {
//		this.rateTable = rateTable;
//	}

	/**
	 * @return the carrier
	 */
	public Carriers getCarrier() {
		return carrier;
	}

	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}
	
	
}
