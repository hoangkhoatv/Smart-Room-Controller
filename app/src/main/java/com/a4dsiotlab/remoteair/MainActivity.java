package com.a4dsiotlab.remoteair;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

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
    String ipAddress;
    int portNumber;



    int MIN_VALUE = 16;

    Button btnFrTime, btnToTime;

    public void updateSettings(){
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

    public  boolean IsReachable(Context context) {
        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;

        if (netInfo != null && netInfo.isConnected()) {
            // Some sort of connection is open, check if server is reachable
            try {
                URL url = new URL("http://"+ipAddress);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "Android Application");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10 * 2000);
                urlc.connect();
                isReachable = (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                //Log.e(TAG, e.getMessage());
            }
        }

        return isReachable;
    }
    @Override
    public void onDestroy() {

      try {
            processData.close();
            exchangeData.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
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
        aSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch = (Switch) findViewById(R.id.switch2);
        lSwitch = (Switch) findViewById(R.id.switch3);
        setLSwitch = (Switch) findViewById(R.id.switch4);




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
                finish();


            }
        });


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
        //Setdata test
        displayInfo.setTime("10.20");
        displayInfo.setTemperature(28);
        displayInfo.setHumidity(70);
        displayInfo.setLight(696);
        displayInfo.setAirConditionerTemperature(27);
        


        //Get Data
        dataSettings = new DataSettings(getBaseContext());
        ipAddress = dataSettings.restoreIpAddress();
        portNumber = dataSettings.restorePort();


        // Go to Settings if no data
        if(dataSettings.restoreIpAddress().equals("")){
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        //Connect to Server
        else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            displayInfo.setFromTime("00:00");
            displayInfo.setToTime("00:00");
            setSeekbar();
            setSwitch();
            exchangeData = new ExchangeData(dataSettings.restoreIpAddress(),dataSettings.restorePort());
            processData = new ProcessData(exchangeData);
            processData.getDataServer(displayInfo,MainActivity.this);
            txtDp.setText(displayInfo.getUpdateDisplay());
            updateTextSwitch();
            updateSettings();

          /*  if (!isHostReachable(ipAddress,portNumber,1000)){
                txtDp.setText("No Internet Access");
                // startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Can not connect to Server:");

                // Setting Dialog Message
                alertDialog.setMessage("Do you want to go to wifi settings?");

                // Setting Icon to Dialog
                // alertDialog.setIcon(R.drawable.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Activity transfer to wifi settings
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
                            }
                        });

                // Setting Negative "NO" Button
                 /*alertDialog.setNegativeButton("no",
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 // Write your code here to invoke NO event

                                 dialog.cancel();
                             }
                         });

                // Showing Alert Message
                alertDialog.show();


            }*/



            }


            //ConnectToServer();


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







    }

    public static boolean isHostReachable(String serverAddress, int serverTCPport, int timeoutMS){
        boolean connected = false;
        Socket socket;
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverTCPport);
            socket.connect(socketAddress, timeoutMS);
            if (socket.isConnected()) {
                connected = true;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket = null;
        }
        return connected;
    }
    protected void ConnectToServer(){
        final Handler handler = new Handler();

        final Runnable updater = new Runnable() {

            public void run() {

                exchangeData = new ExchangeData(ipAddress,portNumber);
                processData = new ProcessData(exchangeData);
                processData.getDataServer(displayInfo,MainActivity.this);
                setSeekbar();
                setSwitch();
                txtDp.setText(displayInfo.getUpdateDisplay());
                updateTextSwitch();
                updateSettings();


            }


        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                while (!isHostReachable(ipAddress,portNumber,1000)){
                   handler.post(updater);

                }
            }
        }, 0, 3000);


        /*final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while () {


                    handler.post(updater);



                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }





            }});

       // t.start(); */// spawn thread
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
                    processData.postManualLight(displayInfo);

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
