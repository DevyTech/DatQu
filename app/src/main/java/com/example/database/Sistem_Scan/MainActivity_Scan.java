package com.example.database.Sistem_Scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.database.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity_Scan extends AppCompatActivity {

    FloatingActionButton scan;
    BottomNavigationView bottomNavigationView;
    public static MainActivity_Scan mainActivityScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scan);

        mainActivityScan = this;
        bottomNavigationView = findViewById(R.id.navigasi_bawah);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl, new PengaturanScanFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.pengaturan);
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fl, new ScanFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.scan);
        }
        scan = findViewById(R.id.nav_scan);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.beranda:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl, new HomeScanFragment()).commit();
                        break;
                    case R.id.input:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl, new InputPegawaiFragment()).commit();
                        break;
                    case R.id.data:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl, new DataGroupScanFragment()).commit();
                        break;
                    case R.id.pengaturan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl, new PengaturanScanFragment()).commit();
                        break;
                }
                return true;
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, new ScanFragment()).commit();
                bottomNavigationView.setSelectedItemId(R.id.scan);
            }
        });
    }

    public void default_menuselected(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl, new ScanFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.scan);
    }

    public void menuselected(){
        bottomNavigationView.setSelectedItemId(R.id.scan);
    }
}