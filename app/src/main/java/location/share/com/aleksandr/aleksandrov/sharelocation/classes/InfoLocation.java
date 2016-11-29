package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

/**
 * Created by Aleksandr on 10/12/2016.
 */

public class InfoLocation {
    private double latitude;
    private double longitude;
    private float speed;

    public InfoLocation(double latitude, double longitude, float speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public float getSpeed() {
        return speed;
    }
}
