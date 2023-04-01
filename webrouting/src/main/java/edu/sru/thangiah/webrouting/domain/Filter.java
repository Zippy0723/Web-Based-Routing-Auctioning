package edu.sru.thangiah.webrouting.domain;

public class Filter {

	private String startDate;
	
	private String endDate;
	
	private String level;
	
	private String user;
	
	
	public Filter(String startDate, String endDate, String level, String user) {
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.level = level;
		this.user = user;
	}
	
	public Filter() {}
	


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}
	
	
}
