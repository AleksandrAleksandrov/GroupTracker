package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

/**
 * Created by Aleksandr on 10/18/2016.
 */

public class UsersLocation implements Serializable {

    private int id;
    private String name = "";
    private double latitude;
    private double longitude;
    private double speed;
    private Marker marker;
    private MarkerOptions markerOptions;


    public UsersLocation(int id, double latitude, double longitude, double speed) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        marker = null;
        markerOptions = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }
}
