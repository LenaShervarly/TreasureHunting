package com.home.croaton.followme.audio;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.home.croaton.followme.R;
import com.home.croaton.followme.activities.IntentNames;
import com.home.croaton.followme.activities.MapsActivity;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.instrumentation.IObservable;
import com.home.croaton.followme.instrumentation.MyObservable;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

// Stack overflow: There can only be one instance of a given Service.
public class AudioService extends android.app.Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{
    public static final String NewTracks = "Tracks";
    public static final String Command = "Command";
    public static final String Progress = "Progress";
    public static final String TrackCaption = "TrackCaption";

    private static final String _serviceName = "Audio Service";
    private static final int notificationId = 1;
    private static String _lastPlayedTrackName = null;
    private static volatile int _position;
    private static PlayerState _playerState;

    private volatile MediaPlayer _mediaPlayer;
    private Queue<Uri> _uriQueue = new LinkedList<>();
    private final int _positionPollTime = 500;
    private int FullProgress = 100;

    private NotificationCompat.Builder _notificationBuilder;
    private Notification _notification;
    private volatile ReentrantLock _playerLock = new ReentrantLock();
    private Thread _positionPoller;

    private static MyObservable<PlayerState> _innerState = new MyObservable<>();
    public static IObservable<PlayerState> State = _innerState;

    private static MyObservable<Integer> _innerPosition = new MyObservable<>();
    public static IObservable<Integer> Position = _innerPosition;

    private static MyObservable<String> _innerTrackName = new MyObservable<>();
    public static IObservable<String> TrackName = _innerTrackName;

    private volatile boolean _playbackFinished = false;
    private volatile boolean _isRunning = true;

