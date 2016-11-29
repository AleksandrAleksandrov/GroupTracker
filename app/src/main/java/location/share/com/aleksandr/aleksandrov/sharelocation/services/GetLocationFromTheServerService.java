package location.share.com.aleksandr.aleksandrov.sharelocation.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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

import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.MapsActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UsersLocation;

/**
 * Created by Aleksandr on 10/18/2016.
 */

public class GetLocationFromTheServerService extends Service {

    ArrayList<UsersLocation> alUsers;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                    sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);
                    alUsers = new ArrayList<>();
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
                                UsersLocation usersLocation = new UsersLocation(obj.getInt(Res.ID), obj.getDouble(Res.LATITUDE), obj.getDouble(Res.LONGITUDE), obj.getDouble(Res.SPEED));
                                Log.d("myMap", "" + obj.getInt(Res.ID)+ obj.getDouble(Res.LATITUDE) + obj.getDouble(Res.LONGITUDE)+ obj.getDouble(Res.SPEED));
                                alUsers.add(usersLocation);
                                intent = new Intent(MapsActivity.BROADCAST_ACTION_FOR_GET_GROUP_LOCATION);
                            }

                        }
                        intent.putExtra(Res.ARRAY_LIST_USERS, alUsers);
                        sendBroadcast(intent);
                        stopSelf();
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
        });
        th.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
