package com.example.database.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.database.R;

public class ListPegawaiAdapter extends ArrayAdapter {
    private String[] Arraynama;
    private String[] Arraynik;
    private String[] Arraydevisi;
    private Context context;

    public ListPegawaiAdapter(@NonNull Context context, String[] arraynama, String[] arraynik, String[] arraydevisi){
        super(context, R.layout.custom_listview_pegawai, arraynama);
        this.Arraynama = arraynama;
        this.Arraynik = arraynik;
        this.Arraydevisi = arraydevisi;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.custom_listview_pegawai,null,true);

        TextView nama = view.findViewById(R.id.tv_nama_pegawai);
        TextView nik = view.findViewById(R.id.tv_nik_pegawai);
        TextView devisi = view.findViewById(R.id.tv_devisi_pegawai);

        nama.setText(Arraynama[position]);
        nik.setText(Arraynik[position]);
        devisi.setText(Arraydevisi[position]);

        return view;
    }
}
