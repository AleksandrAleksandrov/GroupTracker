package location.share.com.aleksandr.aleksandrov.sharelocation.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Aleksandr on 12/27/2016.
 */

public class RegistrationFragment extends Fragment {

    EditText email, userName, password, confirmPassword;
    SharedPreferences sharedPreferences;
    private Button button_sing_up;

    public RegistrationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        email = (EditText) view.findViewById(R.id.edit_text_input_email);
        userName = (EditText) view.findViewById(R.id.edit_text_input_name);
        password = (EditText) view.findViewById(R.id.edit_text_input_password_create_new_account);
        confirmPassword = (EditText) view.findViewById(R.id.edit_text_input_confirm_password_create_new_account);
        button_sing_up = (Button) view.findViewById(R.id.sing_up_button);
        button_sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("") & !userName.getText().equals("") & !password.getText().equals("") & !confirmPassword.getText().equals("")) {
                    SingUp singUp = new SingUp();
                    singUp.execute(email.getText().toString(), userName.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
                }
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);


        return view;
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
                Toast.makeText(getActivity(), "Не верно имя пользователя или пароль", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//            startActivity(intent);
        }
    }
    private void finishWithResult() {
        Bundle conData = new Bundle();
        conData.putString("param_result", "Thanks Thanks");
        Intent intent = new Intent();
        intent.putExtras(conData);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
