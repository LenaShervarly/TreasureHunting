package com.home.croaton.followme.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="geoPoint")
public class SerializableGeoPoint implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SerializableGeoPoint createFromParcel(Parcel in) {
            return new SerializableGeoPoint(in);
        }
        public SerializableGeoPoint[] newArray(int size) {
            return new SerializableGeoPoint[size];
        }
    };

    public SerializableGeoPoint(){
        this(0.0, 0.0);
    }

    public SerializableGeoPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(latitude);
        out.writeDouble(longitude);
    }

    public SerializableGeoPoint(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Attribute(name="latitude")
    private double latitude;

    @Attribute(name="longitude")
    private double longitude;

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public GeoPoint toGeoPoint()
    {
        return new GeoPoint(latitude, longitude);
    }
}
