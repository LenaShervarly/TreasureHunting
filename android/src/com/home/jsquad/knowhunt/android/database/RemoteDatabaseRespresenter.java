package com.home.jsquad.knowhunt.android.database;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

/**
 * Created by lena on 23.03.2017.
 * This class represent the remote database, that will on later stages help to synchronize data between local SQLite database and MySQL database on the remote server.
 */

public class RemoteDatabaseRespresenter {

    private static QuestAndAnswDatabaseHelper questAndAnswDatabaseHelper;
    private String url;
    private RequestQueue queue;

    public RemoteDatabaseRespresenter(Context context) {
        questAndAnswDatabaseHelper  = new QuestAndAnswDatabaseHelper(context);
        insertSampleData();
    }

    private static void insertSampleData(){
        questAndAnswDatabaseHelper.insertDataMusic("Guess the song", "Mamma Mia", "I Am Just a Girl", "Dancing Queen", "Happy New Year", 0, "abba_mamma_mia");
        questAndAnswDatabaseHelper.insertDataMusic("Guess the song", "Dancing Queen", "Mamma Mia", "Let it be", "My love", 0, "dancing_queen");
        questAndAnswDatabaseHelper.insertDataMusic("Guess the song", "Happy New Year", "We are the Champions", "Halleluja", "Paris", 0, "happy_new_year");
        questAndAnswDatabaseHelper.insertDataMusic("Guess the song", "Money - Money", "Halleluja", "Wind of Changes", "Stay", 0, "money_money");
    }

    private static void insertQAsample(){
        questAndAnswDatabaseHelper.insertDataQA("How old is KTH?", "190 years old", "50 years old", "90 years old", "125 years old", 0, null);
        questAndAnswDatabaseHelper.insertDataQA("Who is the president of KTH?", "Sigbritt Karlsson", "Mattias Wiggberg", "Philipp Haller", "Johan Stålnacke", 0, null);
    }
    public static Cursor getAllQuestionsAndAnswers() {
        return questAndAnswDatabaseHelper.getAllQAData();
    }

    public static Cursor getQAForSecretCode(String secretCode) {
        return questAndAnswDatabaseHelper.getAllQuestionsAnswersForSecretCode(secretCode);
    }

    public boolean checkSecretCodeValidity(String secretCode) {
        return questAndAnswDatabaseHelper.checkSecretCodeValidity(secretCode);
    }

    public static boolean updateAlRaw(String ID, String question, String rightAnswer, String optionalAnswer1,
                               String optionalAnswer2, String optionalAnswer3, String melodyRoot, int passed){
        return questAndAnswDatabaseHelper.updateAllDataOfRaw(ID, question, rightAnswer, optionalAnswer1, optionalAnswer2, optionalAnswer3,
                melodyRoot, passed);
    }

    public static void updatePassedQA(String ID) {
        questAndAnswDatabaseHelper.updatePassedQuestionQA(ID);
    }

    public static void updatePassedMusic(String ID) {
        questAndAnswDatabaseHelper.updatePassedQuestionMusic(ID);
    }

    public static Cursor getDataWithMelody(){
        return questAndAnswDatabaseHelper.getDataWithMelody();
    }

    public void getDataFromServer(Context context){

            queue = Volley.newRequestQueue(context);
            url = "http://10.0.2.2:8080/WebServiceForKnowHunt/KnowHuntServlet";


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray =  response.getJSONArray("qaList");

                            for(int i = 0; i < jsonArray.length(); i++) {

                                JSONObject json_data = jsonArray.getJSONObject(i);


                                System.out.print(json_data.toString());
                                questAndAnswDatabaseHelper.insertDataQA(json_data.getString("question"), json_data.getString("rightAnswer"),
                                        json_data.getString("optionalAnswer1"), json_data.getString("optionalAnswer2"), json_data.getString("optionalAnswer3"),
                                        0, json_data.getString("secretCode"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        queue.add(stringRequest);
    }
}
