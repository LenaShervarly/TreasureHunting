package com.home.croaton.followme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.home.croaton.followme.R;
import com.home.croaton.followme.audio.AudioPlaybackController;
import com.home.croaton.followme.audio.AudioPlayerUI;

import java.util.ArrayList;

public class GuessMelodyActivity extends AppCompatActivity implements Iactivity{
    private PowerManager.WakeLock mWakeLock;
    public static final String WAKE_LOCK_NAME = "MyWakeLock";
    private AudioPlayerUI mAudioPlayerUi;
    private AudioPlaybackController mAudioPlaybackController;
    private ArrayList<String> audioToPlay;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_melody);

        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_NAME);
        mWakeLock.acquire();

        mAudioPlayerUi = new AudioPlayerUI(this);
        TextView textView = (TextView)this.findViewById(R.id.textViewSongName);
        textView.setText("Guess a melody");

        audioToPlay = new ArrayList<>();
        audioToPlay.add("android.resource://com.home.croaton.followme/raw/t7_05_museums");

        mAudioPlaybackController = new AudioPlaybackController(audioToPlay);
        mAudioPlaybackController.startPlaying(this, audioToPlay);
    }

    public void onTryingAnswer(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();
        String answer = "smth is wrong";

        switch (buttonText) {
            case "Mamma Mia" : answer = "Right answer";
                points = 5;
                break;
            case "I Am Just a Girl" : answer = "Wrong answer";
                break;
            case "Dancing Queen" : answer = "Wrong answer";
                break;
            case "Happy New Year" : answer = "Wrong answer";
                break;
            default : answer = "Wrong answer";
                break;
        }
        Toast.makeText(this, answer, Toast.LENGTH_SHORT).show();
    }

    public void onDoneButtonClicked(View view){
        Intent score = new Intent();
        score.putExtra("score", "Well done! You've got " + points +" points");
        setResult(Activity.RESULT_OK, score);
        finish();
    }
}
