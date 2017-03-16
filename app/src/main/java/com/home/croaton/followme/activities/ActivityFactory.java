package com.home.croaton.followme.activities;

import android.content.Context;
import android.content.Intent;

public class ActivityFactory {
    private int audioPointNumber;
    private Intent intent;

    public ActivityFactory(int audioPointNumber){
        this.audioPointNumber = audioPointNumber;
    }

    public Intent chooseActivity(Context context, int audioPointNumber){
        String path = "android.activities://com.home.croaton.followme/activities/";
        String nameOfChosenActivity;

        switch (audioPointNumber) {
            case 0:  nameOfChosenActivity = "QuizzActivity";
                break;
            case 1:  nameOfChosenActivity = "QuizzActivity";
                break;
            default: nameOfChosenActivity = "QuizzActivity";
                break;
    }
        Class<?> c = null;
        try {
            c = Class.forName(path + nameOfChosenActivity);
            System.out.println(c.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        intent = new Intent(context, c);
        return intent;
    }
}
