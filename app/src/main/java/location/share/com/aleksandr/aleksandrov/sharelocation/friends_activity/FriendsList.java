package location.share.com.aleksandr.aleksandrov.sharelocation.friends_activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.ProfileActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.adapters.MyListAdapter;

/**
 * Created by Aleksandr on 11/7/2016.
 */

public class FriendsList extends ListFragment {

    String data[];
    ArrayList<String> arrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getActivity().getSharedPreferences(Res.PREFERENCE_KEY, Context.MODE_PRIVATE);

//        LoadFrends loadFrends = new LoadFrends();
//        loadFrends.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent profileIntenet = new Intent(getActivity(), ProfileActivity.class);
        profileIntenet.putExtra("users_name", getListView().getItemAtPosition(position).toString());
        startActivity(profileIntenet);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
 }

