package location.share.com.aleksandr.aleksandrov.sharelocation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aleksandr on 11/23/2016.
 */

public class FriendsListFragment extends Fragment {

    public FriendsListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment, container, false);

        return rootView;
    }
}
