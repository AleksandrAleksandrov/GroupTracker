package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.ChatsActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.adapters.MessageListAdapter;
import location.share.com.aleksandr.aleksandrov.sharelocation.adapters.MyListAdapter;

/**
 * Created by Aleksandr on 11/9/2016.
 */

public class MessagesList extends ListFragment {

    String[] arrayList = new String[10];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int i = 0; i < 10; i++) {
            arrayList[i]="Yesterday";
        }


        MessageListAdapter messageListAdapter = new MessageListAdapter(getActivity().getBaseContext(), R.layout.fragment_list_item_for_messages_list, arrayList);
        setListAdapter(messageListAdapter);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startActivity(new Intent(getActivity().getBaseContext(), ChatsActivity.class));
    }
}
