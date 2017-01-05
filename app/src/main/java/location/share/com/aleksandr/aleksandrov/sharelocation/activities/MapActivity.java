package location.share.com.aleksandr.aleksandrov.sharelocation.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.authorization.AuthorizationActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UserInfo;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UsersLocation;
import location.share.com.aleksandr.aleksandrov.sharelocation.friends_activity.FriendsActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.services.GetLocationFromTheServerService;
import location.share.com.aleksandr.aleksandrov.sharelocation.services.SendLocationService;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private MyTask myTask;
    private HashMap<Integer, UserInfo> userInfoHashMap = new HashMap<>();

//    private TextView person_name_on_navigation_view;
//    private LinearLayout navigation_drawer_header_container;
//    private SwitchCompat nav_share_location_switch;


    private BroadcastReceiver broadcastReceiverForGroupLocation;
    private IntentFilter intentFilterForGroupLocation;
    public final static String BROADCAST_ACTION_FOR_GET_GROUP_LOCATION = "get_group_location";

    private SharedPreferences sharedPreferences;

    private String LOG_TAG = "MyLogTag";
    private String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        Log.d(LOG_TAG, "onCreate");


        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);


        if (sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "").equals("")){
            startActivity(new Intent(getBaseContext(), AuthorizationActivity.class));
            return;
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        makeTheMap();

    }

    private void makeTheMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        broadcastReceiverForGroupLocation = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<UsersLocation> usersLocations = (ArrayList<UsersLocation>) intent.getSerializableExtra(Res.ARRAY_LIST_USERS);

                for (UsersLocation u : usersLocations) {
                    LatLng coordinates = new LatLng(u.getLatitude(), u.getLongitude());

                    if (userInfoHashMap.size() < usersLocations.size()) {


                        UserInfo userInfo = new UserInfo();
                        userInfo.setMarker(mMap.addMarker(new MarkerOptions().position(coordinates)));
                        userInfo.setId(u.getId());
                        userInfo.setLatLng(coordinates);
                        userInfo.setSpeed(u.getSpeed());
                        userInfoHashMap.put(userInfo.getId(), userInfo);

                        if (userInfoHashMap.get(userInfo.getId()).getName() == null) {
                            myTask = new MyTask();
                            myTask.execute(userInfo.getId());

                        }

                    } else {
                        userInfoHashMap.get(u.getId()).setLatLng(coordinates);
                        userInfoHashMap.get(u.getId()).setSpeed(u.getSpeed());
                    }
                    if (userInfoHashMap.get(u.getId()).getMarker().isInfoWindowShown()) {
                        userInfoHashMap.get(u.getId()).getMarker().showInfoWindow();
                    }
                    animateMarker(userInfoHashMap.get(u.getId()).getMarker(), coordinates, false);
                }
            }


        };

        intentFilterForGroupLocation = new IntentFilter(BROADCAST_ACTION_FOR_GET_GROUP_LOCATION);
        registerReceiver(broadcastReceiverForGroupLocation, intentFilterForGroupLocation);
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed/ duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
        if (sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "").equals("")){
            startActivity(new Intent(getBaseContext(), AuthorizationActivity.class));
        } else {
            makeTheMap();
//            person_name_on_navigation_view.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_FIO, ""));
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        return true;
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_hybrid && mMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (id == R.id.nav_terrian && mMap.getMapType() != GoogleMap.MAP_TYPE_TERRAIN) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        if (id == R.id.nav_share_switch_item) {
            if (nav_share_location_switch.isChecked()){
                nav_share_location_switch.setChecked(false);
                stopService(new Intent(this, SendLocationService.class));
            } else {
                nav_share_location_switch.setChecked(true);
                startService(new Intent(this, SendLocationService.class));
            }
            return true;
        } else if (id == R.id.nav_friends) {

            Intent friendsIntent = new Intent(this, FriendsActivity.class);
            startActivity(friendsIntent);
        } else if (id == R.id.nav_messages) {
            startActivity(new Intent(this, MessagesListActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    public void onDestroy() {

        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        stopService(new Intent(this, GetLocationFromTheServerService.class));
//        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiverForGroupLocation);
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    public void onClickGetLocation(View view) {
        startService(new Intent(this, GetLocationFromTheServerService.class));
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for (Map.Entry<Integer, UserInfo> us : userInfoHashMap.entrySet()) {
                    if (us.getValue().getMarker().getId().equals(marker.getId())) {
                        if (us.getValue().getName() == null) {
//                            marker.setTitle("loading");
//                            marker.showInfoWindow();
                            myTask = new MyTask();
                            Log.d("myMarker", us.getKey().toString());
                            myTask.execute(us.getKey());
                        }

                    }
                }
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(getBaseContext(), marker.getId(), Toast.LENGTH_SHORT).show();
                for (Map.Entry<Integer, UserInfo> us : userInfoHashMap.entrySet()) {
                    if (us.getValue().getMarker().getId().equals(marker.getId())) {
                        Intent intentProf = new Intent(getBaseContext(), ProfileActivity.class);
                        intentProf.putExtra("users_name", us.getValue().getName());
                        startActivity(intentProf);
                    }
                }

            }
        });

        startService(new Intent(this, GetLocationFromTheServerService.class));
    }



    class MyTask extends AsyncTask<Integer, UserInfo, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            for (Integer integer : params) {
                UserInfo userInfo = new UserInfo();

                try {
                    URI uri = new URI("http", Res.GET_OBJECT_INFO + Res.ID + "=" + integer + "&" + Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, ""), null);
                    URL url = uri.toURL();
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Log.d("get", "success");
                    }
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    JSONArray jsonArray = new JSONArray(result.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
//                        Log.d("myMarker", "fio=" + obj.getString(Res.NAME) + "/speed=" + obj.getDouble(Res.SPEED));
                        userInfo.setId(integer);
                        userInfo.setName(obj.getString(Res.FIO));
//                        userInfo.setSpeed(obj.getDouble(Res.SPEED));

                    }
                    publishProgress(userInfo);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(UserInfo... userInfos) {
            super.onProgressUpdate(userInfos);
            for (UserInfo userInfo : userInfos) {
//                userInfoHashMap.get(userInfo.getId()).getMarker().setTitle(userInfo.getName());
                userInfoHashMap.get(userInfo.getId()).setName(userInfo.getName());
//                userInfoHashMap.get(userInfo.getId()).setSpeed(userInfo.getSpeed());
                userInfoHashMap.get(userInfo.getId()).getMarker().showInfoWindow();
            }
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View v;
        private String url = "dr.png";
        private Uri imgUri;
//        HashMap<Integer, UserInfo> userInfoHashMap1 = new HashMap<>();

        MyInfoWindowAdapter() {
//            this.userInfoHashMap1 = userInfoHashMap1;
            v = getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            ImageView icon = (ImageView) v.findViewById(R.id.badge);
            TextView titleCustomWindow = (TextView) v.findViewById(R.id.info_window_title);
            TextView speedCustomWindow = (TextView) v.findViewById(R.id.info_window_snippet);
            for (Map.Entry<Integer, UserInfo> us : userInfoHashMap.entrySet()) {
                if (us.getValue().getMarker().getId().equals(marker.getId())) {
                    icon.setImageResource(R.drawable.profile);
                    speedCustomWindow.setText(getString(R.string.speed) + ": " + us.getValue().getSpeed() + " " + getString(R.string.km_per_hour));
                    titleCustomWindow.setText(us.getValue().getName());
                }
            }




            // set some bitmap to the imageview
//            imgUri = Uri.parse(url);
//            new DownloadImageTask(icon).execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");

            return v;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
