package edu.sru.thangiah.webrouting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "freight_rate_tables")
public class FreightRateTable {
	
	public FreightRateTable() {
		
	}
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private Long id;
	
    @Column(name = "distance_break_points", columnDefinition = "TEXT")
	private String distanceBreakPoints;

	@Column(name = "price_per_mile", columnDefinition = "TEXT")
	private String pricePerMileArray;
	
	@ManyToOne
	@JoinColumn(name = "carrier_id")
	private Carriers carrier;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the distanceBreakPoints
	 */
	public String getDistanceBreakPoints() {
		return distanceBreakPoints;
	}

	/**
	 * @param distanceBreakPoints the distanceBreakPoints to set
	 */
	public void setDistanceBreakPoints(String distanceBreakPoints) {
		this.distanceBreakPoints = distanceBreakPoints;
	}

	/**
	 * @return the pricePerMileArray
	 */
	public String getPricePerMileArray() {
		return pricePerMileArray;
	}

	/**
	 * @param pricePerMileArray the pricePerMileArray to set
	 */
	public void setPricePerMileArray(String pricePerMileArray) {
		this.pricePerMileArray = pricePerMileArray;
	}

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
	
}
