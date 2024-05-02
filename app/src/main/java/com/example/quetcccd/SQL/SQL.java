package com.example.quetcccd.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQL extends SQLiteOpenHelper {
    public SQL(@Nullable Context context) {
        super(context, "database.db", null, 1);
    }

    public static final String Table_KhachMoi= "CREATE TABLE KhachMoi (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ThoiGian DATE DEFAULT NULL," +
            "CCCD TEXT NOT NULL," +
            "Ten TEXT NOT NULL," +
            "GioiTinh TEXT );";

    public static final String Table_CheckIn ="CREATE TABLE CheckIn (" +
            "CCCD TEXT," +
            " Name TEXT, " +
            "ThoiGian DATE  DEFAULT NULL, " +
            "Checkout DATE  DEFAULT NULL" +
            ", ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "FOREIGN KEY (CCCD) REFERENCES KhackMoi(CCCD) );";
    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(Table_KhachMoi);
            db.execSQL(Table_CheckIn);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if EXISTS KhachMoi");
        db.execSQL("DROP TABLE if EXISTS CheckIn");

    }
}
