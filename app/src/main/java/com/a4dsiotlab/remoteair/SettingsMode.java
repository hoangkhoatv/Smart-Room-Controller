package com.a4dsiotlab.remoteair;

/**
 * Created by hoangkhoatv on 11/3/16.
 */

public class SettingsMode {
    private boolean modeMachine;
    private boolean modeLight;
    private  String formTime;
    private  String toTime;

    public void setModeMachine(boolean m){
        this.modeMachine = m;
    }

    public void setModeLight(boolean m){
        this.modeLight = m;
    }

    public void setFormTime(String time){
        this.formTime=time;
    }

    public void setToTime(String time){
        this.toTime=time;
    }

    public boolean getModeMachine(){
        return this.modeMachine;
    }

    public boolean getModeLight(){
        return  this.modeLight;
    }

    public String getFormTime(){
        return  this.formTime;
    }

    public String getToTime(){
        return this.toTime;
    }
}
