package location.share.com.aleksandr.aleksandrov.sharelocation.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.Communication;

/**
 * Created by Aleksandr on 11/1/2016.
 */

public class AuthorizationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_auth);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.authorization));

        viewPager = (ViewPager) findViewById(R.id.view_pager_auth);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_auth);
        tabLayout.setupWithViewPager(viewPager);

        //Заглушка для того чтобы когда пользователь уже ввел данные у него их уже не запрашивало
//        if (!sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, "").equals("") && !sharedPreferences.getString(Res.SHARED_PREFERENCES_PASSWORD, "").equals("")) {
//            myLoginAsyncTask = new MyLoginAsyncTask();
//            myLoginAsyncTask.execute(nick_name_et.getText().toString(), password_et.getText().toString());
//        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabsPagerAdapter.addFragment(new LogInFragment(), getString(R.string.authorization));
        tabsPagerAdapter.addFragment(new RegistrationFragment(), getString(R.string.create_a_new_account));
        viewPager.setAdapter(tabsPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK) {
            finish();
        }
    }
}
