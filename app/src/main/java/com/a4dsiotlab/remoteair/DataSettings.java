package com.a4dsiotlab.remoteair;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;


public class DataSettings {
    public static final String SETTINGS_DATA = "settings_data";
    public static final String IP_ADDRESS = "str_ip";
    public static final String Port_NUMBER = "num_port";
    SharedPreferences preSettings;

    public DataSettings(Context context) {
        preSettings = context.getSharedPreferences(SETTINGS_DATA, MODE_PRIVATE);
    }

    public void SaveData(String ipadress, int port) {
        SharedPreferences.Editor edit = preSettings.edit();
        edit.putInt(Port_NUMBER, port);
        edit.putString(IP_ADDRESS, ipadress);
        edit.commit();
    }

    public String restoreIpAddress() {

        return preSettings.getString(IP_ADDRESS, "");

    }

    public int restorePort() {
        return preSettings.getInt(Port_NUMBER, 0);
    }
}
