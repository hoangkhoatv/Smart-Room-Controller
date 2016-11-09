package com.a4dsiotlab.remoteair;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView txtDp, txtFrTime, txtToTime;
    LinearLayout rl, rAutoLL,rTimeLL,rManualLL,rUpDownLL;
    DisplayInfo displayInfo = new DisplayInfo();
    Switch aSwitch, mSwitch, lSwitch, setLSwitch;
    FloatingActionButton fab ;
    SeekBar seekBar;
    ProcessData processData;
    ExchangeData exchangeData;
    DataSettings dataSettings;



    int MIN_VALUE = 16;

    Button btnFrTime, btnToTime;

    protected void updateSettings(){
        if(displayInfo.getAirConditionerMode()){
            aSwitch.setChecked(true);
            setAutoOn();
            setManualOff();
        }
        else {
            aSwitch.setChecked(false);
            setAutoOff();
            setManualOn();
        }

        if(displayInfo.getLightMode()){
            setLSwitch.setEnabled(false);
            lSwitch.setChecked(true);

        }
        else
        {
            setLSwitch.setEnabled(true);
            lSwitch.setChecked(false);
        }

        if (displayInfo.getLightStatus()){
            setLSwitch.setChecked(true);
        }
        else {
            setLSwitch.setChecked(false);
        }

        seekBar.setProgress(displayInfo.getPreferedTemperature());

    }
    protected void updateTextSwitch(){

        if(displayInfo.getAirConditionerMode()){
            aSwitch.setText("Auto");
        }
        else{
            aSwitch.setText("Manual");
        }

        if (displayInfo.getAirConditionerStatus()){
            mSwitch.setText("Air Conditioner ON");
        }
        else {
            mSwitch.setText("Air Conditioner OFF");
        }

        if(displayInfo.getLightStatus()){
            setLSwitch.setText("Light ON");
        }
        else {
            setLSwitch.setText("Light OFF");
        }

        if (displayInfo.getLightMode()){
            lSwitch.setText("Auto");
        }
        else {
            lSwitch.setText("Manual");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtDp = (TextView) findViewById(R.id.textView1);
        txtFrTime = (TextView) findViewById(R.id.textViewTime1);
        txtToTime = (TextView) findViewById(R.id.textViewTime2);
        rl = (LinearLayout) findViewById(R.id.replative) ;
        rAutoLL = (LinearLayout) findViewById(R.id.AutoLL);
        rTimeLL = (LinearLayout) findViewById(R.id.TimeLL);
        rManualLL = (LinearLayout) findViewById(R.id.ManualLL);
        rUpDownLL = (LinearLayout) findViewById(R.id.UpDownLL);
        btnFrTime = (Button) findViewById(R.id.buttonTime1);
        btnToTime = (Button) findViewById(R.id.buttonTime2);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        /*
        //update Display every 5s

        Thread updateDisplayTime = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    processData.getDataServer(displayInfo);
                }
            }
        });
        updateDisplayTime.start();*/

        //Get Data
        dataSettings = new DataSettings(getBaseContext());

        //Settings if no data
        if(dataSettings.restoreIpAddress().equals("")){
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
        }
        else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                           .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    exchangeData = new ExchangeData(dataSettings.restoreIpAddress(),dataSettings.restorePort());
                    processData = new ProcessData(exchangeData);
                  //  processData.getDataServer(displayInfo,this);


        }





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.icon, getBaseContext().getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.icon));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);


            }
        });

        aSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch = (Switch) findViewById(R.id.switch2);
        lSwitch = (Switch) findViewById(R.id.switch3);
        setLSwitch = (Switch) findViewById(R.id.switch4);

        setSwitch();




        /*final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (250 * scale + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, pixels);
        rl.setLayoutParams(params);*/
        //Typeface fontDigital = Typeface.createFromAsset(getAssets(),"Font/digital-7.ttf");
        //txtDp.setTypeface(fontDigital);
        setSeekbar();
        txtDp.setText(displayInfo.getUpdateDisplay());
        updateTextSwitch();
        updateSettings();



    }

    protected  void setSeekbar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                displayInfo.setPreferedTemperature(seekBar.getProgress() + MIN_VALUE);
                txtDp.setText(displayInfo.getUpdateDisplay());
                processData.postAutoAirCon(displayInfo);

            }
        });
    }

    public void setFromTime(final View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String tempTime;
                if(selectedMinute > 9){

                    tempTime = selectedHour + ":" + selectedMinute;

                }
                else {
                    tempTime =  selectedHour + ":0" + selectedMinute;
                }
                txtFrTime.setText( tempTime);
                displayInfo.setFromTime(tempTime);
                processData.postAutoAirCon(displayInfo);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select From Time");
        mTimePicker.show();

    }

    public void setToTime(View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String tempTime;

                if(selectedMinute > 9){
                    tempTime =  selectedHour + ":" + selectedMinute;
                }
                else {
                    tempTime = selectedHour + ":0" + selectedMinute;

                }
                txtToTime.setText( tempTime);
                displayInfo.setToTime(tempTime);
                processData.postAutoAirCon(displayInfo);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select To Time");
        mTimePicker.show();
    }

    protected void setAutoOn(){
        for ( int i = 0; i < rAutoLL.getChildCount();  i++ ){
            View view = rAutoLL.getChildAt(i);
            view.setEnabled(true);
        }
        for ( int i = 0; i < rTimeLL.getChildCount();  i++ ){
            View view = rTimeLL.getChildAt(i);
            view.setEnabled(true);
        }
    }

    protected void setAutoOff(){
        for ( int i = 0; i < rAutoLL.getChildCount();  i++ ){
            View view = rAutoLL.getChildAt(i);
            view.setEnabled(false);
        }
        for ( int i = 0; i < rTimeLL.getChildCount();  i++ ){
            View view = rTimeLL.getChildAt(i);
            view.setEnabled(false);
        }
    }
    protected void setManualOn(){
        for ( int i = 0; i < rManualLL.getChildCount();  i++ ){
            View view = rManualLL.getChildAt(i);
            view.setEnabled(true);
        }

        if(displayInfo.getAirConditionerStatus()){
            for ( int i = 0; i < rUpDownLL.getChildCount();  i++ ){
                View view = rUpDownLL.getChildAt(i);
                view.setEnabled(true);
                Log.d("On","1");
            }
        }
        else {
            for ( int i = 0; i < rUpDownLL.getChildCount();  i++ ){
                View view = rUpDownLL.getChildAt(i);
                view.setEnabled(false);
                Log.d("Off","0");
            }
        }

    }



    protected void setManualOff(){
        for ( int i = 0; i < rManualLL.getChildCount();  i++ ){
            View view = rManualLL.getChildAt(i);
            view.setEnabled(false);
        }

        for ( int i = 0; i < rUpDownLL.getChildCount();  i++ ){
            View view = rUpDownLL.getChildAt(i);
            view.setEnabled(false);
        }
    }


    public void setSwitch(){
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (aSwitch.isChecked()){
                    aSwitch.setText("Auto");
                    setAutoOn();
                    setManualOff();
                    displayInfo.setAirConditionerMode(true);
                    processData.postAutoAirCon(displayInfo);

                }
                else {
                    aSwitch.setText("Manual");
                    setAutoOff();
                    setManualOn();
                    displayInfo.setAirConditionerMode(false);
                    processData.postManualAirCon(displayInfo);
                }
                txtDp.setText(displayInfo.getUpdateDisplay());
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()){
                    mSwitch.setText("Air Conditioner ON");
                    for(int i = 0 ; i <rUpDownLL.getChildCount();i++){
                        View view = rUpDownLL.getChildAt(i);
                        view.setEnabled(true);

                    }
                    displayInfo.setAirConditionerStatus(true);
                    processData.postManualAirCon(displayInfo);
                }
                else{
                    mSwitch.setText("Air Conditioner OFF");
                    for(int i = 0 ; i <rUpDownLL.getChildCount();i++){
                        View view = rUpDownLL.getChildAt(i);
                        view.setEnabled(false);
                    }
                    displayInfo.setAirConditionerStatus(false);
                    processData.postManualAirCon(displayInfo);
                }
                txtDp.setText(displayInfo.getUpdateDisplay());

            }
        });

        lSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lSwitch.isChecked()){
                    lSwitch.setText("Auto");
                    setLSwitch.setEnabled(false);
                    displayInfo.setLightMode(true);
                    processData.postAutoLight(displayInfo);
                }
                else {
                    lSwitch.setText("Manual");
                    setLSwitch.setEnabled(true);
                    displayInfo.setLightMode(false);
                    processData.postAutoLight(displayInfo);
                }
                txtDp.setText(displayInfo.getUpdateDisplay());

            }
        });

        setLSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (setLSwitch.isChecked()){
                    setLSwitch.setText("Light ON");
                    displayInfo.setLightStatus(true);
                    processData.postAutoLight(displayInfo);

                }
                else {
                    setLSwitch.setText("Light OFF");
                    displayInfo.setLightStatus(false);
                    processData.postManualLight(displayInfo);
                }
                txtDp.setText(displayInfo.getUpdateDisplay());

            }
        });
    }

    public void clickUp(View view) {
        processData.postManualAirCon(displayInfo,"UP");


    }

    public void clickDown(View view) {
        processData.postManualAirCon(displayInfo,"DOWN");


    }
}
