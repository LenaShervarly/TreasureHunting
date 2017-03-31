package com.home.jsquad.knowhunt.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="content")
public class GameContentDescription implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GameContentDescription createFromParcel(Parcel in) {
            return new GameContentDescription(in);
        }
        public GameContentDescription[] newArray(int size) {
            return new GameContentDescription[size];
        }
    };

    @Attribute(name="lang")
    private String lang;

    @Element(name="name")
    private String name;

    @Element(name="overview")
    private String overview;

    public GameContentDescription(){
        this("", "", "");
    }

    public GameContentDescription(String lang, String name, String overview) {
        this.lang = lang;
        this.name = name;
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(lang);
        out.writeString(name);
        out.writeString(overview);
    }

    public GameContentDescription(Parcel in) {
        lang = in.readString();
        name = in.readString();
        overview = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getLang() {
        return lang;
    }
}
