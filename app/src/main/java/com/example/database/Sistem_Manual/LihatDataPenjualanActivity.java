package com.example.database.Sistem_Manual;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.database.Adapter.ListPenjualanAdapter;
import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import au.com.bytecode.opencsv.CSVWriter;

public class LihatDataPenjualanActivity extends AppCompatActivity {
    AppCompatImageButton img_back;
    ListView listView;
    String[] arrid,arrtgl,arrproduk,arrjenis,arrqty,arrprice;
    DataHelperManual dataHelperManual;
    ListPenjualanAdapter listPenjualanAdapter;

    FloatingActionButton export;

    AppCompatTextView tv_total;

    protected Cursor cursor;
    private final String KEY_DATA_ID = "anonymous7889127";
    private final String KEY_DATA_PRODUK = "anonymous09123810";
    private final String KEY_DATA_TGL = "anonymous918230";

    public static LihatDataPenjualanActivity lihatDataPenjualanActivity;

    String filename;
    File file_path;
    String get_bundle_extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data_penjualan);

        dataHelperManual = new DataHelperManual(this);
        lihatDataPenjualanActivity = this;
        export = findViewById(R.id.fab_export_penjualan);
        listView = findViewById(R.id.lv_penjualan);

        tv_total = findViewById(R.id.tv_total_penjualan);

        img_back = findViewById(R.id.img_back_2);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle get_bundle = getIntent().getExtras();
        if (get_bundle!=null){
            get_bundle_extras = get_bundle.getString("TGL");
            tampil_data_penjualan(get_bundle_extras);
            total_penjualan(get_bundle_extras);
        }

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/DataQu_Rekap_Pencatatan/";
        file_path = new File(directory_path);
        if (!file_path.exists()){
            file_path.mkdirs();
        }

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String formatdate = dateFormat.format(calendar.getTime());
        filename = "Rekap Data Penjualan("+formatdate+")";

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LihatDataPenjualanActivity.this);
                builder.setTitle("Perhatian");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setMessage("Rekap Data Penjualan?");
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File create_file = new File(file_path,"Rekap Data Penjualan("+get_bundle_extras+").csv");
                        try {
                            CSVWriter csvWriter = new CSVWriter(new FileWriter(create_file));
                            SQLiteDatabase sqLiteDatabase_exp = dataHelperManual.getReadableDatabase();
                            Cursor cursor_cvs = sqLiteDatabase_exp.rawQuery("SELECT * FROM penjualan WHERE TANGGAL='"+get_bundle_extras+"'", null);
                            csvWriter.writeNext(cursor_cvs.getColumnNames());
                            while (cursor_cvs.moveToNext()){
                                String[] arrcsv = {cursor_cvs.getString(0),cursor_cvs.getString(1),
                                cursor_cvs.getString(2),cursor_cvs.getString(3),
                                cursor_cvs.getString(4),cursor_cvs.getString(5)};
                                csvWriter.writeNext(arrcsv);

                            }
                            csvWriter.close();
                            cursor_cvs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String infile = Environment.getExternalStorageDirectory().getPath()+"/DataQu_Rekap_Pencatatan/Rekap Data Penjualan("+get_bundle_extras+").csv";
                        String thisline;
                        int count = 0;

                        ArrayList arlist = null;
                        ArrayList alist = null;
                        try {
                            FileInputStream fis = new FileInputStream(infile);
                            DataInputStream dis = new DataInputStream(fis);
                            int in = 0;
                            arlist = new ArrayList();
                            while ((thisline = dis.readLine())!=null){
                                alist = new ArrayList();
                                String strarr[] = thisline.split(",");
                                for (int j=0;j<strarr.length;j++){
                                    alist.add(strarr[j]);
                                }
                                arlist.add(alist);
                                in++;
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        try {
                            HSSFWorkbook hwb = new HSSFWorkbook();
                            HSSFSheet hss = hwb.createSheet("Rekap Data Penjualan");
                            for (int k=0;k<arlist.size();k++){
                                ArrayList arrdata = (ArrayList) arlist.get(k);
                                HSSFRow hsr = hss.createRow((short)0+k);
                                for (int p=0;p<arrdata.size();p++){
                                    HSSFCell hsc = hsr.createCell((short)p);
                                    String data = arrdata.get(p).toString();
                                    if (data.startsWith("=")){
                                        hsc.setCellType(Cell.CELL_TYPE_STRING);
                                        data=data.replaceAll("\"","");
                                        data=data.replaceAll("=","");
                                        hsc.setCellValue(data);
                                    }else if (data.startsWith("\"")){
                                        data=data.replaceAll("\"","");
                                        hsc.setCellType(Cell.CELL_TYPE_STRING);
                                        hsc.setCellValue(data);
                                    }else{
                                        data=data.replaceAll("\"","");
                                        hsc.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        hsc.setCellValue(data);
                                    }
                                }
                            }
                            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/DataQu_Rekap_Pencatatan/Rekap Data Penjualan("+get_bundle_extras+").xls");
                            hwb.write(fos);
                            fos.close();
                            File del_csv = new File(infile);
                            Boolean del = del_csv.delete();
                            if (del){
                                Toast.makeText(getApplicationContext(), "Rekap Data Berhasil", Toast.LENGTH_SHORT).show();
                                DataExportManualFragment.dataExportManualFragment.tampil_export_manual();
                                finish();
                            }
                        }catch (Exception e2){
                            Toast.makeText(getApplicationContext(), "Error 2 : "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.create().show();
            }
        });



    }

    public void tampil_data_penjualan(String set_tgl){
        Cursor cursor = dataHelperManual.bacadata_penjualan(set_tgl);
        if (cursor.getCount()==0){
            finish();
        }else {
            arrid = new String[cursor.getCount()];
            arrtgl = new String[cursor.getCount()];
            arrproduk = new String[cursor.getCount()];
            arrjenis = new String[cursor.getCount()];
            arrqty = new String[cursor.getCount()];
            arrprice = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int i = 0;i < cursor.getCount(); i++){
                cursor.moveToPosition(i);
                arrid[i] = cursor.getString(0);
                arrtgl[i] = ": "+cursor.getString(1);
                arrproduk[i] = ": "+cursor.getString(2);
                arrjenis[i] = ": "+cursor.getString(3);
                arrqty[i] = ": "+cursor.getString(4);
                arrprice[i] = ": "+cursor.getString(5);
            }
            listPenjualanAdapter = new ListPenjualanAdapter(getApplicationContext(),arrtgl, arrid,arrproduk,arrjenis,arrqty,arrprice);
            listView.setAdapter(listPenjualanAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    BottomModalLihatFragment bottomModalLihatFragment = new BottomModalLihatFragment();
                    bottomModalLihatFragment.show(getSupportFragmentManager(),"bottomsheet");
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_DATA_ID, arrid[position]);
                    bundle.putString(KEY_DATA_PRODUK, arrproduk[position]);
                    bundle.putString(KEY_DATA_TGL, arrtgl[position]);
                    bottomModalLihatFragment.setArguments(bundle);
                }
            });
            listPenjualanAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("Range")
    private void total_penjualan(String set_tgl_penjualan){
        Cursor cursor = dataHelperManual.hitung_total_penjualan(set_tgl_penjualan);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            for (int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                tv_total.setText(cursor.getString(cursor.getColumnIndex("total_penjualan")));
            }
        }
    }
}