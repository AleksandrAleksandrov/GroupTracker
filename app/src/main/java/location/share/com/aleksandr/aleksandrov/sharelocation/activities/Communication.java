package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import location.share.com.aleksandr.aleksandrov.sharelocation.Res;

/**
 * Created by Aleksandr on 12/2/2016.
 */

public class Communication implements Runnable {
    SharedPreferences sharedPreferences;

    public Communication(Context context) {
        sharedPreferences = context.getSharedPreferences(Res.PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    public void getMyInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String param = Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "") + "&" + Res.USER_NAME + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, "");
            URI uri = new URI("http", Res.GET_MY_INFO + param, null);
            URL url2 = uri.toURL();

            HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line2 = null;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                while ((line2 = bufferedReader.readLine()) != null) {
                    result.append(line2);
                }
                JSONObject obj = new JSONObject(result.toString());

                // Проверка для отсеивания своих данных

                editor.putInt(Res.SHARED_PREFERENCES_ID, obj.getInt(Res.ID));
                editor.putString(Res.SHARED_PREFERENCES_EMAIL, obj.getString(Res.EMAIL));
                editor.putString(Res.SHARED_PREFERENCES_FIO, obj.getString(Res.FIO));
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
