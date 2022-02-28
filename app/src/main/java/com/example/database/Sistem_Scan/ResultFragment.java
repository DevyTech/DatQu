package com.example.database.Sistem_Scan;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultFragment extends Fragment {
    TextView rsnik,rsnama,rsdevisi,rsjam;
    Button simpan_scan;
    String KEY_RESULT = "scan_result";
    DataHelperScan dataHelperScan;
    Calendar calendar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        dataHelperScan = new DataHelperScan(getContext());
        simpan_scan = v.findViewById(R.id.btn_simpan_scan);

        rsnik = v.findViewById(R.id.tv_rsnik);
        rsnama = v.findViewById(R.id.tv_rsnama);
        rsdevisi = v.findViewById(R.id.tv_rsdevisi);
        rsjam = v.findViewById(R.id.tv_rsjam);

        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss aa");
        String formatdate = dateFormat.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tanggal = new SimpleDateFormat("dd MMM yyyy");
        String formattanggal = tanggal.format(calendar.getTime());
        Bundle bundle = getArguments();
        if (bundle != null){
            String get_rsnik = bundle.getString(KEY_RESULT);
            SQLiteDatabase sqLiteDatabase = dataHelperScan.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM karyawan WHERE NIK = '"+get_rsnik+"'",null);
            cursor.moveToPosition(0);
            rsnik.setText(cursor.getString(1));
            rsnama.setText(cursor.getString(2));
            rsdevisi.setText(cursor.getString(3));
            rsjam.setText(formatdate);
        }else{
            Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fl, new ScanFragment()).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        simpan_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db_insert = dataHelperScan.getWritableDatabase();
                db_insert.execSQL("INSERT INTO scanresult(NIK_SCAN,NAMA_SCAN,DIVISI_SCAN,JAM_SCAN,TANGGAL) VALUES('"+rsnik.getText().toString()+"'," +
                        "'"+rsnama.getText().toString()+"'," +
                        "'"+rsdevisi.getText().toString()+"'," +
                        "'"+rsjam.getText().toString()+"'," +
                        "'"+formattanggal+"');");
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fl, new ScanFragment()).commit();
                Snackbar.make(view,"Data Scan Disimpan",Snackbar.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}