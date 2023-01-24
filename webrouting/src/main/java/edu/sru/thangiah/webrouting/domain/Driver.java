package edu.sru.thangiah.webrouting.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

/**
 * Sets up the Driver database
 * @author Ian Black		imb1007@sru.edu
 * @since 2/8/2022
 */

@Entity
@Table(name="driver")
public class Driver {
	
		@Id
		@GenericGenerator(name="generate" , strategy="increment")
		@GeneratedValue(generator="generate")
	    private long id;
		
		@ManyToOne
		@JoinColumn(name = "contact_id")
		private Contacts contact;
		
		@ManyToOne
		@JoinColumn(name = "carrier_id")
		private Carriers carrier;
		
		@ManyToOne
		@JoinColumn(name = "vehicle_id")
		private Vehicles vehicle;
		
		@NonNull
		@Column(name="license_number", columnDefinition="varchar(32)")
		private String lisence_number;
		
		@NonNull
		@Column(name="license_expiration", columnDefinition="varchar(12)")
		private String lisence_expiration;
		
		@NonNull
		@Column(name="license_class", columnDefinition="varchar(12) default NULL")
		private String lisence_class;

		/**
		 * Gets the Driver ID
		 * @return id
		 */
		public long getId() {
			return id;
		}

		/**
		 * Sets the Driver ID
		 * @param id ID of the driver
		 */
		public void setId(long id) {
			this.id = id;
		}
		
		/**
		 * Gets the Driver Contact
		 * @return contact
		 */
		public Contacts getContact() {
			return contact;
		}

		/**
		 * Sets the Driver Contact
		 * @param contact Contact of the driver
		 */
		public void setContact(Contacts contact) {
			this.contact = contact;
		}

		/**
		 * Gets the Driver Carrier
		 * @return carrier
		 */
		public Carriers getCarrier() {
			return carrier;
		}

		/**
		 * Sets the Driver Carrier
		 * @param carrier Carrier of the driver
		 */
		public void setCarrier(Carriers carrier) {
			this.carrier = carrier;
		}

		/**
		 * Gets the Driver Vehicle
		 * @return vehicle
		 */
		public Vehicles getVehicle() {
			return vehicle;
		}

		/**
		 * Sets the Driver Vehicle
		 * @param vehicle Vehicle of the driver
		 */
		public void setVehicle(Vehicles vehicle) {
			this.vehicle = vehicle;
		}

		/**
		 * Gets the Driver Lisence Number
		 * @return lisence_number
		 */
		public String getLisence_number() {
			return lisence_number;
		}

		/**
		 * Sets the Driver Lisence Number
		 * @param lisence_number License number of the driver
		 */
		public void setLisence_number(String lisence_number) {
			this.lisence_number = lisence_number.trim();
		}

		/**
		 * Gets the Driver Lisence Expiration
		 * @return lisence_expiration
		 */
		public String getLisence_expiration() {
			return lisence_expiration;
		}

		/**
		 * Sets the Driver Lisence Expiration
		 * @param lisence_expiration License expiration of the driver
		 */
		public void setLisence_expiration(String lisence_expiration) {
			this.lisence_expiration = lisence_expiration.trim();
		}

		/**
		 * Gets the Driver Lisence Class
		 * @return lisence_class
		 */
		public String getLisence_class() {
			return lisence_class;
		}

		/**
		 * Sets the Driver Lisence Class
		 * @param lisence_class License class of the driver
		 */
		public void setLisence_class(String lisence_class) {
			this.lisence_class = lisence_class.trim();
		}
		

		/**
		 * Prints out the name and lisence number of the instance of the driver
		 * @return contact first and last name and lisence number
		 */
		public String toString() {
			return this.getContact().toString() + " Lisence: " + this.getLisence_number();
		}
}
