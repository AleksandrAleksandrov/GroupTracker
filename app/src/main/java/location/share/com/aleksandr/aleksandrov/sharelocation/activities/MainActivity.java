package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TabHost;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 10/12/2016.
 */

public class MainActivity extends TabActivity {

    public static final String MAIN_TAG_FO_TAB = "tag_main";
    public static final String MAP_TAG_FO_TAB = "tag_map";
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;
    public final static String BROADCAST_ACTION = "com.aleksandr.aleksandrov.sharelocation.activities.mainactivity";
    public final static String ON_OFF_SCREEN = "on_off_screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(ON_OFF_SCREEN, true)) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (!intent.getBooleanExtra(ON_OFF_SCREEN, true)) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        };

        intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        //Get TabHost.
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec(MAIN_TAG_FO_TAB);
        tabSpec.setIndicator(getString(R.string.main_tab));
        tabSpec.setContent(new Intent(this, TabActivityMain.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(MAP_TAG_FO_TAB);
        tabSpec.setIndicator(getString(R.string.map_tab));
        tabSpec.setContent(new Intent(this, MapsActivity.class));
        tabHost.addTab(tabSpec);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
