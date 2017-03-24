package com.home.croaton.followme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Player.db";
    public static final String TABLE_NAME = "player_table";
    public static final String _ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_SURNAME = "SURNAME";
    public static final String COL_E_MAIL = "E_MAIL";
    public static final String COL_SCORES = "SCORES";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseHelper.TABLE_NAME + " (" +
                    DatabaseHelper._ID + " INTEGER PRIMARY KEY," +
                    DatabaseHelper.COL_NAME + " TEXT," +
                    DatabaseHelper.COL_SURNAME + " TEXT," +
                    DatabaseHelper.COL_E_MAIL + " TEXT" +
                    DatabaseHelper.COL_SCORES + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( DatabaseHelper.SQL_CREATE_ENTRIES );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL( DatabaseHelper.SQL_DELETE_ENTRIES );
        onCreate(db);
    }

    public void insertPlayer(Player player){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME,player.getName());
        values.put(DatabaseHelper.COL_SURNAME,player.getSurname());
        values.put(DatabaseHelper.COL_E_MAIL,player.geteMail());
        values.put(DatabaseHelper.COL_SCORES,player.getScores());
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public ArrayList<Player> getPlayers() {
        SQLiteDatabase database = this.getReadableDatabase();

        String[] projection = {
                DatabaseHelper._ID,
                DatabaseHelper.COL_NAME,
                DatabaseHelper.COL_SURNAME,
                DatabaseHelper.COL_E_MAIL,
                DatabaseHelper.COL_SCORES,
        };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,                // The table to query
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
                DatabaseHelper._ID,
                DatabaseHelper.COL_NAME,
                DatabaseHelper.COL_SURNAME,
                DatabaseHelper.COL_E_MAIL,
                DatabaseHelper.COL_SCORES,
        };

        String selection = DatabaseHelper.COL_NAME + " = ?";
        String[] selectionArgs = { name };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,                // The table to query
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
            p.seteMail(cursor.getString(cursor.getColumnIndexOrThrow(COL_E_MAIL)));
            p.setScores(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORES)));
            items.add(p);
        }
        cursor.close();

        return items;
    }
}
