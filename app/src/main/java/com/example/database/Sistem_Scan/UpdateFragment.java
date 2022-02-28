package com.example.database.Sistem_Scan;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.android.material.snackbar.Snackbar;

public class UpdateFragment extends Fragment {
    protected Cursor cursor;
    String[] list_item = {"Admin OHS","Admin Paniki",
            "Core Shed","Core Shed Coordinator",
            "Cleaning Services","Geotech"
            ,"General Helper","Junior Geologi",
            "Logistic","Leader",
            "OB","Officer Safety",
            "Office Boy-Wesco","OHS Technical",
            "Relation Officer","RO & Coord Crew",
            "Security","Utility Maintenance"};
    int divisi_position;
    String divisi_setdefault;

    TextView id;
    EditText nik,nama;
    AutoCompleteTextView divisi;
    Button ubah;

    DataHelperScan dataHelperScan;

    String KEY_FRG_UPDT_NIK = "nik_set";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update, container, false);

        dataHelperScan = new DataHelperScan(getContext());

        id = v.findViewById(R.id.ID_pegawai);
        nik = v.findViewById(R.id.et_nik_new);
        nama = v.findViewById(R.id.et_nama_new);
        divisi = v.findViewById(R.id.act_dropdown_new);
        ubah = v.findViewById(R.id.btn_ubah);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, list_item);

        Bundle bundle = getArguments();
        if (bundle != null){
            String get_nik = bundle.getString(KEY_FRG_UPDT_NIK);
            SQLiteDatabase sqLiteDatabase = dataHelperScan.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM karyawan WHERE NIK = '"+get_nik+"'",null);
            cursor.moveToFirst();
            if (cursor.getCount()>0){
                cursor.moveToPosition(0);

                id.setText(cursor.getString(0));
                nik.setText(cursor.getString(1));
                nama.setText(cursor.getString(2));

                divisi_setdefault = cursor.getString(3);
                divisi_position = adapter.getPosition(divisi_setdefault);
                try {
                    divisi.setText(cursor.getString(3));
                    divisi.setAdapter(adapter);
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase sqLiteDatabase = dataHelperScan.getWritableDatabase();
                    sqLiteDatabase.execSQL("UPDATE karyawan SET NIK='"+nik.getText().toString()+"'," +
                            " NAMA='"+nama.getText().toString()+"'," +
                            "DIVISI='"+divisi.getText().toString()+"' " +
                            "WHERE ID='"+id.getText().toString()+"'");
                    Snackbar.make(view,"Data Berhasil DiUbah", Snackbar.LENGTH_LONG).show();
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fl, new DataGroupScanFragment()).commit();
                }catch (SQLException e){
                    Snackbar.make(view,"Gagal Mengubah Data", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fl, new DataGroupScanFragment()).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }
}