package com.home.croaton.followme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.home.croaton.followme.R;
import com.home.croaton.followme.database.DatabaseHelper;
import com.home.croaton.followme.database.Player;

public class RegisterActivity extends AppCompatActivity {
    private Player player;
    private DatabaseHelper myDB;
    private EditText reName, reSurname, reUsername, rePassword, reEmail;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDB = new DatabaseHelper(this);

        reName = (EditText) findViewById(R.id.reName);
        reSurname = (EditText) findViewById(R.id.reSurName);
        reUsername = (EditText) findViewById(R.id.reUsername);
        rePassword = (EditText) findViewById(R.id.rePassword);
        reEmail = (EditText) findViewById(R.id.reEmail);
        register = (Button) findViewById(R.id.reRegist);
        addPlayer();
    }

    public void addPlayer(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player = new Player(reName.getText().toString(), reSurname.getText().toString(),
                        reUsername.getText().toString(), rePassword.getText().toString(), reEmail.getText().toString());

                /*String name = reName.getText().toString();
                String surname = reSurname.getText().toString();
                String username = reUsername.getText().toString();
                String password = rePassword.getText().toString();
                String email = reEmail.getText().toString();

                player.setName(name);
                player.setSurname(surname);
                player.setUsername(username);
                player.setPassword(password);
                player.seteMail(email); */

                boolean isInserted = myDB.insertPlayer(player);
                if(isInserted){
                    Toast.makeText(RegisterActivity.this, "Your data is saved", Toast.LENGTH_SHORT).show();
                    Intent loginIntent  = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(loginIntent);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Registeration failed, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

