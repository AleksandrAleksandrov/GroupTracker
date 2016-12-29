package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.MyProfileInfo;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.Person;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UserInfo;

/**
 * Created by Aleksandr on 12/2/2016.
 */

public class Communication  {
    SharedPreferences sharedPreferences;

    public Communication(Context context) {
        sharedPreferences = context.getSharedPreferences(Res.PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    public List<UserInfo> getUserInfoByPhone(List<Person> personList) {
        List<UserInfo> userInfoList = new ArrayList<>();

        for (Person person : personList) {

                for (int i = 0; i < person.getNumber().size(); i++) {
                    try {
                        URL url = new URL(Res.PROTOCOL_SCHEME
                                + Res.GET_USER_INFO_BY_PHONE
                                + Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "") + "&"
                                + Res.PHONE + "=" + person.getNumber().get(i));

                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            String line;
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            StringBuffer result = new StringBuffer();
                            while ((line = bufferedReader.readLine()) != null) {
                                result.append(line);
                            }
                            JSONObject obj = new JSONObject(result.toString());
                            UserInfo userInfo = new UserInfo();
                            userInfo.setId(obj.getInt(Res.ID));
                            userInfo.setName(obj.getString(Res.USERNAME));
                            userInfo.setFio(person.getName());
                            userInfoList.add(userInfo);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


        }
        return userInfoList;
    }

    public MyProfileInfo getMyInfo() {
        Editor editor = sharedPreferences.edit();
        MyProfileInfo myProfileInfo = null;
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.GET_MY_INFO
                    + Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "") + "&"
                    + Res.USER_NAME + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, ""));

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuffer result = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                JSONObject obj = new JSONObject(result.toString());

                // Проверка для отсеивания своих данных

                editor.putInt(Res.SHARED_PREFERENCES_ID, obj.getInt(Res.ID));
                editor.putString(Res.SHARED_PREFERENCES_EMAIL, obj.getString(Res.EMAIL));
                editor.putString(Res.SHARED_PREFERENCES_FIO, obj.getString(Res.FIO));
                editor.putString(Res.SHARED_PREFERENCES_PHONE_NUMBER, obj.getString(Res.PHONE));
                editor.commit();

                myProfileInfo = new MyProfileInfo(obj.getInt(Res.ID), obj.getString(Res.FIO), obj.getString(Res.EMAIL), obj.getString(Res.PHONE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myProfileInfo;
    }

    public MyProfileInfo setMyInfo(String fio, String email, String phoneNumber) {
        Editor editor = sharedPreferences.edit();
        MyProfileInfo myProfileInfo = new MyProfileInfo();
        try {
            URL url = new URL(Res.PROTOCOL_SCHEME
                    + Res.SET_MY_INFO
                    + Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "") + "&"
                    + Res.FIO + "=" + URLEncoder.encode(fio, "UTF-8") + "&"
                    + Res.EMAIL + "=" + email + "&"
                    + Res.PHONE + "=" + phoneNumber);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line;
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuffer result = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                if (result.toString().equals("0")) {
                    editor.putString(Res.SHARED_PREFERENCES_FIO, fio);
                    editor.putString(Res.SHARED_PREFERENCES_EMAIL, email);
                    editor.putString(Res.SHARED_PREFERENCES_PHONE_NUMBER, phoneNumber);
                    editor.commit();

                    myProfileInfo.setFio(fio);
                    myProfileInfo.setEmail(email);
                    myProfileInfo.setPhone(phoneNumber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myProfileInfo;
    }
}
