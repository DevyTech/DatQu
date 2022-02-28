package com.example.database;

import android.content.Context;
import android.content.SharedPreferences;

public class mSharePreferences {
    SharedPreferences sharedPreferences;
    public mSharePreferences(Context ctx){
        sharedPreferences = ctx.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }
    //Fungsi untuk menyimpan tema gelap state : true or false
    public void setNightMode(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    public void setSistemScan(Boolean sistem){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Scan", sistem);
        editor.apply();
    }

    public void setSistemManual(Boolean sistem){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Manual", sistem);
        editor.apply();
    }
    //fungsi untuk memanggil tema gelap state
    public boolean loadNightMode(){
        return sharedPreferences.getBoolean("NightMode", false);
    }
    public boolean loadSistemScan(){
        return sharedPreferences.getBoolean("Scan", false);
    }
    public boolean loadSistemManual(){
        return sharedPreferences.getBoolean("Manual", false);
    }

}
