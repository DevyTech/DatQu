package com.example.database.Sistem_Scan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

public class Generate_qrcode extends AppCompatActivity {
    TextView tv_nama_qr,tv_nim_qr,tv_divisi_qr;
    ImageView qrcodeview;
    Button share,download;
    DataHelperScan dataHelperScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

        tv_nama_qr = findViewById(R.id.nama_qr);
        tv_nim_qr = findViewById(R.id.nim_qr);
        tv_divisi_qr = findViewById(R.id.divisi_qr);
        qrcodeview = findViewById(R.id.qrcode_view);
        share = findViewById(R.id.shareqr);
        download = findViewById(R.id.downloadqr);

        dataHelperScan = new DataHelperScan(this);

        Bundle get_data = new Bundle();
        get_data = getIntent().getExtras();
        String set_nik = get_data.getString("NIK");
        String set_nama = get_data.getString("NAMA");
        SQLiteDatabase sqLiteDatabase = dataHelperScan.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM karyawan WHERE NIK='"+set_nik+"'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            cursor.moveToPosition(0);
            tv_nama_qr.setText(cursor.getString(2));
            tv_nim_qr.setText(cursor.getString(1));
            tv_divisi_qr.setText(cursor.getString(3));
        }

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(set_nik, BarcodeFormat.QR_CODE,800,800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcodeview.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this, "Gagal Membuat QR Code : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrcodeview.setDrawingCacheEnabled(true);
                Bitmap bitmap = qrcodeview.getDrawingCache();
                File root_img = Environment.getExternalStorageDirectory();
                File cachepath = new File(root_img.getAbsolutePath()+"/DCIM/Camera/"+set_nama+".jpg");
                try {
                    cachepath.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(cachepath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.close();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error 1 : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Intent intent_share_qr = new Intent(Intent.ACTION_SEND);
                intent_share_qr.setType("image/*");
                Uri uri_share_qr = FileProvider.getUriForFile(getApplicationContext(),"com.rfid.application.Database.provider",cachepath);
                intent_share_qr.putExtra(Intent.EXTRA_STREAM, uri_share_qr);
                try {
                    startActivity(intent_share_qr);
                }catch (Exception er){
                    Toast.makeText(getApplicationContext(), "Error 2 : "+er.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Generate_qrcode.this);
                builder.setTitle("Perhatian");
                builder.setMessage("Save QR Code?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        qrcodeview.setDrawingCacheEnabled(true);
                        Bitmap bitmap = qrcodeview.getDrawingCache();
                        File root_img = Environment.getExternalStorageDirectory();
                        File cachepath = new File(root_img.getAbsolutePath()+"/Download/"+set_nama+".jpg");
                        try {
                            cachepath.createNewFile();
                            FileOutputStream outputStream = new FileOutputStream(cachepath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                            outputStream.close();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error 1 : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Intent intent_share_qr = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri_share_qr = FileProvider.getUriForFile(getApplicationContext(),"com.rfid.application.Database.provider",cachepath);
                        intent_share_qr.setDataAndType(uri_share_qr,"image/*");
                        try {
                            sendBroadcast(intent_share_qr);
                            Toast.makeText(Generate_qrcode.this, "Berhasil Simpan", Toast.LENGTH_SHORT).show();
                        }catch (Exception er){
                            Toast.makeText(getApplicationContext(), "Error 2 : "+er.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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
        });
    }
}