package com.home.croaton.followme.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.home.croaton.followme.maps.Circle;
import com.home.croaton.followme.math.Vector2;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Route implements Parcelable {
    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    private ArrayList<AudioPoint> _audioPoints;
    private ArrayList<Point> _geoPoints;
    private ArrayList<Boolean> _passedPoints;
    private HashMap<Integer, ArrayList<String>> _pointTrackMapper;

    public Route()
    {
        _pointTrackMapper = new HashMap<>();
        _geoPoints = new ArrayList<>();
        _audioPoints = new ArrayList<>();
        _passedPoints = new ArrayList<>();
    }

    protected Route(Parcel in) {

        _audioPoints = new ArrayList<>();
        in.readList(_audioPoints, AudioPoint.class.getClassLoader());

        _geoPoints = new ArrayList<>();
        in.readList(_geoPoints, Point.class.getClassLoader());

        _passedPoints = new ArrayList<>();
        in.readList(_passedPoints, Boolean.class.getClassLoader());

        int size = in.readInt();
        _pointTrackMapper = new HashMap<>(size);
        for(int i = 0; i < size; i++)
        {
            int pointId = in.readInt();

            ArrayList<String> trackNames = new ArrayList<>();
            in.readList(trackNames, String.class.getClassLoader());

            _pointTrackMapper.put(pointId, trackNames);
        }
    }

    public void addGeoPoint(int number, GeoPoint position)
    {
        _geoPoints.add(new Point(number, position));
    }

    public void addAudioPoint(AudioPoint audioPoint)
    {
        _audioPoints.add(audioPoint);
        _passedPoints.add(false);
    }

    public ArrayList<Point> geoPoints()
    {
        return (ArrayList<Point>)_geoPoints.clone();
    }

    public ArrayList<AudioPoint> audioPoints()
    {
        return (ArrayList<AudioPoint>)_audioPoints.clone();
    }

    public void markAudioPoint(int pointNumber, boolean passed)
    {
        _passedPoints.set(pointNumber, passed);
    }

    public ArrayList<String> getAudiosForPoint(AudioPoint audioPoint)
    {
        return _pointTrackMapper.get(audioPoint.Number);
    }

    public void addAudioTrack(AudioPoint point, String fileName)
    {
        if (_pointTrackMapper.containsKey(point.Number))
        {
            _pointTrackMapper.get(point.Number).add(fileName);
            return;
        }

        ArrayList<String> audioFilesIds = new ArrayList<>();
        audioFilesIds.add(fileName);
        _pointTrackMapper.put(point.Number, audioFilesIds);
    }

    public void updateAudioPoints(ArrayList<Circle> circles, ArrayList<Marker> pointMarkers)
    {
        for(int i = 0; i < _audioPoints.size(); i++)
        {
            _audioPoints.get(i).Radius = circles.get(i).getRadius();
            _audioPoints.get(i).Position = pointMarkers.get(i).getPosition();
        }
    }

    public boolean isAudioPointPassed(Integer number) {
        return _passedPoints.get(number);
    }

    public String[] getAudioFileNames() {
        HashSet<String> iCreateHashSetInGetter = new HashSet<>();

        for(ArrayList<String> names : _pointTrackMapper.values())
        {
            for(String name : names)
                iCreateHashSetInGetter.add(name);
        }

        return iCreateHashSetInGetter.toArray(new String[iCreateHashSetInGetter.size()]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(_audioPoints);
        dest.writeList(_geoPoints);
        dest.writeList(_passedPoints);

        dest.writeInt(_pointTrackMapper.size());

        for (Map.Entry<Integer, ArrayList<String>> entry : _pointTrackMapper.entrySet()) {
            dest.writeInt(entry.getKey());
            dest.writeList(entry.getValue());
        }
    }

    // Works only if audio points are close to the route
    public void generateDirections() {
        if (_audioPoints.size() == 0)
            return;

        int audioPointIndex = 0;
        for(int i = 0; i < _geoPoints.size() - 1; i++) {

            GeoPoint current = _geoPoints.get(i).Position;
            GeoPoint next = _geoPoints.get(i + 1).Position;
            AudioPoint audioPoint = _audioPoints.get(audioPointIndex);

            while (current.distanceTo(next) > current.distanceTo(audioPoint.Position))
            {
                audioPoint.Direction = new Vector2(current, next).normalize();
                if (++audioPointIndex == _audioPoints.size())
                    return;

                audioPoint = _audioPoints.get(audioPointIndex);
            }
        }
    }
}
