package com.home.jsquad.knowhunt.android.location;

import com.home.jsquad.knowhunt.R;
import com.home.jsquad.knowhunt.android.activities.MapsActivity;
import com.home.jsquad.knowhunt.android.domain.Point;
import com.home.jsquad.knowhunt.android.maps.MapHelper;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class TrackEmulator
{
    private static Thread _trackerThread;

    public static void startFakeLocationTracking(MapsActivity activity, ArrayList<Point> points,
                                                 MapView map)
    {
        final MapsActivity activityCopy = activity;
        final ArrayList<Point> pointsCopy = points;
        final MapView innerMap = map;

        _trackerThread = new Thread()
        {
            public void run()
            {
                Point prev = null;
                final ArrayList<Marker> markers = new ArrayList<>();
                for(Point point : pointsCopy)
                {
                    if (prev == null)
                    {
                        prev = point;
                        continue;
                    }

                    final double step = 0.25;
                    for (double i = step; i <= 1; i += step)
                    {
                        final GeoPoint position = new GeoPoint(
                                point.Position.getLatitude() * i + prev.Position.getLatitude() * (1d - i),
                                point.Position.getLongitude() * i + prev.Position.getLongitude() * (1d - i));

                        activityCopy.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                markers.add(MapHelper.putMarker(activityCopy, innerMap, position, R.drawable.step, Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER, false));

                                activityCopy.locationChanged(position);
                            }
                        });

                        try
                        {
                            Thread.sleep(3000);
                          }
                        catch (InterruptedException e)
                        {
                            return;
                        }
                    }

                    prev = point;
                }
            }
        };

        _trackerThread.start();
    }

    public static void stop()
    {
        if (_trackerThread != null) {
            _trackerThread.interrupt();
        }
    }
}
