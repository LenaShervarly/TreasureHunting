package com.home.croaton.followme.activities;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
}
