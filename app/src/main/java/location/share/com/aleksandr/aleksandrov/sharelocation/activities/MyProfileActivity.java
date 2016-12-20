package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;

/**
 * Created by Aleksandr on 11/22/2016.
 */
public class MyProfileActivity extends BaseActivity {

    public TextView user_login, user_fio, user_email, user_phone;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_layout);

        user_email = (TextView) findViewById(R.id.my_profile_email);
        user_fio = (TextView) findViewById(R.id.my_profile_fio);
        user_login = (TextView) findViewById(R.id.my_profile_login);
        user_phone = (TextView) findViewById(R.id.my_profile_phone_number);

        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);
        user_email.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_EMAIL, ""));
        user_fio.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_FIO, ""));
        user_login.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_NICK_NAME, ""));
        user_phone.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_PHONE_NUMBER, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile_menu, menu);



        return true;
    }

    /* Called when the second activity's finished */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 90:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    user_fio.setText(res.getString(Res.FIO, ""));
                    user_phone.setText(res.getString(Res.PHONE, ""));
                    user_email.setText(res.getString(Res.EMAIL, ""));
                }
                break;
            default:
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.my_profile_menu_item_edit) {
            startActivityForResult(new Intent(getBaseContext(), EditMyProfileActivity.class), 90);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
