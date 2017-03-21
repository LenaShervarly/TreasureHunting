package com.home.croaton.followme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tmpuser-10206 on 3/21/17.
 */

public class QuestAndAnswDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "QuestAndAnsw.db";
    public static final String TABLE_NAME = "QuestAndAnsw_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "QUESTION";
    public static final String COL_3 = "RIGHT_ANSWER";
    public static final String COL_4 = "OPTIONAL_ANSWER_1";
    public static final String COL_5 = "OPTIONAL_ANSWER_2";
    public static final String COL_6 = "OPTIONAL_ANSWER_3";

    public QuestAndAnswDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db  = this.getWritableDatabase();
    }

    public QuestAndAnswDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME  + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, RIGHT_ANSWER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
}
