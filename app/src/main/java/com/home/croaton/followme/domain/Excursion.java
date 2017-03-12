package com.home.croaton.followme.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

//import com.home.croaton.followme.R;
import com.home.croaton.followme.R;
import com.home.croaton.followme.download.ExcursionDownloadManager;

import org.apache.commons.io.IOUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Root(name="content")
public class Excursion implements Parcelable, IExcursion {

    private static Context context;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Excursion createFromParcel(Parcel in) {
            return new Excursion(in);
        }

        public Excursion[] newArray(int size) {
            return new Excursion[size];
        }
    };

    private static final String POINT_NAMES_SUFFIX = "_point_names";
    private static final String XML_EXTENSION = ".xml";
    private static final String MP3_EXTENSION = ".mp3";
    private static final CharSequence FOLDER_SEPARATOR = "/";
    private static final String LOCAL_AUDIO_FOLDER_NAME = "audio";

    private TrackNames trackNames;
    private ExcursionBriefContent excursionBriefContent;
    private Route route;

    @ElementList(name = "content", inline = true)
    private List<ExcursionBriefContent> contentByLanguage;

    @Attribute(name = "key")
    private String key;

    @ElementList(name = "area")
    private List<SerializableGeoPoint> area;

    public Excursion(Context context) {
        this.context = context;
        //excursionBrief = brief;
        loadRoute();
        //tryLoad(context, "ru", new ExcursionDownloadManager(context, excursionBrief, "ru"));
    }

    protected Excursion(Parcel in) {

        //excursionBrief = in.readParcelable(ExcursionBrief.class.getClassLoader());
        trackNames = in.readParcelable(TrackNames.class.getClassLoader());
        route = in.readParcelable(Route.class.getClassLoader());

    }

    public Route getRoute() {
        return this.route;
    }

    /*public void loadRoute(File routeFile) throws FileNotFoundException {
        ArrayList<File> temp = new ArrayList<>(1);
        temp.add(routeFile);
        loadRoute(temp);
    }*/

    public void loadRoute()  {

        route = RouteSerializer.deserializeFromResource(context.getResources(), context.getResources().getIdentifier("tram7", "raw", context.getPackageName()));

        //if (excursionBrief.getUseDirections())
            route.generateDirections();

    }

    private void loadTrackNames(File pointNamesFile) throws FileNotFoundException{
        ArrayList<File> temp = new ArrayList<>(1);
        temp.add(pointNamesFile);
        loadTrackNames(temp);
    }

    public void loadTrackNames(ArrayList<File> files) throws FileNotFoundException {
        String pointNamesFile = "tram7" + POINT_NAMES_SUFFIX + XML_EXTENSION;

        for(File maybePointNames : files)
        {
            if (maybePointNames.getName().equals(pointNamesFile))
            {
                FileInputStream stream = null;
                try {
                    stream = new FileInputStream(maybePointNames);
                    trackNames =  new TrackNames(RouteSerializer.deserializeAudioPointNames(stream));
                }
                finally {
                    IOUtils.closeQuietly(stream);
                }

                return;
            }
        }
        throw new FileNotFoundException("Couldn't find file: " + pointNamesFile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ExcursionBriefContent getContentByLanguage(String language) {
        for (ExcursionBriefContent content : contentByLanguage) {
            if (content.getLang().equals(language))
                return content;
        }

        return null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(trackNames, flags);
        dest.writeParcelable(route, flags);
    }

    public boolean tryLoad(Context context, String language, ExcursionDownloadManager downloadManager) {
        if (route == null) {
                loadRoute();
        }

        if (trackNames == null) {
            try {
                loadTrackNames(downloadManager.getPointNamesFile());
            } catch (FileNotFoundException ex) {
                return false;
            }
        }

        return audiosAreLoaded(route, context, language);
    }

    public boolean isLoaded(Context context, String language) {
        return route != null && trackNames != null && audiosAreLoaded(route, context, language);
    }

    public boolean audiosAreLoaded(Route route, Context context, String language) {
        for(String filename : route.getAudioFileNames())
        {
            File file = new File(TextUtils.join(FOLDER_SEPARATOR, new String[]{ ExcursionDownloadManager.getAudioLocalDir(context, "tram7", language), filename + MP3_EXTENSION}));
            if (!file.exists()) {
                Log.d("Follow Me", "Couldn't find file " + file.getAbsolutePath());
                return false;
            }
        }

        return true;
    }

    public TrackNames getTrackNames() {
        return trackNames;
    }

    public ArrayList<AudioPoint> getAudioPoints() {
        if (route != null)
            return route.audioPoints();

        return new ArrayList<>();
    }

    public ArrayList<Point> getGeoPoints() {
        if (route != null)
            return route.geoPoints();

        return new ArrayList<>();
    }

    @Override
    public String getKey() {
        return key;
    }

}
