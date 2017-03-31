package com.home.jsquad.knowhunt.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class TrackNames implements Parcelable {
    public static final Creator<TrackNames> CREATOR = new Creator<TrackNames>() {
        @Override
        public TrackNames createFromParcel(Parcel in) {
            return new TrackNames(in);
        }

        @Override
        public TrackNames[] newArray(int size) {
            return new TrackNames[size];
        }
    };

    private HashMap<String, HashMap<String, String>> trackNames;

    public TrackNames(HashMap<String, HashMap<String, String>> trackNames) {
        this.trackNames = trackNames;
    }

    public String getTrackName(String language, String fileName) {
        return trackNames.get(language).get(fileName);
    }

    protected TrackNames(Parcel in) {
        int size = in.readInt();
        trackNames = new HashMap<>(size);
        for(int i = 0; i < size; i++) {

            String lang = in.readString();
            int innerSize = in.readInt();

            HashMap<String, String> trackNamePairs = new HashMap<>(innerSize);
            trackNames.put(lang, trackNamePairs);

            for(int j = 0; j < innerSize; j++) {
                trackNamePairs.put(in.readString(), in.readString());
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(trackNames.size());
        for (Map.Entry<String, HashMap<String, String>> entry : trackNames.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeInt(entry.getValue().size());
            for (Map.Entry<String, String> stringPair : entry.getValue().entrySet()) {
                dest.writeString(stringPair.getKey());
                dest.writeString(stringPair.getValue());
            }
        }
    }
}
