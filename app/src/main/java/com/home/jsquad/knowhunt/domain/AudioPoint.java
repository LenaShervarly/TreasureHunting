package com.home.jsquad.knowhunt.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.home.jsquad.knowhunt.math.Vector2;

import org.osmdroid.util.GeoPoint;

public class AudioPoint extends Point implements Cloneable, Parcelable
{
    public static final Creator<AudioPoint> CREATOR = new Creator<AudioPoint>() {
        @Override
        public AudioPoint createFromParcel(Parcel in) {
            return new AudioPoint(in);
        }

        @Override
        public AudioPoint[] newArray(int size) {
            return new AudioPoint[size];
        }
    };

    public Integer Radius;
    public Vector2 Direction;

    public AudioPoint(int number, GeoPoint position, int radius)
    {
        super(0, position);
        Number = number;
        Radius = radius;
    }

    public AudioPoint(int number, GeoPoint position, int radius, Vector2 direction)
    {
        super(0, position);
        Number = number;
        Radius = radius;
        Direction = direction;
    }

    public AudioPoint(Parcel in) {
        super(in);
        Radius = in.readInt();
        Direction = in.readParcelable(Vector2.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(Radius);
        dest.writeParcelable(Direction, flags);
    }

    @Override
    public Object clone() {
        return new AudioPoint(Number, Position.clone(), Radius, Direction);
    }
}
