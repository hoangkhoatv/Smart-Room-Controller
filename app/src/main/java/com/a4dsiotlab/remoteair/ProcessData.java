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

    public ProcessData(ExchangeData exchangeData) {
        this.exchangeData = exchangeData;
        jsonProcess = new JsonProcess();
    }

    public void postAutoAirCon(DisplayInfo displayInfo) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.TO_TIME, displayInfo.getToTime());
            jsonObject.put(jsonProcess.FROM_TIME, displayInfo.getFromTime());
            jsonObject.put(jsonProcess.PREF_TEMPERATURE, displayInfo.getPreferedTemperature());
            jsonObject.put(jsonProcess.AIR_CON_MODE, displayInfo.getAirConditionerMode());
            this.exchangeData.log(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualAirCon(DisplayInfo displayInfo, String control) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE, displayInfo.getAirConditionerMode());
            jsonObject.put(jsonProcess.AIR_CON_POWER, displayInfo.getAirConditionerMode());
            jsonObject.put(jsonProcess.AIR_CON_CONTROL, control);
            this.exchangeData.log(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualAirCon(DisplayInfo displayInfo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.AIR_CON_MODE, displayInfo.getAirConditionerMode());
            this.exchangeData.log(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postAutoLight(DisplayInfo displayInfo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.LIGHT_MODE, displayInfo.getLightMode());
            this.exchangeData.log(jsonObject.toString());
        } catch (Exception e) {
            Log.d("Error Process Data: ", e.getMessage());
        }
    }

    public void postManualLight(DisplayInfo displayInfo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(jsonProcess.LIGHT_MODE, displayInfo.getLightMode());
            jsonObject.put(jsonProcess.LIGHT_POWER, displayInfo.getLightMode());
            this.exchangeData.log(jsonObject.toString());
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

                        /*displayInfo.setAirConditionerTemperature(reponseJson.getInt(jsonProcess.AIR_CON_TEMP));
                        displayInfo.setTemperature(reponseJson.getInt(jsonProcess.TEMPERATURE));
                        displayInfo.setHumidity(reponseJson.getInt(jsonProcess.HUMIDITY));
                        displayInfo.setLight(reponseJson.getInt(jsonProcess.LIGHT));
                        displayInfo.setLightStatus(reponseJson.getBoolean(jsonProcess.LIGHT_POWER));
                        displayInfo.setAirConditionerStatus(reponseJson.getBoolean(jsonProcess.AIR_CON_POWER));
                        displayInfo.setPreferedTemperature(reponseJson.getInt(jsonProcess.PREF_TEMPERATURE));
                        displayInfo.setAirConditionerMode(reponseJson.getBoolean(jsonProcess.AIR_CON_MODE));
                        displayInfo.setLightMode(reponseJson.getBoolean(jsonProcess.LIGHT_MODE));
                        displayInfo.setFromTime(reponseJson.getString(jsonProcess.FROM_TIME));
                        displayInfo.setToTime(reponseJson.getString(jsonProcess.TO_TIME));
                        displayInfo.setAirConditionerMode(reponseJson.getBoolean(jsonProcess.AIR_CON_MODE));*/


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

        Thread requestData = new Thread() {
            public void run() {

                while (true) {
                    handler.post(updater);

                    try {
                        JSONObject requestJson = new JSONObject();
                        try {
                            requestJson.put(jsonProcess.GET_DATA, true);
                            exchangeData.log(requestJson.toString());
                        } catch (Exception e) {
                            Log.d("Error Get Data", e.getMessage());
                        }

                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }

                }
            }
        };
        requestData.start();

        /*mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                final int delay = 1000; //milliseconds
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //do something
                        try {

                            String msg = exchangeData.getMsg();
                            while (msg.equals("")) {
                                msg = exchangeData.getMsg();
                            }

                            try {
                                JSONObject reponseJson = new JSONObject(msg);

                                displayInfo.setAirConditionerTemperature(reponseJson.getInt(jsonProcess.AIR_CON_TEMP));
                                displayInfo.setTemperature(reponseJson.getInt(jsonProcess.TEMPERATURE));
                                displayInfo.setHumidity(reponseJson.getInt(jsonProcess.HUMIDITY));
                                displayInfo.setLight(reponseJson.getInt(jsonProcess.LIGHT));
                                displayInfo.setLightStatus(reponseJson.getBoolean(jsonProcess.LIGHT_POWER));
                                displayInfo.setAirConditionerStatus(reponseJson.getBoolean(jsonProcess.AIR_CON_POWER));
                                displayInfo.setPreferedTemperature(reponseJson.getInt(jsonProcess.PREF_TEMPERATURE));
                                displayInfo.setAirConditionerMode(reponseJson.getBoolean(jsonProcess.AIR_CON_MODE));
                                displayInfo.setLightMode(reponseJson.getBoolean(jsonProcess.LIGHT_MODE));
                                displayInfo.setFromTime(reponseJson.getString(jsonProcess.FROM_TIME));
                                displayInfo.setToTime(reponseJson.getString(jsonProcess.TO_TIME));
                                displayInfo.setAirConditionerMode(reponseJson.getBoolean(jsonProcess.AIR_CON_MODE));


                                TextView txtDp = (TextView) mainActivity.findViewById(R.id.textView1);
                                txtDp.setText(displayInfo.getUpdateDisplay());


                            } catch (JSONException e) {
                                Log.d("Error Reponse Data", e.getMessage());
                            }
                        } catch (InflateException e) {
                            e.printStackTrace();
                        }


                        handler.postDelayed(this, delay);
                    }
                }, delay);


            }
        }

    );*/
}


}
