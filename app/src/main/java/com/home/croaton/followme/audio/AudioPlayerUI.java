package com.home.croaton.followme.audio;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.home.croaton.followme.R;
import com.home.croaton.followme.activities.IntentNames;
import com.home.croaton.followme.activities.MapsActivity;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.instrumentation.IObserver;

public class AudioPlayerUI implements SeekBar.OnSeekBarChangeListener {

    private final MapsActivity _context;
    private final Game excursion;
    private IObserver<PlayerState> stateListener;
    private IObserver<String> trackNameListener;
    private IObserver<Integer> positionListener;

    public AudioPlayerUI(MapsActivity mapsActivity, Game excursion)
    {
        this.excursion = excursion;
        _context = mapsActivity;

        final FloatingActionButton pause = (FloatingActionButton) mapsActivity.findViewById(R.id.button_pause);
        final Context activity = mapsActivity;
        final SeekBar seekBar = (SeekBar) mapsActivity.findViewById(R.id.seekBar);
        int color = ContextCompat.getColor(_context, R.color.blue_light);
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        seekBar.setOnSeekBarChangeListener(this);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startingIntent = new Intent(activity, AudioService.class);
                startingIntent.putExtra(AudioService.Command, AudioServiceCommand.ToggleState);
                activity.startService(startingIntent);
            }
        });

        stateListener = new IObserver<PlayerState>() {
            @Override
            public void notify(PlayerState state) {
                if (state == PlayerState.Paused || state == PlayerState.PlaybackCompleted) {
                    pause.setImageDrawable(ContextCompat.getDrawable(_context, android.R.drawable.ic_media_play));
                }
                if (state == PlayerState.Playing) {
                    pause.setImageDrawable(ContextCompat.getDrawable(_context, android.R.drawable.ic_media_pause));
                }
            }
        };
        AudioService.State.subscribe(stateListener);
        stateListener.notify(AudioService.getCurrentState());

        positionListener = new IObserver<Integer>() {
            @Override
            public void notify(Integer progress) {
                seekBar.setProgress(progress);
            }
        };
        AudioService.Position.subscribe(positionListener);
        positionListener.notify(AudioService.getCurrentPosition());

        trackNameListener = new IObserver<String>() {
            @Override
            public void notify(String trackName) {
                String caption = changeAndGetTrackCaption(trackName);

                if (trackName == "")
                    return;

                Intent startingIntent = new Intent(_context, AudioService.class);
                startingIntent.putExtra(AudioService.Command, AudioServiceCommand.StartForeground);
                startingIntent.putExtra(AudioService.TrackCaption, caption);
                startingIntent.putExtra(IntentNames.SELECTED_EXCURSION, AudioPlayerUI.this.excursion);


                _context.startService(startingIntent);
            }
        };
        AudioService.TrackName.subscribe(trackNameListener);
        trackNameListener.notify(AudioService.getLastTrackName());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Intent startingIntent = new Intent(_context, AudioService.class);
        startingIntent.putExtra(AudioService.Command, AudioServiceCommand.Rewind);
        startingIntent.putExtra(AudioService.Progress, seekBar.getProgress());

        _context.startService(startingIntent);
    }

    private String changeAndGetTrackCaption(String trackName)
    {
        String caption = trackName.equals("")
            ? _context.getString(R.string.audio_choose_tack)
            : excursion.getTrackNames().getTrackName(_context.getLanguage(), trackName);

        TextView textView = (TextView)_context.findViewById(R.id.textViewSongName);
        textView.setText(caption);

        return caption;
    }

    public void close() throws Exception {
        AudioService.State.unSubscribe(stateListener);
        AudioService.TrackName.unSubscribe(trackNameListener);
        AudioService.Position.unSubscribe(positionListener);
    }
}
