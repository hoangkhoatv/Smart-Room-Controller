package com.a4dsiotlab.remoteair;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by hoangkhoatv on 11/3/16.
 */

public class JsonProcess {
    public JSONObject WriteToJson(DisplayInfo df, SettingsMode sm){
        JSONObject obj = new JSONObject();
        try {

            obj.put("PowerMahcine",df.getSttMachine());
            obj.put("PowerLight",df.getSttLight());
            obj.put("Temp",df.getTemp());
            obj.put("Hum",df.getHum());
            obj.put("Light",df.getLight());
            obj.put("ModeMachine",sm.getModeMachine());
            obj.put("ModeLight",sm.getModeLight());
            obj.put("FromTime",sm.getFormTime());
            obj.put("ToTime",sm.getToTime());

        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        return obj;
    }
    public void ReadFromJson(JSONObject obj,DisplayInfo df, SettingsMode sm){
        try {

            df.setSttMachine( obj.getBoolean("PowerMahcine") );
            df.setSttLight( obj.getBoolean("PowerLight"));
            df.setTemp( obj.getInt("Temp") );
            df.setHum( obj.getInt("Hum"));
            df.setLight( obj.getInt("Light"));

            sm.setModeMachine( obj.getBoolean("ModeMachine") );
            sm.setModeLight( obj.getBoolean("ModeLight") );
            sm.setFormTime( obj.getString("FromTime") );
            sm.setToTime( obj.getString("ToTime"));

        }catch (Exception e){
            Log.e("Error",e.getMessage());

        }

    }
}
