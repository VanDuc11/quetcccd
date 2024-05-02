package com.example.quetcccd.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quetcccd.model.CheckIn;

import java.util.ArrayList;
import java.util.List;

public class CheckinDAO {
    SQLiteDatabase db;
    public  CheckinDAO(Context context){
        SQL sql = new SQL(context);
        db = sql.getWritableDatabase();
    }
    public int insert(CheckIn c){
        ContentValues v = new ContentValues();
        v.put("CCCD",c.getCCCD());
        v.put("Name",c.getName());
        v.put("ThoiGian",c.getCheckin());
        long kq = db.insert("CheckIn",null,v);
        if(kq <=0){
            return  -1;
        }
        return 1;
    }
    public int update(CheckIn c ){
        ContentValues values = new ContentValues();
        values.put("Checkout",c.getCheckout());
        long kq = db.update("CheckIn", values, "CCCD=?", new String[]{c.getCCCD()});
        if (kq <= 0) {
            return -1;
        }
        return 1;
    }
    @SuppressLint("Range")
    public List<CheckIn> getData(String sql, String... selectArgs){
        List<CheckIn> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectArgs);
        while (c.moveToNext()){
            CheckIn ck = new CheckIn();
            ck.setID(c.getInt(c.getColumnIndex("ID")));
            ck.setCCCD(c.getString(c.getColumnIndex("CCCD")));
            ck.setName(c.getString(c.getColumnIndex("Name")));
            ck.setCheckin(c.getString(c.getColumnIndex("ThoiGian")));
            ck.setCheckout(c.getString(c.getColumnIndex("Checkout")));
            list.add(ck);
        }
        return list;
    }
    public List<CheckIn> getAll(){
        String sql = "SELECT * FROM CheckIn ORDER BY ID DESC";
        return getData(sql);
    }
    public List<CheckIn> getID( String  CCCD ){
        String sql = "SELECT * FROM CheckIn WHERE CCCD = ?";
        return getData(sql, CCCD );
    }
    public CheckIn getCheckInByCCCD(String CCCD, String  start,String end){
        String sql = "SELECT * FROM CheckIn WHERE CCCD =? AND  ThoiGian BETWEEN ? AND ?";
        List<CheckIn> list= getData(sql,new String[]{CCCD,start,end});
        return list.isEmpty() ? null : list.get(0);
    }
}
