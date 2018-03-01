package com.mialab.healthbutler.utils;

public class ActuaWeather {
	/*
	 * "temp":"12",
	 *	"wind_direction":"����",
	 *	"wind_strength":"3��",
	 *	"humidity":"34%",
	 *	"time":"19:00"
	 */
	
	private int temp;
	private String wind_direction;
	private String wind_strength;
	private String humidity;
	private String time;
	public int getTemp() {
		return temp;
	}
	public void setTemp(int temp) {
		this.temp = temp;
	}
	public String getWind_direction() {
		return wind_direction;
	}
	public void setWind_direction(String wind_direction) {
		this.wind_direction = wind_direction;
	}
	public String getWind_strength() {
		return wind_strength;
	}
	public void setWind_strength(String wind_strength) {
		this.wind_strength = wind_strength;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
