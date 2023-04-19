package edu.sru.thangiah.webrouting.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This sets up the Log object for the ShadowAdmin
 * @author Dakota Myers
 */
public class Log {

	private LocalDate date;
	private String time;
	private String where;
	private String level;
	private String who;
	private String user;
	private String msg;


	/**
	 * Constructor for the Log object
	 * @param date holds the date attribute
	 * @param time holds the time attribute
	 * @param where holds the where attribute
	 * @param level holds the level attribute
	 * @param who holds the who attribute
	 * @param user holds the user attribute
	 * @param msg holds the message attribute
	 */
	
	public Log(LocalDate date, String time, String where, String level, String who, String user, String msg) {

		this.date = date;
		this.time = time;
		this.where = where;
		this.level = level;
		this.who = who;
		this.user = user;
		this.msg = msg;

	}

	/**
	 * Returns date attribute as a LocalDate object
	 * @return date
	 */
	
	public LocalDate getDateAsLocalDate() {
		return date;
	}
	
	/**
	 * Returns date attribute as a string
	 * @return date
	 */
	
	public String getDate() {

		LocalDate localDate = LocalDate.parse(date.toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String formattedDate = localDate.format(formatter);

		return formattedDate;
	}

	/**
	 * Sets date attribute
	 * @param date holds the date being set
	 */
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Returns the time attribute
	 * @return time
	 */
	
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time attribute
	 * @param time holds the time being set
	 */
	
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Returns the where attribute
	 * @return where
	 */
	
	public String getWhere() {
		return where;
	}

	/**
	 * Sets the where attribute
	 * @param where holds the where being set
	 */
	
	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * Returns the level attribute
	 * @return level
	 */
	
	public String getLevel() {
		return level;
	}

	/**
	 * Sets the level attribute
	 * @param level holds the level being set
	 */
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	/**
	 * Returns the who attribute
	 * @return who
	 */

	public String getWho() {
		return who;
	}
	
	/**
	 * Sets the who attribute
	 * @param who holds the who being set
	 */

	public void setWho(String who) {
		this.who = who;
	}

	/**
	 * Returns the message attribute
	 * @return msg
	 */
	
	public String getMsg() {
		return msg;
	}

	/**
	 * Returns the user attribute
	 * @return user
	 */
	
	public String getUser() {
		return user;
	}
	
	/**
	 * Sets the user attribute
	 * @param user holds the user being set
	 */

	public void setPerson(String user) {
		this.user = user;
	}
	
	/**
	 * Sets the message attribute
	 * @param msg holds the message bing set
	 */

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

