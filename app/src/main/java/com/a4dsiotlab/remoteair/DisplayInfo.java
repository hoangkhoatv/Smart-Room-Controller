package com.a4dsiotlab.remoteair;

/**
 * Created by hoangkhoatv on 11/1/16.
 */

public class DisplayInfo {
    private int temperature;
    private int humidity;
    private int light;
    private boolean airConditionerMode;
    private boolean lightMode;
    private int preferedTemperature;
    private boolean airConditionerStatus;
    private boolean lightStatus;
    private int airConditionerTemperature;
    private String fromTime;

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    private String toTime;

    public int getAirConditionerTemperature() {
        return airConditionerTemperature;
    }

    public void setAirConditionerTemperature(int airConditionerTemperature) {
        this.airConditionerTemperature = airConditionerTemperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public boolean getAirConditionerMode() {
        return airConditionerMode;
    }

    public void setAirConditionerMode(boolean airConditionerMode) {
        this.airConditionerMode = airConditionerMode;
    }

    public boolean getLightMode() {
        return lightMode;
    }

    public void setLightMode(boolean lightMode) {
        this.lightMode = lightMode;
    }

    public int getPreferedTemperature() {
        return preferedTemperature;
    }

    public void setPreferedTemperature(int preferedTemperature) {
        this.preferedTemperature = preferedTemperature;
    }

    public boolean getAirConditionerStatus() {
        return airConditionerStatus;
    }

    public void setAirConditionerStatus(boolean airConditionerStatus) {
        this.airConditionerStatus = airConditionerStatus;
    }

    public boolean getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(boolean lightStatus) {
        this.lightStatus = lightStatus;
    }
}
