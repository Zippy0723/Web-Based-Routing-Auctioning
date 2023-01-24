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
 * Sets up the Bids database
 * @author Logan Kirkwood	llk1005@sru.edu
 * @author Ian Black		imb1007@sru.edu
 * @since 4/8/2022
 */

@Entity
@Table(name="bids")
public class Bids {

	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
	private Long id;
	
	@Column(name="price", nullable= true, columnDefinition="varchar(16) default NULL")
	private String price;
	
	@Column(name="date", nullable= true, columnDefinition="varchar(10) default NULL")
	private String date;
	
	@Column(name="time", nullable= true, columnDefinition="varchar(11) default NULL")
	private String time;
	
	@ManyToOne
	@JoinColumn(name = "shipment_id")
	private Shipments shipment;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;

	/**
	 * Gets the ID
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the ID
	 * @param id ID of the bid
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the shipment
	 * @return shipment
	 */
	public Shipments getShipment() {
		return shipment;
	}

	/**
	 * Sets the shipment
	 * @param shipment Shipment of the bid
	 */
	public void setShipment(Shipments shipment) {
		this.shipment = shipment;
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
	 * @param carrier Carrier of the bid
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}

	/**
	 * Gets the price
	 * @return price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * Sets the price
	 * @param price Price of the bid
	 */
	public void setPrice(String price) {
		this.price = price.trim();
	}

	/**
	 * Gets the date
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date
	 * @param date Date of the bid
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the time
	 * @return time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time
	 * @param time Time of the bid
	 */
	public void setTime(String time) {
		this.time = time.trim();
	}
}
