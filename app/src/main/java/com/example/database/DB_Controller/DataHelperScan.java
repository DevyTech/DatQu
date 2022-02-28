package com.example.database.DB_Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelperScan extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBSCAN.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_1 = "scanresult";
    private static final String TABLE_2 = "karyawan";

    public DataHelperScan(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_scan = "CREATE TABLE IF NOT EXISTS "+TABLE_1+"(ID_SCAN INTEGER PRIMARY KEY AUTOINCREMENT, NIK_SCAN TEXT UNIQUE, NAMA_SCAN TEXT, DIVISI_SCAN TEXT, JAM_SCAN TEXT, TANGGAL TEXT)";
        String sql_karyawan = "CREATE TABLE IF NOT EXISTS "+TABLE_2+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, NIK TEXT UNIQUE, NAMA TEXT, DIVISI TEXT)";
        sqLiteDatabase.execSQL(sql_scan);
        sqLiteDatabase.execSQL(sql_karyawan);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Schema Change Table
        //Create temporary table
        //Insert temporary table from main table
        //Drop main table
        //Create new main table
        //Insert main table from temporary table
        //Drop temporary table
        onCreate(sqLiteDatabase);
    }
    public Cursor bacadata_scan(){
        String query = "SELECT * FROM scanresult";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }
    public Cursor bacadata_karyawan(){
        String query = "SELECT * FROM karyawan";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor cari_karyawan(String filter_name){
        String query = "SELECT * FROM karyawan WHERE NAMA LIKE '%"+filter_name+"%'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }
    public void delete_karyawan(String NIK_del){
        this.getWritableDatabase().delete("karyawan","NIK='"+NIK_del+"'",null);
    }
}
