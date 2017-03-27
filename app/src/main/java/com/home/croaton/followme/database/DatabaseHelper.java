package com.home.croaton.followme.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Player.db";
    public static final String TABLE_NAME = "Player_table";
    public static final String _ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_SURNAME = "SURNAME";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_E_MAIL = "E_MAIL";
    public static final String COL_SCORES = "SCORES";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT, " +
                    COL_SURNAME + " TEXT, " +
                    COL_USERNAME + " TEXT, " +
                    COL_PASSWORD + " TEXT, " +
                    COL_E_MAIL + " TEXT, " +
                    COL_SCORES + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( SQL_CREATE_ENTRIES );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( SQL_DELETE_ENTRIES );
        onCreate(db);
    }

    public boolean insertPlayer(Player player){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, player.getName());
        values.put(COL_SURNAME, player.getSurname());
        values.put(COL_USERNAME, player.getUsername());
        values.put(COL_PASSWORD, player.getPassword());
        values.put(COL_E_MAIL, player.geteMail());
        values.put(COL_SCORES, 0);

        long result = database.insert(TABLE_NAME, null, values);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList<Player> getPlayers() {
        SQLiteDatabase database = this.getReadableDatabase();

        String[] projection = {
                _ID,
                COL_NAME,
                COL_SURNAME,
                COL_USERNAME,
                COL_PASSWORD,
                COL_E_MAIL,
                COL_SCORES,
        };

        Cursor cursor = database.query(
                TABLE_NAME,                             // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<Player> items = new ArrayList<Player>();
        while(cursor.moveToNext()) {
            Player p = new Player();
            p.setID(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
            p.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            p.setSurname(cursor.getString(cursor.getColumnIndexOrThrow(COL_SURNAME)));
            p.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)));
            p.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            p.seteMail(cursor.getString(cursor.getColumnIndexOrThrow(COL_E_MAIL)));
            p.setScores(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORES)));
            items.add(p);
        }
        cursor.close();
        return items;
    }


    public ArrayList<Player> getPlayersWithName(String name) {
        SQLiteDatabase database = this.getReadableDatabase();

        String[] projection = {
                _ID,
                COL_NAME,
                COL_SURNAME,
                COL_USERNAME,
                COL_PASSWORD,
                COL_E_MAIL,
                COL_SCORES,
        };

        String selection = DatabaseHelper.COL_NAME + " = ?";
        String[] selectionArgs = { name };

        Cursor cursor = database.query(
                TABLE_NAME,                               // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<Player> items = new ArrayList<Player>();
        while(cursor.moveToNext()) {
            Player p = new Player();
            p.setID(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
            p.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            p.setSurname(cursor.getString(cursor.getColumnIndexOrThrow(COL_SURNAME)));
            p.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)));
            p.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            p.seteMail(cursor.getString(cursor.getColumnIndexOrThrow(COL_E_MAIL)));
            p.setScores(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORES)));
            items.add(p);
        }
        cursor.close();
        return items;
    }
}
