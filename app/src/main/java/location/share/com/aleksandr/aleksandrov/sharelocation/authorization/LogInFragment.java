package location.share.com.aleksandr.aleksandrov.sharelocation.authorization;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Aleksandr on 12/27/2016.
 */

public class LogInFragment extends Fragment {

    private MyLoginAsyncTask myLoginAsyncTask;
    private Button buttonLogin;

    private EditText nick_name_et, password_et;
    private SharedPreferences sharedPreferences;

    public LogInFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in_fragment, container, false);
        nick_name_et = (EditText) view.findViewById(R.id.edit_text_input_nick_name);
        password_et = (EditText) view.findViewById(R.id.edit_text_input_password);
        buttonLogin = (Button) view.findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLoginAsyncTask = new MyLoginAsyncTask();
                myLoginAsyncTask.execute(nick_name_et.getText().toString(), password_et.getText().toString());
            }
        });
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (nick_name_et.isSelected()) {
//            imm.showSoftInput(nick_name_et, InputMethodManager.SHOW_IMPLICIT);
//        }

        sharedPreferences = getActivity().getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);
        if (!sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, "").equals("")) {
            nick_name_et.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, ""));
        }
        if (!sharedPreferences.getString(Res.SHARED_PREFERENCES_PASSWORD, "").equals("")) {
            password_et.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_PASSWORD, ""));
        }
        return view;
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
                        Communication communication = new Communication(getActivity());
                        communication.getMyInfo();
                    }

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
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Не верно имя пользователя или пароль", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//            startActivity(intent);
        }
    }
}
