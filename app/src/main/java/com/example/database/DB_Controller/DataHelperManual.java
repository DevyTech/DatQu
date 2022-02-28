package com.example.database.DB_Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelperManual extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DBMANUAL.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "penjualan";
    private static final String TABLE_ITEM = "menuitem";

    public DataHelperManual(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_penjualan = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID_PENJUALAN INTEGER PRIMARY KEY AUTOINCREMENT, TANGGAL TEXT, PRODUK TEXT, JENIS TEXT, QTY INTEGER, TOTAL_PRICE TEXT)";
        sqLiteDatabase.execSQL(sql_penjualan);
        String sql_item = "CREATE TABLE IF NOT EXISTS "+TABLE_ITEM+"(ID_ITEM INTEGER PRIMARY KEY AUTOINCREMENT, PRODUK TEXT UNIQUE, JENIS TEXT, PRICE INTEGER)";
        sqLiteDatabase.execSQL(sql_item);
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

    //QUERY DATA PENJUALAN
    public Cursor bacadata_penjualan_tanggal(){
        String query = "SELECT DISTINCT TANGGAL FROM "+TABLE_NAME+" ORDER BY TANGGAL";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor bacadata_penjualan(String get_tgl){
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE TANGGAL = '"+get_tgl+"'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor cari_penjualan(String keyword){
        String query = "SELECT DISTINCT TANGGAL FROM "+TABLE_NAME+" WHERE TANGGAL LIKE '%"+keyword+"%'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public void delete_penjualan(String get_del){
        this.getWritableDatabase().delete("penjualan","ID_PENJUALAN='"+get_del+"'",null);
    }

    public Cursor setbarchartdata(){
        String query = "SELECT TANGGAL, COUNT(TANGGAL) AS total FROM penjualan GROUP BY TANGGAL";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor hitung_total_penjualan(String get_tgl){
        String query = "SELECT TANGGAL,SUM(TOTAL_PRICE) AS total_penjualan FROM penjualan WHERE TANGGAL='"+get_tgl+"' GROUP BY TANGGAL";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }


    //QUERY data produk
    public Cursor baca_produk(){
        String query = "SELECT * FROM menuitem";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor cari_produk(String keyword){
        String query = "SELECT * FROM menuitem WHERE PRODUK LIKE '%"+keyword+"%'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase!=null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    public void delete_produk(String id_produk){
        this.getWritableDatabase().delete("menuitem","ID_ITEM='"+id_produk+"'",null);
    }
}
