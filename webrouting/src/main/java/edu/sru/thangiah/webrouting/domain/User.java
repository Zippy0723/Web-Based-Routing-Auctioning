package edu.sru.thangiah.webrouting.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

/**
 * Class for the User handling all user attributes
 * @author Josh Gearhart	jjg1018@sru.edu
 * @since 1/30/2022
 */

@Entity
@Table(name="user")
public class User {

	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
    
    @NonNull
	@Column(name="username", nullable= false, columnDefinition="varchar(32)")
    private String username;
    
    @NonNull
	@Column(name="password", nullable= false, columnDefinition="varchar(255)")
    private String password;
    
    @NonNull
	@Column(name="email", nullable= false, columnDefinition="varchar(64)")
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private Carriers carrier;
    
    @OneToMany(mappedBy = "user")
	private List<Shipments> shipments = new ArrayList<>();
    
    @Column(name = "verification_code", nullable=true, length=64)
    private String verificationCode;
    
    @Column(name = "enabled", nullable=false, columnDefinition="bit(1) default false")
    private boolean enabled;

    
    @Column(name = "otp", nullable=true, columnDefinition="varchar(6) default NULL")
    private String otpCode;
    
    @Transient
    private String confirmPassword;
    
    @Transient
    private String updateEmail;
    

    /**
     * Gets the User ID
     * @return id
     */
	public long getId() {
		return id;
	}

	/**
	 * Sets the User ID
	 * @param id ID of the user
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the User Username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the User Username
	 * @param username Username of the user
	 */
	public void setUsername(String username) {
		this.username = username.trim();
	}

	/**
	 * Gets the User Password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the User Password
	 * @param password Password of the user
	 */
	public void setPassword(String password) {
		this.password = password.trim();
	}

	/**
	 * Gets the User Email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the User Email
	 * @param email Email address of the user
	 */
	public void setEmail(String email) {
		this.email = email.trim();
	}
	
	/**
	 * Gets the User Role
	 * @return role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Set the User Role
	 * @param role Role of the user
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Gets the User Carrier
	 * @return carrier
	 */
	public Carriers getCarrier() {
		return carrier;
	}

	/**
	 * Sets the User Carrier
	 * @param carrier Carrier of the user
	 */
	public void setCarrier(Carriers carrier) {
		this.carrier = carrier;
	}

	/**
	 * Gets the User Shipments
	 * @return shipments
	 */
	public List<Shipments> getShipments() {
		return shipments;
	}

	/**
	 * Sets the User Shipments
	 * @param shipments Shipments of the user
	 */
	public void setShipments(List<Shipments> shipments) {
		this.shipments = shipments;
	}

	/**
	 * Prints out the username of the instance of the user
	 * @return username
	 */
	public String toString() {
		return this.getUsername();
	}
	/**
	 * Prints out the verificationCode of the instance of the user
	 * @return verificationCode
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public String getVerificationCode() {
		return verificationCode;
	}
	/**
	 * sets the verification code of the user
	 * @param verificationCode being set
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	/**
	 * gets the enabled status of a user
	 * @return enabled whether the user is or is not enabled
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public boolean isEnabled() {
		
		
		return enabled;
	}
	
	/**
	 * sets the enabled status of a user
	 * @param enable status being set to true or false
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}


	/**
	 * Gets the users Otp Code
	 * @return otpCode - The users Code
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public String getOtpCode() {
		return otpCode;
	}
	
	/**
	 * sets the specified otpCode for the user
	 * @param otpCode - code being set
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	
	/**
	 * Gets the users confirmedPassword
	 * @return confirmPassword - users retyped password
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	/**
	 * Sets the users confirmed password
	 * @param confirmPassword - confirmed password being set
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	/**
	 * Gets the updateEmail String
	 * @return update Email - used for updating the users email
	 * @author Josh Gearhart 	jjg1018@sru.edu
	 */
	public String getUpdateEmail() {
		return updateEmail;
	}

	/**
	 * Sets the updateEmail
	 * @param updateEmail - dto String for updating an email
	 * @author Josh Gearhart	jjg1018@sru.edu
	 */
	public void setUpdateEmail(String updateEmail) {
		this.updateEmail = updateEmail;
	}



}
