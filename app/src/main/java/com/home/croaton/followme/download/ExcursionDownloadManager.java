package com.home.croaton.followme.download;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.home.croaton.followme.domain.AudioPoint;
import com.home.croaton.followme.domain.IExcursionBrief;
import com.home.croaton.followme.domain.Route;
import com.home.croaton.followme.domain.RouteSerializer;
import com.home.croaton.followme.maps.Circle;

import org.osmdroid.bonuspack.overlays.Marker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcursionDownloadManager {

    private static final String LOCAL_EXCURSIONS_DIR = "excursions";
    private static final CharSequence FOLDER_SEPARATOR = "/";
    private static final String XML_EXTENSION = ".xml";
    private static final String POINT_NAMES_SUFFIX = "_point_names";
    private static final String LOCAL_AUDIO_FOLDER_NAME = "audio";
    private static final String MP3_EXTENSION = ".mp3";

    private final IExcursionBrief excursionBrief;
    private final Context context;
    private String language;

    public ExcursionDownloadManager(Context context, IExcursionBrief brief, String currentLanguage) {
        this.excursionBrief = brief;
        this.language = currentLanguage;
        this.context = context;
    }

    public String getExcursionLocalDir()
    {
        return TextUtils.join(FOLDER_SEPARATOR, new String[]{context.getFilesDir().getAbsolutePath(),
                LOCAL_EXCURSIONS_DIR, excursionBrief.getKey() });
    }

    public static String getAudioLocalDir(Context context, String key, String language)
    {
        return TextUtils.join(FOLDER_SEPARATOR, new String[] {context.getFilesDir().getAbsolutePath(),
                LOCAL_AUDIO_FOLDER_NAME, key, language});
    }

    public String getRouteFileName() {
        String routeName = getExcursionLocalDir();
        return TextUtils.join(FOLDER_SEPARATOR, new String[]{ routeName, excursionBrief.getKey() + XML_EXTENSION });
    }

    public File getRouteFile() {
        return new File(getRouteFileName());
    }

    public String getPointNamesFileName() {
        String routeName = getExcursionLocalDir();
        return TextUtils.join(FOLDER_SEPARATOR, new String[]{ routeName, excursionBrief.getKey() + POINT_NAMES_SUFFIX + XML_EXTENSION });
    }

    public File getPointNamesFile() {
        return new File(getPointNamesFileName());
    }


    @NonNull
    public ArrayList<String> getTracksAtPoint(Route route, AudioPoint closestPoint) {
        ArrayList<String> fullNames = new ArrayList<>();
        for(String fileName : route.getAudiosForPoint(closestPoint))
            fullNames.add(getAudioLocalDir(context, excursionBrief.getKey(), language)
                    + FOLDER_SEPARATOR+ fileName + MP3_EXTENSION);

        return fullNames;
    }

    public void specialSaveRouteToDisc(ArrayList<Circle> circles, ArrayList<Marker> pointMarkers, Route route) {
        if (circles.size() > 0 && pointMarkers.size() > 0)
            route.updateAudioPoints(circles, pointMarkers);

        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(getExcursionLocalDir() + FOLDER_SEPARATOR + excursionBrief.getKey() + XML_EXTENSION);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RouteSerializer.serialize(route, fs);
    }
}
