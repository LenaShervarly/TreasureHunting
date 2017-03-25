package com.home.croaton.followme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.home.croaton.followme.R;
import com.home.croaton.followme.database.DatabaseHelper;
import com.home.croaton.followme.database.Player;
import com.home.croaton.followme.domain.Game;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper myDB;
    private Game game;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDB=new DatabaseHelper(this);
        game = new Game(this);

        final EditText teUsername = (EditText) findViewById(R.id.teUsername);
        final EditText tePassword = (EditText) findViewById(R.id.tePassword);
        final TextView teRegister = (TextView) findViewById(R.id.teRegister);
        final Button Login = (Button) findViewById(R.id.login);

        teRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = teUsername.getText().toString();
                final String password = tePassword.getText().toString();

                if (playerExists(username, password)) {

                    Intent gameIntent = new Intent(LoginActivity.this, MapsActivity.class);
                    gameIntent.putExtra(IntentNames.SELECTED_GAME, game);
                    LoginActivity.this.startActivity(gameIntent);
                } else {
                    Toast.makeText(LoginActivity.this, "Username or password is not correct", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    public boolean playerExists(String username,String password){
        ArrayList<Player> players=new ArrayList<Player>();
        players=myDB.getPlayers();
        for (Player player:players){
            if(username.equals(player.getUsername())&&password.equals(player.getPassword()))
            { return true;}
        }
        return false;
    }
}

