/*
package com.tech.Education.Cognitive.App_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.tech.Education.Cognitive.App_Bean.PartBean;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by admin on 2/2/2018.
 *//*


public class VideoDB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "VideoDb";

    // Contacts table name
    private static final String TABLE_VIDEO = "video";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VIDEO_ID = "video_id";
    private static final String KEY_VIDEO_STATUS = "status";

    public VideoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_VIDEO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_VIDEO_ID + " INTEGER,"
                + KEY_VIDEO_STATUS + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
        // Create tables again
        onCreate(db);
    }

    // Adding new videos
   public void addVideos(PartBean part) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_ID, part.getVideospartid()); // Contact Name
        values.put(KEY_VIDEO_STATUS, part.getStatus()); // Contact Phone
        // Inserting Row
        db.insert(TABLE_VIDEO, null, values);
        db.close(); // Closing database connection
    }

     // Getting single contact
     PartBean getVideo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VIDEO, new String[] { KEY_ID,
                        KEY_VIDEO_ID, KEY_VIDEO_STATUS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PartBean part = new PartBean(cursor.getInt(0),
                cursor.getInt(1));
        // return contact
        return part;
    }

    // Getting All Contacts
    public List<PartBean> getAllVideoParts() {
        List<PartBean> partBeans = new ArrayList<PartBean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PartBean part = new PartBean();
                part.setVideospartid(cursor.getInt(0));
                part.setStatus(cursor.getInt(1));
                // Adding contact to list
                partBeans.add(part);
            } while (cursor.moveToNext());
        }

        // return contact list
        return partBeans;
    }

    public boolean updateDetails(int rowId,int status)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_VIDEO_STATUS, status);
        int i =  db.update(TABLE_VIDEO, args, KEY_ID + "> 1 limit 1" + "=" + rowId, null);
        return i > 0;
    }

    public String getSecondStatus(String userId) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String status ="";
        try {
            cursor = db.rawQuery("SELECT * FROM "+TABLE_VIDEO+" WHERE id > "+userId+" ORDER BY id LIMIT 1",null);
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        status = cursor.getString(cursor.getColumnIndex("status"));
                        Log.e("Status","&& "+cursor.getString(cursor.getColumnIndex("status")));
                    }
                    while (cursor.moveToNext());
                }
            }
            return status;
        }finally {
            cursor.close();
        }
    }

    // Getting parts Count
    public int getVideoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VIDEO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }
}
*/
