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
 * Sets up the Shipments database
 * @author Ian Black		imb1007@sru.edu
 * @author Logan Kirkwood	llk1005@sru.edu
 * @author Fady Aziz		faa1002@sru.edu
 * @since 2/8/2022
 */

@Entity
@Table(name="shipments")
public class Shipments {

	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
	private Long id;
	
	@Column(name="client", nullable= false, columnDefinition="varchar(64)")
	private String client;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;
	
	@Column(name="scac", nullable= false, columnDefinition="varchar(4)")
	private String scac;
	
	@Column(name="client_mode", nullable= false, columnDefinition="varchar(3)")
	private String clientMode;
	
	@Column(name="ship_date", nullable= false, columnDefinition="varchar(12)")
	private String shipDate;
	
	@Column(name="freightbill_number", nullable= false, columnDefinition="varchar(32)")
	private String freightbillNumber;
	
	@Column(name="paid_amount", nullable= false, columnDefinition="varchar(16)")
	private String paidAmount;
	
	@Column(name="full_freight_terms", nullable= false, columnDefinition="varchar(24)")
	private String fullFreightTerms;
	
	@Column(name="commodity_class", nullable= false, columnDefinition="varchar(12)")
	private String commodityClass;
	
	@Column(name="commodity_pieces", nullable= false, columnDefinition="varchar(64)")
	private String commodityPieces;
	
	@Column(name="commodity_paid_weight", nullable= false, columnDefinition="varchar(16)")
	private String commodityPaidWeight;
	
	@Column(name="shipper_city", nullable= false, columnDefinition="varchar(64)")
	private String shipperCity;
	
	@Column(name="shipper_state", nullable= false, columnDefinition="varchar(64)")
	private String shipperState;
	
	@Column(name="shipper_zip", nullable=false, columnDefinition="varchar(12)")
	private String shipperZip;
	
	@Column(name="shipper_latitude", nullable= true, columnDefinition="varchar(12) default NULL")
	private String shipperLatitude;
	
	@Column(name="shipper_longitude", nullable= true, columnDefinition="varchar(12) default NULL")
	private String shipperLongitude;
	
	@Column(name="consignee_city", nullable= false, columnDefinition="varchar(64)")
	private String consigneeCity;
	
	@Column(name="consignee_state", nullable=false, columnDefinition= "varchar(64)")
	private String consigneeState;
	
	@Column(name="consignee_zip", nullable=false, columnDefinition="varchar(12)")
	private String consigneeZip;
	
	@Column(name="consignee_latitude", nullable= true, columnDefinition="varchar(12) default NULL")
	private String consigneeLatitude;
	
