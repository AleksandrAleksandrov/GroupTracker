package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 11/21/2016.
 */

public class CreateNewAccountActivity extends AppCompatActivity {

    EditText email, userName, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account_activity);

        email = (EditText) findViewById(R.id.edit_text_input_email);
        userName = (EditText) findViewById(R.id.edit_text_input_name);
        password = (EditText) findViewById(R.id.edit_text_input_password_create_new_account);


    }


    public void onClickRegister(View view) {
        if (email.getText().toString().equals("")) {
            Toast.makeText(this.getBaseContext(), "Email empty", Toast.LENGTH_SHORT).show();

        } else if (userName.getText().equals("")) {

        } else if (password.getText().equals("")) {

        }

    }

    class SingUp extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }


    }
}
