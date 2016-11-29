package location.share.com.aleksandr.aleksandrov.sharelocation.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 11/9/2016.
 */

public class MessageListAdapter extends ArrayAdapter<String> {

    private static LayoutInflater inflater = null;

    private final String[] values;
    Context context;


    public MessageListAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        values = objects;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_list_item_for_messages_list, viewGroup, false);
        TextView textView = (TextView) rowView.findViewById(R.id.the_last_message_date_in_messages_list);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.picture_in_messages_list);
        textView.setText(values[i]);
        // change the icon for Windows and iPhone
        imageView.setImageResource(R.drawable.profile);

        return rowView;
    }
}