	@Column(name="consignee_longitude", nullable= true, columnDefinition="varchar(12) default NULL")
	private String consigneeLongitude;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicles vehicle;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "shipment")
	private List<Bids> bids = new ArrayList<>();
	
	/**
	 * Gets the Shipment ID
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Shipment ID
	 * @param id ID of the shipment
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the client
	 * @return client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * Sets the client
	 * @param client Client of the shipment
	 */
	public void setClient(String client) {
		this.client = client;
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
	 * @param carrier Carrier of the shipment
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
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
	 * @param scac SCAC of the shipment
	 */
	public void setScac(String scac) {
		this.scac = scac.trim();
	}

	/**
	 * Gets the client mode
	 * @return clientMode
	 */
	public String getClientMode() {
		return clientMode;
	}

	/**
	 * Sets the client mode
	 * @param clientMode Client mode of the shipment
	 */
	public void setClientMode(String clientMode) {
		this.clientMode = clientMode.trim();
	}

	/**
	 * Gets the ship date
	 * @return shipDate
	 */
	public String getShipDate() {
		return shipDate;
	}

	/**
	 * Sets the ship date
	 * @param date Date of the shipment
	 */
	public void setShipDate(String date) {
		this.shipDate = date.trim();
	}

	/**
	 * Gets the freightbill number
	 * @return freightbillNumber 
	 */
	public String getFreightbillNumber() {
		return freightbillNumber;
	}

	/**
	 * Sets the freightbill number
	 * @param freightbillNumber Freightbill number of the shipment
	 */
	public void setFreightbillNumber(String freightbillNumber) {
		this.freightbillNumber = freightbillNumber.trim();
	}

	/**
	 * Gets the paid amount
	 * @return paidAmount
	 */
	public String getPaidAmount() {
		return paidAmount;
	}

	/**
	 * Sets the paid amount
	 * @param paidAmount Paid amount of the shipment
	 */
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount.trim();
	}

	/**
	 * Gets the full freight terms
	 * @return fullFreightTerms
	 */
	public String getFullFreightTerms() {
		return fullFreightTerms;
	}

	/**
	 * Sets the full freight terms
	 * @param fullFreightTerms Full freight terms of the shipment
	 */
	public void setFullFreightTerms(String fullFreightTerms) {
		this.fullFreightTerms = fullFreightTerms.trim();
	}

	/**
	 * Gets the commodity class
	 * @return commodityClass
	 */
	public String getCommodityClass() {
		return commodityClass;
	}

	/**
	 * Sets the commodity class
	 * @param commodityClass Commodity class of the shipment
	 */
	public void setCommodityClass(String commodityClass) {
		this.commodityClass = commodityClass.trim();
	}

	/**
	 * Gets the commodity pieces
	 * @return commodityPieces
	 */
	public String getCommodityPieces() {
		return commodityPieces;
	}

	/**
	 * Sets the commodity pieces
	 * @param commodityPieces Commodity pieces of the shipment
	 */
	public void setCommodityPieces(String commodityPieces) {
		this.commodityPieces = commodityPieces.trim();
	}

	/**
	 * Gets the commodity paid weight
	 * @return commodityPaidWeight
	 */
	public String getCommodityPaidWeight() {
		return commodityPaidWeight;
	}

	/**
	 * Sets the commodity paid weight
	 * @param commodityPaidWeight Commodity paid weight of the shipment
	 */
	public void setCommodityPaidWeight(String commodityPaidWeight) {
		this.commodityPaidWeight = commodityPaidWeight.trim();
	}

	/**
	 * Gets the shipper city
	 * @return shipperCity
	 */
	public String getShipperCity() {
		return shipperCity;
	}

	/**
	 * Sets the shipper city
	 * @param shipperCity Shipper city of the shipment
	 */
	public void setShipperCity(String shipperCity) {
		this.shipperCity = shipperCity.trim();
	}

	/**
	 * Gets the shipper state
	 * @return shipperState
	 */
	public String getShipperState() {
		return shipperState;
	}

	/**
	 * Sets the shipper state
	 * @param shipperState Shipper state of the shipment
	 */
	public void setShipperState(String shipperState) {
		this.shipperState = shipperState.trim();
	}

	/**
	 * Gets the shipper zip
	 * @return shipperZip
	 */
	public String getShipperZip() {
		return shipperZip;
	}

	/**
	 * Sets the shipper zip
	 * @param shipperZip Shipper zip of the shipment
	 */
	public void setShipperZip(String shipperZip) {
		this.shipperZip = shipperZip.trim();
	}

	/**
	 * Gets the shipper latitude
	 * @return shipperLatitude
	 */
	public String getShipperLatitude() {
		return shipperLatitude;
	}

	/**
	 * Sets the shipperLatitude
	 * @param d Shipper latitude of the shipment
	 */
	public void setShipperLatitude(String d) {
		this.shipperLatitude = d;
	}

	/**
	 * Gets the shipper longitude
	 * @return shipperLongitude
	 */
	public String getShipperLongitude() {
		return shipperLongitude;
	}

	/**
	 * Sets the shipper longitude
	 * @param shipperLongitude Shipper longitude of the shipment
	 */
	public void setShipperLongitude(String shipperLongitude) {
		this.shipperLongitude = shipperLongitude.trim();
	}

	/**
	 * Gets the consignee city
	 * @return consigneeCity 
	 */
	public String getConsigneeCity() {
		return consigneeCity;
	}

	/**
	 * Sets the consignee city
	 * @param consigneeCity Consignee city of the shipment
	 */
	public void setConsigneeCity(String consigneeCity) {
		this.consigneeCity = consigneeCity.trim();
	}

	/**
	 * Gets the consignee state
	 * @return consigneeState
	 */
	public String getConsigneeState() {
		return consigneeState;
	}

	/**
	 * Sets the consignee state
	 * @param consigneeState Consignee state of the shipment
	 */
	public void setConsigneeState(String consigneeState) {
		this.consigneeState = consigneeState.trim();
	}

	/**
	 * Gets the consignee zip
	 * @return consigneeZip
	 */
	public String getConsigneeZip() {
		return consigneeZip;
	}

	/**
	 * Sets the consignee zip
	 * @param consigneeZip Consignee zip of the shipment
	 */
	public void setConsigneeZip(String consigneeZip) {
		this.consigneeZip = consigneeZip.trim();
	}

	/**
	 * Gets the consignee latitude
	 * @return consigneeLatitude
	 */
	public String getConsigneeLatitude() {
		return consigneeLatitude;
	}

	/**
	 * Sets the consignee latitude
	 * @param consigneeLatitude Consignee latitude of the shipment
	 */
	public void setConsigneeLatitude(String consigneeLatitude) {
		this.consigneeLatitude = consigneeLatitude.trim();
	}

	/**
	 * Gets the consignee longitude
	 * @return consigneeLongitude 
	 */ 
	public String getConsigneeLongitude() {
		return consigneeLongitude;
	}

	/**
	 * Sets the consignee longitude
	 * @param consigneeLongitude Consignee longitude of the shipment
	 */
	public void setConsigneeLongitude(String consigneeLongitude) {
		this.consigneeLongitude = consigneeLongitude.trim();
	}

	/**
	 * Gets the vehicle
	 * @return vehicle
	 */
	public Vehicles getVehicle() {
		return vehicle;
	}

	/**
	 * Sets the vehicle
	 * @param vehicle Vehicle of the shipment
	 */
	public void setVehicle(Vehicles vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * Gets the user
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user
	 * @param user User of the shipment
	 */
	public void setUser(User user) {
		this.user = user;
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
	 * @param bids Bids of the shipment
	 */
	public void setBids(List<Bids> bids) {
		this.bids = bids;
	}
	
	public String toString() {
		return shipperCity + ", " + shipperState + " to " + consigneeCity + ", " + consigneeState + " created by " + user.getUsername();
	}
}
