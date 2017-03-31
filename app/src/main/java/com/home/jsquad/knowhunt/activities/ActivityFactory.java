package com.home.jsquad.knowhunt.activities;

import android.content.Context;
import android.content.Intent;

public class ActivityFactory {
    private int audioPointNumber;
    private Intent intent;

    public ActivityFactory(int audioPointNumber){
        this.audioPointNumber = audioPointNumber;
    }

    public Intent chooseActivity(Context context, int audioPointNumber){
        //String nameOfChosenActivity; To be used later on in case of choosing activity by name.

        switch (audioPointNumber % 5) {
            case 0:  intent = new Intent(context, Camera.class);
                break;
            case 1:  intent = new Intent(context, GuessMelodyActivity.class);
                break;
            case 2:  intent = new Intent(context, QuizzActivity.class);
                break;
            case 3:  intent = new Intent(context, Quizz4answersActivity.class);
                break;
            case 4:  intent = new Intent(context, GuessMelodyActivity.class);
                break;
            case 5:  intent = new Intent(context, GuessMelodyActivity.class);
                break;
            default: intent = new Intent(context, GuessMelodyActivity.class);
                break;
    }
        return intent;
    }
}
