package location.share.com.aleksandr.aleksandrov.sharelocation.friends_activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.ProfileActivity;

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

