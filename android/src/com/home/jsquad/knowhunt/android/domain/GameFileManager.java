package com.home.jsquad.knowhunt.android.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.osmdroid.bonuspack.overlays.Marker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GameFileManager {

    private static final String LOCAL_EXCURSIONS_DIR = "excursions";
    private static final CharSequence FOLDER_SEPARATOR = "/";
    private static final String XML_EXTENSION = ".xml";
    private static final String POINT_NAMES_SUFFIX = "_point_names";
    private static final String LOCAL_AUDIO_FOLDER_NAME = "audio";
    private static final String MP3_EXTENSION = ".mp3";


    private final Context context;
    private String language;

    public GameFileManager(Context context, String currentLanguage) {
        this.language = currentLanguage;
        this.context = context;
    }

    public String getExcursionLocalDir()
    {
        return TextUtils.join(FOLDER_SEPARATOR, new String[]{context.getFilesDir().getAbsolutePath(),
                LOCAL_EXCURSIONS_DIR, "tram7" });
    }

    public static String getAudioLocalDir(Context context)
    {
        return context.getResources().getResourceName(context.getResources().getIdentifier("t7_01_welcome", "raw", context.getPackageName()));

    }

    public String getRouteFileName() {
        String routeName = getExcursionLocalDir();
        return TextUtils.join(FOLDER_SEPARATOR, new String[]{ routeName, "tram7" + XML_EXTENSION });
    }

    public File getRouteFile() {
        return new File(getRouteFileName());
    }

    public String getPointNamesFileName() {
        String routeName = getExcursionLocalDir();
        return TextUtils.join(FOLDER_SEPARATOR, new String[]{ routeName, "tram7" + POINT_NAMES_SUFFIX + XML_EXTENSION });
    }

    public File getPointNamesFile() {
        return new File(getPointNamesFileName());
    }


    @NonNull
    public ArrayList<String> getTracksAtPoint(Route route, AudioPoint closestPoint) {
        ArrayList<String> fullNames = new ArrayList<>();
        for(String fileName : route.getAudiosForPoint(closestPoint))
            fullNames.add("android.resource://com.home.croaton.followme/raw/" + fileName);

        return fullNames;
    }

    public void specialSaveRouteToDisc(ArrayList<Marker> pointMarkers, Route route) {

        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(getExcursionLocalDir() + FOLDER_SEPARATOR + "tram7" + XML_EXTENSION);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RouteSerializer.serialize(route, fs);
    }
}
