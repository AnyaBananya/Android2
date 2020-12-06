package com.example.baforecast;

import java.io.Serializable;

public class City implements Serializable {
    private String name;
    private String temperature;
    private String pressure;
    private String windSpeed;
    private boolean isNeedPressure;
    private boolean isNeedWindSpeed;

    public City(String name) {
        this.name = name;
        this.isNeedPressure = false;
        this.isNeedWindSpeed = false;
    }

    public City(String name, String temperature, String pressure, String windSpeed) {
        this.name = name;
        this.temperature = temperature;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.isNeedPressure = false;
        this.isNeedWindSpeed = false;
    }

    public City(String name, String temperature) {
        this.name = name;
        this.temperature = temperature;
        this.isNeedPressure = false;
        this.isNeedWindSpeed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public boolean isNeedPressure() {
        return isNeedPressure;
    }

    public void setNeedPressure(boolean needPressure) {
        isNeedPressure = needPressure;
    }

    public boolean isNeedWindSpeed() {
        return isNeedWindSpeed;
    }

    public void setNeedWindSpeed(boolean needWindSpeed) {
        isNeedWindSpeed = needWindSpeed;
    }
}
