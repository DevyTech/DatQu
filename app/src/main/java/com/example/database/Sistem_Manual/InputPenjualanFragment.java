package com.example.database.Sistem_Manual;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InputPenjualanFragment extends Fragment {
    EditText tgl,qty,price;
    AppCompatTextView total_price;
    AutoCompleteTextView produk;
    TextInputLayout til_tgl,til_produk,til_qty,til_price;
    Button simpan_penjualan;
    DataHelperManual dataHelperManual;
    Calendar calendar;
    AppCompatImageButton img_calender;
    MaterialDatePicker.Builder materialDateBuilder;

    MaterialDatePicker materialDatePicker;

    String[] addproduk;
    String[] addprice;
    String[] addjenis;

    String jenis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_penjualan, container, false);

        //Call DataHelperManual
        dataHelperManual = new DataHelperManual(getContext());

        //setTextView Price
        total_price = v.findViewById(R.id.tv_total_price);

        //setMaterialDatePicker
        build_material_date_picker();

        //get Button
        simpan_penjualan = v.findViewById(R.id.btn_simpan_penjualan);
        //get ImageButton Calender;
        img_calender = v.findViewById(R.id.calendar_select);
        //get edittext
        tgl = v.findViewById(R.id.et_tgl_penjualan);
        produk = v.findViewById(R.id.act_dropdown_produk);
        qty = v.findViewById(R.id.et_qty_penjualan);
        price = v.findViewById(R.id.et_price_penjualan);

        //get TextInputLayout
        til_tgl = v.findViewById(R.id.til_tgl_penjualan);
        til_produk = v.findViewById(R.id.til_produk_penjualan);
        til_qty = v.findViewById(R.id.til_qty_penjualan);
        til_price = v.findViewById(R.id.til_price_penjualan);

        //setTanggal
        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String formatTanggal = dateFormat.format(calendar.getTime());
        tgl.setText(formatTanggal);

        //set ImageButton Calender Selector
        img_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                materialDatePicker.show(getFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });

        //set positive button handle
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                tgl.setText(materialDatePicker.getHeaderText());
            }
        });

        set_produk();

        simpan_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tgl.getText().toString().trim().equals("")){
                    til_tgl.setErrorEnabled(true);
                    til_tgl.setError("Tanggal Tidak Boleh Kosong");
                }else if (produk.getText().toString().trim().equals("")){
                    til_produk.setErrorEnabled(true);
                    til_produk.setError("Silahkan Memilih Produk");
                }else if (qty.getText().toString().trim().equals("")){
                    til_qty.setErrorEnabled(true);
                    til_qty.setError("Jumlah Produk Tidak Boleh Kosong");
                    qty.requestFocus();
                }else if (price.getText().toString().trim().equals("")){
                    til_price.setErrorEnabled(true);
                    til_price.setError("Harga Tidak Boleh Kosong");
                    price.requestFocus();
                }else{
                    SQLiteDatabase cek = dataHelperManual.getReadableDatabase();
                    Cursor cursor = cek.rawQuery("SELECT PRODUK FROM penjualan WHERE TANGGAL='"+tgl.getText().toString()+"' AND PRODUK='"+produk.getText().toString()+"'",null);
                    if (cursor.getCount()>0){
                        til_produk.setErrorEnabled(true);
                        til_produk.setError("Penjualan "+produk.getText().toString()+" Sudah Terdata Hari ini");
                    }else {
                        try {
                            SQLiteDatabase sqLiteDatabase = dataHelperManual.getWritableDatabase();
                            sqLiteDatabase.execSQL("INSERT INTO penjualan (TANGGAL,PRODUK,JENIS,QTY,TOTAL_PRICE) VALUES('"+tgl.getText().toString()+"'," +
                                    "'"+produk.getText().toString()+"'," +
                                    "'"+jenis+"'," +
                                    "'"+qty.getText().toString()+"'," +
                                    "'"+total_price.getText().toString()+"');");
                            Toast.makeText(getContext(), "Data Penjualan Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                            kosong();
                        }catch (Exception e){
                            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        tgl.addTextChangedListener(new TextWatcher() {
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
        produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_produk.setErrorEnabled(false);
                if (qty.getText().toString().trim().equals("")){
                    total_price.setText("0");
                }else {
                    int qty_value = Integer.parseInt(qty.getText().toString());
                    int price_value = 0;
                    int hitung = qty_value*price_value;
                    total_price.setText(String.valueOf(hitung));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_qty.setErrorEnabled(false);
                if (qty.getText().toString().trim().equals("")||price.getText().toString().trim().equals("")){
                    total_price.setText("0");
                }else {
                    int qty_value = Integer.parseInt(qty.getText().toString());
                    int price_value = Integer.parseInt(price.getText().toString());
                    int hitung = qty_value*price_value;
                    total_price.setText(String.valueOf(hitung));
                }

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
                if (qty.getText().toString().trim().equals("")||price.getText().toString().trim().equals("")){
                    total_price.setText("0");
                }else{
                    int qty_value = Integer.parseInt(qty.getText().toString());
                    int price_value = Integer.parseInt(price.getText().toString());
                    int hitung = qty_value*price_value;
                    total_price.setText(String.valueOf(hitung));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Manual.mainActivitymanual.finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }

    private void build_material_date_picker(){
        materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("PILIH TANGGAL");
        materialDatePicker = materialDateBuilder.build();
    }

    private void set_produk(){
        try {
            Cursor cursor = dataHelperManual.baca_produk();
            if (cursor.getCount()>0){
                addproduk = new String[cursor.getCount()];
                addjenis = new String[cursor.getCount()];
                addprice = new String[cursor.getCount()];
                cursor.moveToFirst();
                for (int i=0;i<cursor.getCount();i++){
                    cursor.moveToPosition(i);
                    addproduk[i] = cursor.getString(1);
                    addjenis[i] = cursor.getString(2);
                    addprice[i] = cursor.getString(3);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, addproduk);
                    produk.setAdapter(adapter);
                }
                produk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        price.setText(addprice[position]);
                        jenis = addjenis[position];
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void kosong(){
        produk.setText("");
        qty.setText("");
        price.setText("");
        total_price.setText("0");
    }
}