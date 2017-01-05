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

public class ProfileActivity extends BaseActivity {

    private TextView textView;
    private static String EXTRA_PROFILE_ACTIVITY = "location.share.com.aleksandr.aleksandrov.sharelocation.activities.profiel_activity";

    public static Intent newIntent(Context context, String userName) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_ACTIVITY, userName);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Intent intent = getIntent();

        textView = (TextView) findViewById(R.id.profile_name);

        textView.setText(intent.getStringExtra(EXTRA_PROFILE_ACTIVITY));
    }

    public void onClickSendAMessageFromProfile(View view) {
        startActivity(new Intent(this, ChatsActivity.class));
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }
}
