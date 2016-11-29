package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

        LoadFrends loadFrends = new LoadFrends();
        loadFrends.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
        Intent profileIntenet = new Intent(getActivity(), ProfileActivity.class);
        profileIntenet.putExtra("users_name", getListView().getItemAtPosition(position).toString());
        startActivity(profileIntenet);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class LoadFrends extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                URI uri = new URI("http", Res.GET_OBJECTS + Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, ""), null);
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
                Intent intent = null;
                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    // Проверка для отсеивания своих данных
                    if (obj.getInt(Res.ID) != sharedPreferences.getInt(Res.SHARED_PREFERENCES_ID, 0)) {
                        arrayList.add(obj.getString(Res.ID));
                    }

                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int ii = 0;
            data = new String[arrayList.size()];
            for (String integer : arrayList) {

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

                        data[ii] = obj.getString(Res.FIO);
//                        userInfo.setSpeed(obj.getDouble(Res.SPEED));

                    }

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ii ++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyListAdapter adapter = new MyListAdapter(getActivity().getBaseContext(), R.layout.fragment_list_item_for_friends, data);
            setListAdapter(adapter);
        }
    }
 }

