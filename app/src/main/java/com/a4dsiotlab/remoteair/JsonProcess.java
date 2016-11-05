package com.a4dsiotlab.remoteair;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by hoangkhoatv on 11/3/16.
 */



public class JsonProcess {
    public static final String AIR_CON_POWER = "AirConditionerPower";
    public static final String LIGHT_POWER = "LightPower";
    public static final String AIR_CON_TEMP = "AirConditionerTemperature";
    public static final String TEMPERATURE = "Temperature";
    public static final String HUMIDITY = "Humidity";
    public static final String LIGHT = "Light";
    public static final String PREF_TEMPERATURE = "PreferedTemperature";
    public static final String AIR_CON_MODE = "AirConditionerMode";
    public static final String LIGHT_MODE = "LightMode";
    public static final String FROM_TIME = "FromTime";
    public static final String TO_TIME = "ToTime";
    public static final String AIR_CON_CONTROL = "AirConditionerControl"; //"UP/DOWN"

    public JSONObject WriteToJson(DisplayInfo df){
        JSONObject obj = new JSONObject();
        try {
//
//            obj.put(AIR_CON_POWER,df.getSttMachine());
//            obj.put(LIGHT_POWER,df.getSttLight());
//            obj.put("Temp",df.getTemp());
//            obj.put("Hum",df.getHum());
            obj.put("Light",df.getLight());


        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        return obj;
    }
    public void ReadFromJson(JSONObject obj,DisplayInfo df){
        try {

          //  df.setSttMachine( obj.getBoolean("PowerMahcine") );
            //df.setSttLight( obj.getBoolean("PowerLight"));
            //df.setTemp( obj.getInt("Temp") );
            //df.setHum( obj.getInt("Hum"));
            //df.setLight( obj.getInt("Light"));



        }catch (Exception e){
            Log.e("Error",e.getMessage());

        }

    }
}
