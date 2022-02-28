package com.example.database.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.database.R;

public class ListScanAdapter extends ArrayAdapter {
    private String[] arrnik,arrnama,arrdevisi,arrjam;
    private Context context;

    public ListScanAdapter(@NonNull Context context, String[] arrnik_scan, String[] arrnama_scan, String[] arrdevisi_scan, String[] arrjam_scan){
        super(context, R.layout.custom_listview_scan,arrnik_scan);
        this.arrnik = arrnik_scan;
        this.arrnama = arrnama_scan;
        this.arrdevisi = arrdevisi_scan;
        this.arrjam = arrjam_scan;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.custom_listview_scan,null,true);

        TextView nik_scan = view.findViewById(R.id.nik_scan);
        TextView nama_scan = view.findViewById(R.id.nama_scan);
        TextView devisi_scan = view.findViewById(R.id.devisi_scan);
        TextView jam_scan = view.findViewById(R.id.jam_scan);

        nik_scan.setText(arrnik[position]);
        nama_scan.setText(arrnama[position]);
        devisi_scan.setText(arrdevisi[position]);
        jam_scan.setText(arrjam[position]);

        return view;
    }
}
