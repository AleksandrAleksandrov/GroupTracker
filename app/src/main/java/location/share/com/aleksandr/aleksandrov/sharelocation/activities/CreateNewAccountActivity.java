package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Aleksandr on 11/21/2016.
 */

public class CreateNewAccountActivity extends AppCompatActivity {

    EditText email, userName, password, confirmPassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account_activity);

        email = (EditText) findViewById(R.id.edit_text_input_email);
        userName = (EditText) findViewById(R.id.edit_text_input_name);
        password = (EditText) findViewById(R.id.edit_text_input_password_create_new_account);
        confirmPassword = (EditText) findViewById(R.id.edit_text_input_confirm_password_create_new_account);

        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);

    }


    public void onClickRegister(View view) {
        if (!email.getText().toString().equals("") & !userName.getText().equals("") & !password.getText().equals("") & !confirmPassword.getText().equals("")) {
            SingUp singUp = new SingUp();
            singUp.execute(email.getText().toString(), userName.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
        }

    }

    class SingUp extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
//            String email = "";
//            String us = null;
            BufferedReader reader=null;

            try {
                String param = Res.EMAIL + "=" + params[0] + "&" + "username" + "=" + params[1]+ "&" + Res.PASSWORD + "=" + params[2] + "&" + Res.CONFIRM_PASSWORD + "=" + params[3];
                URI uri = new URI("http", Res.SING_UP + param, null);
                URL url = uri.toURL();

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
//                    message+=inputLine;

                    }
                    JSONObject obj = new JSONObject(response.toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Res.SHARED_PREFERENCES_ID, obj.getInt(Res.ID));
                    editor.putString(Res.SHARED_PREFERENCES_E_TOKEN, obj.getString(Res.TOKEN));
                    editor.commit();
                    in.close();
                    return true;
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }





            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                finishWithResult();
            } else {
                Toast.makeText(getBaseContext(), "Не верно имя пользователя или пароль", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//            startActivity(intent);
        }
    }
    private void finishWithResult()
    {
        Bundle conData = new Bundle();
        conData.putString("param_result", "Thanks Thanks");
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
