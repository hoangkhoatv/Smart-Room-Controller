package com.a4dsiotlab.remoteair;

import android.util.Log;
import android.view.InflateException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by hoangkhoatv on 11/8/16.
 */

public class ProcessData {
    private JsonProcess jsonProcess = new JsonProcess();
    private RequestData requestData;
    public ProcessData(RequestData requestData){
        this.requestData = requestData;
        this.requestData.log("Nghi");
        this.requestData.log("Test");

    }

    public void postAutoAirCon(DisplayInfo displayInfo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE,displayInfo.getAirConditionerMode());
            jsonObject.put(jsonProcess.PREF_TEMPERATURE,displayInfo.getPreferedTemperature());
            jsonObject.put(jsonProcess.FROM_TIME,displayInfo.getFromTime());
            jsonObject.put(jsonProcess.TO_TIME,displayInfo.getToTime());
            this.requestData.log(jsonObject.toString());
            Log.d("", jsonObject.toString());
        } catch (Exception e){
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualAirCon(DisplayInfo displayInfo, String control){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE,displayInfo.getAirConditionerMode());
            jsonObject.put(jsonProcess.AIR_CON_CONTROL,control);
            this.requestData.log(jsonObject.toString());
        } catch (Exception e){
            Log.d("Error Process Data: ", e.getMessage());
        }
    }
    public void postManualAirCon(DisplayInfo displayInfo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE,displayInfo.getAirConditionerMode());
            this.requestData.log(jsonObject.toString());
        } catch (Exception e){
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postAutoLight(DisplayInfo displayInfo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.LIGHT_MODE,displayInfo.getLightMode());
            jsonObject.put(jsonProcess.LIGHT_POWER,displayInfo.getLightStatus());
            this.requestData.log(jsonObject.toString());
        } catch (Exception e){
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void getDataServer(final DisplayInfo displayInfo){
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(jsonProcess.GET_DATA,true);
            this.requestData.log(requestJson.toString());
        }catch (Exception e){
            Log.d("Error Get Data",e.getMessage());
        }
        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    String msg = requestData.getMsg();
                    while (msg.equals(null)) {
                        msg = requestData.getMsg();
                    }
                    try {
                        JSONObject reponseJson = new JSONObject(msg);
                        displayInfo.setAirConditionerTemperature(reponseJson.getInt(jsonProcess.AIR_CON_TEMP));
                        displayInfo.setTemperature(reponseJson.getInt(jsonProcess.TEMPERATURE));
                        displayInfo.setHumidity(reponseJson.getInt(jsonProcess.HUMIDITY));
                        displayInfo.setLight(reponseJson.getInt(jsonProcess.LIGHT));

                    } catch (JSONException e) {
                        Log.d("Errpr Reponse Data", e.getMessage());
                    }
                } catch (InflateException e) {
                    e.printStackTrace();
                }

            }

        });


    }

}
