package com.home.croaton.followme.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.home.croaton.followme.R;
import com.home.croaton.followme.audio.AudioPlaybackController;
import com.home.croaton.followme.audio.AudioPlayerUI;
import com.home.croaton.followme.domain.AudioPoint;
import com.home.croaton.followme.domain.Game;
import com.home.croaton.followme.domain.Point;
import com.home.croaton.followme.download.ExcursionDownloadManager;
import com.home.croaton.followme.instrumentation.IObserver;
import com.home.croaton.followme.location.LocationService;
import com.home.croaton.followme.location.TrackerCommand;
import com.home.croaton.followme.maps.MapHelper;
import com.home.croaton.followme.math.Vector2;
import com.home.croaton.followme.security.PermissionAndConnectionChecker;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    public static final String WAKE_LOCK_NAME = "MyWakeLock";

    private MapView mMap;
    private AudioPlaybackController mAudioPlaybackController;
    private PowerManager.WakeLock mWakeLock;
    private AudioPlayerUI mAudioPlayerUi;
    private ArrayList<Marker> mAudioPointMarkers = new ArrayList<>();
    private String mLanguage;
    private IObserver<GeoPoint> mLocationListener;
    private Game mCurrentExcursion;
    private int mLastActiveMarker = -1;
    private volatile MyLocationNewOverlay mLocationOverlay;
    private boolean mIsActivityPresentOnScreen;
    private GeoPoint mPreviousLocation = new GeoPoint(0,0);
    private ExcursionDownloadManager mDownloadManager;
    private MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_NAME);
        mWakeLock.acquire();

        mCurrentExcursion = getIntent().getParcelableExtra(IntentNames.SELECTED_EXCURSION);

        loadState(savedInstanceState);
        mDownloadManager = new ExcursionDownloadManager(this, mLanguage);

        setContentView(R.layout.activity_maps2);
        setUpMap();

        startLocationTracking();

        PermissionAndConnectionChecker.checkForPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionAndConnectionChecker.LocalStorageRequestCode);

        mAudioPlayerUi = new AudioPlayerUI(this, mCurrentExcursion);
        mPlayer = MediaPlayer.create(this, R.raw.t7_01_welcome);
    }

    private void loadState(Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mLanguage = "en";

        mAudioPlaybackController = new AudioPlaybackController(mLanguage, mCurrentExcursion);

        if (savedInstanceState == null)
            return;

        boolean[] passed = savedInstanceState.getBooleanArray(getString(R.string.audio_point_state));
        if (passed != null) {
            int i = 0;
            for (AudioPoint p : mCurrentExcursion.getAudioPoints())
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

        ArrayList<String> trackNames = mDownloadManager.getTracksAtPoint(mCurrentExcursion.getRoute(), audioPoint);

        mAudioPlaybackController.startPlaying(this, trackNames);
        mAudioPlaybackController.markAudioPoint(audioPoint.Number, true);

        if (mMap != null) {
            if (mLastActiveMarker != -1)
                MapHelper.setMarkerIconFromResource(this, R.drawable.game_point_big, mAudioPointMarkers.get(mLastActiveMarker));
            MapHelper.setMarkerIconFromResource(this, R.drawable.game_point_big_active, mAudioPointMarkers.get(audioPoint.Number));
            mMap.invalidate();
            mLastActiveMarker = audioPoint.Number;
        }
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

       List<Point> routePoints = mCurrentExcursion.getGeoPoints();
        MapHelper.drawRoute(this, mMap, routePoints);
        MapHelper.focusCameraOnPoint(mMap, mAudioPlaybackController.getFirstNotDoneAudioPoint());
        MapHelper.setStartRouteIcon(this, mMap, routePoints.get(0).Position);
        MapHelper.setEndRouteIcon(this, mMap, routePoints.get(routePoints.size() - 1).Position);
        MapHelper.drawAudioPoints(this, mMap, mAudioPlaybackController, mCurrentExcursion, mAudioPointMarkers);


        // Map click - forwarded as location
//        mMap.getOverlays().add(new MapEventsOverlay(this, new MapEventsReceiver() {
//            @Override
//            public boolean singleTapConfirmedHelper(GeoPoint p) {
//                locationChanged(p);
//                return false;
//            }
//
//            @Override
//            public boolean longPressHelper(GeoPoint p) {
//                return false;
//            }
//        }));

        for(Marker marker : mAudioPointMarkers)
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    /*Intent intent = new Intent(MapsActivity.this, QuizzActivity.class);
                    startActivity(intent);
                    */
                    AudioPoint gamePoint = mAudioPlaybackController.getResourceToPlay(marker.getPosition());
                    ArrayList<String> trackNames = mDownloadManager.getTracksAtPoint(mCurrentExcursion.getRoute(), gamePoint);

                    if (mLastActiveMarker != -1)
                        MapHelper.setMarkerIconFromResource(MapsActivity.this, R.drawable.game_point_big, mAudioPointMarkers.get(mLastActiveMarker));
                    MapHelper.setMarkerIconFromResource(MapsActivity.this, R.drawable.game_point_big_active, marker);
                    mapView.invalidate();

                    mAudioPlaybackController.startPlaying(MapsActivity.this, trackNames);
                    mLastActiveMarker = gamePoint.Number;

                    return true;
                }
            });
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
        savedInstanceState.putParcelable(IntentNames.SELECTED_EXCURSION_BRIEF, mCurrentExcursion);
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