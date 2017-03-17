package com.home.croaton.followme.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ActivityFactory {
    private int audioPointNumber;
    private Intent intent;

    public ActivityFactory(int audioPointNumber){
        this.audioPointNumber = audioPointNumber;
    }

    public Intent chooseActivity(Context context, int audioPointNumber){
        //String nameOfChosenActivity;

        switch (audioPointNumber) {
            case 0:  intent = new Intent(context, QuizzActivity.class);
                break;
            case 1:  intent = new Intent(context, GuessMelodyActivity.class);
                break;
            case 2:  intent = new Intent(context, QuizzActivity.class);
                break;
            case 3:  intent = new Intent(context, GuessMelodyActivity.class);
                break;
            case 4:  intent = new Intent(context, QuizzActivity.class);
                break;
            default: intent = new Intent(context, QuizzActivity.class);
                break;
    }
        /*Class<?> c = null;
        try {
            Uri filePath = Uri.parse("android:com.home.croaton.followme/activities/" + nameOfChosenActivity);
            c = Class.forName(filePath.toString());
            System.out.println(c.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        return intent;
    }
}
