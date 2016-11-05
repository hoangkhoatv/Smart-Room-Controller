package com.a4dsiotlab.remoteair;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
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

    int MIN_VALUE = 16;

    Button btnFrTime, btnToTime;

    void updateSettings(){
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
    void updateTextSwitch(){

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

    void updateDisplay() {

        /*
        Temperature: 30 oC
        Humidity: 40 %
        Light: 200 lux
        Air conditioner status: On/Off
        Air conditioner mode: Auto/Manual
        [Air conditioner temperature: 20oC] #neu dang bat moi hien
        [Prefered temperature: 26oC] #neu mode auto moi hien
        Light status: On/Off
        Light mode: Auto/Manual
         */

        StringBuilder lcd = new StringBuilder();
        lcd.append(String.format("Temperature: %d °C\n", displayInfo.getTemperature()));
        lcd.append(String.format("Humidity: %d %s \n", displayInfo.getHumidity(),"%"));
        lcd.append(String.format("Light: %d lux\n", displayInfo.getLight()));
        lcd.append(String.format("Air conditioner status: %s\n", displayInfo.getAirConditionerStatus()?"On":"Off"));
        lcd.append(String.format("Air conditioner mode: %s\n", displayInfo.getAirConditionerMode()?"Auto":"Manual"));
        if (displayInfo.getAirConditionerStatus()) {
            lcd.append(String.format("Air conditioner temperature: %d\n", displayInfo.getAirConditionerTemperature()));
        }
        if (displayInfo.getAirConditionerMode()) {
            lcd.append(String.format("Prefered temperature: %d °C\n", displayInfo.getPreferedTemperature()));
        }
        lcd.append(String.format("Light status: %s\n", displayInfo.getLightStatus()?"On":"Off"));
        lcd.append(String.format("Light mode: %s", displayInfo.getLightMode()?"Auto":"Manual"));
        txtDp.setText(lcd.toString());
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

        MyLogger msd = new MyLogger();
        msd.log("Nghi");
        Log.d("DATA",msd.getMsg());





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
        updateDisplay();
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
                updateDisplay();
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
                if(selectedMinute > 9){
                    txtToTime.setText( selectedHour + ":" + selectedMinute);
                }
                else {
                    txtToTime.setText( selectedHour + ":0" + selectedMinute);

                }
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
                }
                else {
                    aSwitch.setText("Manual");
                    setAutoOff();
                    setManualOn();
                    displayInfo.setAirConditionerMode(false);

                }
                updateDisplay();
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
                }
                else{
                    mSwitch.setText("Air Conditioner OFF");
                    for(int i = 0 ; i <rUpDownLL.getChildCount();i++){
                        View view = rUpDownLL.getChildAt(i);
                        view.setEnabled(false);
                    }
                    displayInfo.setAirConditionerStatus(false);
                }
                updateDisplay();
            }
        });

        lSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lSwitch.isChecked()){
                    lSwitch.setText("Auto");
                    setLSwitch.setEnabled(false);
                    displayInfo.setLightMode(true);
                }
                else {
                    lSwitch.setText("Manual");
                    setLSwitch.setEnabled(true);
                    displayInfo.setLightMode(false);
                }
                updateDisplay();

            }
        });

        setLSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (setLSwitch.isChecked()){
                    setLSwitch.setText("Light ON");
                    displayInfo.setLightStatus(true);
                }
                else {
                    setLSwitch.setText("Light OFF");
                    displayInfo.setLightStatus(false);
                }
                updateDisplay();
            }
        });
    }

}
