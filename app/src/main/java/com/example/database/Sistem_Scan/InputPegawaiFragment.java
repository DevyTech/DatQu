package com.example.database.Sistem_Scan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.android.material.textfield.TextInputLayout;

public class InputPegawaiFragment extends Fragment {

    String[] list_item = {"Admin OHS","Admin Paniki",
            "Core Shed","Core Shed Coordinator",
            "Cleaning Services","Geotech"
            ,"General Helper","Junior Geologi",
            "Logistic","Leader",
            "OB","Officer Safety",
            "Office Boy-Wesco","OHS Technical",
            "Relation Officer","RO & Coord Crew",
            "Security","Utility Maintenance"};
    EditText nik,nama;
    Button simpan;
    TextInputLayout til_nik,til_nama,til_divisi;
    AutoCompleteTextView divisi;

    DataHelperScan dataHelperScan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_pegawai, container, false);

        dataHelperScan = new DataHelperScan(getContext());

        nik = v.findViewById(R.id.et_nik);
        nama = v.findViewById(R.id.et_nama);
        simpan = v.findViewById(R.id.btn_simpan);
        divisi = v.findViewById(R.id.act_dropdown);
        til_nik = v.findViewById(R.id.til_nik);
        til_nama = v.findViewById(R.id.til_nama);
        til_divisi = v.findViewById(R.id.til_divisi);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, list_item);
        divisi.setAdapter(adapter);
        close_keyboard();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getnikvalue = nik.getText().toString();
                String getnamavalue = nama.getText().toString();
                if(getnikvalue.trim().equals("")){
                    til_nik.setErrorEnabled(true);
                    til_nik.setError("NIK Tidak Boleh Kosong");
                    nik.requestFocus();
                }else if (getnamavalue.trim().equals("")){
                    til_nama.setErrorEnabled(true);
                    til_nama.setError("Nama Tidak Boleh Kosong");
                    nama.requestFocus();
                }else if (divisi.getText().equals("")){
                    til_nama.setErrorEnabled(true);
                    til_divisi.setError("Divisi Tidak Boleh Kosong");
                }
                else{
                    try {
                        SQLiteDatabase db = dataHelperScan.getWritableDatabase();
                        db.execSQL("INSERT INTO karyawan (NIK, NAMA, DIVISI) VALUES ('"+nik.getText().toString()+"'," +
                                "'"+nama.getText().toString()+"'," +
                                "'"+divisi.getText().toString()+"');");
                        kosong();
                        Toast.makeText(getContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                    }catch (SQLiteException e){
                        nik.setText("");
                        nik.requestFocus();
                        nik.setError("NIK Sudah Terpakai");
                    }
                }
            }
        });

        nik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_nik.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_nama.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Scan.mainActivityScan.default_menuselected();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }

    private void kosong(){
        nik.setText("");
        nama.setText("");
        divisi.setText("");
        nik.requestFocus();
    }

    private void close_keyboard(){
        View view = this.getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(),0);
        }
    }
}