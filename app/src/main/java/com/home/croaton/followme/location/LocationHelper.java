package com.home.croaton.followme.location;

import android.location.Location;
import org.osmdroid.util.GeoPoint;

public class LocationHelper
{
    public static float GetDistance(GeoPoint point1, GeoPoint point2)
    {
        float[] results = new float[10];
        Location.distanceBetween(
                point1.getLatitude(),
                point1.getLongitude(),
                point2.getLatitude(),
                point2.getLongitude(),
                results);

        // ToDo: check, is that always results[0]?
        return results[0];
    }
}
