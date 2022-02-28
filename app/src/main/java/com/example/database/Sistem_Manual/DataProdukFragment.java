package com.example.database.Sistem_Manual;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.database.Adapter.ListProdukAdapter;
import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
public class DataProdukFragment extends Fragment {
    AppCompatTextView textView;
    AppCompatImageView imageView;
    DataHelperManual dataHelperManual;
    ListView listView;
    ListProdukAdapter listProdukAdapter;
    SearchView cari;
    String[] arridproduk,arrproduk,arrjenis,arrprice;

    CardView cardView;

    public static DataProdukFragment dataProdukFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data_produk, container, false);

        dataHelperManual = new DataHelperManual(getContext());
        dataProdukFragment = this;
        listView = v.findViewById(R.id.lv_produk);

        cardView = v.findViewById(R.id.cv_search_data_produk);
        cari = v.findViewById(R.id.cari_produk);

        textView = v.findViewById(R.id.tv_produk_null);
        imageView = v.findViewById(R.id.img_produk_null);

        tampil_data();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cari.onActionViewExpanded();
            }
        });
        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor cursor = dataHelperManual.cari_produk(query);
                if (cursor.getCount()==0){
                    Toast.makeText(getContext(), "Produk Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }else {
                    arridproduk = new String[cursor.getCount()];
                    arrproduk = new String[cursor.getCount()];
                    arrjenis = new String[cursor.getCount()];
                    arrprice = new String[cursor.getCount()];
                    cursor.moveToFirst();
                    for (int i=0;i < cursor.getCount(); i++){
                        cursor.moveToPosition(i);
                        arridproduk[i] = cursor.getString(0);
                        arrproduk[i] = cursor.getString(1);
                        arrjenis[i] = cursor.getString(2);
                        arrprice[i] = cursor.getString(3);
                    }
                    listProdukAdapter = new ListProdukAdapter(getContext(),arridproduk,arrproduk,arrjenis,arrprice);
                    listView.setAdapter(listProdukAdapter);
                    listProdukAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = dataHelperManual.cari_produk(newText);
                if (cursor.getCount()==0){
                    Toast.makeText(getContext(), "Produk Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }else {
                    arridproduk = new String[cursor.getCount()];
                    arrproduk = new String[cursor.getCount()];
                    arrjenis = new String[cursor.getCount()];
                    arrprice = new String[cursor.getCount()];
                    cursor.moveToFirst();
                    for (int i=0;i < cursor.getCount(); i++){
                        cursor.moveToPosition(i);
                        arridproduk[i] = cursor.getString(0);
                        arrproduk[i] = cursor.getString(1);
                        arrjenis[i] = cursor.getString(2);
                        arrprice[i] = cursor.getString(3);
                    }
                    listProdukAdapter = new ListProdukAdapter(getContext(),arridproduk,arrproduk,arrjenis,arrprice);
                    listView.setAdapter(listProdukAdapter);
                    listProdukAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        return v;
    }

    public void tampil_data(){
        Cursor cursor = dataHelperManual.baca_produk();
        if (cursor.getCount()==0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            arridproduk = new String[cursor.getCount()];
            arrproduk = new String[cursor.getCount()];
            arrjenis = new String[cursor.getCount()];
            arrprice = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int i=0;i < cursor.getCount(); i++){
                cursor.moveToPosition(i);
                arridproduk[i] = cursor.getString(0);
                arrproduk[i] = cursor.getString(1);
                arrjenis[i] = cursor.getString(2);
                arrprice[i] = cursor.getString(3);
            }
            listProdukAdapter = new ListProdukAdapter(getContext(), arridproduk,arrproduk,arrjenis,arrprice);
            listView.setAdapter(listProdukAdapter);
            listProdukAdapter.notifyDataSetChanged();
        }
    }
}