package com.home.jsquad.knowhunt.android.maps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.home.jsquad.knowhunt.R;
import com.home.jsquad.knowhunt.android.math.Vector2;
import com.home.jsquad.knowhunt.android.audio.AudioPlaybackController;
import com.home.jsquad.knowhunt.android.domain.AudioPoint;
import com.home.jsquad.knowhunt.android.domain.Game;
import com.home.jsquad.knowhunt.android.domain.Point;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapHelper
{
    public static Marker putMarker(Context context, MapView map, GeoPoint position, int resourceId,
        float horizontalAnchor, float verticalAnchor, boolean isDraggable)
    {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(horizontalAnchor, verticalAnchor);
        marker.setDraggable(isDraggable);

        setMarkerIconFromResource(context, resourceId, marker);

        map.getOverlays().add(marker);
        map.invalidate();

        return marker;
    }

    public static void chooseBeautifulMapProvider(Context context, MapView map)
    {
        MapBoxTileSource tileSource = new MapBoxTileSource(context);
        map.setTilesScaledToDpi(true);
        tileSource.setMapboxMapid("mapbox.emerald");
        map.setTileSource(tileSource);
    }

    public static MyLocationNewOverlay addLocationOverlay(Context context, MapView map)
    {
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(context,
                new GpsMyLocationProvider(context), map);

        if (!locationOverlay.enableMyLocation())
            return null;

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_location);
        locationOverlay.setPersonIcon(icon);
        locationOverlay.setPersonHotspot(icon.getWidth() / 2, icon.getHeight());
        map.getOverlays().add(locationOverlay);

        return locationOverlay;
    }

    public static void drawRoute(Context context, MapView map, List<Point> points) {
        Polyline line = new Polyline(context);

        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(15f);
        line.setColor(ContextCompat.getColor(context, R.color.orange_partially_transparent));

        List<GeoPoint> geoPoints = new ArrayList<>();

        for(Point point : points) {

//            Marker marker = new Marker(map);
//            marker.setPosition(point.Position);
//            marker.setAnchor(0.5f, 1f);
//
//            setMarkerIconFromResource(context, R.drawable.bonuspack_bubble, marker);
//            marker.setTitle(Integer.toString(point.Number));
//            marker.setDraggable(true);
//            map.getOverlays().add(marker);
            geoPoints.add(point.Position);
        }

        line.setPoints(geoPoints);
        line.setGeodesic(true);
        map.getOverlayManager().add(line);
    }

    public static void drawDirections(Context context, MapView map, List<AudioPoint> points) {
        for(AudioPoint point : points) {
            Polyline line = new Polyline(context);
            line.setSubDescription(Polyline.class.getCanonicalName());
            line.setWidth(3f);
            line.setColor(0xFFFF0050);
            ArrayList<GeoPoint> tmp = new ArrayList<>();
            tmp.add(point.Position);
            tmp.add(new Vector2(point.Position).add(point.Direction.mult(0.0005)).toGeoPoint());
            line.setPoints(tmp);
            map.getOverlayManager().add(line);
        }
    }

    public static void drawAudioPoints(Context context, MapView map, AudioPlaybackController controller,
                                       Game excursion, List<Marker> markers) {
        final boolean isDebug = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        for(AudioPoint point : excursion.getAudioPoints())
        {
            boolean isPointPassed = controller.isAudioPointPassed(point.Number);
            int resId = isPointPassed
                    ? R.drawable.passed
                    : R.drawable.game_point_big;

            float anchor = isPointPassed
                    ? Marker.ANCHOR_CENTER
                    : Marker.ANCHOR_BOTTOM;

            Marker marker = putMarker(context, map, point.Position, resId, Marker.ANCHOR_CENTER, anchor, isDebug);
            markers.add(marker);

        }
    }

    public static void focusCameraOnPoint(MapView map, Point point) {
        IMapController mapController = map.getController();
        mapController.setZoom(16);
        mapController.setCenter(point.Position);
    }

   /*public static void setStartRouteIcon(MapsActivity context, MapView map, GeoPoint position) {
        Marker marker = putMarker(context, map, position, R.drawable.start, 0.15f, 0.9f, false);
        makeMarkerNotClickable(marker);
    }

    public static void setEndRouteIcon(MapsActivity context, MapView map, GeoPoint position) {
        Marker marker = putMarker(context, map, position, R.drawable.finish, 0.15f, 0.9f, false);
        makeMarkerNotClickable(marker);
    }*/

    public static void makeMarkerNotClickable(Marker marker)
    {
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                return true;
            }
        });
    }

    public static void setMarkerIconFromResource(Context context, int resourceId, Marker marker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            marker.setIcon(context.getResources().getDrawable(resourceId, null));
        }
        else
        {
            marker.setIcon(context.getResources().getDrawable(resourceId));
        }
    }


}
