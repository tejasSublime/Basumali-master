package com.tech.Education.Cognitive.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tech.Education.Cognitive.database.TableAccessKey;
import com.tech.Education.Cognitive.database.TableAllPrograms;
import com.tech.Education.Cognitive.database.TableBannerImages;
import com.tech.Education.Cognitive.database.TableMyPrograms;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String dbName = "winnersClub.db";
    private static final int DATABASE_VERSION = 1;

    private MySQLiteHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableAllPrograms.onCreate(db);
        TableAccessKey.onCreate(db);
        TableMyPrograms.onCreate(db);
        TableBannerImages.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableAllPrograms.onUpdate(db,oldVersion,newVersion);
        TableAccessKey.onUpdate(db,oldVersion,newVersion);
        TableMyPrograms.onUpdate(db,oldVersion,newVersion);
        TableBannerImages.onUpdate(db,oldVersion,newVersion);
    }

    public static MySQLiteHelper getMySQLiteHelper(Context context){
        return new MySQLiteHelper(context,dbName,null, DATABASE_VERSION);
    }
}

