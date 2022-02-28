package com.example.database.Sistem_Scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.Adapter.ListPegawaiAdapter;
import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.android.material.snackbar.Snackbar;

public class DataPegawaiFragment extends Fragment {
    DataHelperScan dataHelperScan;
    ListView listView;
    String[] arrnama,arrnik,arrdevisi;
    AppCompatImageView imageView;
    TextView textView;
    ListPegawaiAdapter listPegawaiAdapter;
    SearchView cari;
    CardView cardView;

    public static DataPegawaiFragment LF;
    private String KEY_FRG_UPDT_NIK = "nik_set";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_data_pegawai, container, false);

        imageView = v.findViewById(R.id.img_pegawai_null);
        textView = v.findViewById(R.id.tv_pegawai_null);

        dataHelperScan = new DataHelperScan(getContext());
        listView = v.findViewById(R.id.lv_pegawai);
        cari = v.findViewById(R.id.cari_pegawai);
        cardView = v.findViewById(R.id.cv_search_data_pegawai);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cari.onActionViewExpanded();
            }
        });

        LF = this;
        tampil_data();
        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    Cursor cursor = dataHelperScan.cari_karyawan(query);
                    if (cursor.getCount() == 0){
                        //do something if database is null/empty
                        Toast.makeText(getContext(), "Pegawai Tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        arrnama = new String[cursor.getCount()];
                        arrnik = new String[cursor.getCount()];
                        arrdevisi = new String[cursor.getCount()];
                        cursor.moveToFirst();
                        for (int cc = 0; cc < cursor.getCount(); cc++){
                            cursor.moveToPosition(cc);
                            arrnama[cc] = cursor.getString(2).toString();
                            arrnik[cc] = cursor.getString(1).toString();
                            arrdevisi[cc] = cursor.getString(3).toString();
                        }
                        listPegawaiAdapter = new ListPegawaiAdapter(getContext(), arrnama, arrnik, arrdevisi);
                        listView.setAdapter(listPegawaiAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                final String selection = arrnik[position];
                                final String selection_nama = arrnama[position];
                                final CharSequence[] dialogitem = {"Buat QR Code","Ubah Data","Hapus Data"};
                                builder.setTitle("Pilihan");
                                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int itemposition) {
                                        switch (itemposition){
                                            case 0 :
                                                Intent intent = new Intent(getContext(), Generate_qrcode.class);
                                                intent.putExtra("NIK",selection);
                                                intent.putExtra("NAMA",selection_nama);
                                                startActivity(intent);
                                                break;
                                            case 1 :
                                                UpdateFragment updateFragment = new UpdateFragment();
                                                assert getFragmentManager() != null;
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                Bundle bundle = new Bundle();
                                                bundle.putString(KEY_FRG_UPDT_NIK, selection);
                                                updateFragment.setArguments(bundle);
                                                transaction.replace(R.id.fl, updateFragment).commit();
                                                break;
                                            case 2 :
                                                AlertDialog.Builder tanya = new AlertDialog.Builder(getContext());
                                                tanya.setTitle("Perhatian");
                                                tanya.setIcon(R.drawable.ic_baseline_warning_24);
                                                tanya.setMessage("Hapus Data?");
                                                tanya.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dataHelperScan.delete_karyawan(selection);
                                                        refresh_del(view);
                                                    }
                                                });
                                                tanya.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                                tanya.create().show();
                                                break;
                                        }
                                    }
                                });
                                builder.create().show();
                            }
                        });
                        listPegawaiAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    Cursor cursor = dataHelperScan.cari_karyawan(newText);
                    if (cursor.getCount() == 0){
                        //do something if database is null/empty
                        Toast.makeText(getContext(), "Pegawai Tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }else{
                        arrnama = new String[cursor.getCount()];
                        arrnik = new String[cursor.getCount()];
                        arrdevisi = new String[cursor.getCount()];
                        cursor.moveToFirst();
                        for (int cc = 0; cc < cursor.getCount(); cc++){
                            cursor.moveToPosition(cc);
                            arrnama[cc] = cursor.getString(2).toString();
                            arrnik[cc] = cursor.getString(1).toString();
                            arrdevisi[cc] = cursor.getString(3).toString();
                        }
                        listPegawaiAdapter = new ListPegawaiAdapter(getContext(), arrnama, arrnik, arrdevisi);
                        listView.setAdapter(listPegawaiAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                final String selection = arrnik[position];
                                final String selection_nama = arrnama[position];
                                final CharSequence[] dialogitem = {"Buat QR Code","Ubah Data","Hapus Data"};
                                builder.setTitle("Pilihan");
                                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int itemposition) {
                                        switch (itemposition){
                                            case 0 :
                                                Intent intent = new Intent(getContext(), Generate_qrcode.class);
                                                intent.putExtra("NIK",selection);
                                                intent.putExtra("NAMA",selection_nama);
                                                startActivity(intent);
                                                break;
                                            case 1 :
                                                UpdateFragment updateFragment = new UpdateFragment();
                                                assert getFragmentManager() != null;
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                Bundle bundle = new Bundle();
                                                bundle.putString(KEY_FRG_UPDT_NIK, selection);
                                                updateFragment.setArguments(bundle);
                                                transaction.replace(R.id.fl, updateFragment).commit();
                                                break;
                                            case 2 :
                                                AlertDialog.Builder tanya = new AlertDialog.Builder(getContext());
                                                tanya.setTitle("Perhatian");
                                                tanya.setIcon(R.drawable.ic_baseline_warning_24);
                                                tanya.setMessage("Hapus Data?");
                                                tanya.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dataHelperScan.delete_karyawan(selection);
                                                        refresh_del(view);
                                                    }
                                                });
                                                tanya.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                                tanya.create().show();
                                                break;
                                        }
                                    }
                                });
                                builder.create().show();
                            }
                        });
                        listPegawaiAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return v;
    }

    public void refresh_del(View view){
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.fl, new DataGroupScanFragment()).commit();
        Snackbar.make(view,"Data Terhapus",Snackbar.LENGTH_LONG).show();
    }
    public void tampil_data(){
        Cursor cursor = dataHelperScan.bacadata_karyawan();
        if (cursor.getCount() == 0){
            //do something if database is null/empty
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else{
            arrnama = new String[cursor.getCount()];
            arrnik = new String[cursor.getCount()];
            arrdevisi = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int cc = 0; cc < cursor.getCount(); cc++){
                cursor.moveToPosition(cc);
                arrnama[cc] = cursor.getString(2).toString();
                arrnik[cc] = cursor.getString(1).toString();
                arrdevisi[cc] = cursor.getString(3).toString();
            }
            listPegawaiAdapter = new ListPegawaiAdapter(getContext(), arrnama, arrnik, arrdevisi);
            listView.setAdapter(listPegawaiAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final String selection = arrnik[position];
                    final String selection_nama = arrnama[position];
                    final CharSequence[] dialogitem = {"Buat QR Code","Ubah Data","Hapus Data"};
                    builder.setTitle("Pilihan");
                    builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int itemposition) {
                            switch (itemposition){
                                case 0 :
                                    Intent intent = new Intent(getContext(), Generate_qrcode.class);
                                    intent.putExtra("NIK",selection);
                                    intent.putExtra("NAMA",selection_nama);
                                    startActivity(intent);
                                    break;
                                case 1 :
                                    UpdateFragment updateFragment = new UpdateFragment();
                                    assert getFragmentManager() != null;
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(KEY_FRG_UPDT_NIK, selection);
                                    updateFragment.setArguments(bundle);
                                    transaction.replace(R.id.fl, updateFragment).commit();
                                    break;
                                case 2 :
                                    AlertDialog.Builder tanya = new AlertDialog.Builder(getContext());
                                    tanya.setTitle("Perhatian");
                                    tanya.setIcon(R.drawable.ic_baseline_warning_24);
                                    tanya.setMessage("Hapus Data?");
                                    tanya.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dataHelperScan.delete_karyawan(selection);
                                            refresh_del(view);
                                        }
                                    });
                                    tanya.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    tanya.create().show();
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                }
            });
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                private int mLastVisibleItem;
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (mLastVisibleItem<firstVisibleItem){
                        DataGroupScanFragment.dataGroupScanFragment.hide_fab_import();
                    }
                    if (mLastVisibleItem>firstVisibleItem){
                        DataGroupScanFragment.dataGroupScanFragment.show_fab_import();
                    }
                    mLastVisibleItem=firstVisibleItem;
                }
            });
            listPegawaiAdapter.notifyDataSetChanged();
        }
    }
}