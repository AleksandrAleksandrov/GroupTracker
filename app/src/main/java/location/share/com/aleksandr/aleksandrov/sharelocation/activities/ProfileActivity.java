package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 11/7/2016.
 */

public class ProfileActivity extends AppCompatActivity {

    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Intent intent = getIntent();

        textView = (TextView) findViewById(R.id.profile_name);

        textView.setText(intent.getStringExtra("users_name"));
//        Log.d("myProfile", "users_name");

    }

    public void onClickSendAMessageFromProfile(View view) {
        startActivity(new Intent(this, ChatsActivity.class));
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }
}
