package com.home.croaton.followme.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="content")
public class ExcursionBriefContent implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ExcursionBriefContent createFromParcel(Parcel in) {
            return new ExcursionBriefContent(in);
        }
        public ExcursionBriefContent[] newArray(int size) {
            return new ExcursionBriefContent[size];
        }
    };

    @Attribute(name="lang")
    private String lang;

    @Element(name="name")
    private String name;

    @Element(name="overview")
    private String overview;

    public ExcursionBriefContent(){
        this("", "", "");
    }

    public ExcursionBriefContent(String lang, String name, String overview) {
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

    public ExcursionBriefContent(Parcel in) {
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
