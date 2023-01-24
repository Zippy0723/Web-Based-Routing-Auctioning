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

/**
 * Sets up the Role database
 * @author Logan Kirkwood	llk1005@sru.edu
 * @since 2/7/2022
 */

@Entity
@Table(name="role")
public class Role {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@Column(name = "name", unique=true, nullable=false, columnDefinition="varchar(24)")
	private String name;
	
	@OneToMany(mappedBy = "role")
	List<User> users = new ArrayList<>();

	/**
	 * Gets the Role ID
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the Role ID
	 * @param id ID of the role
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the Role Name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the Role Name
	 * @param name Name of the role
	 */
	public void setName(String name) {
		this.name = name.trim();
	}
	
	/**
	 * Gets the Users List
	 * @return users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * Sets the Users List
	 * @param users Users of the role
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Prints out the name of the instance of the role
	 * @return role name
	 */
	public String toString() {
		return this.getName();
	}
	
}
