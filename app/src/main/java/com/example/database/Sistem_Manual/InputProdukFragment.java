package com.example.database.Sistem_Manual;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.google.android.material.textfield.TextInputLayout;

public class InputProdukFragment extends Fragment {
    DataHelperManual dataHelperManual;
    TextInputLayout til_produk,til_jenis,til_price;
    EditText produk,jenis,price;

    Button simpan_produk;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_produk, container, false);

        dataHelperManual = new DataHelperManual(getContext());

        til_produk = v.findViewById(R.id.til_produk_produk);
        til_jenis = v.findViewById(R.id.til_jenis_produk);
        til_price = v.findViewById(R.id.til_price_produk);

        produk = v.findViewById(R.id.et_produk_produk);
        jenis = v.findViewById(R.id.et_jenis_produk);
        price = v.findViewById(R.id.et_price_produk);

        simpan_produk = v.findViewById(R.id.btn_simpan_produk);

        simpan_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (produk.getText().toString().trim().equals("")){
                    til_produk.setErrorEnabled(true);
                    til_produk.setError("Produk Tidak Boleh Kosong");
                    produk.requestFocus();
                }else if (jenis.getText().toString().trim().equals("")){
                    til_jenis.setErrorEnabled(true);
                    til_jenis.setError("Jenis Tidak Boleh Kosong");
                    jenis.requestFocus();
                }else if (price.getText().toString().trim().equals("")){
                    til_price.setErrorEnabled(true);
                    til_price.setError("Harga Tidak Boleh Kosong");
                    price.requestFocus();
                }else{
                    try {
                        SQLiteDatabase sqLiteDatabase_cek_data_produk = dataHelperManual.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase_cek_data_produk.rawQuery("SELECT * FROM menuitem WHERE PRODUK LIKE'%"+produk.getText().toString()+"%'",null);
                        cursor.moveToFirst();
                        if (cursor.getCount()>0){
                            cursor.moveToPosition(0);
                            produk.requestFocus();
                            produk.setError("Produk Sudah Terdaftar");
                        }else{
                            SQLiteDatabase sql_input_produk = dataHelperManual.getWritableDatabase();
                            sql_input_produk.execSQL("INSERT INTO Menuitem (PRODUK,JENIS,PRICE) VALUES('"+produk.getText().toString()+"'," +
                                    "'"+jenis.getText().toString()+"'," +
                                    "'"+price.getText().toString()+"');");
                            Toast.makeText(getContext(), "Data Produk Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                            kosong();
                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_produk.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        jenis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_jenis.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_price.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Manual.mainActivitymanual.go_default_menu();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }

    private void kosong(){
        produk.setText("");
        jenis.setText("");
        price.setText("");
        produk.requestFocus();
    }
}