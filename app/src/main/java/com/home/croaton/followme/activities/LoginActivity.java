package com.home.croaton.followme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.home.croaton.followme.R;
import com.home.croaton.followme.database.DatabaseHelper;
import com.home.croaton.followme.database.Player;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.instrumentation.ConnectionHelper;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper userData;
    private EditText userName, password;
    private TextView register;
    private Button login;
    private Game game;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userData = new DatabaseHelper(this);
        userName = (EditText) findViewById(R.id.teUsername);
        password = (EditText) findViewById(R.id.tePassword);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.teRegister);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentLanguage = sharedPref.getString(getString(R.string.settings_language_preference), "en");

        game = new Game(this);

        PermissionAndConnectionChecker.checkForPermissions(LoginActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);

    }

    public void onPressingLogin(View view) {
        String username = userName.getText().toString();
        String pass = password.getText().toString();

        if (playerExists(username, pass)) {
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            intent.putExtra(IntentNames.SELECTED_GAME, game);
            startActivity(intent);
        } else
            Toast.makeText(LoginActivity.this, "Username or password is not correct", Toast.LENGTH_SHORT).show();
    }

    public void onPressingRegister(View view) {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == PermissionAndConnectionChecker.LocalStorageRequestCode) {
            for (int i = 0; i < grantResults.length; i++)
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    System.exit(0);
                }
        }
    }
    

    public boolean playerExists(String username,String password){
        ArrayList<Player> players;
        players = userData.getPlayers();
        for (Player player : players){
            if(username.equals(player.getUsername()) && password.equals(player.getPassword()))
            { return true;}
        }
        return false;
    }
}





