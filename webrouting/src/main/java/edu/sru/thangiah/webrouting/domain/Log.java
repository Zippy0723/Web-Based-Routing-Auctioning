package edu.sru.thangiah.webrouting.domain;

import java.time.LocalDate;

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
	private String person;
	private String msg;

	
	public Log(LocalDate date, String time, String where, String level, String who, String person, String msg) {
		
		this.date = date;
		this.time = time;
		this.where = where;
		this.level = level;
		this.who = who;
		this.person = person;
		this.msg = msg;
		
	}
	
	public LocalDate getDateAsLocalDate() {
		return date;
	}

	public String getDate() {
		return date.toString();
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getMsg() {
		return msg;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
	
