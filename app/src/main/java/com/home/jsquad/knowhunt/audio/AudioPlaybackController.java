package com.home.jsquad.knowhunt.audio;

import android.content.Context;
import android.content.Intent;

import com.home.jsquad.knowhunt.domain.AudioPoint;
import com.home.jsquad.knowhunt.domain.IGame;
import com.home.jsquad.knowhunt.location.LocationHelper;
import com.home.jsquad.knowhunt.math.Vector2;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// ToDo: split excursion scenarios and logic in different classes.
public class AudioPlaybackController {
    private static final CharSequence FOLDER_SEPARATOR = "/";
    private IGame excursion;
    private String language;
    private Set<AudioPoint> banned;
    private ArrayList<String> audioToPlay;

    public AudioPlaybackController(String language, IGame excursion) {
        this.excursion = excursion;
        this.language = language;
        banned = new HashSet<>();
    }

    public AudioPlaybackController(ArrayList<String> audioToPlay){
        this.audioToPlay = audioToPlay;
    }

    public static void stopAnyPlayback(Context context) {
        context.stopService(new Intent(context, AudioService.class));
    }

    public AudioPoint getResourceToPlay(final GeoPoint position) {
        return Collections.min(excursion.getRoute().audioPoints(), new Comparator<AudioPoint>() {
            @Override
            public int compare(AudioPoint left, AudioPoint right) {
                return Double.compare(left.Position.distanceTo(position), right.Position.distanceTo(position));
            }
        });
    }

    public AudioPoint getResourceToPlay(GeoPoint position, Vector2 direction) {
        float min = Integer.MAX_VALUE;
        AudioPoint closestPoint = null;

        cleanUpBanned(position);

        for (AudioPoint point : excursion.getRoute().audioPoints()) {
            if (excursion.getRoute().isAudioPointPassed(point.Number))
                continue;

            if (banned.contains(point))
                continue;

            float distance = LocationHelper.GetDistance(position, point.Position);
            if (distance < min && distance <= point.Radius)
            {
                    min = distance;
                    closestPoint = point;
            }
        }

        if (closestPoint == null)
            return null;

        return closestPoint;
    }

    private void cleanUpBanned(GeoPoint position) {
        for (Iterator<AudioPoint> i = banned.iterator(); i.hasNext();) {
            AudioPoint point = i.next();
            if (point.Position.distanceTo(position) > point.Radius)
                i.remove();
        }
    }

    public void markAudioPoint(int pointNumber, boolean passed) {
        excursion.getRoute().markAudioPoint(pointNumber, passed);
    }

    public boolean[] getDoneArray() {
        ArrayList<AudioPoint> audioPoints = excursion.getRoute().audioPoints();
        boolean[] doneIndicators = new boolean[audioPoints.size()];

        for (int i = 0; i < audioPoints.size(); i++)
            doneIndicators[i] = excursion.getRoute().isAudioPointPassed(i);

        return doneIndicators;
    }

    public void startPlaying(Context context, ArrayList<String> audioToPlay) {
        Intent startingIntent = new Intent(context, AudioService.class);
        startingIntent.putExtra(AudioService.Command, AudioServiceCommand.LoadTracks);
        startingIntent.putExtra(AudioService.NewTracks, audioToPlay);

        context.startService(startingIntent);
    }

    public boolean isAudioPointPassed(Integer number) {
        return excursion.getRoute().isAudioPointPassed(number);
    }

    public AudioPoint getFirstNotDoneAudioPoint() {
        for (AudioPoint audioPoint : excursion.getRoute().audioPoints()) {
            if (!excursion.getRoute().isAudioPointPassed(audioPoint.Number))
                return (AudioPoint) audioPoint.clone();
        }

        return null;
    }
}
