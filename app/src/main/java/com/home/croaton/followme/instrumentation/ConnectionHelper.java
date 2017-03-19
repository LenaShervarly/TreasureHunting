package com.home.croaton.followme.instrumentation;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionHelper {
    public static boolean hasInternetConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
