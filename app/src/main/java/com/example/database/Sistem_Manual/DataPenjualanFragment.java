package com.example.database.Sistem_Manual;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.database.Adapter.ListTanggalPenjualanAdapter;
import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;

import java.util.ArrayList;

public class DataPenjualanFragment extends Fragment {

    private RecyclerView rvdatapenjualan;
    DataHelperManual dataHelperManual;
    ArrayList<String> tanggal;
    ListTanggalPenjualanAdapter listTanggalPenjualanAdapter;
    AppCompatImageView imageView;
    AppCompatTextView textView;
    SearchView cari_data_penjual;
    CardView cardView;

    public static DataPenjualanFragment dataPenjualanFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data_penjualan, container, false);

        rvdatapenjualan = v.findViewById(R.id.rv_data_penjualan);
        dataHelperManual = new DataHelperManual(getContext());
        dataPenjualanFragment = this;
        cardView = v.findViewById(R.id.cv_search_data_penjualan);
        cari_data_penjual = v.findViewById(R.id.cari_penjualan);

        imageView = v.findViewById(R.id.img_penjualan_null);
        textView = v.findViewById(R.id.tv_penjualan_null);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cari_data_penjual.onActionViewExpanded();
            }
        });

        cari_data_penjual.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    Cursor cursor = dataHelperManual.cari_penjualan(query);
                    if (cursor.getCount() == 0){
                        Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        tanggal = new ArrayList<>();
                        while (cursor.moveToNext()){
                            tanggal.add(cursor.getString(0));
                        }
                        listTanggalPenjualanAdapter = new ListTanggalPenjualanAdapter(getContext(), tanggal);
                        rvdatapenjualan.setAdapter(listTanggalPenjualanAdapter);
                        rvdatapenjualan.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    Cursor cursor = dataHelperManual.cari_penjualan(newText);
                    if (cursor.getCount() == 0){
                        Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        tanggal = new ArrayList<>();
                        while (cursor.moveToNext()){
                            tanggal.add(cursor.getString(0));
                        }
                        listTanggalPenjualanAdapter = new ListTanggalPenjualanAdapter(getContext(), tanggal);
                        rvdatapenjualan.setAdapter(listTanggalPenjualanAdapter);
                        rvdatapenjualan.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
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

    @Override
    public void onResume() {
        super.onResume();
        tanggal = new ArrayList<>();

        tampildata();

        listTanggalPenjualanAdapter = new ListTanggalPenjualanAdapter(getContext(), tanggal);
        rvdatapenjualan.setAdapter(listTanggalPenjualanAdapter);
        rvdatapenjualan.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void tampildata(){
        Cursor cursor = dataHelperManual.bacadata_penjualan_tanggal();
        if (cursor.getCount()==0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            while (cursor.moveToNext()){
                tanggal.add(cursor.getString(0));
            }
        }
    }
}