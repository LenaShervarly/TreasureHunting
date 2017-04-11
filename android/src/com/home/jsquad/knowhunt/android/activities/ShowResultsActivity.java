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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowResultsActivity extends AppCompatActivity {
    private Player player;
    private DatabaseHelper myDB;
    private EditText firstName, firstScore, secondName, secondScore, name3, score3, name4, score4, name5, score5;
    private Button ok;
    private MapsActivity mapsActivityObject;
    private ArrayList<Player> players;

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

        name3 = (EditText) findViewById(R.id.name3);
        score3 = (EditText) findViewById(R.id.score3);

        name4 = (EditText) findViewById(R.id.name4);
        score4 = (EditText) findViewById(R.id.score4);

        name5 = (EditText) findViewById(R.id.name5);
        score5 = (EditText) findViewById(R.id.score5);

        ok=(Button) findViewById(R.id.ok);


        showTopTwo();
        goToLastActivity();
    }

    /**

     * Finds the best two players
     * @return Array list containing the best two players
     */
    public ArrayList<Player> sortPlayers() {
        int max = 0;
        int index;

        List<Player> bestTwoPlayers = new ArrayList<Player>();
        ArrayList<Player> players = myDB.getPlayers();

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player lhs, Player rhs) {
                return rhs.compareTo(lhs);
            }
        });
        return players;

        /*if(players.size()==0||players.size()==1) return players;


        for (int j = 0; j < 5; j++) {
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
        return bestTwoPlayers;*/
    }

    /**
     * Gets the best two players information from the database
     * @return ArrayList containing the best two players with their info
     */
    /*public ArrayList<Player> getTopTwoInfo() {
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
    } */

    /**
     * Shows the top two players
     */
    public void showTopTwo(){
        players = sortPlayers();

        firstName.setText((players.get(0)).getUsername());
        firstScore.setText(String.valueOf((players.get(0)).getScores()));

        secondName.setText((players.get(1)).getUsername());
        secondScore.setText(String.valueOf((players.get(1)).getScores()));

        name3.setText((players.get(2)).getUsername());
        score3.setText(String.valueOf((players.get(2)).getScores()));

        name4.setText((players.get(3)).getUsername());
        score4.setText(String.valueOf((players.get(3)).getScores()));

        name5.setText((players.get(4)).getUsername());
        score5.setText(String.valueOf((players.get(4)).getScores()));

        /*ArrayList<Player> bestTwoPlayers = new ArrayList<Player>();
        bestTwoPlayers = getTopTwoInfo();
        if (bestTwoPlayers.size() == 0) return;
        if (bestTwoPlayers.size() > 0) {
            firstName.setText((bestTwoPlayers.get(0)).getUsername());
            firstScore.setText(String.valueOf((bestTwoPlayers.get(0)).getScores()));
        }

        if (bestTwoPlayers.size() > 1) {
            secondName.setText((bestTwoPlayers.get(1)).getUsername());
            secondScore.setText(String.valueOf((bestTwoPlayers.get(1)).getScores()));
        } */
    }

    public void goToLastActivity(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ShowResultsActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

}
