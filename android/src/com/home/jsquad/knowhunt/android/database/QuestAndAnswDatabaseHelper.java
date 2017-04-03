package com.home.jsquad.knowhunt.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lena on 3/21/17.
 * This class is used in order to create a SQLite database for storing questions, answers and melody roots of activities, used for the Game.
 */

public class QuestAndAnswDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "QuentAnswer.db";
    public static final String TABLE_NAME = "QuestAndAnsw_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "QUESTION";
    public static final String COL_3 = "RIGHT_ANSWER";
    public static final String COL_4 = "OPTIONAL_ANSWER_1";
    public static final String COL_5 = "OPTIONAL_ANSWER_2";
    public static final String COL_6 = "OPTIONAL_ANSWER_3";
    public static final String COL_7 = "MELODY_ROOT";
    public static final String COL_8 = "PASSED";


    /**
     * COnstractor of Questions and Answers Database Helper
     * @param context
     */
    public QuestAndAnswDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

   /* public QuestAndAnswDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME  + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, RIGHT_ANSWER TEXT, " +
                "OPTIONAL_ANSWER_1 TEXT, OPTIONAL_ANSWER_2 TEXT, OPTIONAL_ANSWER_3 TEXT, MELODY_ROOT TEXT, PASSED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Method enables adding new line to the Database of Questions and answers
     * @param question Question the user will receive
     * @param rightAnswer correct answer
     * @param optionalAnswer1 optional anwer to choose from
     * @param optionalAnswer2 optional anwer to choose from
     * @param optionalAnswer3 optional anwer to choose from
     * @param melodyRoot path of the file where the melody to play is stored
     */
    public void insertData(String question, String rightAnswer, String optionalAnswer1, String optionalAnswer2, String optionalAnswer3, String melodyRoot, int passed) {
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, rightAnswer);
        contentValues.put(COL_4, optionalAnswer1);
        contentValues.put(COL_5, optionalAnswer2);
        contentValues.put(COL_6, optionalAnswer3);
        contentValues.put(COL_7, melodyRoot);
        contentValues.put(COL_8, passed);
        long result = db.insert(TABLE_NAME, null, contentValues);
    }

    /**
     * Provides all the data of the database
     * @return all the containt of the database
     */
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_7  + " IS NULL", null);
        //Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }

    public boolean updateAllDataOfRaw(String ID, String question, String rightAnswer, String optionalAnswer1, String optionalAnswer2, String optionalAnswer3, String melodyRoot, int passed){
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, rightAnswer);
        contentValues.put(COL_4, optionalAnswer1);
        contentValues.put(COL_5, optionalAnswer2);
        contentValues.put(COL_6, optionalAnswer3);
        contentValues.put(COL_7, melodyRoot);
        contentValues.put(COL_8, passed);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ID});
        return true;
    }

    public void updatePassedQuestion(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL_8 + " = '1' "+ " WHERE id= " + id);
    }

    public Cursor getDataWithMelody(){
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_7  + " IS NOT NULL";
        Cursor cursor = db.rawQuery(sql, null);

        return cursor;
    }
}
