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
    private String toTime;
    private String time;


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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdateDisplay() {
        /*
        Time: 11:11
        Temperature: 30 oC
        Humidity: 40 %
        Light: 200 lux
        Air conditioner status: On/Off
        Air conditioner mode: Auto/Manual
        Air conditioner temperature: 20°C #Nếu máy lạnh đang bật mới hiện
        Prefered temperature: 26 °C #Nếu Air conditioner mode: Auto mới hiện
        Light status: On/Off
        Light mode: Auto/Manual
        */

        StringBuilder lcd = new StringBuilder();
        lcd.append(String.format("Time: %s \n", this.time));
        lcd.append(String.format("Temperature: %d °C\n", this.temperature));
        lcd.append(String.format("Humidity: %d %s \n", this.humidity, "%"));
        lcd.append(String.format("Light: %d lux\n", this.light));
        lcd.append(String.format("Air conditioner temperature: %d °C\n", this.airConditionerTemperature));
        lcd.append(String.format("Air conditioner power: %s\n", this.airConditionerStatus ? "On" : "Off"));
        lcd.append(String.format("Air conditioner mode: %s\n", this.airConditionerMode ? "Auto" : "Manual"));
        if (airConditionerStatus) {
            lcd.append(String.format("Air conditioner temperature: %d\n", this.airConditionerTemperature));
        }
        if (airConditionerMode) {
            lcd.append(String.format("Prefered temperature: %d °C\n", this.preferedTemperature));
        }
        lcd.append(String.format("Light status: %s\n", this.lightStatus ? "On" : "Off"));
        lcd.append(String.format("Light mode: %s", this.lightMode ? "Auto" : "Manual"));
        return lcd.toString();
    }


}
