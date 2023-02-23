package edu.sru.thangiah.webrouting.domain;

public class Log {
	
	private String date;
	private String time;
	private String where;
	private String level;
	private String who;
	private String msg;

	
	
	public Log(String date, String time, String where, String level, String who, String msg) {
		
		this.date = date;
		this.time = time;
		this.where = where;
		this.level = level;
		this.who = who;
		this.msg = msg;
		
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
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



	public void setMsg(String msg) {
		this.msg = msg;
	}
}
	
