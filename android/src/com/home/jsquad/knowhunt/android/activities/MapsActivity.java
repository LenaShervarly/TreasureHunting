package com.home.jsquad.knowhunt.android.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.home.jsquad.knowhunt.R;
import com.home.jsquad.knowhunt.android.math.Vector2;
import com.home.jsquad.knowhunt.android.audio.AudioPlaybackController;
import com.home.jsquad.knowhunt.android.audio.AudioPlayerUI;
import com.home.jsquad.knowhunt.android.database.DatabaseHelper;
import com.home.jsquad.knowhunt.android.domain.AudioPoint;
import com.home.jsquad.knowhunt.android.domain.Game;
import com.home.jsquad.knowhunt.android.domain.Point;
import com.home.jsquad.knowhunt.android.domain.GameFileManager;
import com.home.jsquad.knowhunt.android.instrumentation.IObserver;
import com.home.jsquad.knowhunt.android.location.LocationService;
import com.home.jsquad.knowhunt.android.location.TrackerCommand;
import com.home.jsquad.knowhunt.android.maps.MapHelper;
import com.home.jsquad.knowhunt.android.security.PermissionAndConnectionChecker;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements  Iactivity{

    public static final String WAKE_LOCK_NAME = "MyWakeLock";

    private MapView mMap;
    private AudioPlaybackController mAudioPlaybackController;
    private PowerManager.WakeLock mWakeLock;
    private AudioPlayerUI mAudioPlayerUi;
    private ArrayList<Marker> mAudioPointMarkers = new ArrayList<>();
    private String mLanguage;
    private IObserver<GeoPoint> mLocationListener;
    private Game mCurrentGame;
    private int mLastActiveMarker = -1;
    private volatile MyLocationNewOverlay mLocationOverlay;
    private boolean mIsActivityPresentOnScreen;
    private GeoPoint mPreviousLocation = new GeoPoint(0,0);
    private GameFileManager mfileManager;
    private ActivityFactory factory;
    private int activityAudioPointNumber;
    static final int STORE_SCORES = 0;
    private CharSequence timeSpentForSolving;
    private int totalUserScores;
    private Chronometer chronometer;
    private List<Point> routePoints;
    private boolean isLastActivity = false;
    private DatabaseHelper databaseHelper;
    private String currentPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_NAME);
        mWakeLock.acquire();

        //mCurrentGame = getIntent().getParcelableExtra(IntentNames.SELECTED_GAME);
        mCurrentGame = new Game(this);

        loadState(savedInstanceState);
        mfileManager = new GameFileManager(this, mLanguage);

        setContentView(R.layout.activity_maps2);
        setUpMap();

        startLocationTracking();

        PermissionAndConnectionChecker.checkForPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        //mAudioPlayerUi = new AudioPlayerUI(this, mCurrentGame);
        startTimer();

        databaseHelper = new DatabaseHelper(this);
        currentPlayer = getIntent().getStringExtra(IntentNames.CURRENT_PLAYER_USERNAME);
    }

    private void loadState(Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mLanguage = "en";

        mAudioPlaybackController = new AudioPlaybackController(mLanguage, mCurrentGame);

        if (savedInstanceState == null)
            return;

        boolean[] passed = savedInstanceState.getBooleanArray(getString(R.string.audio_point_state));
        if (passed != null) {
            int i = 0;
            for (AudioPoint p : mCurrentGame.getAudioPoints())
                mAudioPlaybackController.markAudioPoint(p.Number, passed[i++]);
        }
        mLastActiveMarker = savedInstanceState.getInt(getString(R.string.last_active_marker));
    }

    public String getLanguage()
    {
        return mLanguage;
    }

    public void locationChanged(GeoPoint point) {
        if (mLocationOverlay == null && mIsActivityPresentOnScreen)
            enableMyLocation();

        AudioPoint audioPoint = mAudioPlaybackController.getResourceToPlay(point, new Vector2(mPreviousLocation, point));
        mPreviousLocation = point;

        if (audioPoint == null)
            return;

        ArrayList<String> trackNames = mfileManager.getTracksAtPoint(mCurrentGame.getRoute(), audioPoint);

        mAudioPlaybackController.startPlaying(this, trackNames);
        mAudioPlaybackController.markAudioPoint(audioPoint.Number, true);

        if (mMap != null) {
            if (mLastActiveMarker != -1)
                MapHelper.setMarkerIconFromResource(this, R.drawable.game_point_big, mAudioPointMarkers.get(mLastActiveMarker));
            MapHelper.setMarkerIconFromResource(this, R.drawable.game_point_big_active, mAudioPointMarkers.get(audioPoint.Number));
            mMap.invalidate();
            mLastActiveMarker = audioPoint.Number;
        }
        activityAudioPointNumber = audioPoint.Number;

        Intent intent = callActivity(activityAudioPointNumber);
        if(audioPoint.Position.equals(routePoints.get(routePoints.size() - 1).Position))
            isLastActivity = true;
        startActivityForResult(intent, STORE_SCORES);
    }

    private synchronized void startLocationTracking()
    {
        String[] requestedPermissions = new String[]
        {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (!PermissionAndConnectionChecker.checkForPermissions(this, requestedPermissions,
                PermissionAndConnectionChecker.LocationRequestCode)) {
            return;
        }

        askToEnableGps();
        enableMyLocation();

        mLocationListener = new IObserver<GeoPoint>() {
            @Override
            public void notify(GeoPoint location) {
                locationChanged(location);
            }
        };
        LocationService.LocationChanged.subscribe(mLocationListener);

        sendCommandToLocationService(TrackerCommand.Start);
    }

    private void askToEnableGps() {
        if ( PermissionAndConnectionChecker.gpsIsEnabled(this))
            return;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.enable_gps_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendCommandToLocationService(TrackerCommand command) {
        Intent startingIntent = new Intent(this, LocationService.class);
        startingIntent.putExtra(LocationService.Command, command);

        startService(startingIntent);
    }

    public void setUpMap() {
        mMap = (MapView) findViewById(R.id.map);

        MapHelper.chooseBeautifulMapProvider(this, mMap);

        mMap.setMultiTouchControls(true);
        mMap.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mMap.setFlingEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            enableMyLocation();

       routePoints = mCurrentGame.getGeoPoints();
        MapHelper.drawRoute(this, mMap, routePoints);
        MapHelper.focusCameraOnPoint(mMap, mAudioPlaybackController.getFirstNotDoneAudioPoint());
        //MapHelper.setStartRouteIcon(this, mMap, routePoints.get(0).Position);
        //MapHelper.setEndRouteIcon(this, mMap, routePoints.get(routePoints.size() - 1).Position);
        MapHelper.drawAudioPoints(this, mMap, mAudioPlaybackController, mCurrentGame, mAudioPointMarkers);

        for(Marker marker : mAudioPointMarkers)
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    AudioPoint gamePoint = mAudioPlaybackController.getResourceToPlay(marker.getPosition());
                    activityAudioPointNumber = gamePoint.Number;
                    // System.out.println("Audio point number is:" + activityAudioPointNumber);
                    ArrayList<String> trackNames = mfileManager.getTracksAtPoint(mCurrentGame.getRoute(), gamePoint);

                    if (mLastActiveMarker != -1)
                        MapHelper.setMarkerIconFromResource(MapsActivity.this, R.drawable.game_point_big, mAudioPointMarkers.get(mLastActiveMarker));

                    MapHelper.setMarkerIconFromResource(MapsActivity.this, R.drawable.game_point_big_active, marker);
                    mapView.invalidate();

                    mAudioPlaybackController.startPlaying(MapsActivity.this, trackNames);
                    mLastActiveMarker = gamePoint.Number;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent intent = callActivity(activityAudioPointNumber);
                    if(marker.getPosition().equals(routePoints.get(routePoints.size() - 1).Position))
                        isLastActivity = true;
                    startActivityForResult(intent, STORE_SCORES);

                    return true;
                }
            });
    }

    private Intent callActivity(int activityAudioPointNumber){
        Intent intent = null;
        factory = new ActivityFactory(activityAudioPointNumber);

        mCurrentGame.getAudioPoints();
        intent = factory.chooseActivity(this, activityAudioPointNumber);
        //startTimer();
        return intent;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent score) {
        if (requestCode == STORE_SCORES) {
            if (resultCode == RESULT_OK) {
                Toast t = Toast.makeText(this, score.getStringExtra("message"), Toast.LENGTH_SHORT);
                t.show();
                totalUserScores += score.getIntExtra("scores", 0);
            }
        }
        if(isLastActivity) {
            showResults();
            stopTimer();
            ArrayList<String> finalTrackNames = new ArrayList<>();
                finalTrackNames.add("android.resource://com.home.croaton.followme/raw/queen_we_are_the_champions_full");
            mAudioPlaybackController.startPlaying(this, finalTrackNames);
        }
        databaseHelper.updateScores(currentPlayer, totalUserScores);
    }

    private void startTimer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void stopTimer() {
        timeSpentForSolving = chronometer.getContentDescription();
        chronometer.setText(timeSpentForSolving);
        chronometer.stop();
    }

    private void showResults(){

        AlertDialog.Builder resultsBuilder = new AlertDialog.Builder(MapsActivity.this);
        View scoresView = getLayoutInflater().inflate(R.layout.scores_item, null);
        TextView user = (TextView) scoresView.findViewById(R.id.user);
        TextView scoreValue = (TextView) scoresView.findViewById(R.id.scores_value);
        TextView timeValue = (TextView) scoresView.findViewById(R.id.time_value);
        Button allResultsButton = (Button) scoresView.findViewById(R.id.see_all_results);
        Button restartButton = (Button) scoresView.findViewById(R.id.start_again);

        user.setText("User: " + currentPlayer);
        scoreValue.setText(getTotalUserScores() + " scores.");
        timeValue.setText(getTimeSpentForSolving());

        allResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast toast = Toast.makeText(MapsActivity.this, "Coming soon", Toast.LENGTH_SHORT);
                toast.show();*/
                Intent showRes = new Intent(MapsActivity.this, ShowResultsActivity.class);
                startActivity( showRes);
            }
        }
        );

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restartGame = new Intent(MapsActivity.this, GameOverviewActivity.class);
                startActivity(restartGame);
            }
        });
        resultsBuilder.setView(scoresView);

        AlertDialog dialog = resultsBuilder.create();
        dialog.show();
    }

    public int getTotalUserScores(){
        return totalUserScores;
    }

    public CharSequence getTimeSpentForSolving(){
        return chronometer.getContentDescription();
    }

    private void enableMyLocation() {
        if (mLocationOverlay != null)
            return;

        mLocationOverlay = MapHelper.addLocationOverlay(this, mMap);
        if (mLocationOverlay == null)
            return;

        FloatingActionButton btCenterMap = (FloatingActionButton) findViewById(R.id.button_center_map);
        mLocationOverlay.setDrawAccuracyEnabled(true);
        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint myPosition = mLocationOverlay.getMyLocation();
                if (myPosition != null)
                    mMap.getController().animateTo(myPosition);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PermissionAndConnectionChecker.LocationRequestCode:
                for(int i = 0; i < grantResults.length; i++)
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        return;

                startLocationTracking();

                break;
            case PermissionAndConnectionChecker.LocalStorageRequestCode:
                for(int i = 0; i < grantResults.length; i++)
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    {
                        finish();
                        System.exit(0);
                    }
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mIsActivityPresentOnScreen = false;
        if (mLocationOverlay != null)
            mLocationOverlay.disableMyLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putBooleanArray(getString(R.string.audio_point_state), mAudioPlaybackController.getDoneArray());
        savedInstanceState.putParcelable(IntentNames.SELECTED_GAME_BRIEF, mCurrentGame);
        savedInstanceState.putInt(getString(R.string.last_active_marker), mLastActiveMarker);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mIsActivityPresentOnScreen = true;
        if (mLocationOverlay != null)
            mLocationOverlay.enableMyLocation();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            try {
                mAudioPlayerUi.close();
                mAudioPlayerUi = null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            LocationService.LocationChanged.unSubscribe(mLocationListener);

            AudioPlaybackController.stopAnyPlayback(this);
            sendCommandToLocationService(TrackerCommand.Stop);
            stopService(new Intent(this, LocationService.class));
        }

        return super.onKeyDown(keyCode, event);
    }

}