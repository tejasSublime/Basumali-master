package com.tech.Education.Cognitive.database;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TableMyPrograms {

    public static final String TAG = "TableMyPrograms";
    public static void onCreate(SQLiteDatabase db){
        String query = "create table myPrograms("
                + "id integer,"
                + "name text,"
                + "imageUrl text,"
                + "videoUrl text,"
                + "audioUrl text,"
                + "description text,"
                + "general text,"
                + "business text,"
                + "sales text"
                + ")";
        db.execSQL(query);
    }

    public static void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "drop table if exists myPrograms";
        db.execSQL(query);
        onCreate(db);
    }

    public static void saveMyPrograms(int id, String name, String imageUrl, String videoUrl
            , String audioUrl, String description, String general, String business, String sales, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("imageUrl", imageUrl);
        values.put("videoUrl", videoUrl);
        values.put("audioUrl", audioUrl);
        values.put("description", description);
        values.put("general", general);
        values.put("business", business);
        values.put("sales", sales);
        String query = "SELECT * from myPrograms where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()>0){
            db.update("myPrograms", values, "id="+id, null);
        }else {
            db.insert("myPrograms",null, values);
        }
        cursor.close();
    }

    public static void deleteMyPrograms(SQLiteDatabase db){
        String query = "DELETE from myPrograms";
        db.execSQL(query);
    }
}