package com.example.database.Sistem_Scan;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.zxing.Result;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ImageButton flash;
    private boolean isFlashOn;
    private ZXingScannerView zXingScannerView;
    Boolean myflash = false;

    DataHelperScan dataHelperScan;
    protected Cursor cursor_del;
    Calendar calendar;
    String KEY_RESULT = "scan_result";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);

        flash = v.findViewById(R.id.swicth_flash);
        zXingScannerView = v.findViewById(R.id.scan_lay);

        dataHelperScan = new DataHelperScan(getContext());

        cek_data_scan();

        isFlashOn = false;
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFlashOn){
                    zXingScannerView.setFlash(true);
                    isFlashOn = true;
                    toggleButtonImage();
                    myflash = true;
                }else{
                    zXingScannerView.setFlash(false);
                    isFlashOn = false;
                    toggleButtonImage();
                    myflash = false;
                }
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == getTargetRequestCode() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            MainActivity_Scan.mainActivityScan.default_menuselected();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Check Permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, getTargetRequestCode());
        }else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, getTargetRequestCode());
        }else{
            start_scaning();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    private void toggleButtonImage(){
        if (isFlashOn){
            flash.setImageResource(R.drawable.ic_baseline_flash_on_24);
        }else{
            flash.setImageResource(R.drawable.ic_baseline_flash_off_24);
        }
    }

    @Override
    public void handleResult(Result result) {
        if (result.getText() != null){
            String data_scan = result.getText();
            SQLiteDatabase sqLiteDatabase = dataHelperScan.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM karyawan WHERE NIK = '"+data_scan+"'",null);
            if (cursor.getCount()>0){
                SQLiteDatabase db_cek = dataHelperScan.getReadableDatabase();
                Cursor hitung = db_cek.rawQuery("SELECT * FROM scanresult WHERE NIK_SCAN = '"+data_scan+"'",null);
                if (hitung.getCount() <=0 ){
                    ResultFragment resultFragment = new ResultFragment();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_RESULT, data_scan);
                    resultFragment.setArguments(bundle);
                    transaction.replace(R.id.fl, resultFragment).commit();
                }else {
                    hitung.moveToPosition(0);
                    String get_nama = hitung.getString(2);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Sudah Melakukan Scan");
                    builder.setMessage(get_nama);
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            zXingScannerView.setFlash(myflash);
                            start_scaning();
                        }
                    });
                    builder.create().show();
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Kode QR Tidak Terdaftar");
                builder.setMessage(result.getText());
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        zXingScannerView.setFlash(myflash);
                        start_scaning();
                    }
                });
                builder.create().show();
            }
        }
    }

    public void start_scaning(){
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        if (myflash==false){
            zXingScannerView.setFlash(false);
        }else{
            zXingScannerView.setFlash(true);
        }
        zXingScannerView.setAutoFocus(true);
    }

    public void cek_data_scan(){
        calendar = Calendar.getInstance();
        //set_tanggal
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tanggal = new SimpleDateFormat("dd MMM yyyy");
        String formattanggal = tanggal.format(calendar.getTime());

        cursor_del = dataHelperScan.bacadata_scan();
        if (cursor_del.getCount() > 0){
            cursor_del.moveToFirst();
            for (int i=0;i<cursor_del.getCount();i++){
                cursor_del.moveToPosition(i);
                if (!cursor_del.getString(5).equals(formattanggal)){
                    String filename = "Rekap Data Scan("+cursor_del.getString(5)+").xls";
                    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Scan/";
                    File file = new File(directory_path);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    //Export File ke Excel
                    SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(getContext(), DataHelperScan.DATABASE_NAME, directory_path);
                    sqLiteToExcel.exportSingleTable(DataHelperScan.TABLE_1, filename, new SQLiteToExcel.ExportListener() {
                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onCompleted(String filePath) {
                            SQLiteDatabase delete_scan = dataHelperScan.getWritableDatabase();
                            delete_scan.execSQL("DELETE FROM scanresult");
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

}