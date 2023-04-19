package edu.sru.thangiah.webrouting.domain;

public class Filter {

	private String startDate;

	private String endDate;

	private String level;

	private String user;


	/**
	 * Overloaded constructor for the filter object
	 * @param startDate holds the start date attribute of the filter
	 * @param endDate holds the end date attribute of the filter
	 * @param level holds the level attribute of the filter
	 * @param user holds the user attribute of the filter
	 */
	
	public Filter(String startDate, String endDate, String level, String user) {

		this.startDate = startDate;
		this.endDate = endDate;
		this.level = level;
		this.user = user;
	}

	/*
	 * Constructor for the filter class
	 */
	
	public Filter() {}

	/**
	 * Returns the start date attribute
	 * @return startDate
	 */
	
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date attribute
	 * @param startDate holds the starting date
	 */
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns the end date attribute
	 * @return endDate
	 */
	
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date attribute
	 * @param endDate holds the ending date
	 */

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	 * @param level holds the level
	 */
	
	public void setLevel(String level) {
		this.level = level;
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
	 * @param user holds the user
	 */

	public void setUser(String user) {
		this.user = user;
	}

}
