package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Aleksandr on 10/19/2016.
 */

public class MarkerAndOptions {

    public int id;
    public Marker marker;
    private MarkerOptions markerOptions;

    public MarkerAndOptions(Marker marker) {

        this.marker = marker;
    }


    public int getId() {
        return id;
    }

    public Marker getMarker() {
        return marker;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }
}
