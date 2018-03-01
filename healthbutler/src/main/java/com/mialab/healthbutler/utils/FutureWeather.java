package com.mialab.healthbutler.utils;

public class FutureWeather {
	
	/**
	 * "temperature":"9��~18��",
				"weather":"��",
				"weather_id":{
					"fa":"00",
					"fb":"00"
				},
				"wind":"����3-4 ��",
				"week":"������",
				"date":"20160327"
	 * 
	 * 
	 * 
	 */
	private String temperature;
	private String weather;
	private String weather_id;
	private String wind;
	private String week;
	private String date;
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWeather_id() {
		return weather_id;
	}
	public void setWeather_id(String weather_id) {
		this.weather_id = weather_id;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
