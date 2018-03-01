package com.mialab.healthbutler.domain;

import java.io.Serializable;

public class SportRecords implements Serializable {
	
	private String date;
	private String time;
	private String calorie;
	private String distance;
	private String stepcount;
	private String speed;
	private String savetime;
	
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
	public String getCalorie() {
		return calorie;
	}
	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getStepcount() {
		return stepcount;
	}
	public void setStepcount(String stepcount) {
		this.stepcount = stepcount;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}

}
