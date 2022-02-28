package com.example.database.Sistem_Manual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.database.R;
import com.example.database.SelectionActivity;
import com.example.database.mSharePreferences;

public class PengaturanManualFragment extends Fragment {

    com.example.database.mSharePreferences mSharePreferences;
    CardView logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaturan_manual, container, false);

        mSharePreferences = new mSharePreferences(getContext());
        logout = view.findViewById(R.id.cdv_logout_manual);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Perhatian");
                builder.setMessage("Keluar Sistem Manual?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSharePreferences.setSistemManual(false);
                        Intent intent_logout = new Intent(getContext(), SelectionActivity.class);
                        startActivity(intent_logout);
                        MainActivity_Manual.mainActivitymanual.finish();
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Manual.mainActivitymanual.go_default_menu();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }
}