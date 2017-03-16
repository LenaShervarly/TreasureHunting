package com.home.croaton.followme.domain;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//import com.home.croaton.followme.R;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Root(name="content")
public class Game implements Parcelable, IGame {

    private static Context context;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    private static final String POINT_NAMES_SUFFIX = "_point_names";
    private static final String XML_EXTENSION = ".xml";
    private static final String MP3_EXTENSION = ".mp3";
    private static final CharSequence FOLDER_SEPARATOR = "/";
    private static final String LOCAL_AUDIO_FOLDER_NAME = "audio";

    private TrackNames trackNames;
    private GameContentDescription gameContentDescription;
    private Route route;

    @ElementList(name = "content", inline = true)
    private List<GameContentDescription> contentByLanguage;

    @Attribute(name = "key")
    private String key;

    @ElementList(name = "area")
    private List<SerializableGeoPoint> area;

    public Game(Context context) {
        this.context = context;
        loadRoute();
        tryLoad(context, "en", new GameFileManager(context, "en"));
    }

    protected Game(Parcel in) {

        trackNames = in.readParcelable(TrackNames.class.getClassLoader());
        route = in.readParcelable(Route.class.getClassLoader());

    }

    public Route getRoute() {
        return this.route;
    }


    public void loadRoute()  {
        route = RouteSerializer.deserializeFromResource(context.getResources(), context.getResources().getIdentifier("tram7", "raw", context.getPackageName()));
        //route.generateDirections();
    }

    private void loadTrackNames(File pointNamesFile) throws FileNotFoundException{
        ArrayList<File> temp = new ArrayList<>(1);
        temp.add(pointNamesFile);
        loadTrackNames(temp);
    }

    public void loadTrackNames(ArrayList<File> files) throws FileNotFoundException {
        trackNames =  new TrackNames(RouteSerializer.deserializeAudioPointNamesFromResource(context.getResources(), context.getResources().getIdentifier("game" + POINT_NAMES_SUFFIX, "raw", context.getPackageName())));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public GameContentDescription getContentByLanguage(String language) {
        for (GameContentDescription content : contentByLanguage) {
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


    public boolean tryLoad(Context context, String language, GameFileManager gameFileManager) {

        if (route == null) {
                loadRoute();
        }

        if (trackNames == null) {
            try {
                loadTrackNames(gameFileManager.getPointNamesFile());
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
            Uri filePath = Uri.parse("android.resource://com.home.croaton.followme/raw/" + filename);
            File file = new File(filePath.toString());
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
