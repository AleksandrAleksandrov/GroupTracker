package location.share.com.aleksandr.aleksandrov.sharelocation.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import location.share.com.aleksandr.aleksandrov.sharelocation.activities.MapsActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.TabActivityMain;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.InfoLocation;

/**
 * Created by Aleksandr on 10/12/2016.
 */

public class SendLocationService extends Service {

    public static final String SERVICE_TAG = "SendLocationService";

    private LocationManager locationManager;
    private SharedPreferences sharedPreferences;

    boolean gps_enabled = false;
    boolean network_enabled = false;

    long lastGpsTime;

    int timeForGPS_PROVIDER = 1;
    int timeForNETWORK_PROVIDER = 1;
    int meters = 0;
    InfoLocation infoLocation;
    MyTask mt;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SERVICE_TAG, "start");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);

        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * timeForNETWORK_PROVIDER, meters, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * timeForGPS_PROVIDER, meters, locationListener);

        String source = "";
        Location net_loc = null, gps_loc = null, finalLoc = null;
        if (gps_enabled)
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null && net_loc != null) {

            if (gps_loc.getAccuracy() >= net_loc.getAccuracy()) {
                finalLoc = gps_loc;
                source = "gps";
            }

            else {
                finalLoc = net_loc;
                source = "network";
            }

            // I used this just to get an idea (if both avail, its upto you which you want to take as I taken location with more accuracy)

        } else {

            if (gps_loc != null) {
                finalLoc = net_loc;
                source = "network";
            } else if (net_loc != null) {
                finalLoc = gps_loc;
                source = "gps";

            }
        }
        showLocation(finalLoc, source);


    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            String source = "";
            Location net_loc = null, gps_loc = null, finalLoc = null;
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                finalLoc = location;
                lastGpsTime = new Date().getTime();
                source = "gps";
            } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER) & (lastGpsTime + 1000 * timeForGPS_PROVIDER) < new Date().getTime()) {
                finalLoc = location;
                source = "network";
            }
            showLocation(finalLoc, source);
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderEnabled(String provider) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    private void showLocation(Location location, String source) {
        if (location == null) {
            return;
        }




//        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            Log.d("TAG", "gpsURL + lat=" + location.getLatitude()+"&lon="+location.getLongitude() +"&sp="+location.getSpeed());

            double roundLatitude = new BigDecimal(location.getLatitude()).setScale(7, RoundingMode.UP).doubleValue();
            double roundLongitude = new BigDecimal(location.getLongitude()).setScale(7, RoundingMode.UP).doubleValue();
            float roundSpeed = new BigDecimal(location.getSpeed()).setScale(2, RoundingMode.UP).floatValue();
            infoLocation = new InfoLocation(roundLatitude, roundLongitude, roundSpeed);

            Intent intent = new Intent(TabActivityMain.BROADCAST_ACTION);
            intent.putExtra(TabActivityMain.ZERO, "0");
            intent.putExtra(TabActivityMain.LATITUDE, infoLocation.getLatitude());
            intent.putExtra(TabActivityMain.LONGITUDE, infoLocation.getLongitude());
            intent.putExtra(TabActivityMain.SPEED, infoLocation.getSpeed());
            intent.putExtra("source", source);
            sendBroadcast(intent);

//            Intent intent1 = new Intent(MapsActivity.BROADCAST_ACTION);
//            intent1.putExtra(TabActivityMain.ZERO, "0");
//            intent1.putExtra(TabActivityMain.LATITUDE, infoLocation.getLatitude());
//            intent1.putExtra(TabActivityMain.LONGITUDE, infoLocation.getLongitude());
//            intent1.putExtra(TabActivityMain.SPEED, infoLocation.getSpeed());
//            sendBroadcast(intent1);
            mt = new MyTask();
            mt.setInfo(infoLocation);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    mt.run();
                }
            });
            th.start();
//        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
//            Log.d("TAG", "networkURL + lat=" + location.getLatitude()+"&lon="+location.getLongitude() +"&sp="+location.getSpeed());
//            double roundLatitude = new BigDecimal(location.getLatitude()).setScale(7, RoundingMode.UP).doubleValue();
//            double roundLongitude = new BigDecimal(location.getLongitude()).setScale(7, RoundingMode.UP).doubleValue();
//            float roundSpeed = new BigDecimal(location.getSpeed()).setScale(5, RoundingMode.UP).floatValue();
//            infoLocation = new InfoLocation(roundLatitude, roundLongitude, roundSpeed);
//
//            Intent intent = new Intent(TabActivityMain.BROADCAST_ACTION);
//            intent.putExtra(TabActivityMain.ZERO, "0");
//            intent.putExtra(TabActivityMain.LATITUDE, infoLocation.getLatitude());
//            intent.putExtra(TabActivityMain.LONGITUDE, infoLocation.getLongitude());
//            intent.putExtra(TabActivityMain.SPEED, infoLocation.getSpeed());
//            sendBroadcast(intent);
//
//            Intent intent1 = new Intent(MapsActivity.BROADCAST_ACTION);
//            intent1.putExtra(TabActivityMain.ZERO, "0");
//            intent1.putExtra(TabActivityMain.LATITUDE, infoLocation.getLatitude());
//            intent1.putExtra(TabActivityMain.LONGITUDE, infoLocation.getLongitude());
//            intent1.putExtra(TabActivityMain.SPEED, infoLocation.getSpeed());
//            sendBroadcast(intent1);
//            mt = new MyTask();
//            mt.setInfo(infoLocation);
//            Thread th = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mt.run();
//                }
//            });
//            th.start();
//        }

    }

    class MyTask implements Runnable {
        String message;
        InfoLocation infoLocation;
        public void setInfo(InfoLocation infoLocation) {
            this.infoLocation = infoLocation;
        }

        @Override
        public void run() {
            try {
                String param = Res.ID + "=" + sharedPreferences.getInt(Res.ID, 0) + "&" + Res.LATITUDE + "=" + infoLocation.getLatitude()+ "&" + Res.LONGITUDE + "=" + infoLocation.getLongitude() + "&" + Res.SPEED + "=" + infoLocation.getSpeed() + "&" + Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "");
                URI uri = new URI("http", Res.URL + param, null);
                URL url = uri.toURL();

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    message = "ok";
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    message+=inputLine;

                }
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SERVICE_TAG, "stop");
    }
}
