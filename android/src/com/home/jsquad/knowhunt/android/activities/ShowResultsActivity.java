package com.home.jsquad.knowhunt.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.home.jsquad.knowhunt.R;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.home.jsquad.knowhunt.R;
import com.home.jsquad.knowhunt.android.database.DatabaseHelper;
import com.home.jsquad.knowhunt.android.database.Player;

import java.util.ArrayList;

public class ShowResultsActivity extends AppCompatActivity {
    private Player player;
    private DatabaseHelper myDB;
    private EditText firstName, firstScore, secondName, secondScore;
    private Button ok;
    private MapsActivity mapsActivityObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        myDB = new DatabaseHelper(this);
        player = new Player();

        firstName = (EditText) findViewById(R.id.sfirstName);
        firstScore = (EditText) findViewById(R.id.sfirstScore);

        secondName = (EditText) findViewById(R.id.ssecondName);
        secondScore = (EditText) findViewById(R.id.ssecondScore);
        ok=(Button) findViewById(R.id.ok);


        showTopTwo();
        goToLastActivity();
    }

    /**
     * Finds the best two players
     * @return Array list containing the best two players
     */
    public ArrayList<Player> TopTwoPlayersDecider() {
        int max = 0;
        int index;
        ArrayList<Player> players = new ArrayList<Player>();
        ArrayList<Player> bestTwoPlayers = new ArrayList<Player>();
        players = myDB.getPlayers();
        if(players.size()==0||players.size()==1)return players;
        for (int j = 0; j < 2; j++) {
            max = (players.get(0)).getScores();
            index = 0;
            for (int i = 1; i < players.size(); i++) {

                if (max < (players.get(i)).getScores()) {
                    max = (players.get(i)).getScores();
                    index = i;
                }
            }
            if(players.size()<j+1)return bestTwoPlayers;
            bestTwoPlayers.add(j, players.get(index));
            (players.get(index)).setScores(Integer.MIN_VALUE);
        }
        return bestTwoPlayers;
    }

    /**
     * Gets the best two players information from the database
     * @return ArrayList containing the best two players with their info
     */
    public ArrayList<Player> getTopTwoInfo() {
        ArrayList<Player> topTwo = new ArrayList<Player>();
        ArrayList<Player> players = new ArrayList<Player>();
        ArrayList<Player> bestTwoPlayers=TopTwoPlayersDecider();
        players = myDB.getPlayers();
        if((players.size()==0||players.size()==1))return players;
        for (Player bestTwoPlayer : bestTwoPlayers) {
            for (Player player : players) {
                if (bestTwoPlayer.getID() == player.getID()) {
                    topTwo.add(player);

                }
            }
        }
        return topTwo;
    }

    /**
     * Shows the top two players
     */
    public void showTopTwo(){

        ArrayList<Player> bestTwoPlayers = new ArrayList<Player>();
        bestTwoPlayers = getTopTwoInfo();
        if (bestTwoPlayers.size() == 0) return;
        if (bestTwoPlayers.size() > 0) {
            firstName.setText((bestTwoPlayers.get(0)).getUsername());
            firstScore.setText(String.valueOf((bestTwoPlayers.get(0)).getScores()));
        }

        if (bestTwoPlayers.size() > 1) {
            secondName.setText((bestTwoPlayers.get(1)).getUsername());
            secondScore.setText(String.valueOf((bestTwoPlayers.get(1)).getScores()));
        }
    }

    public void goToLastActivity(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ShowResultsActivity.this, LoginActivity.class);
                ShowResultsActivity.this.startActivity(loginIntent);
            }
        });
    }

}
