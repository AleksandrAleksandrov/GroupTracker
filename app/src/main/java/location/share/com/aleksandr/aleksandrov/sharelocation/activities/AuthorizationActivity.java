package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UsersLocation;

/**
 * Created by Aleksandr on 11/1/2016.
 */

public class AuthorizationActivity extends AppCompatActivity {

    MyLoginAsyncTask myLoginAsyncTask;

    EditText nick_name_et, password_et;
    SharedPreferences sharedPreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_layout);




        nick_name_et = (EditText) findViewById(R.id.edit_text_input_nick_name);
        password_et = (EditText) findViewById(R.id.edit_text_input_password);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (nick_name_et.isSelected()) {
            imm.showSoftInput(nick_name_et, InputMethodManager.SHOW_IMPLICIT);
        }

        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);
        if (!sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, "").equals("")) {
            nick_name_et.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, ""));
        }
        if (!sharedPreferences.getString(Res.SHARED_PREFERENCES_PASSWORD, "").equals("")) {
            password_et.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_PASSWORD, ""));
        }


        //Заглушка для того чтобы когда пользователь уже ввел данные у него их уже не запрашивало
//        if (!sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, "").equals("") && !sharedPreferences.getString(Res.SHARED_PREFERENCES_PASSWORD, "").equals("")) {
//            myLoginAsyncTask = new MyLoginAsyncTask();
//            myLoginAsyncTask.execute(nick_name_et.getText().toString(), password_et.getText().toString());
//        }

    }

    public void onClickLogin(View view) {
        myLoginAsyncTask = new MyLoginAsyncTask();
        myLoginAsyncTask.execute(nick_name_et.getText().toString(), password_et.getText().toString());

    }

    public void onClickCreateNewAccount(View view) {
        startActivityForResult(new Intent(this.getBaseContext(), CreateNewAccountActivity.class), RESULT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK) {
            finish();
        }
    }

    class MyLoginAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String text = "";
            String line = null;
            BufferedReader reader=null;

            try
            {
                String data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(params[0], "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(params[1], "UTF-8");

                // Defined URL  where to send data
                URL url = new URL("http:" + Res.LOGIN);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                StringBuilder sb = new StringBuilder();


                // Read Server Response
                while((line = reader.readLine()) != null) {
                    text = line.replace("\"", "");
                    if (text.equals("400")) {
//                        Toast.makeText(getBaseContext(), "Не верно имя пользователя или пароль", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Res.SHARED_PREFERENCES_NICK_NAME, params[0]);
//                        editor.putString(Res.SHARED_PREFERENCES_PASSWORD, params[1]);
                        editor.putString(Res.SHARED_PREFERENCES_E_TOKEN, text);
                        editor.commit();
                        Communication communication = new Communication(getBaseContext());
                        communication.getMyInfo();
//                        try {
//                            String param = Res.TOKEN + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_E_TOKEN, "") + "&" + Res.USER_NAME + "=" + sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, "");
//                            URI uri = new URI("http", Res.GET_MY_INFO + param, null);
//                            URL url2 = uri.toURL();
//
//                            HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
//
//                            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                                String line2 = null;
//                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//                                StringBuilder result = new StringBuilder();
//                                while ((line2 = bufferedReader.readLine()) != null) {
//                                    result.append(line2);
//                                }
//                                JSONObject obj = new JSONObject(result.toString());
//
//                                    // Проверка для отсеивания своих данных
//
//                                editor.putInt(Res.SHARED_PREFERENCES_ID, obj.getInt(Res.ID));
//                                editor.putString(Res.SHARED_PREFERENCES_EMAIL, obj.getString(Res.EMAIL));
//                                editor.putString(Res.SHARED_PREFERENCES_FIO, obj.getString(Res.FIO));
//                                editor.commit();
////                                        UsersLocation usersLocation = new UsersLocation(obj.getInt(Res.ID), obj.getDouble(Res.LATITUDE), obj.getDouble(Res.LONGITUDE), obj.getDouble(Res.SPEED));
////                                        Log.d("myMap", "" + obj.getInt(Res.ID)+ obj.getDouble(Res.LATITUDE) + obj.getDouble(Res.LONGITUDE)+ obj.getDouble(Res.SPEED));
//
//
//                            }
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (URISyntaxException e) {
//                            e.printStackTrace();
//                        }

//                        return line;

//                        text = line;

                    }

                    return true;
                }


//                text = line;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }




//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(Res.SHARED_PREFERENCES_NICK_NAME, params[0]);
//            editor.putString(Res.SHARED_PREFERENCES_PASSWORD, params[1]);
//            editor.commit();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Не верно имя пользователя или пароль", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//            startActivity(intent);
        }
    }
}
