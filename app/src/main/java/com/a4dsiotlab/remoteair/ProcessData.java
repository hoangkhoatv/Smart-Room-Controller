package com.a4dsiotlab.remoteair;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InflateException;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by hoangkhoatv on 11/8/16.
 */

public class ProcessData {
    private JsonProcess jsonProcess;
    private ExchangeData exchangeData;
    private Thread sendRequest;
    private boolean stop =false;

    public ProcessData(ExchangeData exchangeData) {
        this.exchangeData = exchangeData;
        jsonProcess = new JsonProcess();
    }
    public void close(){
        stop = true;
    }
    public void postAutoAirCon(DisplayInfo displayInfo) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.TO_TIME, displayInfo.getToTime());
            jsonObject.put(jsonProcess.FROM_TIME, displayInfo.getFromTime());
            jsonObject.put(jsonProcess.PREF_TEMPERATURE, displayInfo.getPreferedTemperature());
            jsonObject.put(jsonProcess.AIR_CON_MODE, displayInfo.getAirConditionerMode());
            this.exchangeData.setMsg(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualAirCon(DisplayInfo displayInfo, String control) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE, displayInfo.getAirConditionerMode());
            jsonObject.put(jsonProcess.AIR_CON_POWER, displayInfo.getAirConditionerStatus());
            jsonObject.put(jsonProcess.AIR_CON_CONTROL, control);
            this.exchangeData.setMsg(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualAirCon(DisplayInfo displayInfo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE, displayInfo.getAirConditionerMode());
            jsonObject.put(jsonProcess.AIR_CON_POWER, displayInfo.getAirConditionerStatus());
            this.exchangeData.setMsg(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postAutoLight(DisplayInfo displayInfo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.LIGHT_MODE, displayInfo.getLightMode());
            this.exchangeData.setMsg(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualLight(DisplayInfo displayInfo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.LIGHT_MODE, displayInfo.getLightMode());
            jsonObject.put(jsonProcess.LIGHT_POWER, displayInfo.getLightStatus());
            this.exchangeData.setMsg(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void getDataServer(final DisplayInfo displayInfo, final AppCompatActivity mainActivity) {

        final Handler handler = new Handler();
        final Runnable updater = new Runnable() {

            public void run() {
                try {
                    String msg = exchangeData.getMsg();
                    while (msg.equals("")) {
                        msg = exchangeData.getMsg();
                    }

                    try {

                        JSONObject reponseJson = new JSONObject(msg);
                        displayInfo.setTime(reponseJson.getString(jsonProcess.TIME));
                        displayInfo.setAirConditionerTemperature(reponseJson.getInt(jsonProcess.AIR_CON_TEMP));
                        displayInfo.setTemperature(reponseJson.getInt(jsonProcess.TEMPERATURE));

                        //Caculator Humidity
                        int hum = getHumidityTrue(reponseJson.getInt(jsonProcess.HUMIDITY),reponseJson.getInt(jsonProcess.TEMPERATURE));

                        displayInfo.setHumidity(hum);
                        displayInfo.setLight(reponseJson.getInt(jsonProcess.LIGHT));
                        displayInfo.setLightStatus(reponseJson.getBoolean(jsonProcess.LIGHT_POWER));
                        displayInfo.setAirConditionerStatus(reponseJson.getBoolean(jsonProcess.AIR_CON_POWER));
                        displayInfo.setPreferedTemperature(reponseJson.getInt(jsonProcess.PREF_TEMPERATURE));
                        displayInfo.setAirConditionerMode(reponseJson.getBoolean(jsonProcess.AIR_CON_MODE));
                        displayInfo.setLightMode(reponseJson.getBoolean(jsonProcess.LIGHT_MODE));
                        displayInfo.setFromTime(reponseJson.getString(jsonProcess.FROM_TIME));
                        displayInfo.setToTime(reponseJson.getString(jsonProcess.TO_TIME));
                        //Update Sreen
                        TextView txtDp = (TextView) mainActivity.findViewById(R.id.textView1);
                        txtDp.setText(displayInfo.getUpdateDisplay());

                    } catch (JSONException e) {
                        Log.d("Error Reponse Data", e.getMessage());
                    }
                } catch (InflateException e) {
                    e.printStackTrace();
                }
            }
        };

         sendRequest = new Thread() {
            public void run() {

                while (!stop) {

                    JSONObject requestJson = new JSONObject();
                        try {
                            requestJson.put(jsonProcess.GET_DATA, true);
                            exchangeData.setMsg(requestJson.toString());
                        } catch (Exception e) {
                            Log.d("Error Get Data", e.getMessage());
                        }

                    handler.post(updater);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        sendRequest.start();

}
    public int getHumidityTrue(int sorh, int tc){
        float humidity = (float) (-4 + 0.0405*sorh + (-2.8 * Math.pow(10,-6))*(sorh^2));
        int humidity_true =  (int) ((tc - 25) * (0.01 + 0.00008*sorh) + humidity);
        return humidity_true;
    }

}
