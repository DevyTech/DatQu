package com.example.database.Sistem_Scan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;

import java.io.File;
import java.io.FilenameFilter;

public class HomeScanFragment extends Fragment {

    TextView pegawai,scan,export;
    DataHelperScan dataHelperScan;
    Cursor cursor_pegawai,cursor_scan;
    int hitung=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_scan, container, false);

        pegawai = v.findViewById(R.id.tv_head_home_pegawai);
        scan = v.findViewById(R.id.tv_head_home_scan);
        export = v.findViewById(R.id.tv_head_home_export);

        dataHelperScan = new DataHelperScan(getContext());

        hitung_data();
        hitung_export();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Scan.mainActivityScan.default_menuselected();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }

    public void hitung_data(){
        SQLiteDatabase database = dataHelperScan.getReadableDatabase();
        //hitung_data_pegawai
        cursor_pegawai = database.rawQuery("SELECT * FROM karyawan",null);
        int data_pegawai = cursor_pegawai.getCount();
        pegawai.setText(String.valueOf(data_pegawai));
        //hitung_data_scan
        cursor_scan = database.rawQuery("SELECT * FROM scanresult", null);
        int data_scan = cursor_scan.getCount();
        scan.setText(String.valueOf(data_scan));
    }

    public void hitung_export(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Scan/";
        File directory = new File(directory_path);
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                //filename.endsWith(".xls")||
                String keyword = "Scan";
                int position = filename.indexOf(keyword);
                if (position >= 0 && filename.endsWith(".xls")){
                    hitung++;
                    export.setText(String.valueOf(hitung));
                    return true;
                }
                return false;
            }
        });
    }
}