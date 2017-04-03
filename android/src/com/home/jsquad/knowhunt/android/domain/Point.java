package com.home.jsquad.knowhunt.android.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;

public class Point implements Cloneable, Parcelable
{
    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public Integer Number;
    public GeoPoint Position;

    public Point(int number, GeoPoint position)
    {
        Number = number;
        Position = position;
    }

    protected Point(Parcel in) {
        Number = in.readInt();
        Position = in.readParcelable(GeoPoint.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Point)) {
            return false;
        }

        Point point = (Point) o;

        return point.Position == Position;
    }

    @Override
    public int hashCode()
    {
        return Position.hashCode();
    }

    @Override
    public Object clone() {
        return new Point(Number, Position.clone());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Number);
        dest.writeParcelable(Position, flags);
    }
}