    @Override
    // OOP cries. Me too.
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent == null)
            return START_STICKY;

        AudioServiceCommand command = (AudioServiceCommand)intent.getSerializableExtra(Command);
        if (command == AudioServiceCommand.ToggleState)
        {
            if (_mediaPlayer == null)
                return START_STICKY;

            command = _mediaPlayer.isPlaying()
                    ? AudioServiceCommand.Pause
                    : AudioServiceCommand.Play;
        }
        switch (command)
        {
            case LoadTracks:
                ArrayList<String> newTracks = (ArrayList<String>)intent.getSerializableExtra(NewTracks);
                RenewPlayer();

                _uriQueue.clear();
                for(String eachTrack : newTracks)
                    _uriQueue.offer(Uri.parse(eachTrack));

                setPlayerListeners();
                preparePlayerWithNextTrack();
                break;
            case Pause:
                _playerLock.lock();
                if (_mediaPlayer != null)
                {
                    _mediaPlayer.pause();
                    _innerState.notifyObservers(PlayerState.Paused);
                    _playerState = PlayerState.Paused;
                }
                _playerLock.unlock();
                break;
            case Play:
                _playerLock.lock();
                if (_mediaPlayer != null)
                {
                    _playbackFinished = false;
                    _mediaPlayer.start();
                    _innerState.notifyObservers(PlayerState.Playing);
                    _playerState = PlayerState.Playing;
                }
                _playerLock.unlock();
                break;
            case Rewind:
                int progress = intent.getIntExtra(Progress, 0);
                _playerLock.lock();
                if (_mediaPlayer != null)
                    _mediaPlayer.seekTo(_mediaPlayer.getDuration() * progress / 100);
                _playerLock.unlock();
                break;
            case StartForeground:
                String caption = intent.getStringExtra(TrackCaption);
                Game game = intent.getParcelableExtra(IntentNames.SELECTED_GAME);
                setUpAsForeground(caption, game);
                break;
        }

        return START_STICKY;
    }

    private void setPlayerListeners() {
        _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        _mediaPlayer.setOnErrorListener(this);
        _mediaPlayer.setOnPreparedListener(this);
        _mediaPlayer.setOnCompletionListener(this);
    }

    private void RenewPlayer()
    {
        _playerLock.lock();

        if (_mediaPlayer != null)
        {
            _mediaPlayer.release();
        }
        _mediaPlayer = new MediaPlayer();

        _playerLock.unlock();

        _innerState.notifyObservers(PlayerState.NotPrepared);
        _playerState = PlayerState.NotPrepared;

        if (_positionPoller == null) {
            _positionPoller = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean onPrevStepWasPlaying = false;
                    while (_isRunning)
                    {
                        _playerLock.lock();

                        if (_mediaPlayer == null)
                            continue;

                        boolean isPlaying = _mediaPlayer.isPlaying();
                        if (isPlaying)
                        {
                            int total = _mediaPlayer.getDuration();
                            int currentPosition = _mediaPlayer.getCurrentPosition();
                            int newPosition = (int)((double) currentPosition / (double)total * FullProgress);
                            if (newPosition != _position) {
                                _position = newPosition;
                                _innerPosition.notifyObservers(_position);
                            }
                            onPrevStepWasPlaying = isPlaying;
                        }
                        else if (onPrevStepWasPlaying)
                        {
                            onPrevStepWasPlaying = false;
                            if (_playbackFinished)
                                _innerPosition.notifyObservers(FullProgress);
                        }
                        _playerLock.unlock();

                        try
                        {
                            Thread.sleep(_positionPollTime);
                        }
                        catch (InterruptedException e)
                        {
                            return;
                        }
                    }
                }
            });
            _positionPoller.start();
        }

    }

    void setUpAsForeground(String text, Game game)
    {
        if (_notificationBuilder == null)
        {
            Intent notIntent = new Intent(this, MapsActivity.class);
            notIntent.putExtra(IntentNames.SELECTED_GAME, game);
            notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                    notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            _notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentIntent(pendInt)
                    .setOngoing(true)
                    .setContentTitle(getString(R.string.game_track_notification_caption))
                    .setSmallIcon(R.drawable.notification_icon_small);
        }
        _notificationBuilder.setContentText(text);
        _notificationBuilder.setTicker(text);
        _notification = _notificationBuilder.build();

        startForeground(notificationId, _notification);
    }

    private void preparePlayerWithNextTrack()
    {
        try
        {
            if (!NextTrackExists())
                return;

            _lastPlayedTrackName = getFileName(_uriQueue.peek().toString());
            _innerTrackName.notifyObservers(_lastPlayedTrackName);
            _mediaPlayer.setDataSource(this,_uriQueue.poll());
        } catch (Exception e)
        {
            Log.e(_serviceName, e.toString());
        }

        try
        {
            _mediaPlayer.prepareAsync();
        } catch (IllegalStateException e)
        {
            Log.e(_serviceName, e.getMessage());
        }
    }

    private boolean NextTrackExists() {
      /*  while(!new File(_uriQueue.peek()).exists()) {
            CharSequence text = getResources().getString(R.string.file_not_found);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            _uriQueue.poll();
            if (_uriQueue.size() == 0)
                return false;
        }*/

        return true;
    }

    private String getFileName(String fullName) {
        String[] elements = fullName.split("/");
        return elements[elements.length - 1].split("\\.")[0];
    }

    private boolean hasNextTrack() {
        return !_uriQueue.isEmpty();
    }

    public void onPrepared(MediaPlayer player)
    {
        if (!_isRunning)
            return;

        _playerLock.lock();

        if (playerIsActual(player))
        {
            player.start();
            _playbackFinished = false;
            _innerState.notifyObservers(PlayerState.Playing);
            _playerState = PlayerState.Playing;
        }

        _playerLock.unlock();
    }

    private boolean playerIsActual(MediaPlayer player)
    {
        return _mediaPlayer != null && _mediaPlayer.hashCode() == player.hashCode();
    }

    public static String getLastTrackName()
    {
        return _lastPlayedTrackName;
    }

    public static int getCurrentPosition()
    {
        return _position;
    }

    public static PlayerState getCurrentState()
    {
        return _playerState;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer player)
    {
        stopForeground(true);
        _playerLock.lock();
        _playbackFinished = true;
        _innerState.notifyObservers(PlayerState.PlaybackCompleted);
        _playerState = PlayerState.PlaybackCompleted;

        if (playerIsActual(player) && hasNextTrack())
        {
            _mediaPlayer.reset();
            setPlayerListeners();
            preparePlayerWithNextTrack();
        }

        _playerLock.unlock();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        _isRunning = false;
        stopForeground(true);

        if (_mediaPlayer != null)
        {
            _playerLock.lock();

            _mediaPlayer.release();
            _mediaPlayer = null;

            _playerLock.unlock();
        }

        _lastPlayedTrackName = "";
        _position = 0;
        _playerState = PlayerState.PlaybackCompleted;
    }

    public void onTaskRemoved(Intent rootIntent)
    {
        stopForeground(true);
        stopSelf();
    }
}