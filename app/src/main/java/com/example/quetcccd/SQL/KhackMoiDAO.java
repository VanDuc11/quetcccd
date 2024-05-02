package com.example.quetcccd.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quetcccd.model.KhackMoi;

import java.util.ArrayList;
import java.util.List;

public class KhackMoiDAO {
    SQLiteDatabase db;
    public KhackMoiDAO(Context context){
        SQL sql = new SQL(context);
        db = sql.getWritableDatabase();
    }
    public int insert(KhackMoi k){
        ContentValues values = new ContentValues();
        values.put("ThoiGian",k.getThoiGian());
        values.put("CCCD",k.getCCCD());
        values.put("Ten",k.getTen());
        values.put("GioiTinh",k.getGioiTinh());

        long kq = db.insert("KhachMoi",null,values);
        if (kq <= 0){
            return -1;
        }
        return 1;
    }
    @SuppressLint("Range")
    public List<KhackMoi> getData(String sql, String... selectArgs){
        List<KhackMoi> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,selectArgs);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String thoiGian = cursor.getString(cursor.getColumnIndex("ThoiGian"));
            String cccd = cursor.getString(cursor.getColumnIndex("CCCD"));
            String ten = cursor.getString(cursor.getColumnIndex("Ten"));
            String gioiTinh = cursor.getString(cursor.getColumnIndex("GioiTinh"));
            KhackMoi khachMoi = new KhackMoi(id, cccd, ten, gioiTinh, thoiGian);
            list.add(khachMoi);
        }
        return list;
    }
    public List<KhackMoi> getAll(){
        String sql = "SELECT * FROM KhachMoi";
        return getData(sql);
    }
    public List<KhackMoi> getCCCD(String cccd){
        String sql = "SELECT * FROM KhachMoi WHERE CCCD=?";
        return getData(sql,cccd);
    }
    public KhackMoi getCCCDAndTime(String cccd,String start,String end){
        String sql = "SELECT * FROM KhachMoi WHERE CCCD=? AND  ThoiGian BETWEEN ? AND ?";
        List<KhackMoi> list= getData(sql,new String[]{cccd,start,end});
        return list.isEmpty() ? null : list.get(0);
    }
}
