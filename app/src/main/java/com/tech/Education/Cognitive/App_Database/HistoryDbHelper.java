package com.tech.Education.Cognitive.App_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tech.Education.Cognitive.App_Bean.History;
import com.tech.Education.Cognitive.App_Bean.QuizBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/1/2018.
 */

public class HistoryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "History";
    // tasks table name
    public static final String TABLE_HISTORY = "history_table";
    // tasks Table Columns names
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_DAY = "day";
    public static final String KEY_CURRENT_DATE = "current_date";

    private SQLiteDatabase dbase;
    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase=db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " ( "
                + KEY_USER_ID + " INTEGER PRIMARY KEY, " +KEY_DAY+" INTEGER, "+KEY_CURRENT_DATE+" TEXT)";
        db.execSQL(sql);
        // addQuestions();
        //db.close();
    }
   /* private void addQuestions()
    {
        Question q1=new Question("If permissions are missing the application will get this at runtime","Parser", "SQLiteOpenHelper ", "Security Exception", "Security Exception");
        this.addQuestion(q1);
        Question q2=new Question("An open source standalone database", "SQLite", "BackupHelper", "NetworkInfo", "SQLite");
        this.addQuestion(q2);
        Question q3=new Question("Sharing of data in Android is done via?","Wi-Fi radio", "Service Content Provider","Ducking", "Service Content Provider" );
        this.addQuestion(q3);
        Question q4=new Question("Main class through which your application can access location services on Android", "LocationManager", "AttributeSet", "SQLiteOpenHelper","LocationManager");
        this.addQuestion(q4);
        Question q5=new Question("Android is?","NetworkInfo","GooglePlay","Linux Based","Linux Based");
        this.addQuestion(q5);
    }*/

    //delete table if need
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Create tables again
        onCreate(db);
    }

    // Adding new question
    public void addHistory(History history) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, history.getId());
        values.put(KEY_DAY, history.getDay());
        values.put(KEY_CURRENT_DATE, history.getCurrent_date());
        // Inserting Row
        database.insert(TABLE_HISTORY, null, values);
        database.close(); // Closing database connection
    }

    //getAllRecond from database
    public List<History> getAllQuestions() {
        List<History> historyList = new ArrayList<History>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setId(cursor.getInt(0));
                history.setDay(cursor.getInt(1));
                history.setCurrent_date(cursor.getString(2));
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        // return quest list
        return historyList;
    }

    /*
 * get single todo
 */
    public String getCurrentDate(String userId) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = "";
        try {
            cursor = db.rawQuery("SELECT * FROM history_table WHERE user_id="+userId,null);
            if(cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        currentDate = cursor.getString(cursor.getColumnIndex("current_date"));
                        Log.e("currrr","&& "+cursor.getString(cursor.getColumnIndex("current_date")));
                    }
                    while (cursor.moveToNext());
                }
            }
            return currentDate;
        }finally {
            cursor.close();
        }
    }

    public int getDay(String userId) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int day = 0;
        try {
            cursor = db.rawQuery("SELECT day FROM history_table WHERE user_id="+userId,null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                day = cursor.getInt(cursor.getColumnIndex("day"));
            }
            return day;
        }finally {
            cursor.close();
        }
    }


    //upadted App open day's
    public void updateDay(int userId)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            //String rawQuery = "update "+TABLE_HISTORY+ " set " +KEY_DAY+ " ="+KEY_DAY + 1 +" WHERE " +KEY_USER_ID+ " ="+userId;
            db.execSQL("UPDATE " + TABLE_HISTORY + " SET " + KEY_DAY + "=" + KEY_DAY + "+1" + " WHERE " + KEY_USER_ID + "='?'",
                    new String[] { String.valueOf(userId) } );
            //db.rawQuery(rawQuery, null);
            Log.v("UpdatedInt", "ID updated");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //upadted date
    public void updateDate(String date,int userId)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_HISTORY + " SET " + KEY_CURRENT_DATE + "=" + date + " WHERE " + KEY_USER_ID + "='?'",
                    new String[] { String.valueOf(userId) } );
           // db.rawQuery(rawQuery, null);
            Log.v("UpdatedInt", "Date updated");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean updateData(String id,int day,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID,id);
        contentValues.put(KEY_DAY,day);
        contentValues.put("current_date",date);
        db.update(TABLE_HISTORY, contentValues, "user_id = ?",new String[] { id });
        return true;
    }


    //Delete all data from table
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_HISTORY);
    }

    //Count how many raw store in database
    public int rowcount()
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;
        SQLiteDatabase db = this.getWritableDatabase();
        dbase=this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }

    //Check table is exist or not
    public boolean doesTableExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_HISTORY + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void countColumn(){
        SQLiteDatabase mDataBase;
        mDataBase = getReadableDatabase();
        Cursor dbCursor = mDataBase.query(TABLE_HISTORY, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
    }
}
