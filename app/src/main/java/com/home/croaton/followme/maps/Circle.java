package com.home.croaton.followme.maps;

import android.content.Context;

import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;

public class Circle extends Polygon {

    private GeoPoint Center;
    private int Radius;

    public Circle(Context ctx, GeoPoint center, int radius)
    {
        super(ctx);

        if (center == null || radius <= 0)
            throw new IllegalArgumentException();

        Center = center;
        Radius = radius;

        this.setPoints(pointsAsCircle(Center, Radius));
    }

    public GeoPoint getCenter()
    {
        return new GeoPoint(Center);
    }

    public int getRadius()
    {
        return Radius;
    }

    public void setRadius(int radius)
    {
        Radius = radius;
        this.setPoints(pointsAsCircle(Center, Radius));
    }
}
