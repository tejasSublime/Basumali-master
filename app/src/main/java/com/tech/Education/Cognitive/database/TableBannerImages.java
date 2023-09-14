package com.tech.Education.Cognitive.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bodacious on 10/2/18.
 */

public class TableBannerImages {

    public static final String TAG = "TableBannerImages";
    public static void onCreate(SQLiteDatabase db){
        String query = "create table bannerImages("
                + "id integer,"
                + "imageUrl text"
                + ")";
        db.execSQL(query);
    }

    public static void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "drop table if exists bannerImages";
        db.execSQL(query);
        onCreate(db);
    }

    public static void saveBannersImages(int id, String imageUrl, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("imageUrl", imageUrl);
        String query = "SELECT * from bannerImages where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()>0){
            db.update("bannerImages", values, "id="+id, null);
        }else {
            db.insert("bannerImages",null, values);
        }
        cursor.close();
    }

    public static void deleteBannerImages(SQLiteDatabase db){
        String query = "delete from bannerImages";
        db.execSQL(query);
    }

}
