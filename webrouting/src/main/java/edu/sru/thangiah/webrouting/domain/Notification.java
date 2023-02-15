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

@Entity
@Table(name="notifications")
public class Notification {
	
	@Id
	@GenericGenerator(name="generate" , strategy="increment")
	@GeneratedValue(generator="generate")
    private long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@NonNull
	@Column(name="message", nullable = false, columnDefinition="varchar(225)")
    private String message;
	
	@NonNull
	@Column(name="timesent", nullable = false, columnDefinition="varchar(225)")
	private String timesent; //Converted from LocalDateTime.now()
	
	/**
	 * Constructor for Notification
	 * 
	 * This Entity needs a constructor unlike the other ones because it is instanciated through the static method NotificationController.addNotification
	 * @param user : The user the notification is intended for
	 * @param timesent : a formatted string containing the results of LocalDateTime.now() at the time of message sending
	 * @param message: the message of the notification
	 */
	public Notification(User user, String timesent, String message) {
		this.user = user;
		this.timesent = timesent;
		this.message = message;
	}

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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the timeSent
	 */
	public String getTimeSent() {
		return timesent;
	}

	/**
	 * @param timeSent the timeSent to set
	 */
	public void setTimeSent(String timeSent) {
		this.timesent = timeSent;
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
