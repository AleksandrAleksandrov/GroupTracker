package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.services.SendLocationService;

/**
 * Created by Aleksandr on 10/13/2016.
 */

public class TabActivityMain extends FragmentActivity {

    TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    Switch onOffLocationSwitch;
    IntentFilter intentFilter;

    BroadcastReceiver broadcastReceiver;
    public final static String LATITUDE = "latitude", LONGITUDE = "longitude", SPEED = "speed";
    public final static String ZERO = "0";
    public final static String BROADCAST_ACTION = "com.aleksandr.aleksandrov.send.location.service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);


        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
        tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        onOffLocationSwitch = (Switch) findViewById(R.id.onOffLocation);



        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ZERO.equals(intent.getStringExtra(ZERO))) {
                    double latitude = intent.getDoubleExtra(LATITUDE, 0);
                    double longitude = intent.getDoubleExtra(LONGITUDE, 0);
                    float speed = (intent.getFloatExtra(SPEED, 0)*3600)/1000;
                    String source = (intent.getStringExtra("source"));
                    tvLocationGPS.setText(formatTime(latitude, longitude, speed) + source);
                }

            }
        };

        intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void onOffLocationListener(View view) {
        if (onOffLocationSwitch.isChecked()) {
            registerReceiver(broadcastReceiver, intentFilter);
            Intent intent = new Intent(this, SendLocationService.class);
            startService(intent);
        } else if (!onOffLocationSwitch.isChecked()) {
            unregisterReceiver(broadcastReceiver);

            Intent intent = new Intent(this, SendLocationService.class);
            stopService(intent);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format("Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT", location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
    }

    private String formatTime(double latitude, double longitude, float speed) {

        return String.format("Coordinates: lat = %1$.4f, lon = %2$.4f , speed = %3$.4f, time = %4$tF %4$tT", latitude, longitude, speed, new Date().getTime());
    }


    public void onClickLocationSettings(View view) {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

}
