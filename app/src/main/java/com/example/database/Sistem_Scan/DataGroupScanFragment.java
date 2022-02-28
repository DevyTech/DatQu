package com.example.database.Sistem_Scan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ajts.androidmads.library.ExcelToSQLite;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataGroupScanFragment extends Fragment {
    //design
    LinearLayout ll_title;

    //content
    FloatingActionButton fab_expo,fab_impo;
    ViewPager viewPager;
    TabLayout tabLayout;
    String filename;
    public static DataGroupScanFragment dataGroupScanFragment;
    DataHelperScan dataHelperScan;
    File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data_group_scan, container, false);
        tabLayout = v.findViewById(R.id.tablayout);
        viewPager = v.findViewById(R.id.view_pager);

        dataGroupScanFragment = this;
        dataHelperScan = new DataHelperScan(getContext());
        fab_expo = v.findViewById(R.id.fab_export);
        fab_impo = v.findViewById(R.id.fab_import);

        //setting data export
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Scan/";
        file = new File(directory_path);
        if (!file.exists()){
            file.mkdirs();
        }

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        PagerScanAdapter pagerScanAdapter = new PagerScanAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerScanAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());
                if (tabLayout.getTabAt(tab.getPosition())==tabLayout.getTabAt(0)){
                    fab_expo.setVisibility(v.INVISIBLE);
                    fab_impo.setVisibility(v.VISIBLE);
                }else if (tabLayout.getTabAt(tab.getPosition())==tabLayout.getTabAt(2)){
                    fab_expo.setVisibility(v.INVISIBLE);
                    fab_impo.setVisibility(v.INVISIBLE);
                }else{
                    fab_expo.setVisibility(v.VISIBLE);
                    fab_impo.setVisibility(v.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().clearColorFilter();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Scan.mainActivityScan.default_menuselected();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Fungsi Event klik Import Data
        fab_impo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PICKFILE_RESULT_CODE = 1;
                Intent buka_file = new Intent(Intent.ACTION_GET_CONTENT);
                buka_file.setType("application/vnd.ms-excel");
                startActivityForResult(buka_file, PICKFILE_RESULT_CODE);
            }
        });

        //set nama file export
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String formatdate = dateFormat.format(calendar.getTime());
        filename = "Rekap Data Scan("+formatdate+").xls";

        //Fungsi Event Klik Export
        fab_expo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase cek_data_scan = dataHelperScan.getReadableDatabase();
                Cursor row = cek_data_scan.rawQuery("SELECT * FROM scanresult",null);
                if (row.getCount()<=0){
                    Toast.makeText(getContext(), "Tidak Ada Data Scan", Toast.LENGTH_SHORT).show();
                }else {
                    //Buka file export
                    File open_file = new File(Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Scan/"+filename);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Perhatian");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setMessage("Rekap Data Scan?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Export File ke Excel
                            SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(getContext(), DataHelperScan.DATABASE_NAME, directory_path);
                            sqLiteToExcel.exportSingleTable(DataHelperScan.TABLE_1, filename, new SQLiteToExcel.ExportListener() {
                                @Override
                                public void onStart() {

                                }
                                @Override
                                public void onCompleted(String filePath) {
                                    DataExportScanFragment.dataExportScanFragment.tampil_data_export();
                                    Snackbar snackbar = Snackbar.make(view,"Berhasil Export", Snackbar.LENGTH_LONG).setAction("Buka", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                Uri uri = FileProvider.getUriForFile(getContext(),"com.rfid.application.Database.provider", open_file);
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                intent.setDataAndType(uri,"application/vnd.ms-excel");
                                            } else{
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.setDataAndType(Uri.fromFile(open_file),"application/vnd.ms-excel");
                                            }
                                            try {
                                                startActivity(intent);
                                            }catch (Exception er){
                                                Toast.makeText(getContext(),"Error : "+er.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    snackbar.show();
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0){
            return;
        }else {
            assert data != null;
            //get filename & filepath
            Uri uri_file = data.getData();
            String get_file_name = uri_file.getLastPathSegment();
            String[] keyword = get_file_name.split(":");
            String get_file_path = Environment.getExternalStorageDirectory().getPath() + "/" + keyword[1];
            //set file to import
            File cek_file = new File(get_file_path);
            if (!cek_file.exists()){
                Toast.makeText(getContext(), "File Tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    InputStream is = new FileInputStream(cek_file);
                    HSSFWorkbook workbook = new HSSFWorkbook(is);
                    Sheet sheet = workbook.getSheetAt(0);
                    HSSFRow row = (HSSFRow) sheet.getRow(0);
                    short colmax = row.getLastCellNum();
                    if (workbook.getSheetName(0).equals("karyawan") && colmax == 3){
                        ExcelToSQLite excelToSQLite = new ExcelToSQLite(getContext(), DataHelperScan.DATABASE_NAME, false);
                        excelToSQLite.importFromFile(get_file_path, new ExcelToSQLite.ImportListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onCompleted(String dbName) {
                                assert getFragmentManager() != null;
                                getFragmentManager().beginTransaction().replace(R.id.fl, new DataGroupScanFragment()).commit();
                                Toast.makeText(getContext(), "Import Data Berhasil", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Terjadi Kesalahan Saat Membaca File");
                        builder.setIcon(R.drawable.ic_baseline_warning_24);
                        builder.setMessage("Mohon untuk mengubah nama sheet menjadi karyawan");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void show_fab_export(){
        fab_expo.setVisibility(View.VISIBLE);
    }
    public void hide_fab_export(){
        fab_expo.setVisibility(View.INVISIBLE);
    }
    public void show_fab_import(){
        fab_impo.setVisibility(View.VISIBLE);
    }
    public void hide_fab_import(){
        fab_impo.setVisibility(View.INVISIBLE);
    }
}