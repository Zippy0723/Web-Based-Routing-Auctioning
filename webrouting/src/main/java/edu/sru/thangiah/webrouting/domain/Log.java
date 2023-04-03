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


	public Log(LocalDate date, String time, String where, String level, String who, String user, String msg) {

		this.date = date;
		this.time = time;
		this.where = where;
		this.level = level;
		this.who = who;
		this.user = user;
		this.msg = msg;

	}

	public LocalDate getDateAsLocalDate() {
		return date;
	}

	public String getDate() {

		LocalDate localDate = LocalDate.parse(date.toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String formattedDate = localDate.format(formatter);

		return formattedDate;
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

	public String getUser() {
		return user;
	}

	public void setPerson(String user) {
		this.user = user;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

