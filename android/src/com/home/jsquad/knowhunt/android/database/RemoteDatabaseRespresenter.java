package com.home.jsquad.knowhunt.android.database;

import android.content.Context;
import android.database.Cursor;

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
    private HttpURLConnection httpURLConnection;
    private String url;
    private BufferedReader bufferedReader;
    private String resultForJson;
    private RequestQueue queue;

    public RemoteDatabaseRespresenter(Context context) {
        questAndAnswDatabaseHelper  = new QuestAndAnswDatabaseHelper(context);
        //questAndAnswDatabaseHelper.deleteTable();
        insertSampleData();
    }

    private static void insertSampleData(){
        //questAndAnswDatabaseHelper.insertDataQA("What is the name of the founder of Stockholm?", "Birger Jarl", "\tEric XI\n" +
                //"Valdemar", "Matilda of Holstein", "Karl IX", null, 0);
        //questAndAnswDatabaseHelper.insertDataQA("How old is KTH?", "190 years old", "50 years old", "90 years old", "125 years old", null, 0);
        //questAndAnswDatabaseHelper.insertDataQA("How many students can simultaneously study at KTH?", "12 000", "2 000", "45 000", "7 000", null, 0);
        //questAndAnswDatabaseHelper.insertDataQA("Who made Sweden Independent?", "Gustav Vasa", "Karl XII", "Ulrika Eleonora", "Christian II", null, 0);
        questAndAnswDatabaseHelper.insertDataMusic("The name of the song is:", "Mamma Mia", "I Am Just a Girl", "Dancing Queen", "Happy New Year", 0, "android.resource://com.home.croaton.followme/raw/abba_mamma_mia");
        questAndAnswDatabaseHelper.insertDataMusic("The name of the song is:", "Halleluja", "Mamma Mia", "Let it be", "My love", 0, "android.resource://com.home.croaton.followme/raw/halleluja");
        questAndAnswDatabaseHelper.insertDataMusic("The name of the song is:", "Let it be", "We are the Champions", "Halleluja", "Paris", 0, "android.resource://com.home.croaton.followme/raw/let_it_be");
        questAndAnswDatabaseHelper.insertDataMusic("The name of the song is:", "We are the Champions", "We are the Winners", "Wind of Changes", "Stay", 0, "android.resource://com.home.croaton.followme/raw/we_are_the_champions");
    }

    public static Cursor getAllData() {
        return questAndAnswDatabaseHelper.getAllQAData();
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
                                        json_data.getString("optionalAnswer1"), json_data.getString("optionalAnswer2"), json_data.getString("optionalAnswer3"), 0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "operation failed!", Toast.LENGTH_LONG).show();
            }
        });


        queue.add(stringRequest);


            /*httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer receivedData = new StringBuffer();
            String dataline = "";

            while ((dataline = bufferedReader.readLine()) != null) {
                receivedData.append(dataline + "\n");
            }

            resultForJson = receivedData.toString(); */



       /* try {
            JSONObject jsonObject = new JSONObject(resultForJson);
            JSONArray jsonArray = jsonObject.getJSONArray("qaList");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                questAndAnswDatabaseHelper.insertDataQA(json_data.getString("question"), json_data.getString("rightAnswer"),
                        json_data.getString("optionalAnswer1"), json_data.getString("optionalAnswer2"), json_data.getString("optionalAnswer3"), null, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }
}
