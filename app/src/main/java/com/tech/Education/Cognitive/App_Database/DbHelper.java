package com.tech.Education.Cognitive.App_Database;

/**
 * Created by admin on 1/22/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.tech.Education.Cognitive.App_Bean.QuizBean;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Quiz";
    // tasks table name
    public static final String TABLE_QUEST = "quiz_table";
    // tasks Table Columns names
    public static final String KEY_QUES_ID = "ques_id";
    public static final String KEY_QUIZ_ID = "quiz_id";
    public static final String KEY_QUES = "question";
    public static final String KEY_ANSWER = "answer"; //correct option
    public static final String KEY_OPTA = "opta"; //option a
    public static final String KEY_OPTB = "optb"; //option b
    public static final String KEY_OPTC = "optc"; //option c
    public static final String KEY_OPTD = "optd"; //option d
    public static final String KEY_QUES_IMG = "img"; //option d

    private SQLiteDatabase dbase;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase = db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
                + KEY_QUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUIZ_ID + " INTEGER, " + KEY_QUES
                + " TEXT, " + KEY_ANSWER + " TEXT, " + KEY_OPTA + " TEXT, "
                + KEY_OPTB + " TEXT, " + KEY_OPTC + " TEXT, " + KEY_OPTD + " TEXT, " + KEY_QUES_IMG + " TEXT)";
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }

    // Adding new question
    public void addQuestion(QuizBean quiz) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_ID, quiz.getQuestion_quizid());
        values.put(KEY_QUES, quiz.getQuestion_question());
        values.put(KEY_ANSWER, quiz.getQuestion_ans());
        values.put(KEY_OPTA, quiz.getQuestion_option_a());
        values.put(KEY_OPTB, quiz.getQuestion_option_b());
        values.put(KEY_OPTC, quiz.getQuestion_option_c());
        values.put(KEY_OPTD, quiz.getQuestion_option_d());
        values.put(KEY_QUES_IMG, quiz.getQuestion_image());
        // Inserting Row
        database.insert(TABLE_QUEST, null, values);
        database.close(); // Closing database connection
    }

    //getAllRecond from database
    public List<QuizBean> getAllQuestions() {
        List<QuizBean> quizList = new ArrayList<QuizBean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        dbase = this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizBean quiz = new QuizBean();
                quiz.setQuestionid(cursor.getInt(0));
                quiz.setQuestion_quizid(cursor.getInt(1));
                quiz.setQuestion_question(cursor.getString(2));
                quiz.setQuestion_ans(cursor.getString(3));
                quiz.setQuestion_option_a(cursor.getString(4));
                quiz.setQuestion_option_b(cursor.getString(5));
                quiz.setQuestion_option_c(cursor.getString(6));
                quiz.setQuestion_option_d(cursor.getString(7));
                quiz.setQuestion_image(cursor.getString(8));
                quizList.add(quiz);
            } while (cursor.moveToNext());
        }
        // return quest list
        return quizList;
    }


    //Delete all data from table
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_QUEST);
    }

    //Count how many raw store in database
    public int rowcount() {
        int row = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        dbase = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row = cursor.getCount();
        return row;
    }

    //Check table is exist or not
    public boolean doesTableExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_QUEST + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void countColumn() {
        SQLiteDatabase mDataBase;
        mDataBase = getReadableDatabase();
        Cursor dbCursor = mDataBase.query(TABLE_QUEST, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
    }
}