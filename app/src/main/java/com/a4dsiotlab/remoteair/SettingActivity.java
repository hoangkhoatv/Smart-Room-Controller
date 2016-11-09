package com.a4dsiotlab.remoteair;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by hoangkhoatv on 11/3/16.
 */

public class SettingActivity extends AppCompatActivity {
    FloatingActionButton fab ;
    EditText edtIp, edtPort;
    DataSettings dataSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        edtIp = (EditText) findViewById(R.id.editTextIP);
        edtPort = (EditText) findViewById(R.id.editTextPort);
        dataSettings = new DataSettings(getBaseContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.done, getBaseContext().getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.done));
        }
        restoreData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save and Back To Home", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if( !(edtIp.getText().toString().equals("") && edtPort.getText().toString().equals("0")) ){
                    if(validateIp(edtIp.getText().toString())){
                        if (validatePort(edtPort.getText().toString())){
                            saveData();
                            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Snackbar.make(view, "Port wrong! Ranging from 0 to 65535", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    else {
                        Snackbar.make(view, "IP Address wrong!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
                else {
                    Snackbar.make(view, "Please, Input IP and Port!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }



            }
        });
    }
    protected void saveData(){
        dataSettings.SaveData(edtIp.getText().toString(),Integer.valueOf(edtPort.getText().toString()));
    }

    protected void restoreData(){

        String strIp = dataSettings.restoreIpAddress();
        edtIp.setText(strIp);
        int intPort = dataSettings.restorePort();
        edtPort.setText(String.valueOf(intPort));

    }
    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public boolean validateIp( String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public boolean validatePort(String port){
        int numPort=Integer.valueOf(port);
        if( numPort>= 0 && numPort<=65535){
            return true;
        }
        return false;
    }

}
