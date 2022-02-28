package com.example.database.Sistem_Scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.Adapter.ListExportScanAdapter;
import com.example.database.R;

import java.io.File;
import java.io.FilenameFilter;

public class DataExportScanFragment extends Fragment {
    ImageView imageView;
    TextView textView;

    ListView lv;
    int[] iconitem;
    String[] text;
    public static DataExportScanFragment dataExportScanFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_data_export_scan, container, false);
        lv = v.findViewById(R.id.lv_export);

        imageView = v.findViewById(R.id.img_export_null);
        textView = v.findViewById(R.id.tv_export_null);
        dataExportScanFragment = this;
        tampil_data_export();
        return v;
    }

    public void tampil_data_export(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Scan/";
        File directory = new File(directory_path);
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                //filename.endsWith(".xls")||
                String keyword = "Scan";
                int position = filename.indexOf(keyword);
                if (position >= 0 && filename.endsWith(".xls")){
                    return true;
                }
                return false;
            }
        });
        iconitem = new int[files.length];
        text = new String[files.length];
        for (int i=0; i < files.length; i++){
            iconitem[i] = R.drawable.ic_baseline_chrome_reader_mode_32;
            text[i] = files[i].getName();
        }
        ListExportScanAdapter listExportScanAdapter = new ListExportScanAdapter(getContext(),iconitem,text);
        lv.setAdapter(listExportScanAdapter);
        listExportScanAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final File open_file = files[position];
                final String[] dialogitem = {"Kirim File","Buka File","Hapus File"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int itemposition) {
                        switch (itemposition){
                            case 0:
                                Intent intent_share = new Intent(Intent.ACTION_SEND);
                                intent_share.setType("application/vnd.ms-excel");
                                Uri uri_share = FileProvider.getUriForFile(getContext(),"com.rfid.application.Database.provider", open_file);
                                intent_share.putExtra(Intent.EXTRA_SUBJECT,"Laporan Data Scan");
                                intent_share.putExtra(Intent.EXTRA_TEXT,"Di bagikan dari DataQu");
                                intent_share.putExtra(Intent.EXTRA_STREAM, uri_share);
                                try {
                                    startActivity(intent_share);
                                }catch (Exception er){
                                    Toast.makeText(getContext(),"Error : "+er.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 1:
                                Intent intent_open = new Intent(Intent.ACTION_VIEW);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                    intent_open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    Uri uri_open = FileProvider.getUriForFile(getContext(),"com.rfid.application.Database.provider", open_file);
                                    intent_open.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent_open.setDataAndType(uri_open,"application/vnd.ms-excel");
                                }else{
                                    intent_open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent_open.setDataAndType(Uri.fromFile(open_file),"application/vnd.ms-excel");
                                }
                                try {
                                    startActivity(intent_open);
                                }catch (Exception er){
                                    Intent intent_play_store = new Intent(Intent.ACTION_VIEW);
                                    intent_play_store.setData(Uri.parse("https://play.google.com/store/apps/details?id=cn.wps.moffice_eng"));
                                    try {
                                        startActivity(intent_play_store);
                                    }catch (Exception e1){
                                        Toast.makeText(getContext(), "Error : "+e1.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(getContext(),"Silahkan install aplikasi pembuka office untuk membuka file", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 2:
                                AlertDialog.Builder tanya = new AlertDialog.Builder(getContext());
                                tanya.setTitle("Perhatian");
                                tanya.setIcon(R.drawable.ic_baseline_warning_24);
                                tanya.setMessage("Hapus Rekapan?");
                                tanya.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        boolean hapus_file = open_file.delete();
                                        if (hapus_file){
                                            dataExportScanFragment.tampil_data_export();
                                        }else{
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
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
    }
}