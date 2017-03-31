package com.home.jsquad.knowhunt.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import com.home.jsquad.knowhunt.instrumentation.MyObservable;

import org.osmdroid.util.GeoPoint;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String Command = "Command";
    private GoogleApiClient _googleApiClient;
    private LocationRequest _locationRequest;
    public static MyObservable<GeoPoint> LocationChanged = new MyObservable<>();

    private boolean _isRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent == null)
            return START_STICKY;

        TrackerCommand command = (TrackerCommand)intent.getSerializableExtra(Command);
        switch (command)
        {
            case Start:
                if (_googleApiClient == null)
                    setUpComponents();

                _googleApiClient.connect();
                _isRunning = true;
                break;
            case Stop:
                if (_googleApiClient != null)
                _googleApiClient.disconnect();
                _isRunning = false;
                break;
        }

        return START_STICKY;
    }

    private void setUpComponents() {
        _locationRequest = new LocationRequest();
        _locationRequest.setInterval(10000);
        _locationRequest.setFastestInterval(5000);
        _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        _googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (!_isRunning)
            return;

        LocationChanged.notifyObservers(new GeoPoint(location.getLatitude(), location.getLongitude()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(_googleApiClient,
                    _locationRequest, this);
        }
        catch(SecurityException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ToDo: notify user that we are not tracking her right now
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void onTaskRemoved(Intent rootIntent)
    {
        if (_googleApiClient != null)
            _googleApiClient.disconnect();

        stopSelf();
    }
}
