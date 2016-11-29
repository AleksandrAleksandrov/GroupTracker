package location.share.com.aleksandr.aleksandrov.sharelocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;

/**
 * Created by Aleksandr on 11/7/2016.
 */

public class MyListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MyListAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_list_item_for_friends, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.picture_in_frends_list);
        textView.setText(values[position]);
        // change the icon for Windows and iPhone
        imageView.setImageResource(R.drawable.profile);

        return rowView;
    }
}
