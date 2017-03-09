package com.home.croaton.followme.math;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.osmdroid.util.GeoPoint;

public class Vector2 implements Parcelable {
    public static final Creator<Vector2> CREATOR = new Creator<Vector2>() {
        @Override
        public Vector2 createFromParcel(Parcel in) {
            return new Vector2(in);
        }

        @Override
        public Vector2[] newArray(int size) {
            return new Vector2[size];
        }
    };
    private static final double EPSILON = 0e-10;

    public double X;
    public double Y;

    public Vector2(double x, double y)
    {
        X = x;
        Y = y;
    }

    protected Vector2(Parcel in) {
        X = in.readDouble();
        Y = in.readDouble();
    }

    public Vector2(GeoPoint begin, GeoPoint end) {
        X = end.getLatitude() - begin.getLatitude();
        Y = end.getLongitude() - begin.getLongitude();
    }

    public Vector2(GeoPoint point) {
        X = point.getLatitude();
        Y = point.getLongitude();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(X);
        dest.writeDouble(Y);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(X).append(Y).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2))
            return false;
        if (obj == this)
            return true;

        Vector2 rhs = (Vector2) obj;
        return new EqualsBuilder().append(X, rhs.X).append(Y, rhs.Y).isEquals();
    }

    public Vector2 normalize() {
        double length = Math.sqrt(X * X + Y * Y);

        if (length > EPSILON) {
            X /= length;
            Y /= length;
        }

        return this;
    }

    public boolean isZero() {
        return X < EPSILON && Y < EPSILON;
    }

    public GeoPoint toGeoPoint(){
        return new GeoPoint(X, Y);
    }

    public double dot(Vector2 other) {
        return X * other.X + Y * other.Y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(X + other.X, Y + other.Y);
    }

    public Vector2 mult(double multiplicator) {
        return new Vector2(X * multiplicator, Y * multiplicator);
    }
}
