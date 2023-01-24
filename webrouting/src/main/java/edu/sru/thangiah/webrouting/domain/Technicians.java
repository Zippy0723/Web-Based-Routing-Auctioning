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
 * Sets up the Shipments database
 * @author Ian Black		imb1007@sru.edu
 * @since 2/8/2022
 */

@Entity
@Table(name="technicians")
public class Technicians {

	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "contact_id")
	private Contacts contact;
	
	@NonNull
	@Column(name="skill_grade",  columnDefinition="varchar(12) default NULL")
	private String skill_grade;
	
	@OneToMany(mappedBy = "technician")
	private List<MaintenanceOrders> orders = new ArrayList<>();

	/**
	 * Gets the Technician ID
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Technician ID
	 * @param id ID of the technician
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the Technician Contact
	 * @return contact
	 */
	public Contacts getContact() {
		return contact;
	}

	/**
	 * Sets the Technician Contact
	 * @param contact Contact of the technician
	 */
	public void setContact(Contacts contact) {
		this.contact = contact;
	}

	/**
	 * Gets the Technician Skill Grade
	 * @return skill_grade
	 */
	public String getSkill_grade() {
		return skill_grade;
	}

	/**
	 * Sets the Technician Skill Grade
	 * @param skill_grade Skill grade of the technician
	 */
	public void setSkill_grade(String skill_grade) {
		this.skill_grade = skill_grade;
	}
	
	/**
	 * Gets the Orders List
	 * @return orders
	 */
	public List<MaintenanceOrders> getOrders() {
		return orders;
	}

	/**
	 * Sets the Orders List
	 * @param orders Maintenance orders of the technicians
	 */
	public void setOrders(List<MaintenanceOrders> orders) {
		this.orders = orders;
	}

	/**
	 * Prints out the name and skill grade of the instance of the technician
	 * @return contact name and skill grade
	 */
	public String toString() {
		return this.getContact().toString() + " Skill: " + this.getSkill_grade();
	}
}
