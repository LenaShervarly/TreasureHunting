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
    public static final String TABLE_QA = "QuestAndAnsw_table";
    public static final String TABLE_MUSIC = "GuessTheMelody_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "QUESTION";
    public static final String COL_3 = "RIGHT_ANSWER";
    public static final String COL_4 = "OPTIONAL_ANSWER_1";
    public static final String COL_5 = "OPTIONAL_ANSWER_2";
    public static final String COL_6 = "OPTIONAL_ANSWER_3";
    public static final String COL_7 = "PASSED";
    public static final String COL_8 = "MELODY_ROOT";


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
        db.execSQL("create table " + TABLE_MUSIC + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, RIGHT_ANSWER TEXT, " +
        "OPTIONAL_ANSWER_1 TEXT, OPTIONAL_ANSWER_2 TEXT, OPTIONAL_ANSWER_3 TEXT, PASSED INTEGER, MELODY_ROOT TEXT)");
        db.execSQL("create table " + TABLE_QA + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, RIGHT_ANSWER TEXT, " +
                "OPTIONAL_ANSWER_1 TEXT, OPTIONAL_ANSWER_2 TEXT, OPTIONAL_ANSWER_3 TEXT, PASSED INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_QA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MUSIC);
        onCreate(db);
    }

    /**
     * Method enables adding new line to the Database of Questions and answers
     * @param question Question the user will receive
     * @param rightAnswer correct answer
     * @param optionalAnswer1 optional anwer to choose from
     * @param optionalAnswer2 optional anwer to choose from
     * @param optionalAnswer3 optional anwer to choose from
     */
    public void insertDataQA(String question, String rightAnswer, String optionalAnswer1, String optionalAnswer2, String optionalAnswer3, int passed) {
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, rightAnswer);
        contentValues.put(COL_4, optionalAnswer1);
        contentValues.put(COL_5, optionalAnswer2);
        contentValues.put(COL_6, optionalAnswer3);
        contentValues.put(COL_7, passed);
        long result = db.insert(TABLE_QA, null, contentValues);
    }

    /**
     * Method enables adding new line to the Database of Questions and answers with Melodies
     * @param question Question the user will receive
     * @param rightAnswer correct answer
     * @param optionalAnswer1 optional anwer to choose from
     * @param optionalAnswer2 optional anwer to choose from
     * @param optionalAnswer3 optional anwer to choose from
     * @param melodyRoot path of the file where the melody to play is stored
     */
    public void insertDataMusic(String question, String rightAnswer, String optionalAnswer1, String optionalAnswer2, String optionalAnswer3, int passed, String melodyRoot) {
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, question);
        contentValues.put(COL_3, rightAnswer);
        contentValues.put(COL_4, optionalAnswer1);
        contentValues.put(COL_5, optionalAnswer2);
        contentValues.put(COL_6, optionalAnswer3);
        contentValues.put(COL_7, passed);
        contentValues.put(COL_8, melodyRoot);
        long result = db.insert(TABLE_MUSIC, null, contentValues);
    }

    /**
     * Provides all the data of the database
     * @return all the containt of the database
     */
    public Cursor getAllQAData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_QA, null);
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
        contentValues.put(COL_7, passed);
        db.update(TABLE_QA, contentValues, "ID = ?", new String[]{ID});
        return true;
    }

    public void updatePassedQuestionQA(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_QA + " SET " + COL_7 + " = '1' "+ " WHERE id= " + id);
    }

    public void updatePassedQuestionMusic(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_MUSIC + " SET " + COL_7 + " = '1' "+ " WHERE id= " + id);
    }


    public Cursor getDataWithMelody(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public void renewTables() {
        SQLiteDatabase db  = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_QA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MUSIC);
        db.execSQL("create table " + TABLE_MUSIC + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, RIGHT_ANSWER TEXT, " +
                "OPTIONAL_ANSWER_1 TEXT, OPTIONAL_ANSWER_2 TEXT, OPTIONAL_ANSWER_3 TEXT, PASSED INTEGER, MELODY_ROOT TEXT)");
        db.execSQL("create table " + TABLE_QA + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT, RIGHT_ANSWER TEXT, " +
                "OPTIONAL_ANSWER_1 TEXT, OPTIONAL_ANSWER_2 TEXT, OPTIONAL_ANSWER_3 TEXT, PASSED INTEGER)");
    }
}
