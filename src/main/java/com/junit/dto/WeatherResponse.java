package com.junit.dto;

public class WeatherResponse {
	private float temperature;
	private String humidity;
	private String atmosphericPressure;
	private String weatherDescription;

	public float getTemperature() {
		return this.temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return this.humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getAtmosphericPressure() {
		return this.atmosphericPressure;
	}

	public void setAtmosphericPressure(String atmosphericPressure) {
		this.atmosphericPressure = atmosphericPressure;
	}

	public String getWeatherDescription() {
		return this.weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public WeatherResponse(float temperature, String humidity, String atmosphericPressure, String weatherDescription) {
		super();
		this.temperature = temperature;
		this.humidity = humidity;
		this.atmosphericPressure = atmosphericPressure;
		this.weatherDescription = weatherDescription;
	}

	public WeatherResponse() {
		super();
	}

	@Override
	public String toString() {
		return "WeatherResponse [temperature=" + temperature + ", humidity=" + humidity + ", atmosphericPressure="
				+ atmosphericPressure + ", weatherDescription=" + weatherDescription + "]";
	}

}
