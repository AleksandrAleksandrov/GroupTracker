package location.share.com.aleksandr.aleksandrov.sharelocation.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.services.SendLocationService;

/**
 * Created by Aleksandr on 12/20/2016.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView person_name_on_navigation_view;
    LinearLayout navigation_drawer_header_container;
    SwitchCompat nav_share_location_switch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        DrawerLayout drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.navigation_view, null);
        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);


        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

         if (id == R.id.nav_share_switch_item) {
            if (nav_share_location_switch.isChecked()){
                Log.d("SendLocationService", "true");
                nav_share_location_switch.setChecked(false);
                stopService(new Intent(this, SendLocationService.class));
            } else {
                Log.d("SendLocationService", "false");
                nav_share_location_switch.setChecked(true);
                startService(new Intent(this, SendLocationService.class));
            }
            return false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        person_name_on_navigation_view = (TextView) findViewById(R.id.name_of_person_in_navigation_view);
        nav_share_location_switch = (SwitchCompat) findViewById(R.id.nav_share_switch);

//        if (isMyServiceRunning(SendLocationService.class)) {
//            nav_share_location_switch.setChecked(true);
//        }
//
//        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);
//
//        person_name_on_navigation_view.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_FIO, ""));
        navigation_drawer_header_container = (LinearLayout) findViewById(R.id.navigation_drawer_header_container);
        navigation_drawer_header_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), MyProfileActivity.class));
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
