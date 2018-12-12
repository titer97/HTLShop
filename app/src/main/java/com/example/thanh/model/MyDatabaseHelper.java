package com.example.thanh.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ChiTietHoaDonList";
    private static final String TABLE_NAME = "ChiTietHoaDon";
    private static final String ID="id";
    private static final String MASP="masp";
    private static final String MAKH="makh";
    private static final String SOLUONG="soluong";
    private static final String DONGIA="dongia";

    private Context context;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID+ " integer primary key, "+
                MASP + " integer, "+
                MAKH + " integer, " +
                SOLUONG + " integer, "+
                DONGIA + " integer)";
        sqLiteDatabase.execSQL(sqlQuery);
        Toast.makeText(context,"Create successfully",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
        Toast.makeText(context,"Drop successfully", Toast.LENGTH_LONG).show();
    }

    public List<ChiTietHoaDon> layDsChiTietHd(){
        List<ChiTietHoaDon> dsCthd = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setId(cursor.getInt(0));
                cthd.setMasp(cursor.getInt(1));
                cthd.setMakh(cursor.getInt(2));
                cthd.setSoluong(cursor.getInt(3));
                cthd.setDongia(cursor.getInt(4));
                dsCthd.add(cthd);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dsCthd;
    }

    public void themChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=  new ContentValues();
        values.put(MASP,chiTietHoaDon.getMasp());
        values.put(MAKH,chiTietHoaDon.getMakh());
        values.put(SOLUONG,chiTietHoaDon.getSoluong());
        values.put(DONGIA,chiTietHoaDon.getDongia());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public int suaChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MASP,chiTietHoaDon.getMasp());
        values.put(MAKH,chiTietHoaDon.getMakh());
        values.put(SOLUONG,chiTietHoaDon.getSoluong());
        values.put(DONGIA,chiTietHoaDon.getDongia());
        int update = db.update(TABLE_NAME,values,ID + "=?", new String[]{String.valueOf(chiTietHoaDon.getId())});
        db.close();
        return update;
    }

    public void xoaChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ID + "=?", new String[]{String.valueOf(chiTietHoaDon.getId())});
        db.close();
    }

    public void xoaTatCaChiTietHoaDon(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();}

}
