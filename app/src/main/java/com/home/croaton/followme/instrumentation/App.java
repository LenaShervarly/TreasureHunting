package com.home.croaton.followme.instrumentation;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
