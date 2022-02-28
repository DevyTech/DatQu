package com.example.database.Sistem_Manual;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.database.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity_Manual extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    public static MainActivity_Manual mainActivitymanual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manual);

        mainActivitymanual = this;

        bottomNavigationView = findViewById(R.id.navigasi_bawah_manual);
        floatingActionButton = findViewById(R.id.nav_input_penjualan);

        bottomNavigationView.setBackground(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new InputPenjualanFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.input_penjual);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.beranda_manual:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new HomeManualFragment()).commit();
                        break;
                    case R.id.input_produk:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new InputProdukFragment()).commit();
                        break;
                    case R.id.data_penjual:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new DataGroupManualFragment()).commit();
                        break;
                    case R.id.pengaturan_manual:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new PengaturanManualFragment()).commit();
                        break;
                }
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new InputPenjualanFragment()).commit();
                bottomNavigationView.setSelectedItemId(R.id.input_penjual);
            }
        });
    }

    public void go_default_menu(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new InputPenjualanFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.input_penjual);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, getTargetRequestCode());
        }else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, getTargetRequestCode());
        }else{

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == getTargetRequestCode() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //do something here if permission access
        }
    }

    private int getTargetRequestCode() {
        return 0;
    }
}