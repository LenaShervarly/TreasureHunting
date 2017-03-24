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

        mAudioPlaybackController = new AudioPlaybackController(audioToPlay);
        mAudioPlaybackController.startPlaying(this, audioToPlay);
    }

    public void onTryingAnswer(View view) {
        Button button = (Button)view;
        //if(button.isActivated())
        String buttonText = button.getText().toString();
        String answer = "You've chosen " + buttonText;

        switch (buttonText) {
            case "Mamma Mia" : points = 5;
                break;
            case "I Am Just a Girl" : points = 0;
                break;
            case "Dancing Queen" : points = 0;
                break;
            case "Happy New Year" : points = 0;
                break;
            default : answer += "Please chose an answer";
                break;
        }
    }

        Intent score = new Intent();
        if(points > 0)
            score.putExtra("score", "Well done! You've got " + points +" points");
        else
            score.putExtra("score", "Nice try! Unfortunately you haven't got point for this round");
        setResult(Activity.RESULT_OK, score);
        finish();
       mAudioPlaybackController.stopAnyPlayback(this);
    }
}
