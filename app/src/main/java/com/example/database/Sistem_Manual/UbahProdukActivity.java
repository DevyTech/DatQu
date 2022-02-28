package com.example.database.Sistem_Manual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.google.android.material.textfield.TextInputLayout;

public class UbahProdukActivity extends AppCompatActivity {
    DataHelperManual dataHelperManual;
    protected Cursor cursor;
    AppCompatImageButton img_back;
    AppCompatEditText produk,jenis,price;
    TextInputLayout til_produk,til_jenis,til_price;
    Button ubah_produk;
    String get_id_produk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_produk);

        dataHelperManual = new DataHelperManual(this);

        img_back = findViewById(R.id.img_back_ubah_produk);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        til_produk = findViewById(R.id.til_produk_produk_ubah);
        til_jenis = findViewById(R.id.til_jenis_produk_ubah);
        til_price = findViewById(R.id.til_price_produk_ubah);

        produk = findViewById(R.id.et_produk_produk_ubah);
        jenis = findViewById(R.id.et_jenis_produk_ubah);
        price = findViewById(R.id.et_price_produk_ubah);

        ubah_produk = findViewById(R.id.btn_ubah_produk);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            get_id_produk = bundle.getString("ID_PRODUK");
            SQLiteDatabase sqLiteDatabase = dataHelperManual.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM menuitem WHERE ID_ITEM='"+get_id_produk+"'",null);
            cursor.moveToFirst();
            if (cursor.getCount()>0){
                cursor.moveToPosition(0);
                produk.setText(cursor.getString(1));
                jenis.setText(cursor.getString(2));
                price.setText(cursor.getString(3));
            }
        }

        ubah_produk.setOnClickListener(new View.OnClickListener() {
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
                }else {
                    try {
                        SQLiteDatabase sqLiteDatabase_ubah = dataHelperManual.getWritableDatabase();
                        sqLiteDatabase_ubah.execSQL("UPDATE menuitem SET PRODUK='"+produk.getText().toString()+"', " +
                                "JENIS='"+jenis.getText().toString()+"'," +
                                "PRICE='"+price.getText().toString()+"' " +
                                "WHERE ID_ITEM='"+get_id_produk+"'");
                        Toast.makeText(getApplicationContext(), "Data Produk Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        DataProdukFragment.dataProdukFragment.tampil_data();
                        finish();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}