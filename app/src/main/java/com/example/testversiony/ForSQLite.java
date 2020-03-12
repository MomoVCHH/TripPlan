package com.example.testversiony;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ForSQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TripPlan.db";
    public static final String TABLE_NAME = "tripplan_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "VON";
    public static final String COL_3 = "NACH";




    public ForSQLite(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "  + TABLE_NAME +" ( id INTEGER PRIMARY KEY AUTOINCREMENT,VON TEXT, NACH TEXT)");
      //  db.execSQL("create table" + TABLE_NAME+ "(NAME TEXT,EMAIL TEXT,PASSWORD TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
                onCreate(db);
    }

    public boolean inserData(String von, String nach){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2,von);
        contentValues.put(COL_3,nach);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}
