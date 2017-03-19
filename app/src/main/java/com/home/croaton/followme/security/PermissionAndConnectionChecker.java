package com.home.croaton.followme.security;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionAndConnectionChecker {
    public static final int LocationRequestCode = 1;
    public static final int LocalStorageRequestCode = 2;

    public static boolean checkForPermissions(Activity activity, String[] permissionNames,
                                              int requestCode) {
        Integer[] permissions = new Integer[permissionNames.length];
        ArrayList<String> notPermitted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = ActivityCompat.checkSelfPermission(activity, permissionNames[i]);
            if (permissions[i] != PackageManager.PERMISSION_GRANTED)
                notPermitted.add(permissionNames[i]);
        }

        if (notPermitted.size() == 0)
            return true;

        ActivityCompat.requestPermissions(activity, notPermitted.toArray(new String[0]), requestCode);

        return false;
    }

    public static boolean gpsIsEnabled(Context context)
    {
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
