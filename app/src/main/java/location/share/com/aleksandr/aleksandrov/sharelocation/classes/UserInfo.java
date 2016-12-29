package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 10/27/2016.
 */

public class UserInfo {
    private int id;
    private String name = null;
    private String fio;
    private LatLng latLng;
    private double latitude;
    private double longitude;
    private double speed;
    private Marker marker;
    private MarkerOptions markerOptions;

    public UserInfo() {

    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
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
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gt_2_42px));
        return marker;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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
