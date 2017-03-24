package com.home.croaton.followme.database;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by lena on 23.03.2017.
 * This class represent the remote database, that will on later stages help to synchronize data between local SQLite database and MySQL database on the remote server.
 */

public class RemoteDatabaseRespresenter {

    private QuestAndAnswDatabaseHelper questAndAnswDatabaseHelper;

    public RemoteDatabaseRespresenter(Context context) {
        questAndAnswDatabaseHelper  = new QuestAndAnswDatabaseHelper(context);
        insertSampleData();
    }

    private void insertSampleData(){
        questAndAnswDatabaseHelper.insertData("What is the name of the founder of Stockholm?", "Birger Jarl", "\tEric XI\n" +
                "Valdemar", "Matilda of Holstein", "Karl IX", null, 1);
        questAndAnswDatabaseHelper.insertData("How old is KTH?", "190 years old", "50 years old", "90 years old", "125 years old", null, 1);
        questAndAnswDatabaseHelper.insertData("How many students can simultaneously study at KTH?", "12 000", "2 000", "45 000", "7 000", null, 1);
        questAndAnswDatabaseHelper.insertData("Who made Sweden Independent?", "Gustav Vasa", "Karl XII", "Ulrika Eleonora", "Christian II", null, 1);
        questAndAnswDatabaseHelper.insertData("The name of the song is:", "Mamma Mia", "I Am Just a Girl", "Dancing Queen", "Happy New Year", "android.resource://com.home.croaton.followme/raw/abba_mamma_mia", 0);
        questAndAnswDatabaseHelper.insertData("The name of the song is:", "Halleluja", "Mamma Mia", "Let it be", "My love", "android.resource://com.home.croaton.followme/raw/halleluja", 0);
        questAndAnswDatabaseHelper.insertData("The name of the song is:", "Let it be", "We are the Champions", "Halleluja", "Paris", "android.resource://com.home.croaton.followme/raw/let_it_be", 0);
        questAndAnswDatabaseHelper.insertData("The name of the song is:", "We are the Champions", "We are the Winners", "Wind of Changes", "Stay", "android.resource://com.home.croaton.followme/raw/we_are_the_champions", 0);
    }

    public Cursor getAllData() {
        return questAndAnswDatabaseHelper.getAllData();
    }

    public boolean updateAlRaw(String ID, String question, String rightAnswer, String optionalAnswer1,
                               String optionalAnswer2, String optionalAnswer3, String melodyRoot, int passed){
        return questAndAnswDatabaseHelper.updateAllDataOfRaw(ID, question, rightAnswer, optionalAnswer1, optionalAnswer2, optionalAnswer3,
                melodyRoot, passed);
    }

    public void updatePassed(String ID) {
        questAndAnswDatabaseHelper.updatePassedQuestion(ID);
    }

    public Cursor getDataWithMelody(){
        return questAndAnswDatabaseHelper.getDataWithMelody();
    }
}
