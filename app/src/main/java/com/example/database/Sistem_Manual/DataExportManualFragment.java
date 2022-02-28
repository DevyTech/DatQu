package com.example.database.Sistem_Manual;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.database.Adapter.ListExportManualAdapter;
import com.example.database.R;

import java.io.File;
import java.io.FilenameFilter;

public class DataExportManualFragment extends Fragment {

    ListView lv;
    int[] iconitem;
    String[] text;
    public static DataExportManualFragment dataExportManualFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_export_manual, container, false);
        lv = view.findViewById(R.id.lv_export_manual);

        dataExportManualFragment = this;
        tampil_export_manual();
        return view;
    }

    public void tampil_export_manual(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Pencatatan/";
        File directory = new File(directory_path);
        if (!directory.exists()){
            directory.mkdirs();
        }
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                String keyword = "Penjualan";
                int position = filename.indexOf(keyword);
                if (position >=0 && filename.endsWith(".xls")){
                    return true;
                }
                return false;
            }
        });
        iconitem = new int[files.length];
        text = new String[files.length];
        for (int i=0;i < files.length; i++){
            iconitem[i] = R.drawable.ic_baseline_chrome_reader_mode_32;
            text[i] = files[i].getName();
        }
        ListExportManualAdapter listExportManualAdapter = new ListExportManualAdapter(getContext(),iconitem,text);
        lv.setAdapter(listExportManualAdapter);
        listExportManualAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final  File open_file = files[position];
                //String
                BottomModalExportFragment bottomModalExportFragment = new BottomModalExportFragment();
                assert getFragmentManager() != null;
                bottomModalExportFragment.show(getFragmentManager(),"bottomsheet");
                Bundle bundle = new Bundle();
                bundle.putString("FILE", String.valueOf(open_file));
                bottomModalExportFragment.setArguments(bundle);
            }
        });
    }
}