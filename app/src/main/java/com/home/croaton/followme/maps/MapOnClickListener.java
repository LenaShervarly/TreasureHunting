package com.home.croaton.followme.maps;


import com.home.croaton.followme.location.LocationHelper;

import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MapOnClickListener implements MapEventsReceiver
{

    Callable<Void> _callback;

    public MapOnClickListener(Callable<Void> callback)
    {
        _callback = callback;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        boolean shouldCallCallback = false;

        if (shouldCallCallback) {
            try {
                _callback.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}
