package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.database.Sistem_Manual.MainActivity_Manual;
import com.example.database.Sistem_Scan.MainActivity_Scan;

@SuppressLint("CustomSplashScreen")
public class Splashscreen extends AppCompatActivity {
    private int waktu_loading=2000;
    mSharePreferences mSharePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mSharePreferences = new mSharePreferences(this);
        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags){
            case Configuration.UI_MODE_NIGHT_YES:
                mSharePreferences.setNightMode(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                //do something
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                //do something
                break;
        }
        if (mSharePreferences.loadNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (mSharePreferences.loadSistemScan()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main_scan = new Intent(Splashscreen.this, MainActivity_Scan.class);
                    startActivity(main_scan);
                    finish();
                }
            }, waktu_loading);
        }else if (mSharePreferences.loadSistemManual()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main_manual = new Intent(Splashscreen.this, MainActivity_Manual.class);
                    startActivity(main_manual);
                    finish();
                }
            }, waktu_loading);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent main = new Intent(Splashscreen.this, SelectionActivity.class);
                    startActivity(main);
                    finish();
                }
            }, waktu_loading);
        }


    }
}