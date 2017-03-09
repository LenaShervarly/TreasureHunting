package com.home.croaton.followme.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.home.croaton.followme.R;
import com.home.croaton.followme.instrumentation.App;

import org.osmdroid.util.GeoPoint;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "excursionBrief")
public class ExcursionBrief implements Parcelable, IExcursionBrief {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ExcursionBrief createFromParcel(Parcel in) {
            return new ExcursionBrief(in);
        }

        public ExcursionBrief[] newArray(int size) {
            return new ExcursionBrief[size];
        }
    };

    @ElementList(name = "content", inline = true)
    private List<ExcursionBriefContent> contentByLanguage;

    @Attribute(required=false, name = "useDirections")
    private Boolean useDirections;

    @Attribute(name = "key")
    private String key;

    @Attribute(name = "thumbnailFilePath")
    private String thumbnailFilePath;


    @ElementList(name = "area")
    private List<SerializableGeoPoint> area;

    private boolean mPurchased = false;

    private String mStringCost;

    public ExcursionBrief() {
        this("", "", new ArrayList<SerializableGeoPoint>());
    }

    public ExcursionBrief(
            String key,
            String thumbnailFilePath,
            //List<ExcursionBriefContent> contentByLanguage,
            //boolean useDirections,
            List<SerializableGeoPoint> area) {
        this.key = key;
        this.thumbnailFilePath = thumbnailFilePath;
        //this.contentByLanguage = contentByLanguage;  new ArrayList<ExcursionBriefContent>()
        //this.useDirections = useDirections;
        this.area = area;
    }

    private ExcursionBrief(Parcel in) {
        key = in.readString();
        thumbnailFilePath = in.readString();
        //useDirections = in.readByte() != 0;
        //contentByLanguage = new ArrayList<>();
        in.readList(contentByLanguage, ExcursionBriefContent.class.getClassLoader());
        area = new ArrayList<>();
        in.readList(area, GeoPoint.class.getClassLoader());
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getThumbnailFilePath() {
        return thumbnailFilePath;
    }


    @Override
    public ExcursionBriefContent getContentByLanguage(String language) {
        for (ExcursionBriefContent content : contentByLanguage) {
            if (content.getLang().equals(language))
                return content;
        }

        return null;
    }

    @Override
    public boolean getUseDirections() {
        return useDirections;
    }

    @Override
    public List<SerializableGeoPoint> getArea() {
        return area;
    }

    public boolean getPurchased() {
        return mPurchased;
    }

    public void setPurchased(boolean purchased) {
        this.mPurchased = purchased;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(key);
        out.writeString(thumbnailFilePath);
        out.writeList(contentByLanguage);
        out.writeList(area);
    }
}
