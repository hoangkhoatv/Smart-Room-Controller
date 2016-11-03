package com.a4dsiotlab.remoteair;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView txtDp, txtFrTime, txtToTime;
    LinearLayout rl, rAutoLL,rTimeLL,rManualLL,rUpDownLL;
    DisplayInfo displayInfo;
    Switch aSwitch, mSwitch, lSwitch, setLSwitch;
    FloatingActionButton fab ;

    Button btnFrTime, btnToTime;

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
        aSwitch.setText("Auto");
        aSwitch.setChecked(true);
        mSwitch.setText("Machine On");
        mSwitch.setChecked(true);
        setSwitch();




        /*final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (250 * scale + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, pixels);
        rl.setLayoutParams(params);*/
        Typeface fontDigital = Typeface.createFromAsset(getAssets(),"Font/digital-7.ttf");
        txtDp.setTypeface(fontDigital);
        displayInfo = new DisplayInfo();
        displayInfo.setTemp(26);
        displayInfo.setHum(500);
        displayInfo.setLight(400);
        displayInfo.setSttLight(true);
        displayInfo.setSttMachine(false);


        String txtDisplay = "Temp: " + displayInfo.getTemp() + "\n" +
                            "Hud: " + displayInfo.getHum() + "\n" +
                            "Light: " + displayInfo.getLight() + "\n" +
                            "STT Light: " + displayInfo.getSttLight() + "\n" +
                            "STT Machine: " + displayInfo.getSttMachine();
        txtDp.setText(txtDisplay);
    }

    public void setFromTime(final View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute > 9){
                    txtFrTime.setText( selectedHour + ":" + selectedMinute);

                }
                else {
                    txtFrTime.setText( selectedHour + ":0" + selectedMinute);

                }

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
        for ( int i = 0; i < rUpDownLL.getChildCount();  i++ ){
            View view = rUpDownLL.getChildAt(i);
            view.setEnabled(true);
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
                }
                else {
                    aSwitch.setText("Manual");
                    setAutoOff();
                    setManualOn();
                }
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()){
                    mSwitch.setText("Machine On");
                    for(int i = 0 ; i <rUpDownLL.getChildCount();i++){
                        View view = rUpDownLL.getChildAt(i);
                        view.setEnabled(true);
                    }
                }
                else{
                    mSwitch.setText("Machine Off");
                    for(int i = 0 ; i <rUpDownLL.getChildCount();i++){
                        View view = rUpDownLL.getChildAt(i);
                        view.setEnabled(false);
                    }
                }
            }
        });

        lSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lSwitch.isChecked()){
                    lSwitch.setText("Auto");
                    setLSwitch.setEnabled(false);
                }
                else {
                    lSwitch.setText("Manual");
                    setLSwitch.setEnabled(true);
                }

            }
        });

        setLSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (setLSwitch.isChecked()){
                    setLSwitch.setText("Light On");
                }
                else {
                    setLSwitch.setText("Light Off");
                }
            }
        });
    }

}
