package com.tech.Education.Cognitive.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bodacious on 3/2/18.
 */

public class TableAccessKey {

    public static final String TAG = "TableAccessKey";
    public static void onCreate(SQLiteDatabase db){
        String query = "create table accessKey("
                + "id integer,"
                + "key integer"
                + ")";
        db.execSQL(query);
    }

    public static void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "drop table if exists accessKey";
        db.execSQL(query);
        onCreate(db);
    }
    public static void saveAccessKeys(int id, int key, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("key", key);
        String query = "SELECT * from accessKey where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()>0){
            db.update("accessKey", values, "id="+id, null);
        }else {
            db.insert("accessKey",null, values);
        }
        cursor.close();
    }

    public static void deleteAccessKey(SQLiteDatabase db){
        String query = "delete from accessKey";
        db.execSQL(query);
    }
}