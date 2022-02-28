package com.example.database.Sistem_Scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.database.R;
import com.example.database.SelectionActivity;
import com.example.database.mSharePreferences;

public class PengaturanScanFragment extends Fragment {

    mSharePreferences mSharePreferences;
    SwitchCompat aSwitch;

    CardView card_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_pengaturan_scan, container, false);
        aSwitch = v.findViewById(R.id.sw_dark);
        card_logout = v.findViewById(R.id.cdv_logout);
        aSwitch.setChecked(false);
        mSharePreferences = new mSharePreferences(getContext());
        if (mSharePreferences.loadNightMode() || AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mSharePreferences.setNightMode(true);
                    MainActivity_Scan.mainActivityScan.menuselected();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mSharePreferences.setNightMode(false);
                    MainActivity_Scan.mainActivityScan.menuselected();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Scan.mainActivityScan.default_menuselected();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        card_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Perhatian");
                builder.setMessage("Keluar Sistem Scan?");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSharePreferences.setSistemScan(false);
                        Intent intent_logout = new Intent(getContext(), SelectionActivity.class);
                        startActivity(intent_logout);
                        MainActivity_Scan.mainActivityScan.finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        return v;
    }

}