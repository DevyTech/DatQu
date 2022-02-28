package com.example.database.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import android.widget.TextView;

import com.example.database.R;
import com.example.database.mSharePreferences;

public class ListPenjualanAdapter extends ArrayAdapter {
    private String[] Arrayid;
    private String[] Arraytanggal;
    private String[] Arrayproduk;
    private String[] Arrayjenis;
    private String[] Arrayqty;
    private String[] Arrayprice;
    private Context context;
    mSharePreferences mSharePreferences;

    public ListPenjualanAdapter(@NonNull Context context, String[] Arraytanggal, String[] Arrayid, String[] Arrayproduk, String[] Arrayjenis, String[] Arrayqty, String[] Arrayprice){
        super(context, R.layout.custom_listview_lihat_data_penjualan, Arrayid);
        this.Arrayid = Arrayid;
        this.Arraytanggal = Arraytanggal;
        this.Arrayproduk = Arrayproduk;
        this.Arrayjenis = Arrayjenis;
        this.Arrayqty = Arrayqty;
        this.Arrayprice = Arrayprice;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.custom_listview_lihat_data_penjualan, null, true);
        //tv_title
        TextView tgl = view.findViewById(R.id.tv_tgl);
        TextView prdk = view.findViewById(R.id.tv_produk);
        TextView jns = view.findViewById(R.id.tv_jenis);
        TextView qty1 = view.findViewById(R.id.tv_qty);
        TextView prc = view.findViewById(R.id.tv_price);

        //tv_show
        TextView tanggal = view.findViewById(R.id.tv_data_show_tgl);
        TextView produk = view.findViewById(R.id.tv_data_show_produk);
        TextView jenis = view.findViewById(R.id.tv_data_show_jenis);
        TextView qty = view.findViewById(R.id.tv_data_show_Qty);
        TextView price = view.findViewById(R.id.tv_data_show_Price);

        mSharePreferences = new mSharePreferences(context);

        if (mSharePreferences.loadNightMode() || AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            tgl.setTextColor(Color.parseColor("#FFFFFF"));
            prdk.setTextColor(Color.parseColor("#FFFFFF"));
            jns.setTextColor(Color.parseColor("#FFFFFF"));
            qty1.setTextColor(Color.parseColor("#FFFFFF"));
            prc.setTextColor(Color.parseColor("#FFFFFF"));

            tanggal.setTextColor(Color.parseColor("#FFFFFF"));
            produk.setTextColor(Color.parseColor("#FFFFFF"));
            jenis.setTextColor(Color.parseColor("#FFFFFF"));
            qty.setTextColor(Color.parseColor("#FFFFFF"));
            price.setTextColor(Color.parseColor("#FFFFFF"));
        }

        tanggal.setText(Arraytanggal[position]);
        produk.setText(Arrayproduk[position]);
        jenis.setText(Arrayjenis[position]);
        qty.setText(Arrayqty[position]);
        price.setText(Arrayprice[position]);

        return view;
    }
}
