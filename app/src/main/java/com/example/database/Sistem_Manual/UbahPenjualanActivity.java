package com.example.database.Sistem_Manual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

public class UbahPenjualanActivity extends AppCompatActivity {
    AppCompatImageButton imageButton_back,img_calender_ubah;
    AppCompatTextView id_key,ubah_total_price;
    AppCompatEditText ubah_tgl,ubah_produk,ubah_qty,ubah_price;
    TextInputLayout til_tgl,til_produk,til_qty,til_price;
    private DataHelperManual dataHelperManual;
    Button btn_ubah;
    protected Cursor cursor;
    MaterialDatePicker.Builder materialDateBuilder;
    MaterialDatePicker materialDatePicker;

    String[] addprodukubah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_penjualan);
        imageButton_back = findViewById(R.id.img_back_ubah);
        img_calender_ubah = findViewById(R.id.calendar_select_ubah);

        til_tgl = findViewById(R.id.til_tgl_penjualan_ubah);
        til_produk = findViewById(R.id.til_price_produk_ubah);
        til_qty = findViewById(R.id.til_qty_penjualan_ubah);
        til_price = findViewById(R.id.til_price_produk_ubah);

        id_key = findViewById(R.id.et_key_id);
        ubah_total_price = findViewById(R.id.tv_total_price_ubah);
        ubah_tgl = findViewById(R.id.et_ubah_tgl);
        ubah_produk = findViewById(R.id.et_ubah_produk);
        ubah_qty = findViewById(R.id.et_ubah_qty);
        ubah_price = findViewById(R.id.et_ubah_price);
        btn_ubah = findViewById(R.id.btn_ubah_penjualan);

        dataHelperManual = new DataHelperManual(this);

        build_material_date_picker();

        Bundle get_bundle = getIntent().getExtras();
        if (get_bundle!=null){
            String get_bundle_extras = get_bundle.getString("ID");
            SQLiteDatabase sqLiteDatabase_baca = dataHelperManual.getReadableDatabase();
            cursor = sqLiteDatabase_baca.rawQuery("SELECT * FROM penjualan WHERE ID_PENJUALAN = '"+get_bundle_extras+"'",null);
            cursor.moveToFirst();
            if (cursor.getCount()>0){
                cursor.moveToPosition(0);
                id_key.setText(cursor.getString(0));
                ubah_tgl.setText(cursor.getString(1));
                ubah_produk.setText(cursor.getString(2));
                ubah_qty.setText(cursor.getString(4));
                ubah_total_price.setText(cursor.getString(5));
                set_price();
            }
        }

        img_calender_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                ubah_tgl.setText(materialDatePicker.getHeaderText());
            }
        });

        btn_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ubah_tgl.getText().toString().trim().equals("")){
                    til_tgl.setErrorEnabled(true);
                    til_tgl.setError("Tanggal Tidak Boleh Kosong");
                }else if (ubah_produk.getText().toString().trim().equals("")){
                    til_produk.setErrorEnabled(true);
                    til_produk.setError("Produk Tidak Boleh Kosong");
                } else if (ubah_qty.getText().toString().trim().equals("")){
                    til_qty.setErrorEnabled(true);
                    til_qty.setError("Jumlah Produk Tidak Boleh kosong");
                }else if (ubah_price.getText().toString().trim().equals("")){
                    til_price.setErrorEnabled(true);
                    til_price.setError("Harga Tidak Boleh Kosong");
                }else if (ubah_total_price.getText().toString().equals("0")){
                    Toast.makeText(getApplicationContext(), "Total Tidak Valid", Toast.LENGTH_SHORT).show();
                } else{
                    SQLiteDatabase sqLiteDatabase_ubah = dataHelperManual.getWritableDatabase();
                    sqLiteDatabase_ubah.execSQL("UPDATE penjualan SET TANGGAL='"+ubah_tgl.getText().toString()+"', " +
                            "PRODUK='"+ubah_produk.getText().toString()+"', " +
                            "QTY='"+ubah_qty.getText().toString()+"', " +
                            "TOTAL_PRICE='"+ubah_total_price.getText().toString()+"' " +
                            "WHERE ID_PENJUALAN='"+id_key.getText().toString()+"'");
                    LihatDataPenjualanActivity.lihatDataPenjualanActivity.tampil_data_penjualan(ubah_tgl.getText().toString());
                    Toast.makeText(getApplicationContext(), "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ubah_tgl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_tgl.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ubah_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_qty.setErrorEnabled(false);
                total_price();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ubah_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_price.setErrorEnabled(false);
                total_price();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void build_material_date_picker(){
        materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("PILIH TANGGAL");
        materialDatePicker = materialDateBuilder.build();
    }

    private void set_price(){
        int qty_value = Integer.parseInt(ubah_qty.getText().toString());
        int total_price_value = Integer.parseInt(ubah_total_price.getText().toString());
        int bagi = total_price_value/qty_value;
        ubah_price.setText(String.valueOf(bagi));
    }
    private void total_price(){
        if (ubah_qty.getText().toString().trim().equals("")||ubah_price.getText().toString().trim().equals("")){
            ubah_total_price.setText("0");
        }else{
            int qty = Integer.parseInt(ubah_qty.getText().toString());
            int price = Integer.parseInt(ubah_price.getText().toString());
            int kali = qty*price;
            ubah_total_price.setText(String.valueOf(kali));
        }
    }
}