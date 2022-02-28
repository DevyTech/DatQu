package com.example.database;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.database.Sistem_Manual.MainActivity_Manual;
import com.example.database.Sistem_Scan.MainActivity_Scan;
import com.example.database.Sistem_Scan.ScanFragment;

public class SelectionActivity extends AppCompatActivity {

    Button scan,manual;
    mSharePreferences mSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        scan = findViewById(R.id.sistem_scan);
        manual = findViewById(R.id.sistem_manual);
        mSharePreferences = new mSharePreferences(this);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharePreferences.setSistemScan(true);
                Intent main_scan = new Intent(SelectionActivity.this, MainActivity_Scan.class);
                startActivity(main_scan);
                finish();
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharePreferences.setSistemManual(true);
                Intent main_manual = new Intent(SelectionActivity.this, MainActivity_Manual.class);
                startActivity(main_manual);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == getTargetRequestCode() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //do something here if permission access
        }
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

    private int getTargetRequestCode() {
        return 0;
    }
}