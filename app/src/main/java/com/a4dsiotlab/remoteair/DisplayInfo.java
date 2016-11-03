package com.a4dsiotlab.remoteair;

/**
 * Created by hoangkhoatv on 11/1/16.
 */

public class DisplayInfo {
    private int temp;
    private int hud;
    private int light;
    private boolean sttLight;
    private boolean sttMachine;


    public  void setTemp(int t){
        this.temp = t;
    }

    public  void setHum(int h){
        this.hud = h;
    }

    public  void setLight(int l){
        this.light = l;
    }

    public  void setSttLight(boolean l){
        this.sttLight = l;
    }

    public  void setSttMachine(boolean m){
        this.sttMachine = m;
    }

    public int getTemp(){
        return  this.temp;
    }

    public  int getHum(){
        return  this.hud;
    }

    public int getLight(){
        return this.light;
    }

    public boolean getSttLight(){
        return  this.sttLight;
    }

    public boolean getSttMachine(){
        return  this.sttMachine;
    }

}
